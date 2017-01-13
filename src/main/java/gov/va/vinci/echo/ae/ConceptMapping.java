package gov.va.vinci.echo.ae;

/*
 * #%L
 * Echo Concept Extractor
 * %%
 * Copyright (C) 2017 Department of Veterans Affairs
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import gov.va.vinci.leo.tools.LeoUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.StringArray;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import gov.va.vinci.echo.types.*;
import gov.va.vinci.leo.ae.LeoBaseAnnotator;
import gov.va.vinci.leo.descriptors.LeoTypeSystemDescription;
import gov.va.vinci.lookup.MatchType;
import gov.va.vinci.lookup.ae.LookupAnnotator;

public class ConceptMapping extends LeoBaseAnnotator {
	private static final Logger log /*    */= Logger.getLogger(LeoUtils.getRuntimeClass().toString());
	protected Connection connection = null;
	private String dataFileName = null;
	private boolean debug = false;
	protected MatchType matchType = null;
	protected String classificationFeatureName = null;
	protected static String DB_DRIVER = "org.hsqldb.jdbcDriver";
	protected static Map<MatchType, String> QUERY_MAP = new HashMap<MatchType, String>();
	protected static boolean normalize = true;
	protected String lookupFeature[] = null;
	public boolean singleMappingOnly = false;
	static {

		QUERY_MAP.put(MatchType.EQUALS, "select classification from dictionary where term = ?");
		QUERY_MAP.put(MatchType.EQUALS_IGNORE_CASE,
		    "select classification from dictionary where lower(term) = lower(?)");
		QUERY_MAP.put(MatchType.CONTAINS,
		    "select classification from dictionary where  " +
		        " term like concat('% ', ?, ' %') or " +
		        " term like concat('' , ?, ' %') or " +
		        " term like concat('% ', ?, '') or " +
		        " term like ? ");
		QUERY_MAP.put(MatchType.CONTAINS_IGNORE_CASE,
		    "select classification from dictionary where " +
		        "lower(term) like lower(concat('% ', ?, ' %')) or " +
		        "lower(term) like lower(concat('' , ?, ' %')) or " +
		        "lower(term) like lower(concat('% ', ?, ''))or " +
		        "lower(term) like lower(?)");
	}

	@Override
	public void initialize(UimaContext aContext) throws ResourceInitializationException {
		super.initialize(aContext);
		log.info("Initializing ConceptMapping annotator");
		inputTypes = (String[]) aContext.getConfigParameterValue(Param.INPUT_TYPE.getName());
		outputType = (String) aContext.getConfigParameterValue(Param.OUTPUT_TYPE.getName());

		dataFileName =
		    (String) aContext.getConfigParameterValue(Param.DATA_FILE_NAME.getName());

		if (dataFileName == null) {
			throw new RuntimeException(
			    "dataFileName must be set as a parameter with the full path to the dictionary data file.");
		}

		matchType =
		    MatchType.getMatchType((String) aContext.getConfigParameterValue(Param.MATCH_TYPE.getName()));

		if (matchType == null) {
			throw new RuntimeException("matchType must be specified to determine lookup method, and be one of: "
			    + Arrays.toString(MatchType.getMatchTypeNames()));
		}

		Boolean tempNormalize = (Boolean) aContext.getConfigParameterValue(Param.NORMALIZE.getName());
		if (tempNormalize != null) {
			normalize = tempNormalize;
		}

		classificationFeatureName = (String) aContext.getConfigParameterValue(Param.CLASSIFICATION_FEATURE
		    .getName());

		if (classificationFeatureName == null) {
			throw new RuntimeException(Param.CLASSIFICATION_FEATURE.getName()
			    + " must be set as a parameter with the feature name to put classification in.");
		}

		lookupFeature = (String[]) aContext.getConfigParameterValue(Param.LOOKUP_FEATURE.getName());

		if (aContext.getConfigParameterValue(Param.DEBUG.getName()) != null) {
			debug = (Boolean) aContext.getConfigParameterValue(Param.DEBUG.getName());
		}

		try {
			setupDatabase();
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
		log.info("ConceptMapping is initialized");
	}

	/* (non-Javadoc)
	 * @see gov.va.vinci.lookup.ae.LookupAnnotator#process(org.apache.uima.jcas.JCas)
	 */
	@Override
	public void annotate(JCas aJCas) throws AnalysisEngineProcessException {
		// Line from the base class
		numberOfCASesProcessed++;

		try {
			if (this.inputTypes != null && this.inputTypes.length > 0) { // Process just specific types
				int counter = 0;
				for (String type : this.inputTypes) {
					FSIterator<Annotation> annotations = this.getAnnotationListForType(aJCas, type);
					while (annotations.hasNext()) {
						Annotation toProcess = annotations.next();
						String term = "";
						if (lookupFeature != null && this.lookupFeature[counter] != null) {
							Feature pFeature = toProcess.getType().getFeatureByBaseName(lookupFeature[counter]);
							term = toProcess.getFeatureValueAsString(pFeature);
						} else {
							term = toProcess.getCoveredText();
						}
						//TODO: normalize term

						term = normalizeText(term);
						HashSet<String> maps = new HashSet<String>();
						//TODO: Change to singleMappings
						maps.addAll(performFlexibleMapping(term));
						createNewForMappings(maps, toProcess, aJCas, Mapping.class.getCanonicalName());
						// updateForMappings(performFlexibleMapping(concept), toProcess); 
					} // end while
				}//end for
				counter++;
			} else { // Process whole document.
				HashSet<String> maps = new HashSet<String>();
				maps.addAll(performFlexibleMapping(aJCas.getDocumentText()));
				createNewForMappings(maps, new Annotation(aJCas, 0, aJCas.getDocumentText().length()), aJCas,
				    Mapping.class.getCanonicalName());
			} // end if no inputTypes
		} catch (Exception ex) {
			log.error("Mapping failed" + ex.getMessage());
		} //end try
	} //end process

	/**
	 * 
	 * @param mappings
	 * @param toProcess
	 * The method takes toProcess annotation and creates a new 
	 * annotation with the same span but 
	 */
	protected void createNewForMappings(HashSet<String> mappings, Annotation toProcess, JCas aJCas,
	    String outType) {
		// Put all items into the 
		if (mappings.size() > 0) {
			Mapping o;
			try {
				// Converting hash set into string array
				StringArray sa = new StringArray(aJCas, mappings.size());
				int i = 0;
				for (String str : mappings) {
					sa.set(i, str);
					i++;
				}
				o = (Mapping) this.addOutputAnnotation(outType, aJCas, toProcess.getBegin(),
				    toProcess.getEnd());
				Feature pFeature = o.getType().getFeatureByBaseName(classificationFeatureName);
				o.setFeatureValue(pFeature, sa);
			} catch (AnalysisEngineProcessException e) {
				e.printStackTrace();
			}
		} else {
			log.debug("Lookup term " + toProcess.getCoveredText() + " did not find any mappings.");
		}

	} //  end while  

	// Update annotation method
	// Create new annotation method

	public HashSet<String> performFlexibleMapping(String concept) {
		HashSet<String> mappings = new HashSet<String>();
		try {
			mappings.addAll(doLookupEqualsIgnoreCase(concept));

			// If there is no exact match, match every word separately, 
			// count how many mappings each word has and select the concept that is mapped the most
			if (mappings.size() == 0) {
				if (StringUtils.isNotBlank(concept)) {
					String[] splitConcept = concept.split(" ");
					HashMap<String, Integer> mappingCounts = new HashMap<String, Integer>();

					for (String str : splitConcept) {
						HashSet<String> tempSet = new HashSet<String>();
						tempSet.addAll(doLookupContainsIgnoreCase(str));
						for (String s : tempSet) {
							if (mappingCounts.containsKey(s)) {
								mappingCounts.put(s, mappingCounts.get(s) + 1);
							} else {
								mappingCounts.put(s, 1);
							}
						}
						if (mappingCounts.size() > 0) {
							mappings.addAll(findMostFrequent(mappingCounts));
						}

					} // end for
				} //  if Concept is not blank
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (AnalysisEngineProcessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// TODO: If single mapping and more than one mapping
		if (singleMappingOnly && mappings.size() > 1)
			mappings = new HashSet<String>();

		return mappings;
	}

	public static HashSet<String> findMostFrequent(HashMap<String, Integer> map) {
		HashSet<String> result = new HashSet<String>();
		Entry<String, Integer> maxEntry = null;

		for (Entry<String, Integer> entry : map.entrySet()) {
			if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
				maxEntry = entry;
			}
		}
		if (maxEntry != null) {
			log.debug("Max entry " + maxEntry);
			for (Entry<String, Integer> entry : map.entrySet()) {
				if (entry.getValue() == maxEntry.getValue()) {
					result.add(entry.getKey());
				}
			}
		}
		return result;
	}

	/**
	 * TODO: Info only -- doLookup(text)
	 * @param text
	 * @return
	 * @throws SQLException
	 * @throws AnalysisEngineProcessException
	 */
	protected ArrayList<String> doLookup(String text, MatchType matchType) throws SQLException,
	    AnalysisEngineProcessException {

		PreparedStatement ps = connection.prepareStatement(QUERY_MAP.get(matchType));
		String value = null;
		ArrayList<String> resultList = new ArrayList<String>();

		if (normalize) {
			if (StringUtils.isNotBlank(text)) {
				value = normalizeText(text);
			} else {
				value = "";
			}
		} else {
			value = text;
		}
		//System.out.println("Attempting to map : " + value);
		if (matchType.equals(MatchType.CONTAINS) || matchType.equals(MatchType.CONTAINS_IGNORE_CASE)) {
			value = text.replaceAll("%", "\\%");
			ps.setString(2, value);
			ps.setString(3, value);
			ps.setString(4, value);
		}
		ps.setString(1, value);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			resultList.add(rs.getString(1));
		}
		return resultList;
	} // doLookup(text)

	protected ArrayList<String> doLookupContainsIgnoreCase(String text) throws SQLException,
	    AnalysisEngineProcessException {
		if (StringUtils.isBlank(text))
			return new ArrayList<String>();
		return doLookup(text, MatchType.CONTAINS_IGNORE_CASE);
	} // doLookup(text)

	protected ArrayList<String> doLookupEqualsIgnoreCase(String text) throws SQLException,
	    AnalysisEngineProcessException {
		if (StringUtils.isBlank(text))
			return new ArrayList<String>();
		return doLookup(text, MatchType.EQUALS_IGNORE_CASE);
	} // doLookup(text)

	/**
	 * Incoming text is normalized by 
	 * setting everything to lower case 
	 * removing dashes, parentheses, and extra spaces
	 */
	public String normalizeText(String text) {
		return text.toLowerCase()
		    .replaceAll("\\(", " ") //remove parenteses
		    .replaceAll("\\)", " ")
		    .replaceAll("-", "")
		    .replaceAll(":", "")
		    .replaceAll("\\.", " ")
		    .replaceAll("%", " ")
		    .replaceAll("///", " ")
		    .replaceAll("\\/", " ")
		    .replaceAll("\\s+", " ").trim();
	}

	protected void setupDatabase() throws ClassNotFoundException, SQLException, IllegalAccessException,
	    InstantiationException, IOException {
		System.setProperty("textdb.allow_full_path", "true");
		Class.forName(DB_DRIVER).newInstance();

		File dbFile = File.createTempFile("hsqldb", "db");
		final String url = "jdbc:hsqldb:file:" + dbFile.getAbsolutePath() + ";";

		final StringBuilder createTable = new StringBuilder();
		createTable.append("CREATE TEXT TABLE dictionary (");
		createTable.append("term VARCHAR(2000), classification VARCHAR(2000))");
		final StringBuilder linkTable = new StringBuilder();
		linkTable.append("SET TABLE dictionary SOURCE ");
		linkTable.append("\"" + dataFileName + ";ignore_first=false;all_quoted=false;fs=,\"");

		connection = DriverManager.getConnection(url, "sa", "");
		Statement stm = connection.createStatement();
		stm.execute(createTable.toString());
		stm.execute(linkTable.toString());

		if (debug) {
			printDictionaryToStdOut();
		}
	}

	protected void printDictionaryToStdOut() throws SQLException {
		Statement stm = connection.createStatement();
		ResultSet resultSet = stm.executeQuery("SELECT * FROM dictionary");
		System.out.println("Dictionary:");
		while (resultSet.next()) {
			System.out.println("\tTerm: [" + resultSet.getString(1) + "]  --> Classification: ["
			    + resultSet.getString(2) + "]");
		}
	}

	public void close() {
		try {
			connection.close();
		} catch (Exception e) {
			log.error(e);
		}
	}

	@Override
	protected void finalize() {
		try {
			connection.close();
		} catch (Exception e) {

		}
	}

	/**
	 * Enumeration of the parameters this annotator uses.
	 */
	public enum Param {
		/**
		 * The output type annotation to create.
		 */
		OUTPUT_TYPE("outputType", true, false, "String"),
		/**
		 * The input types this annotation processes. If null, the whole document is processed.
		 */
		INPUT_TYPE("inputTypes", false, true, "String"),
		/**
		 * the path to the semi-colon delimeter dictionary file
		 */
		DATA_FILE_NAME("dataFileName", true, false, "String"),
		/**
		 * the match method to perform. (equals, equalsIgnoreCase)
		 */
		MATCH_TYPE("matchType", true, false, "String"),
		/**
		 * the feature name to put the classification in.
		 */
		CLASSIFICATION_FEATURE("classificationFeature", true, false, "String"),
		/**
		 * the feature to use for the lookup instead of the covered text. If set, the feature value is used
		 * for the lookup, not the annotations covered text.
		 */
		LOOKUP_FEATURE("lookupFeature", false, true, "String"),
		/**
		 * If true, the dictionary is written out to stdout after being read in for verification.
		 */
		DEBUG("debug", false, false, "Boolean"),
		/**
		 * defaults to <strong>true</strong>. This implementation removes all tabs, new lines, and
		 * duplicate white spaces characters with a single space, then trims the text. Tabs and new lines are converted
		 * to a single space.
		 */
		NORMALIZE("normalize", false, false, "Boolean");

		String name;
		private Boolean required = false;
		private Boolean multiValue;
		private String type;

		private Param(String name, Boolean required, Boolean multiValue, String type) {
			this.name = name;
			this.required = required;
			this.multiValue = multiValue;
			this.type = type;
		}

		public String getName() {
			return name;
		}

		public Boolean getRequired() {
			return required;
		}

		public Boolean getMultiValue() {
			return multiValue;
		}

		public String getType() {
			return type;
		}
	}

	@Override
	public LeoTypeSystemDescription getLeoTypeSystemDescription() {
		// TODO Auto-generated method stub
		return null;
	}

}
