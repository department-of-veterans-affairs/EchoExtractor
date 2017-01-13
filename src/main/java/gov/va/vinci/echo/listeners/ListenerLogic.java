package gov.va.vinci.echo.listeners;

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

import gov.va.vinci.echo.types.*;
import gov.va.vinci.leo.AnnotationLibrarian;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.cas.StringArray;
import org.apache.uima.jcas.tcas.Annotation;

import java.util.HashMap;
import java.util.List;

/**
 * Created by vhaslcpatteo on 3/24/2015.
 */
public class ListenerLogic {


  private static Logger log =  Logger.getLogger(gov.va.vinci.leo.tools.LeoUtils.getRuntimeClass().toString());

  public static  HashMap<String, String> getInstanceData(JCas aJCas, Annotation currAnnotation) throws CASException {

        // output to csv
        HashMap<String, String> currentDoc = new HashMap<String, String>();

        String overlap = "0";
        try {
          if (AnnotationLibrarian.getAllOverlappingAnnotationsOfType(currAnnotation, RefStLV_Term.type, true)
              .size() > 0) {
            overlap = "1";
          }
        } catch (CASException ex) {
          // TODO Auto-generated catch block
          ex.printStackTrace();
        }
        getFeatures(currAnnotation, currentDoc);
        currentDoc.put("Snippets", currAnnotation.getCoveredText());
        currentDoc.put("SpanStart", Integer.toString(currAnnotation.getBegin()));
        currentDoc.put("SpanEnd", Integer.toString(currAnnotation.getEnd()));
        currentDoc.put("Overlap", overlap);


    return currentDoc;
  }

  public static void getFeatures(Annotation annotation, HashMap<String, String> currentDoc) {

    // Recurse & insert features.
    Type type = annotation.getType();
    List<Feature> featuresList = type.getFeatures();
    for (int i = 0; i < featuresList.size(); ++i) {
      Feature feature = (Feature) featuresList.get(i);
      String nameFeat = feature.getName();

      if (nameFeat.startsWith("uima.")) {
        continue;
      }

      Type typeRange = feature.getRange();

      if (typeRange.isPrimitive()) {

        currentDoc.put(feature.getShortName(),
            annotation.getFeatureValueAsString(feature));

      } else {
        // This is a Feature Structure, recurse through it.
        FeatureStructure fs = annotation.getFeatureValue(feature);

        if (fs == null) {
          continue;
        }

        /** See if this is a reference to another annotation. If so, add it. **/
        if (fs instanceof Annotation) {
          currentDoc.put(feature.getShortName(), annotation.getFeatureValue(feature).toString());
          log.debug("Referenced annotation in feature has not been stored. This is likely because the referenced "
              +
              "annotation type was not included in input type list to store.");

          continue;
        } else if (fs instanceof StringArray) {
          //TODO: sort the array alphabetically?
          int counter = 0;
          String out = "";
          if (((StringArray) fs).toArray().length > 0) {

            if (((StringArray) fs).toArray().length > 1) {
              for (String value : ((StringArray) fs).toArray()) {
                if (StringUtils.isBlank(out))
                  out = "|" + counter + ":" + value + "|";
                else
                  out = out + counter + ":" + value + "|";
                counter++;
              }
              currentDoc.put(feature.getShortName(), out);
              continue;
            } else {
              String value = ((StringArray) fs).toArray()[0];
              currentDoc.put(feature.getShortName(), value);
              continue;
            }
          }
        } else if (fs instanceof FSArray) {
          FeatureStructure[] fsArray = ((FSArray) fs).toArray();

          int counter = 0;
          for (FeatureStructure item : fsArray) {
            // TODO Implement, especially on recursive features.

          }
          continue;
        } else {
          throw new RuntimeException(
              "Unknown Feature Structure Type:"
                  + fs.getType().getName());
        }
      }
    }
  }

}
