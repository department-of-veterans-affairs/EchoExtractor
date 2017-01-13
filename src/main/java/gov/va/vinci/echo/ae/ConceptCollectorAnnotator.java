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
import java.util.regex.Pattern;

import gov.va.vinci.echo.types.*;
import gov.va.vinci.leo.AnnotationLibrarian;
import gov.va.vinci.leo.ae.LeoBaseAnnotator;
import gov.va.vinci.leo.descriptors.LeoTypeSystemDescription;

import gov.va.vinci.leo.tools.LeoUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CASException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

/***
 * @author OVP LogicAnnotator creates a new annotation
 */
public class ConceptCollectorAnnotator extends LeoBaseAnnotator {

	private static final Logger log = Logger.getLogger(LeoUtils.getRuntimeClass().toString());
	protected Pattern newLinePattern = Pattern.compile("\r\n");
	protected Pattern delimiterPattern = Pattern.compile(": |= |\\.");

	@Override
	public void initialize(UimaContext aContext)
	    throws ResourceInitializationException {

	}

	@Override
	public void annotate(JCas aJCas) throws AnalysisEngineProcessException {

		String documentText = aJCas.getDocumentText();
		/*
		 * Steps:
		 * 1. find first anatomy or measurement annotation
		 * 2. keep adding them if they are contiguous.
		 * 3. Mark each contiguous set of Anatomy-Measurement as Concept
		 */
		String pattern = "";
		Annotation anatomyFirst = null;
    try {
      anatomyFirst = AnnotationLibrarian.getFirstAnnotationOfType(aJCas,
          Anatomy.class.getCanonicalName(), false);
    } catch (CASException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
		Annotation measurementFirst = null;
    try {
      measurementFirst = AnnotationLibrarian.getFirstAnnotationOfType(aJCas,
          Measurement.class.getCanonicalName(), false);
    } catch (CASException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
		Annotation tempAnnotation = null;

		//ArrayList<Annotation> annotationSet = new ArrayList<Annotation>();
		ArrayList<Annotation> cumulativeAnnotation = new ArrayList<Annotation>();

		Collection<Annotation> conceptPartsAnnotations = AnnotationLibrarian.getAllAnnotationsOfType(aJCas,
		    Anatomy.type, false);
		conceptPartsAnnotations.addAll(AnnotationLibrarian.getAllAnnotationsOfType(aJCas, Measurement.type, false));

		if (conceptPartsAnnotations.size() > 0) {
			// There is at least one Anatomy or Measurement. Need to find first.
			if (anatomyFirst != null) {
				tempAnnotation = anatomyFirst;
			} //end of if anatomyFirst
			if (measurementFirst != null) {
				if (tempAnnotation != null) {
					if (tempAnnotation.getBegin() > measurementFirst.getBegin()) {
						tempAnnotation = measurementFirst;
					}
				} else {
					tempAnnotation = measurementFirst;
				}
			}// end if measurementFirst
			 // at this time tempAnnotation contains the first annotation of either Anatomy or Measurement type

			cumulativeAnnotation.add(tempAnnotation);
			while (AnnotationLibrarian.getNextClosestAnnotations(tempAnnotation, conceptPartsAnnotations).size() > 0) {
				// get next
				Annotation nextAnnotation = ((ArrayList<Annotation>) AnnotationLibrarian.getNextClosestAnnotations(
				    tempAnnotation, conceptPartsAnnotations))
				    .get(0);
				// get the next annotation and see what is between temp and next
				String inBetween = AnnotationLibrarian.getTextInBetween(tempAnnotation, nextAnnotation);
				inBetween = inBetween.replaceAll(" +", " ")
				    .replaceAll("\\(", "")
				    .replaceAll("\\/", "")
				    // .replaceAll(":", "")
				    .replaceAll("\\-", "")
				    .replaceAll("\\)", "");

				//TODO: Check if the text that is left is double new line
				// or if it is not blank
				// That means to stop collecting and create an output annotation.
				if (inBetween.matches("\r\n *\r\n") || StringUtils.isNotBlank(inBetween)) {
					if ((inBetween.trim()).matches("of the")) {
						// else means that the text inbetween is blank and does not contain two new lines
						//  If it is, it means that the concept is still going
						// Still in concept
						cumulativeAnnotation.add(nextAnnotation);
						tempAnnotation = nextAnnotation;
						pattern = pattern + " <" + nextAnnotation.getType().getShortName() + "/>";
					} else {
						// the next thing is not in Concept. So need to output the current annotation
						int beginAnnotation = cumulativeAnnotation.get(0).getBegin();
						int endAnnotation = cumulativeAnnotation.get(cumulativeAnnotation.size() - 1).getEnd();
						if (documentText.substring(endAnnotation, endAnnotation + 1).contains(")"))
							endAnnotation++;
						ConceptCollection outAnnotation = (ConceptCollection) this.addOutputAnnotation(
						    ConceptCollection.class.getCanonicalName(), aJCas,
						    beginAnnotation, endAnnotation);
						outAnnotation.setPattern(pattern);
						// Reset cumulative and add the next annotation
						cumulativeAnnotation = new ArrayList<Annotation>();
						cumulativeAnnotation.add(nextAnnotation);
						pattern = "<" + nextAnnotation.getType().getShortName() + "/>";
						tempAnnotation = nextAnnotation;
					}
				} else {
					// else means that the text inbetween is blank and does not contain two new lines
					//  If it is, it means that the concept is still going
					// Still in concept
					cumulativeAnnotation.add(nextAnnotation);
					tempAnnotation = nextAnnotation;
					pattern = pattern + " <" + nextAnnotation.getType().getShortName() + "/>";
				} // end else
			} //  end while there are more annotations;
				if (cumulativeAnnotation.size() > 0) { // output the last ConceptCollection
					int beginAnnotation = cumulativeAnnotation.get(0).getBegin();
					int endAnnotation = cumulativeAnnotation.get(cumulativeAnnotation.size() - 1).getEnd();
					ConceptCollection outAnnotation = (ConceptCollection) this.addOutputAnnotation(
					    ConceptCollection.class.getCanonicalName(), aJCas,
					    beginAnnotation, endAnnotation);
					outAnnotation.setPattern(pattern);
				}
			 

			
		}// end if there are any annotations of types Anatomy or Measurement
	}// end process

  @Override
  public LeoTypeSystemDescription getLeoTypeSystemDescription() {
    // TODO Auto-generated method stub
    return null;
  }
}// end class

