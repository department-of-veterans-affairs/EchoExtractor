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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.StringArray;
import org.apache.uima.jcas.tcas.Annotation;
import gov.va.vinci.leo.AnnotationLibrarian;

import static gov.va.vinci.leo.tools.LeoUtils.*;

/**
 * This class is designed to remove extra mappings from the input type
 * annotations in those cases when there are multiple mappings
 * 
 * 
 * @author OVP
 *
 */
public class DisambiguationAnnotator extends ConceptMapping {

	private static final Logger log /*    */= Logger.getLogger(getRuntimeClass().toString());

	/* (non-Javadoc)
	 * @see gov.va.vinci.lookup.ae.LookupAnnotator#process(org.apache.uima.jcas.JCas)
	 */
	@Override
	public void annotate(JCas aJCas) throws AnalysisEngineProcessException {
		// Line from the base class
		numberOfCASesProcessed++;

		// 1. get all annotations of the input type
		// 2. get those annotations that have multiple mappings in the Mapping feature
		// 3. if there are more than one mapping, perform mapping of the covered text 
		// 4. if the lookup results in some mappings, remove those mappings from the list of the existing annotation
		// 5. if all mappings are removed, delete the existing annotation
		try {
			if (this.inputTypes != null && this.inputTypes.length > 0) { // Process just specific types
				int counter = 0;

				for (String type : this.inputTypes) {
					ArrayList<Annotation> annotations = new ArrayList<Annotation>();
					annotations.addAll(AnnotationLibrarian.getAllAnnotationsOfType(aJCas, aJCas.getTypeSystem().getType(type), false));
					for (Annotation toProcess : annotations) {
						//check size of Mapping feature                      

						Feature cFeature = toProcess.getType().getFeatureByBaseName(classificationFeatureName);
						String[] mappingList = ((StringArray) toProcess.getFeatureValue(cFeature)).toStringArray();
						if (mappingList.length > 0) {

							String term = "";
							if (lookupFeature != null && this.lookupFeature[counter] != null) {
								Feature pFeature = toProcess.getType().getFeatureByBaseName(lookupFeature[counter]);
								term = toProcess.getFeatureValueAsString(pFeature);
							} else {
								term = toProcess.getCoveredText();
							}
							term = normalizeText(term);
							//createNewForMappings(performFlexibleMapping(term), toProcess, aJCas);
							HashSet<String> oppositeMappings = new HashSet<String>();
							oppositeMappings.addAll(performFlexibleMapping(term));

							if (oppositeMappings.size() > 0) {
								removeExtraMappings(oppositeMappings, toProcess, mappingList, aJCas);
							}
						}

						// updateForMappings(performFlexibleMapping(concept), toProcess); 
					} // end while
				}//end for
				counter++;
			}
		} catch (Exception ex) {
			log.error("Disambiguation Mapping failed!!!  ");
			ex.printStackTrace();
		} //end try
	} //end process

	private void removeExtraMappings(HashSet<String> mappingsToRemove, Annotation toProcess,
	    String[] mappingList,
	    JCas aJCas) {
		if (mappingsToRemove.size() > 0) { //this is a list of new mappings
			ArrayList<String> updatedMappings = new ArrayList<String>();

			for (String a : mappingList) {// read from the list of the existing mappings
				if (!mappingsToRemove.contains(a)) { // if the existing mapping is not in the list of new opposite mappings
					updatedMappings.add(a);
				}
			}

			if (updatedMappings.size() == 0) {
				try {
					toProcess.removeFromIndexes(aJCas);
				} catch (Exception e) {
					log.error("Failed removing disambiguation mapping ");
					e.printStackTrace();
				}
			} else {

				StringArray sa = new StringArray(aJCas, updatedMappings.size());
				int i = 0;
				for (String str : updatedMappings) {
					sa.set(i, str);
					i++;
				}
				Feature pFeature = toProcess.getType().getFeatureByBaseName(classificationFeatureName);
				toProcess.setFeatureValue(pFeature, sa);
			}
		}
	}

	@Override
	public HashSet<String> performFlexibleMapping(String concept) {
		HashSet<String> mappings = new HashSet<String>();
		try {
			if (StringUtils.isNotBlank(concept)) {
				String[] splitConcept = concept.split(" |,|-|\\(|\\)");
				for (String str : splitConcept) {

					mappings.addAll(doLookupContainsIgnoreCase(str));

				} // end for
			} //  if Concept is not blank

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (AnalysisEngineProcessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mappings;
	}

}
