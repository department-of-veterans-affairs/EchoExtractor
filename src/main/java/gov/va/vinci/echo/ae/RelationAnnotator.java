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
import java.util.Arrays;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.StringArray;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import gov.va.vinci.echo.types.*;
import gov.va.vinci.leo.tools.LeoUtils;
import gov.va.vinci.leo.AnnotationLibrarian;
import gov.va.vinci.leo.ae.LeoBaseAnnotator;
import gov.va.vinci.leo.descriptors.LeoTypeSystemDescription;

public class RelationAnnotator extends LeoBaseAnnotator {

  private static final Logger log = Logger.getLogger(LeoUtils.getRuntimeClass().toString());

  public enum CONCEPT_TYPE {
    EF, PAP, ConceptCollection, Concept, TenConcept
  }

  ;

  public String efString = "left ventricular ejection fraction";
  public String papString = "pulmonary artery pressure";
  private String singleTargetMapping = Relation1.class.getCanonicalName();
  private String singleMapping = Relation2.class.getCanonicalName();
  private String multipleMappings = Relation3.class.getCanonicalName();
  private String noMappings = Relation4.class.getCanonicalName();
  protected Pattern numericPatterns;


  /* (non-Javadoc)
   * @see gov.va.vinci.leo.ae.LeoBaseAnnotator#initialize(org.apache.uima.UimaContext)
   */
  @Override
  public void initialize(UimaContext aContext) throws ResourceInitializationException {
    // initialize digit patterns
    String regex = "(\\d+(\\.\\d+)?)|(\\.\\d+)";
    numericPatterns = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

  }

  /* (non-Javadoc)
   * @see gov.va.vinci.leo.ae.LeoBaseAnnotator#process(org.apache.uima.jcas.JCas)
   */
  @Override
  public void annotate(JCas aJCas) throws AnalysisEngineProcessException {
    // The task is to link concepts-values-units into relations
    // Step 1: get Template
    // Step 2: if there is an anchor, take that anchor as a concept
    // Step 3: check what type of concept --  Concept, TenConcept, ConceptCollection
    // Step 4: get numeric value and unit within the template
    // Step 5: Create Relation annotation
    // -- mapping should be done in a different annotator

    FSIterator<Annotation> templates = this.getAnnotationListForType(aJCas,
        RelationPattern.class.getCanonicalName());
    while (templates.hasNext()) {
      try {
        RelationPattern template = (RelationPattern) templates.next();
        //checkNumericTemplate(aJCas, template);
        checkTemplate(aJCas, template);
      } catch (Exception e) {
        log.warn("Failed processing numeric template patterns.");
      }
    }

  }


  /**
   * @param anchorAnnotation
   * @return
   */
  private boolean isEF(Mapping anchorAnnotation) {
    try {
      String[] maps = ((StringArray) anchorAnnotation.getMapping()).toStringArray();
      if (maps.length == 1) {
        for (String b : maps) {
          if (b.equals(efString)) {
            return true;
          }
        }
      } else {
        return false;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  private boolean isPAP(Mapping anchorAnnotation) {
    try {
      String[] maps = ((StringArray) anchorAnnotation.getMapping()).toStringArray();
      if (maps.length == 1) {
        for (String b : maps) {
          if (b.equals(papString)) {
            return true;
          }
        }
      } else {
        return false;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }


  /**
   *
   */
  private void checkTemplate(JCas aJCas, AnnotationPattern template) {
    String concept = "";
    String strValue = "";
    String assessment = "";
    String unit = "";
    String range = "";
    String anchor = "";
    String modifier = "";
    Mapping anchorAnnotation = null;
    //Create a different relation type for each concept type
    if (template.getAnchor() != null) {
      anchor = template.getAnchor().getCoveredText(); // this is my concept
      anchorAnnotation = (Mapping) template.getAnchor();
      if (StringUtils.isNotBlank(anchor)) {
        concept = normalize(anchor, true);
        // check the number of mappings
        StringArray maps = ((Mapping) anchorAnnotation).getMapping();
        String outTypeName = "";
        if (maps.size() == 1) {
          if (targetConcepts.contains(maps.get(0))) {
            outTypeName = singleTargetMapping;
          } else {
            outTypeName = singleMapping;
          }
        } else if (maps.size() > 1) {
          outTypeName = multipleMappings;
        } else {
          outTypeName = noMappings;
        }
        ArrayList<Annotation> values = new ArrayList<Annotation>();
        ArrayList<Annotation> units = new ArrayList<Annotation>();
        ArrayList<Annotation> ranges = new ArrayList<Annotation>();
        ArrayList<Annotation> modifiers = new ArrayList<Annotation>();
        ArrayList<Annotation> assessments = new ArrayList<Annotation>();
        try {
          values = (ArrayList<Annotation>) AnnotationLibrarian
              .getAllCoveredAnnotationsOfType(template, NumericValue.type, true);
          units = (ArrayList<Annotation>) AnnotationLibrarian
              .getAllCoveredAnnotationsOfType(template, Units.type, true);
          ranges = (ArrayList<Annotation>) AnnotationLibrarian
              .getAllCoveredAnnotationsOfType(template, Range.type, true);
          assessments = (ArrayList<Annotation>) AnnotationLibrarian.getAllCoveredAnnotationsOfType(template,
              QValue.type, true);
          // TODO: Get modifier - closest Anatomy to the left if not separated by blank

          HashSet<String> tempSet = new HashSet<String>();
          if (assessments.size() > 0) {
            for (Annotation ann : assessments) {
              tempSet.add(normalize(ann.getCoveredText(), false));
            }
          }
          for (String s : tempSet) {
            if (StringUtils.isBlank(assessment)) {
              assessment = s;
            } else
              assessment = assessment + "; " + s;
          }

          if (values.size() > 0) {
            strValue = values.get(0).getCoveredText();
          }

          if (units.size() > 0)
            unit = normalize(units.get(0).getCoveredText(), true);
          if (ranges.size() > 0) {
            range = (ranges.get(0).getCoveredText()).replace("(", "")
                .replace(")", "").replaceAll("\\s+", " ").trim();
          }

          modifiers.addAll((ArrayList<Annotation>) AnnotationLibrarian.getPreviousAnnotationsOfType(template,
              Header.type, 1, true));
          modifiers.addAll((ArrayList<Annotation>) AnnotationLibrarian.getAllOverlappingAnnotationsOfType(
              template, Modifier.type, true));
          modifiers.addAll(AnnotationLibrarian.getPreviousAnnotationsOfType((new Annotation(
              aJCas, template.getEnd(), template.getEnd())), aJCas.getCasType(Anatomy.type), 1, true));
          // Combine all modifiers into one string.
          HashSet<String> mods = new HashSet<String>();
          if (modifiers.size() > 0) {
            for (Annotation str : modifiers) {
              String n = normalize(str.getCoveredText(), false);
              if (StringUtils.isNotBlank(n)) {
                mods.add(n);
              }// don't add if it is blank
            }
          }
          for (String mod : mods) {
            if (StringUtils.isBlank(modifier))
              modifier = mod;
            else
              modifier = modifier + ";" + mod;
          }

          if (StringUtils.isNotBlank(assessment)
              || StringUtils.isNotBlank(strValue)
              || StringUtils.isNotBlank(range)) {
            //// Starting building output annotation
            Relation outAnnotation = null;

            if (isEF(anchorAnnotation)) {
              //conceptType = "EF";

              if (modifier.contains("rv") || modifier.contains("right")) {
                // skip this instance and move to the next template annotation
                return;
              } else {
                //TODO: additional check if there is right in the previous several anatomies.
                ArrayList<Annotation> anatomies = new ArrayList<Annotation>();
                anatomies = (ArrayList<Annotation>) AnnotationLibrarian.getPreviousAnnotationsOfType(
                    (new Annotation(aJCas, template.getEnd(),
                        template.getEnd())), aJCas.getCasType(Anatomy.type), 5, true);
                boolean notFoundRight = true;
                checkAnatomy:
                // this is a label for anatomy loop
                for (Annotation a : anatomies) {
                  String anatomy = a.getCoveredText().toLowerCase();
                  if (notFoundRight) {
                    //TODO this code does not seem to work. Need to check the unit tests.
                    if (anatomy.contains("rv") || anatomy.contains("right")) {
                      notFoundRight = false;
                      return;

                    } else if ((a.getCoveredText().toLowerCase()).contains("lv")
                        || (a.getCoveredText().toLowerCase()).contains("left")) {
                      break checkAnatomy; //exits the loop when left is found before right
                    }
                  } else {
                    continue;
                  }
                }

                if (notFoundRight) {
                  if (StringUtils.isBlank(strValue)) {
                    if (StringUtils.isNotBlank(range)) {
                      strValue = range;
                    }
                  }
                  if (StringUtils.isNotBlank(strValue) || StringUtils.isNotBlank(assessment)) {
                    outAnnotation = (Relation) this.addOutputAnnotation(outTypeName,
                        aJCas, template.getBegin(), template.getEnd());
                  } else { // exit if strValue is still blank
                    return;
                  }
                } // end if when creating output annotation
              } // end else if rv
            } else if (isPAP(anchorAnnotation)) {
              if (StringUtils.isBlank(strValue)) {
                if (StringUtils.isNotBlank(range)) {
                  strValue = range;
                }
              }
              if (StringUtils.isNotBlank(strValue) || StringUtils.isNotBlank(assessment)) {
                outAnnotation = (Relation) this.addOutputAnnotation(outTypeName,
                    aJCas, template.getBegin(), template.getEnd());
              } else { // exit if strValue is still blank
                return;
              }
              //conceptType = "PAP";
            } else {
              if (StringUtils.isNotBlank(strValue) || StringUtils.isNotBlank(assessment)) {
                outAnnotation = (Relation) this.addOutputAnnotation(outTypeName, aJCas,
                    template.getBegin(), template.getEnd());
                // conceptType = "Any other concept";
              } else {
                return;
              }
            }

            if (outAnnotation != null) {
              String value = "";
              String value2 = "";
              Matcher matching = numericPatterns.matcher(strValue);
              if (matching.find()) {
                value = strValue.substring(matching.start(), matching.end());
              }
              if (matching.find()) {
                value2 = strValue.substring(matching.start(), matching.end());
              }

              String pattern = template.getPattern();
              strValue = normalize(strValue, false);
              outAnnotation.setMapping(anchorAnnotation.getMapping());
              outAnnotation.setTerm(concept);
              outAnnotation.setValue(value);
              outAnnotation.setValue2(value2);
              outAnnotation.setValueString(strValue);
              outAnnotation.setUnit(unit);
              outAnnotation.setModifier(modifier);
              outAnnotation.setAssessment(assessment);
              outAnnotation.setConceptType(pattern);
              AnnotationLibrarian.trimAnnotation(outAnnotation);
            }
          }
        } catch (AnalysisEngineProcessException e) {
          log.warn("Processing TemplatePattern failed ");
          e.printStackTrace();
        } catch (CASException e) {
          log.warn("Processing TemplatePattern failed ");
          e.printStackTrace();
        } catch (Exception e) {
          log.warn("Processing TemplatePattern failed ");
          e.printStackTrace();
        }
      }
    }
  }

  /***/
  /**
   * Normalization removes [,][;][=][:][multiple -][|][*]
   * , replaces multiple whitespaces with one
   * and sets to lower case
   *
   * @param concept
   * @return
   */

  private String normalize(String concept, boolean removeIfNumeric) {
    if (StringUtils.isNotBlank(concept)) {
      String str = concept.toLowerCase()
          .replaceAll(",", " ")
          .replaceAll(";", " ")
          .replaceAll("==+", " ")
              //.replaceAll(":", "")
          .replaceAll("--+", " ")
          .replaceAll("\\|", "")
          .replaceAll("\\*", " ")
          .replaceAll("\\s+", " ").trim();
      if (removeIfNumeric) {
        try {
          Double.parseDouble(str);
          return "";
        } catch (Exception e) {
          return str;
        }
      } else {
        return str;
      }
    } else {
      return "";
    }
  }

  /**
   *
   */
  public ArrayList<String> targetConcepts = new ArrayList<String>(
      Arrays.asList("aortic valve max pressure gradient", //1
          "aortic valve mean gradient", //2
          "aortic valve orifice area", //3
          "aortic valve regurgitation",//4
          "aortic valve regurgitation peak velocity", //5
          "aortic valve stenosis",  //6
          "e/e prime ratio",  //7
          "interventricular septum dimension at end diastole", //8
          "left atrium size at end systole", //9

          "left ventricular contractility",  //10
          "left ventricular dimension at end diastole", //11
          "left ventricular dimension at end systole",  //12

          "left ventricular ejection fraction",   //13

          "left ventricular hypertrophy",   //14
          "left ventricular posterior wall thickness at end diastole", //15
          "left ventricular size",          //16
          "left ventricular systolic function",  //17
          "mitral valve mean gradient",     //18
          "mitral valve orifice area",      //19
          "mitral valve regurgitation",     //20
          "mitral valve regurgitation peak velocity", //21
          "mitral valve stenosis",          //22
          "pulmonary artery pressure",      //23
          "right atrial pressure",          //24
          "tricuspid valve mean gradient",  //25
          "tricuspid valve orifice area",   //26
          "tricuspid valve regurgitation",  //27
          "tricuspid valve regurgitation peak velocity"  //28

      ));

  @Override
  public LeoTypeSystemDescription getLeoTypeSystemDescription() {
    // TODO Auto-generated method stub
    return null;
  }
}
