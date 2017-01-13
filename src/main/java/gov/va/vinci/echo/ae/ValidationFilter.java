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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.StringArray;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import gov.va.vinci.echo.types.*;
import gov.va.vinci.leo.AnnotationLibrarian;
import gov.va.vinci.leo.ae.LeoBaseAnnotator;
import gov.va.vinci.leo.descriptors.LeoTypeSystemDescription;

public class ValidationFilter extends LeoBaseAnnotator {
	private static final Logger log = Logger.getLogger(gov.va.vinci.leo.tools.LeoUtils.getRuntimeClass().toString());

	@Override
	public void initialize(UimaContext aContext) throws ResourceInitializationException {
	}

	@Override
	public void annotate(JCas aJCas) throws AnalysisEngineProcessException {
		numberOfCASesProcessed++;
		String[] types = { Relation1.class.getCanonicalName(),
			Relation2.class.getCanonicalName(),
			Relation3.class.getCanonicalName(),
			Relation4.class.getCanonicalName() };
		HashMap<String, String[]> selectQuery = new HashMap<String, String[]>();

		selectQuery.put("Mapping", new String[] { "#size=1" });
		selectQuery.put("ConceptType", new String[] { "ConceptCollection", "Concept" });

		for (String type : types) {
			FSIterator<Annotation> annotations = this.getAnnotationListForType(aJCas, type);
			while (annotations.hasNext()) {
				Relation curRelation = (Relation) annotations.next();
				processQuery(aJCas, curRelation, selectQuery);
			}
		}
	}

	private void processQuery(JCas aJCas, Relation inputAnnotation, HashMap<String, String[]> selectQuery) {
		// Review the annotation
		// if Feature
		Type type = inputAnnotation.getType();

		// Initialize query result check. Set all to false
		HashMap<String, Boolean> querySuccess = new HashMap<String, Boolean>();
		for (Entry<String, String[]> queryEntry : selectQuery.entrySet()) {
			querySuccess.put(queryEntry.getKey(), false);
		}
		// Check the relation, enter the success to the result check
		for (Entry<String, String[]> queryEntry : selectQuery.entrySet()) {
			String queryKey = queryEntry.getKey();
			// Get the feature with the name as the key in the query entry
			Feature feature = type.getFeatureByBaseName(queryKey);
			// Get the feature type
			Type typeRange = feature.getRange();
			// If the feature is a simple String, check that the value is one of the
			// specified in the
			if (typeRange.isPrimitive()) {
				// check if the feature value is the same as in EntryValue
				for (String queryValue : queryEntry.getValue()) {
					String featureValue = inputAnnotation.getFeatureValueAsString(feature); // this is the value of the feature
					// if the value of the feature, i.e. Relation.ConceptType="Concept" equals to the
					// query String i.e. "Concept",
					if (featureValue.equals(queryValue))
						querySuccess.put(queryKey, true);
				}
			} else {
				// This is a Feature Structure, recurse through it.
				FeatureStructure fs = inputAnnotation.getFeatureValue(feature);

				if (fs == null) {
					continue;
				} else if (fs instanceof StringArray) {
					for (String queryValue : queryEntry.getValue()) {
						if (queryValue.startsWith("#size=")) {
							try {
								int sizeToCheck = Integer.parseInt(queryValue.substring(6));
								int featureValueSize = ((StringArray) fs).toArray().length;
								if (featureValueSize == sizeToCheck)
									querySuccess.put(queryKey, true);
							} catch (Exception e) {
								log.error("Check Validation filter at StringArray size check process");
							}
						}
					}
				} else {
					continue;
				}
			}
		}
		// Check if all entries are true
		if (querySuccess.containsValue(false)) {
			// Nothing
		} else {
			try {
				ValidationRelation outAnnotation = (ValidationRelation)
					this.addOutputAnnotation(ValidationRelation.class.getCanonicalName(), aJCas,
						inputAnnotation.getBegin(), inputAnnotation.getEnd());
				outAnnotation.setConceptType(inputAnnotation.getConceptType());
				outAnnotation.setTerm(inputAnnotation.getTerm());
				outAnnotation.setAssessment(inputAnnotation.getAssessment());
				outAnnotation.setMapping(inputAnnotation.getMapping());
				outAnnotation.setModifier(inputAnnotation.getModifier());
				outAnnotation.setUnit(inputAnnotation.getUnit());
				outAnnotation.setValue(inputAnnotation.getValue());

			} catch (AnalysisEngineProcessException e) {
				log.error(e.getMessage() + e.getStackTrace().toString());
			} catch (Exception e) {
				log.error(e.getMessage() + e.getStackTrace().toString());
			}

		}
	}

  @Override
  public LeoTypeSystemDescription getLeoTypeSystemDescription() {
    // TODO Auto-generated method stub
    return null;
  }
}
