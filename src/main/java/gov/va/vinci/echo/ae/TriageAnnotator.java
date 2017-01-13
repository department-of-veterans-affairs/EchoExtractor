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

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gov.va.vinci.leo.types.CSI;
import gov.va.vinci.leo.ae.LeoBaseAnnotator;
import gov.va.vinci.leo.descriptors.LeoTypeSystemDescription;
import gov.va.vinci.echo.types.*;

import org.apache.log4j.Logger;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.examples.SourceDocumentInformation;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import static gov.va.vinci.leo.tools.LeoUtils.*;

/***
 * @author OVP
 * 
 */
public class TriageAnnotator extends LeoBaseAnnotator {

	private static final Logger log = Logger.getLogger(getRuntimeClass().toString());
	// Patterns to detect the document type

	protected Pattern[] templatePattern = new Pattern[] {
	    /*Pattern0*/Pattern.compile("MMode\\/2D Measurements & Calculations",
	        Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
	    /*Pattern1*/Pattern.compile("M-MODE MEASUREMENTS:? *\r\n|" +
	        "M-MODE MEASUREMENTS: *\\(Normals\\)*\r\n|" +
	        "2.?D MEASUREMENT *\\(Normals\\)*\r\n|",
	        Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
	    /*Pattern2*/Pattern.compile("HEMODYNAMIC DATA|ANATOMICAL DATA",
	        Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
	    /*Pattern3*/Pattern.compile("(ECHOCARDIOGRAM REPORT *\r\n.*MEASUREMENTS:? *\r\n)" +
	        "|CHAMBER SIZE AND FUNCTION",
	        Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
	    /*Pattern4*/Pattern.compile("VA Loma Linda.+(Aortic Doppler|" +
	        "Measurements Screen|Mitral Doppler|" +
	        "Tricuspid/Pulmonary Doppler)",
	        Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
	    /*Pattern5*/Pattern.compile("MEASUREMENT TABLES",
	        Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
	    /*Pattern6*/Pattern.compile("M Mode Measurements:",
	        Pattern.CASE_INSENSITIVE | Pattern.DOTALL), /**/
	    /*Pattern7*/Pattern.compile("ECHOCARDIOGRAPHY REPORT.*CHAMBER MEASUREMENTS",
	        Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
	    /*Pattern8*/Pattern.compile("\r\n *M-MODE AND 2-D RESULTS: *\r\n|",
	        Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
	  	    /*Pattern9*/Pattern.compile("CARDIAC ULTRASOUND EXAMINATION *\r\n|",
	  	        Pattern.CASE_INSENSITIVE | Pattern.DOTALL)	        
	};

	// Patterns to detect the start of the table
	protected Pattern[] templateStart = new Pattern[] {
	    /*Pattern0*/Pattern.compile("MMode\\/2D Measurements & Calculations",
	        Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
	    /*Pattern1*/Pattern.compile("M-MODE MEASUREMENTS:? *\r\n|2D MEASUREMENTS: *\r\n|" +
	        "M-MODE MEASUREMENTS: *\\(Normals\\) *\r\n|" +
	        "2.?D MEASUREMENT *\\(Normals\\) *\r\n",
	        Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
	    /*Pattern2*/Pattern.compile("PROCEDURAL DATA|ANATOMICAL DATA|HEMODYNAMIC DATA|AORTIC VALVE",
	        Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
	    /*Pattern3*/Pattern.compile("\r\n *(-)* *MEASUREMENTS:? *\r\n|" +
	        "CHAMBER SIZE AND FUNCTION|M-MODE MEASUREMENTS",
	        Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
	    /*Pattern4*/Pattern.compile("(Aortic Doppler|" +
	        "Measurements Screen|Mitral Doppler|" +
	        "Tricuspid/Pulmonary Doppler)",
	        Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
	    /*Pattern5*/Pattern.compile("MEASUREMENT TABLES",
	        Pattern.CASE_INSENSITIVE
	            | Pattern.DOTALL),
	    /*Pattern6*/Pattern.compile("\r\n *M Mode Measurements: *\r\n",
	        Pattern.CASE_INSENSITIVE
	            | Pattern.DOTALL),
	    /*Pattern7*/Pattern.compile("LEFT\\sVENTRICULAR\\sSEGMENTAL\\sWALL\\sMOTION\\s+ANALYSIS",
	        Pattern.CASE_INSENSITIVE
	            | Pattern.DOTALL),
	    /*Pattern8*/Pattern.compile("Patient +Normal *\r\n",
	        Pattern.CASE_INSENSITIVE
	            | Pattern.DOTALL),
	      	    /*Pattern9*/Pattern.compile("CARDIAC ULTRASOUND ASSESSMENT DETAILS *\r\n",
	      	        Pattern.CASE_INSENSITIVE
	      	            | Pattern.DOTALL) /**/
	};
	//Patterns to detect the end of the table
	protected Pattern[] templateEnd = new Pattern[] {
	    /*Pattern0*/Pattern
	        .compile(
	            "\\bA\\/P\\b|\\bA&P\\b|ARTERIAL BLOO|\\bATRIA\\b|\\bDate:|" +
	                "\\bFH:|\\bHPI:|\\bLABS\\b|\\bPE:|\\bPlan:|\\bPMH:|\\bPFTS\\b|" +
	                "Abbreviations|ACTIVE PROBLEMS|Attending( Physician)?|Assessment|" +
	                "Cardiac Catheterizations|CONCLUSIONs?|counseling|DIAGNOSIS SECTION|" +
	                "DIFFERENTIAL|DISCHARGE|ECHO:|EVALUATION|Exam date:|Exm Date:|" +
	                "Findings|Impression|INTERPETATION|Interpretation|Interpreted by|" +
	                "LAB VALUES|LABORATORY DATA|Left Ventricle|Medications|OUTPATIENT MEDS|" +
	                "Reading Physician|RECOMMENDATIONS?|Results|Rhythm|Signed|Stress( Results)?|" +
	                "Test\\(s\\) ordered|TESTS ORDERED|VITAL SIGNS:|ASSESSMENT (&|and) PLAN|ASSESSMENT:|" +
	                "PLAN/RECOMMENDATIONS:|acronyms",
	            Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
	    /*Pattern1*/Pattern
	        .compile(
	            "\\bA\\/P\\b|\\bA&P\\b|PROCEDURE|PAST MEDICAL HISTORY|" +
	                "MEDICATIONS|PHYSICAL EXAM|summary|Attending|Physician|VITAL SIGNS:|Interpreted by|" +
	                "ASSESSMENT (&|and) PLAN|PLAN\\/RECOMMENDATIONS:|CONCLUSION|acronyms",
	            Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
	    /*Pattern2*/Pattern
	        .compile(
	            "\\bA\\/P\\b|\\bA&P\\b|ARTERIAL BLOO|\\bATRIA\\b|\\bDate:|" +
	                "\\bFH:|\\bHPI:|\\bLABS\\b|\\bPE:|\\bPlan:|\\bPMH:|\\bPFTS\\b|" +
	                "Abbreviations|ACTIVE PROBLEMS|Attending( Physician)?|" +
	                "Assessment|Cardiac Catheterizations|CONCLUSIONs?|counseling|" +
	                "DIAGNOSIS SECTION|DIFFERENTIAL|DISCHARGE|ECHO:|EVALUATION|" +
	                "Exam date:|Exm Date:|Findings|Impression|INTERPETATION|" +
	                "Interpretation|Interpreted by|LAB VALUES|LABORATORY DATA|" +
	                "Left Ventricle|Medications|OUTPATIENT MEDS|Reading Physician|" +
	                "RECOMMENDATIONS?|Results|Rhythm|Signed|Stress( Results)?|" +
	                "Test\\(s\\) ordered|TESTS ORDERED|VITAL SIGNS:|" +
	                "LEFT VENTRICULAR WALL MOTION ANALYSIS|acronyms",
	            Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
	    /*Pattern3*/Pattern
	        .compile(
	            "\\bA\\/P\\b|\\bA&P\\b|ARTERIAL BLOO|\\bATRIA\\b|\\bDate:|\\bFH:" +
	                "|\\bHPI:|\\bLABS\\b|\\bPE:|\\bPlan:|\\bPMH:|\\bPFTS\\b|" +
	                "Abbreviations|ACTIVE PROBLEMS|Attending( Physician)?|" +
	                "Assessment|Cardiac Catheterizations|CONCLUSIONs?|counseling|" +
	                "DIAGNOSIS SECTION|DIFFERENTIAL|DISCHARGE|ECHO:|EVALUATION|" +
	                "Exam date:|Exm Date:|Findings|Impression|INTERPETATION|" +
	                "Interpretation|Interpreted by|LAB VALUES|LABORATORY DATA|" +
	                "Medications|OUTPATIENT MEDS|Reading Physician|" +
	                "RECOMMENDATIONS?|Results|Rhythm|Signed|Stress( Results)?|" +
	                "Test\\(s\\) ordered|TESTS ORDERED|VITAL SIGNS:",
	            Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
	    /*Pattern4*/Pattern.compile("\\bA\\/P\\b|\\bA&P\\b|PROCEDURE|" +
	        "PAST MEDICAL HISTORY|MEDICATIONS|PHYSICAL EXAM|Attending|Physician" +
	        "|VITAL SIGNS:|Interpreted by|ASSESSMENT (&|and) PLAN|PLAN\\/RECOMMENDATIONS:|" +
	        "CONCLUSION|acronyms|CARDIAC ULTRASOUND LABORATORY INFORMATION|signed",
	        Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
	    /*Pattern5*/Pattern.compile("PROCEDURE INFORMATION:|signed",
	        Pattern.CASE_INSENSITIVE
	            | Pattern.DOTALL),
	    /*Pattern6*/Pattern.compile(
	        "RECOMMENDATIONS?|Results|Rhythm|Signed|Stress( Results)?|CONCLUSION",
	        Pattern.CASE_INSENSITIVE
	            | Pattern.DOTALL),
	    /*Pattern7*/Pattern.compile(
	        "LEFT +VENTRICULAR +FUNCTION",
	        Pattern.CASE_INSENSITIVE
	            | Pattern.DOTALL),
	    /*Pattern8*/Pattern.compile("\r\n *\\[x\\] ",
	        Pattern.CASE_INSENSITIVE
	            | Pattern.DOTALL),
	    /*Pattern9*/Pattern.compile("Sonographer",
	        Pattern.CASE_INSENSITIVE
	            | Pattern.DOTALL) /**/
	};
	protected CSI docInfo;

	@Override
	public void initialize(UimaContext aContext)
	    throws ResourceInitializationException {
		outputType = (String) aContext.getConfigParameterValue("outputType");
	}

	@Override
	public void annotate(JCas aJCas) throws AnalysisEngineProcessException {
		String coveredText = aJCas.getDocumentText();
		try {
			// Find each document type and detect the template
			int templatetype = 0;
			for (Pattern patt : templatePattern) {

				Matcher triageMatcher = patt.matcher(coveredText);
				//Find which template the document belongs to
				// If a match is found
				if (triageMatcher.find()) {
					log.debug("Found template type " + templatetype + " using pattern -- " + patt.toString());
					/* Detect the beginning of the template */
					Matcher templateStartMatcher = templateStart[templatetype].matcher(coveredText);
					if (templateStartMatcher.find()) {
						int templateBegin = templateStartMatcher.end();
						/*Detect the end of the template  */
						Matcher templateEndMatcher = templateEnd[templatetype].matcher(coveredText);
						int templateLastIndex = templateBegin;
						if (templateEndMatcher.find(templateBegin)) {
							templateLastIndex = templateEndMatcher.end();
						} else {
							templateLastIndex = coveredText.length();
						}
						// Create an instance of the class whose name is outputType
						String outputType = "gov.va.vinci.echo.types.Template";
						Template newAnnotation = (Template) this.addOutputAnnotation(outputType,
						    aJCas, templateBegin, templateLastIndex);
						newAnnotation.setTemplateType(templatetype);
					}
				}
				templatetype++;
			}
		} catch (Exception e) {
			log.error("\r\nERROR: Triage processing failed in document: \r\n"
			    + this.getReferenceLocation(aJCas) + "\r\n" + e.getMessage());
		}
	} // end process

	protected String getReferenceLocation(JCas jcas) {
		Iterator<Annotation> it = null;
		try {
			if (jcas.getAnnotationIndex(CSI.type).iterator().hasNext()) {
				it = jcas.getAnnotationIndex(CSI.type).iterator();
				CSI srcDocInfo = (CSI) it.next();
				if (srcDocInfo != null)
					this.docInfo = srcDocInfo;
				return srcDocInfo.getID();
			} else if (jcas.getAnnotationIndex(SourceDocumentInformation.type)
			    .iterator().hasNext()) {
				it = jcas.getAnnotationIndex(SourceDocumentInformation.type)
				    .iterator();
				SourceDocumentInformation srcDocInfo = (SourceDocumentInformation) it
				    .next();
				return srcDocInfo.getUri();
			} else {
				return null;
			}// else
		} catch (Exception e) {
			// Happens when CSI is not in the descriptor.
			log.error("Exception happens when CSI is not in the descriptor." + e.getLocalizedMessage());
			return null;
		}
	}

  @Override
  public LeoTypeSystemDescription getLeoTypeSystemDescription() {
    // TODO Auto-generated method stub
    return null;
  }
}
