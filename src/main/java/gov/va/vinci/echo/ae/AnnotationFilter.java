package gov.va.vinci.echo.ae;

/*
 * #%L
 * Echo concept extractor
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

import gov.va.vinci.echo.types.*;
import gov.va.vinci.leo.AnnotationLibrarian;
import gov.va.vinci.leo.ae.LeoBaseAnnotator;
import gov.va.vinci.leo.descriptors.LeoTypeSystemDescription;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import static gov.va.vinci.leo.tools.LeoUtils.*;

public class AnnotationFilter extends LeoBaseAnnotator {
	public static enum FilterType {
		BASIC_APA, BASIC_TYPES, CONCEPT, TEMPLATES
	}

	private static final Logger log = Logger.getLogger(getRuntimeClass().toString());

	private boolean DEBUG = false;

	protected String step = "";

	/**
	 * 
	 * @param a
	 * @param punc
	 */
	public static void trimPunctuationAnnotation(Annotation a, String punc) {
		if (punc.startsWith("^")) {
		} else {
			punc = "^" + punc;
		}
		if (a == null) {
			throw new IllegalArgumentException("Annotation cannot be null.");
		}
		if (a.getCoveredText().equals(a.getCoveredText().replaceFirst(punc, "")))
			return;
		else {
			String str = a.getCoveredText();
			int i = 0;
			while (!(str.equals(str.replaceFirst(punc, "")))) {
				str = str.replaceFirst(punc, "");
				i++;
			}
			a.setBegin(a.getBegin() + i);
		}
		AnnotationLibrarian.trimAnnotation(a, true);
		
	}

	/**
	 * 
	 * @param aJCas
	 * @throws AnalysisEngineProcessException
	 */
	protected void filerOutTemplates(JCas aJCas) throws AnalysisEngineProcessException {

		AnnotationLibrarian.removeCoveredAnnotations(aJCas, RelationPattern.class.getCanonicalName(), true, null);
		Collection<Annotation> annotations = AnnotationLibrarian.getAllAnnotationsOfType(aJCas,
				RelationPattern.type, true);

		// for all annotations remove those that are all blank or consist of only punctuation.
		for (Annotation a : annotations) {
			AnnotationLibrarian.trimAnnotation(a);
			if (a.getBegin() == a.getEnd())
				a.removeFromIndexes(aJCas);
		}	
	}

	/**
	 * Basic APA includes range and concept collection
	 * @param aJCas
	 * @throws AnalysisEngineProcessException
	 */
	protected void filterOutBasicAPA(JCas aJCas) throws AnalysisEngineProcessException {
		AnnotationLibrarian.removeCoveredAnnotations(aJCas, Range.class.getCanonicalName(), true, null);
		AnnotationLibrarian.removeCoveredAnnotations(aJCas, Range.class.getCanonicalName(),
		    NumericValue.class.getCanonicalName(), true, true, null);

		AnnotationLibrarian.removeCoveredAnnotations(aJCas, ConceptCollection.class.getCanonicalName(), true, null);
		AnnotationLibrarian.removeCoveredAnnotations(aJCas, ExcludeConcept.class.getCanonicalName(),
		    ConceptCollection.class.getCanonicalName(), true, true, null);
		AnnotationLibrarian.removeCoveredAnnotations(aJCas, ConceptCollection.class.getCanonicalName(),
		    MiddleStuff.class.getCanonicalName(), true, true, null);

		Collection<Annotation> annotations = AnnotationLibrarian.getAllAnnotationsOfType(aJCas,
		    ConceptCollection.type, true);

		// for all annotations remove those that are all blank or consist of only punctuation.
		for (Annotation a : annotations) {
			AnnotationLibrarian.trimAnnotation(a);
			if (a.getBegin() == a.getEnd())
				a.removeFromIndexes(aJCas);
		}
		for (Annotation a : annotations) {
			trimPunctuationAnnotation(a, "(-|\\/|\\.|#|%|'|,| |\\(|\\))");
			if (a.getBegin() == a.getEnd())
				a.removeFromIndexes(aJCas);
		}

		AnnotationLibrarian.removeCoveredAnnotations(aJCas, ConceptCollection.class.getCanonicalName(),
		    Concept.class.getCanonicalName(), true, true, null);
		AnnotationLibrarian.removeCoveredAnnotations(aJCas, Concept.class.getCanonicalName(),
		    ConceptCollection.class.getCanonicalName(), true, true, null);
		AnnotationLibrarian.removeCoveredAnnotations(aJCas, Concept.class.getCanonicalName(),
		    NumericValue.class.getCanonicalName(), true, true, null);
		AnnotationLibrarian.removeCoveredAnnotations(aJCas, ConceptCollection.class.getCanonicalName(),
		    NumericValue.class.getCanonicalName(), true, true, null);

	}

	/**
	 * 		 Look through all Value and Unit annotations
				 1: if two annotations of the same type overlap, get the longest and delete 
				 the shortest
				 2. if Unit and Value overlap, delete Value
	 */
	protected void filterOutBasicTypes(JCas aJCas) throws AnalysisEngineProcessException {
		AnnotationLibrarian.removeCoveredAnnotations(aJCas, Anatomy.class.getCanonicalName(), true, null);
		AnnotationLibrarian.removeCoveredAnnotations(aJCas, Concept.class.getCanonicalName(),true, null);
		AnnotationLibrarian.removeCoveredAnnotations(aJCas, ExcludeConcept.class.getCanonicalName(),true, null);
		AnnotationLibrarian.removeCoveredAnnotations(aJCas, ExcludeValue.class.getCanonicalName(),true, null);
		AnnotationLibrarian.removeCoveredAnnotations(aJCas, Header.class.getCanonicalName(),true, null);
		AnnotationLibrarian.removeCoveredAnnotations(aJCas, Measurement.class.getCanonicalName(),true, null);
		AnnotationLibrarian.removeCoveredAnnotations(aJCas, Method.class.getCanonicalName(),true, null);
		AnnotationLibrarian.removeCoveredAnnotations(aJCas, MiddleStuff.class.getCanonicalName(),true, null);
		AnnotationLibrarian.removeCoveredAnnotations(aJCas, Modifier.class.getCanonicalName(),true, null);
		AnnotationLibrarian.removeCoveredAnnotations(aJCas, NumericValue.class.getCanonicalName(),true, null);
		AnnotationLibrarian.removeCoveredAnnotations(aJCas, QValue.class.getCanonicalName(),true, null);
		AnnotationLibrarian.removeCoveredAnnotations(aJCas, Units.class.getCanonicalName(),true, null);


		// Remove anatomy and measurement that are consist of punctuation or are numbers
		try {
			Collection<Annotation> annotations = AnnotationLibrarian.getAllAnnotationsOfType(aJCas, Anatomy.type, true);
			annotations.addAll(AnnotationLibrarian.getAllAnnotationsOfType(aJCas, Measurement.type, true));
			// for all annotations remove those that are all blank or consist of only punctuation.
			for (Annotation a : annotations) {
				AnnotationLibrarian.trimAnnotation(a);
				if (a.getBegin() == a.getEnd())
					a.removeFromIndexes(aJCas);
			}
			for (Annotation a : annotations) {
				trimPunctuationAnnotation(a, "(-|\\/|\\.|#|%|'|,| |\\(|\\))");
				if (a.getBegin() == a.getEnd())
					a.removeFromIndexes(aJCas);
			}
		} catch (Exception e) {
			log.warn("There was an issue with cleaning up anatomy.");
		}
		try {
			Collection<Annotation> annotations = AnnotationLibrarian.getAllAnnotationsOfType(aJCas,
			    MiddleStuff.type, true);
			for (Annotation a : annotations) {
				AnnotationLibrarian.trimAnnotation(a);
			}
		} catch (Exception e) {
			log.warn("There was an issue with cleaning up middle stuff.");
		}

		try {
			Collection<Annotation> annotations = AnnotationLibrarian.getAllAnnotationsOfType(aJCas,
			    QValue.type, true);
			for (Annotation a : annotations) {
				AnnotationLibrarian.trimAnnotation(a);
				if (a.getBegin() == a.getEnd())
					a.removeFromIndexes(aJCas);
			}
		} catch (Exception e) {
			log.warn("There was an issue with cleaning up middle stuff.");
		}
		// Remove units that overlap with concept, anatomy or measurement
		AnnotationLibrarian.removeCoveredAnnotations(aJCas, Concept.class.getCanonicalName(),
		    NumericValue.class.getCanonicalName(), true, true, null);
		AnnotationLibrarian.removeCoveredAnnotations(aJCas, Concept.class.getCanonicalName(),
		    Units.class.getCanonicalName(), true, true, null);
		AnnotationLibrarian.removeCoveredAnnotations(aJCas, Anatomy.class.getCanonicalName(),
		    Units.class.getCanonicalName(), true, true, null);
		AnnotationLibrarian.removeCoveredAnnotations(aJCas, Measurement.class.getCanonicalName(),
		    Anatomy.class.getCanonicalName(), true, true, null);
		AnnotationLibrarian.removeCoveredAnnotations(aJCas, Measurement.class.getCanonicalName(),
		    Units.class.getCanonicalName(), true, true, null);
		AnnotationLibrarian.removeCoveredAnnotations(aJCas, MiddleStuff.class.getCanonicalName(),
		    Units.class.getCanonicalName(), true, true, null);
		AnnotationLibrarian.removeCoveredAnnotations(aJCas, Modifier.class.getCanonicalName(),
		    Units.class.getCanonicalName(), true, true, null);
		AnnotationLibrarian.removeCoveredAnnotations(aJCas, Header.class.getCanonicalName(),
		    Units.class.getCanonicalName(), true, true, null);
		AnnotationLibrarian.removeCoveredAnnotations(aJCas, QValue.class.getCanonicalName(),
		    Units.class.getCanonicalName(), true, true, null);
		AnnotationLibrarian.removeCoveredAnnotations(aJCas, Units.class.getCanonicalName(),
		    NumericValue.class.getCanonicalName(), true, true, null);

		// Remove concepts and values that overlaps with Exclude	
		AnnotationLibrarian.removeCoveredAnnotations(aJCas, ExcludeConcept.class.getCanonicalName(),
		    Concept.class.getCanonicalName(), true, true, null);
		AnnotationLibrarian.removeCoveredAnnotations(aJCas, ExcludeValue.class.getCanonicalName(),
		    NumericValue.class.getCanonicalName(), true, true, null);
		AnnotationLibrarian.removeCoveredAnnotations(aJCas, ExcludeValue.class.getCanonicalName(),
		    QValue.class.getCanonicalName(), true, true, null);
	}

	@Override
	public void initialize(UimaContext aContext) throws ResourceInitializationException {
		if (aContext.getConfigParameterValue("debug") != null) {
			try {
				DEBUG = (Boolean) aContext.getConfigParameterValue("debug");
			} catch (Exception ex) {
				//TODO: just swallowing this error for now. Maybe I should fix it.
			}
		}
		if (aContext.getConfigParameterValue("step") != null) {
			try {
				step = (String) aContext.getConfigParameterValue("step");
			} catch (Exception ex) {
				step = FilterType.TEMPLATES.name();
			}
		}
	}

	@Override
	public void annotate(JCas aJCas) throws AnalysisEngineProcessException {

		if (step.equals(FilterType.BASIC_TYPES.name())) {
			filterOutBasicTypes(aJCas);
		}
		// Remove overlapping templates
		else if (step.equals(FilterType.TEMPLATES.name())) {
			filerOutTemplates(aJCas);

			// Remove overlapping Range annotations and ConceptCollection
		} else if (step.equals(FilterType.BASIC_APA.name())) {
			filterOutBasicAPA(aJCas);

		} else {
			log.warn("Annotation filter is not specified");
			filterOutBasicTypes(aJCas);
			filterOutBasicAPA(aJCas);
			filerOutTemplates(aJCas);
		}
	}

  @Override
  public LeoTypeSystemDescription getLeoTypeSystemDescription() {
    // TODO Auto-generated method stub
    return null;
  }
}
