package gov.va.vinci.echo.pipeline;

/*
 * #%L
 * Leo Examples
 * %%
 * Copyright (C) 2010 - 2014 Department of Veterans Affairs
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

import gov.va.vinci.echo.ae.AnnotationFilter;
import gov.va.vinci.echo.types.AnnotationPattern;
import gov.va.vinci.echo.types.Range;
import gov.va.vinci.leo.annotationpattern.ae.AnnotationPatternAnnotator;
import gov.va.vinci.leo.descriptors.LeoAEDescriptor;
import gov.va.vinci.leo.descriptors.LeoTypeSystemDescription;
import gov.va.vinci.leo.pipeline.PipelineInterface;
import gov.va.vinci.leo.regex.ae.RegexAnnotator;
import gov.va.vinci.leo.tools.LeoUtils;
import gov.va.vinci.leo.types.TypeLibrarian;
import org.apache.log4j.Logger;
import org.apache.uima.resource.metadata.TypeDescription;
import org.apache.uima.resource.metadata.impl.TypeDescription_impl;

import java.io.File;
import java.util.ArrayList;

public class Pipeline implements PipelineInterface {
  LeoAEDescriptor pipeline = null;
  LeoTypeSystemDescription description = null;
  String regexParentType = "gov.va.vinci.leo.regex.types.RegularExpressionType";
  String apaParentType = "gov.va.vinci.leo.annotationpattern.types.AnnotationPatternType";
  protected static String RESOURCE_PATH = "src" + File.separator + "main" + File.separator + "resources" + File.separator;

  public static final Logger log = Logger.getLogger(LeoUtils.getRuntimeClass().toString());

  /**
   * Constructor
   *
   * @throws Exception
   */

  public Pipeline() throws Exception {
        /*
     * List for holding our annotators. This list, and the order of the list
		 * created the annotator pipeline.
		 */
    ArrayList<LeoAEDescriptor> annotators = new ArrayList<LeoAEDescriptor>();
    LeoTypeSystemDescription types = getLeoTypeSystemDescription();
    log.info("Resource files will be used  : \r\n"
        + (new java.io.File(RESOURCE_PATH)).getAbsolutePath());

    /*************************************************************************/
    /***************         Start of initial regexes   **********************/
    /*************************************************************************/

    ////////  Term boundaries
		    /* Find the anatomy such as left ventricle wall */
    ;
    if (!new java.io.File(RESOURCE_PATH + "anatomy.regex").exists()) {
      log.error("Resource file is missing. System exit!");
      System.exit(0);
    }
    annotators.add(createRegexAnnotator("AnatomyAnnotator",
        RESOURCE_PATH + "anatomy.regex",
        gov.va.vinci.echo.types.Anatomy.class.getCanonicalName(), types))
		    /*  Find measurement such as thickness or velocity */
    ;
    annotators.add(createRegexAnnotator("MeasurementAnnotator",
        RESOURCE_PATH + "measurement.regex",
        gov.va.vinci.echo.types.Measurement.class.getCanonicalName(), types))
		    /*  Find other possible concepts  */
    ;
    annotators.add(createRegexAnnotator("ConceptAnnotator",
        RESOURCE_PATH + "concept.regex",
        gov.va.vinci.echo.types.Concept.class.getCanonicalName(), types))

    ////////////  Values and assessment
		    /*  Find qualitative values such as normal, mild, or severe */
    ;
    annotators.add(createRegexAnnotator("QValueAnnotator",
        RESOURCE_PATH + "qValues.regex",
        gov.va.vinci.echo.types.QValue.class.getCanonicalName(), types))
		    /* Find numerical values such as 1, 1.1, 0.1, or .1 */
    ;
    annotators.add(createRegexAnnotator("NumericValueAnnotator",
        RESOURCE_PATH + "numericValues.regex",
        gov.va.vinci.echo.types.NumericValue.class.getCanonicalName(), types))
		    /* Find units of measure such as cm, mm, mmHz */
    ;
    annotators.add(createRegexAnnotator("UnitsAnnotator",
        RESOURCE_PATH + "units.regex",
        gov.va.vinci.echo.types.Units.class.getCanonicalName(), types))

    ///// Support annotations
		    /*  Find some middle stuff that usually goes between concept and values */
    ;
    annotators.add(createRegexAnnotator("MiddleAnnotator",
        RESOURCE_PATH + "middleStuff.regex",
        gov.va.vinci.echo.types.MiddleStuff.class.getCanonicalName(), types))
		    /* Find method of image or measurement acquisition  */
    ;
    annotators.add(createRegexAnnotator("MethodAnnotator",
        RESOURCE_PATH + "method.regex",
        gov.va.vinci.echo.types.Method.class.getCanonicalName(), types))
		    /* Create header annotations that would mark a possible header for the concepts that follow */
    ;
    annotators.add(createRegexAnnotator("HeaderAnnotator",
        RESOURCE_PATH + "conceptHeader.regex",
        gov.va.vinci.echo.types.Header.class.getCanonicalName(), types))
		    /* */
    ;
    annotators.add(createRegexAnnotator("ExcludeValueAnnotator",
        RESOURCE_PATH + "valueExclude.regex",
        gov.va.vinci.echo.types.ExcludeValue.class.getCanonicalName(), types))
		    /*  */
    ;
    annotators.add(createRegexAnnotator("ExcludeConceptAnnotator",
        RESOURCE_PATH + "conceptExclude.regex",
        gov.va.vinci.echo.types.ExcludeConcept.class.getCanonicalName(), types))
		    /*  */
    ;
    annotators.add(createRegexAnnotator("ModifierAnnotator",
        RESOURCE_PATH + "modifier.regex",
        gov.va.vinci.echo.types.Modifier.class.getCanonicalName(), types))
		    /* */
    /********************************************************************************************/
    /****    End of initial regexes      ***/
    /********************************************************************************************/

    /**** Servicing annotations, filtering, and combining ***************************************/
		    /*  Annotation filter removes some types if they overlap with other types:
		     *    Removing overlapping numeric and assessment values and term annotations*/
    ;
    annotators.add(new LeoAEDescriptor()
        .setName("AnnotationFilter1Annotator")
        .setImplementationName(AnnotationFilter.class.getCanonicalName())
        .addParameterSetting("debug", false, false, "Boolean", false)
        .addParameterSetting("step", true, false, "String",
            AnnotationFilter.FilterType.BASIC_TYPES.name())
        .addTypeSystemDescription(types));

		    /* Patterns to combine numeric values into ranges */
    annotators.add(
            new AnnotationPatternAnnotator()
                    .setResource(RESOURCE_PATH + "range.pattern")
                    .setOutputType(Range.class.getCanonicalName())
                    .setInputTypes(gov.va.vinci.echo.types.NumericValue.class.getCanonicalName())
                    .setName("RangeAnnotator").getLeoAEDescriptor().addTypeSystemDescription(types)
    )
		    /*  The following annotator combines a set of consecutive Anatomy and Measurement annotations
		     * into one. That is a different way to find concepts*/
    ;
    annotators.add(new LeoAEDescriptor()
        .setName("ConceptCollectorAnnotator")
        .setImplementationName(gov.va.vinci.echo.ae.ConceptCollectorAnnotator.class.getCanonicalName())
        .addTypeSystemDescription(types))
		    /*  Annotation filter removes some types if they overlap with other types*/
    ;
    annotators.add(new LeoAEDescriptor()
        .setName("AnnotationFilterAnnotator")
        .setImplementationName(AnnotationFilter.class.getCanonicalName())
        .addParameterSetting("debug", false, false, "Boolean", false)
        .addParameterSetting("step", true, false, "String", AnnotationFilter.FilterType.BASIC_APA.name())
        .addTypeSystemDescription(types))

    /**             */
    ;
    annotators.add(new LeoAEDescriptor()
        .setName("ConceptMapping")
        .setImplementationName(gov.va.vinci.echo.ae.ConceptMapping.class.getCanonicalName())
        .addParameterSetting(
            gov.va.vinci.echo.ae.ConceptMapping.Param.INPUT_TYPE.getName(), false, true, "String",
            new String[]{gov.va.vinci.echo.types.ConceptCollection.class.getCanonicalName(),
                gov.va.vinci.echo.types.Concept.class.getCanonicalName()})
            //Lookup feature name is a multivalue parameter
        .addParameterSetting(gov.va.vinci.echo.ae.ConceptMapping.Param.LOOKUP_FEATURE.getName(), false, true, "String",
            null)
        .addParameterSetting(gov.va.vinci.echo.ae.ConceptMapping.Param.DATA_FILE_NAME.getName(), false, false, "String",
            RESOURCE_PATH + "dictionary.csv")
        .addParameterSetting(gov.va.vinci.echo.ae.ConceptMapping.Param.MATCH_TYPE.getName(), false, false, "String",
            gov.va.vinci.lookup.MatchType.CONTAINS.getName())
        .addParameterSetting(gov.va.vinci.echo.ae.ConceptMapping.Param.OUTPUT_TYPE.getName(), false, false, "String",
            gov.va.vinci.echo.types.Mapping.class.getCanonicalName())
        .addParameterSetting(gov.va.vinci.echo.ae.ConceptMapping.Param.CLASSIFICATION_FEATURE.getName(), false, false,
            "String", "Mapping")
        .addParameterSetting(gov.va.vinci.echo.ae.ConceptMapping.Param.DEBUG.getName(), false, false, "Boolean", false)
        .setTypeSystemDescription(types))
    /**                  */
    ;
    annotators.add(new LeoAEDescriptor()
        .setName("ConceptDisambiguation")
        .setImplementationName(gov.va.vinci.echo.ae.DisambiguationAnnotator.class.getCanonicalName())
        .addParameterSetting(
            gov.va.vinci.echo.ae.DisambiguationAnnotator.Param.INPUT_TYPE.getName(),
            false, true, "String", new String[]{gov.va.vinci.echo.types.Mapping.class.getCanonicalName()})
            //Lookup feature name is a multivalue parameter
        .addParameterSetting(gov.va.vinci.echo.ae.DisambiguationAnnotator.Param.LOOKUP_FEATURE.getName(), false, true,
            "String", null)
        .addParameterSetting(gov.va.vinci.echo.ae.DisambiguationAnnotator.Param.DATA_FILE_NAME.getName(), false, false,
            "String",
            RESOURCE_PATH + "opposites.csv")
        .addParameterSetting(gov.va.vinci.echo.ae.DisambiguationAnnotator.Param.MATCH_TYPE.getName(), false, false,
            "String",
            gov.va.vinci.lookup.MatchType.CONTAINS.getName())
        .addParameterSetting(gov.va.vinci.echo.ae.DisambiguationAnnotator.Param.OUTPUT_TYPE.getName(), false, false,
            "String",
            gov.va.vinci.echo.types.Mapping.class.getCanonicalName())
        .addParameterSetting(gov.va.vinci.echo.ae.DisambiguationAnnotator.Param.CLASSIFICATION_FEATURE.getName(), false,
            false, "String",
            "Mapping")
        .addParameterSetting(gov.va.vinci.echo.ae.DisambiguationAnnotator.Param.DEBUG.getName(), false, false, "Boolean",
            false)
        .setTypeSystemDescription(types))

    /********************                 Defining patterns *************************************/

    /**************/
		    /*  Annotating <Concept> <NumericValue> patterns     */
    ;



    annotators.add(
            new AnnotationPatternAnnotator()
                    .setResource(RESOURCE_PATH + "relation.pattern")
                    .setOutputType(gov.va.vinci.echo.types.RelationPattern.class.getCanonicalName())
                    .setName("RelationPatternAnnotator")
                    .getLeoAEDescriptor().addTypeSystemDescription(types)
    )
		    /*  Annotation filter removes some types if they overlap with other types*/
    ;
    annotators.add(new LeoAEDescriptor()
        .setName("AnnotationFilterAnnotator")
        .setImplementationName(AnnotationFilter.class.getCanonicalName())
        .addParameterSetting("step", true, false, "String", AnnotationFilter.FilterType.TEMPLATES.name())
        .addTypeSystemDescription(types))
		    /* Create final annotations*/
    ;
    annotators.add(new LeoAEDescriptor()
        .setName("RelationAnnotator")
        .setImplementationName(gov.va.vinci.echo.ae.RelationAnnotator.class.getCanonicalName())
        .addTypeSystemDescription(types))
		/**/
    ;
    pipeline = new LeoAEDescriptor(annotators);

  }

  @Override
  public LeoAEDescriptor getPipeline() {
    return pipeline;
  }

  /**
   *
   */
  @Override
  public LeoTypeSystemDescription getLeoTypeSystemDescription() {

    description = new LeoTypeSystemDescription();
    // Relation Type description
    TypeDescription relationFtsd;
    String relationParent = "gov.va.vinci.echo.types.Relation";
    relationFtsd = new TypeDescription_impl(relationParent, "", "uima.tcas.Annotation");
    relationFtsd.addFeature("Term", "", "uima.cas.String"); // Extracted normalized string
    relationFtsd.addFeature("Value", "", "uima.cas.String"); // Numeric  value
    relationFtsd.addFeature("Value2", "", "uima.cas.String"); // Numeric  value
    relationFtsd.addFeature("ValueString", "", "uima.cas.String"); // Numeric  value

    relationFtsd.addFeature("Mapping", "", "uima.cas.StringArray", "uima.cas.String", true); // String identifier of the mapped concept
    relationFtsd.addFeature("Assessment", "", "uima.cas.String"); // Assessment value
    relationFtsd.addFeature("Unit", "", "uima.cas.String"); // Measurement units
    // relationFtsd.addFeature("Range", "", "uima.cas.String"); // Normal range - not needed.
    // relationFtsd.addFeature("Method", "", "uima.cas.String"); // Not needed for now
    relationFtsd.addFeature("Modifier", "", "uima.cas.String");
    relationFtsd.addFeature("ConceptType", "", "uima.cas.String");

    // Pattern Type description
    TypeDescription regexFtsd;
    String regexParent = "gov.va.vinci.echo.types.Regex";
    regexFtsd = new TypeDescription_impl(regexParent, "", "uima.tcas.Annotation");
    regexFtsd.addFeature("Pattern", "", "uima.cas.String");
    regexFtsd.addFeature("Groups", "", "uima.cas.StringArray");

    // Pattern Type description
    TypeDescription patternFtsd;
    String patternParent = "gov.va.vinci.echo.types.AnnotationPattern";
    patternFtsd = new TypeDescription_impl(patternParent, "", "uima.tcas.Annotation");
    patternFtsd.addFeature("anchor", "", "uima.tcas.Annotation");
    patternFtsd.addFeature("anchorPattern", "", "uima.cas.String");
    patternFtsd.addFeature("target", "", "uima.tcas.Annotation");
    patternFtsd.addFeature("targetPattern", "", "uima.cas.String");
    patternFtsd.addFeature("pattern", "", "uima.cas.String");

    TypeDescription templateFtsd;
    String templateParent = "gov.va.vinci.echo.types.Template";
    templateFtsd = new TypeDescription_impl(templateParent, "", "uima.tcas.Annotation");
    templateFtsd.addFeature("TemplateType", "", "uima.cas.Integer");

    TypeDescription mappingFtsd;
    String mappingParent = "gov.va.vinci.echo.types.Mapping";
    mappingFtsd = new TypeDescription_impl(mappingParent, "", "uima.tcas.Annotation");
    mappingFtsd.addFeature("Mapping", "", "uima.cas.StringArray", "uima.cas.String", true);
    //regexFtsd.addFeature("Mapping", "", "uima.cas.StringArray");

    // ///// Total type definition
    try {
      description.addType(TypeLibrarian.getCSITypeSystemDescription())
          .addType(TypeLibrarian.getRelationshipAnnotationTypeSystemDescription())
          .addType(TypeLibrarian.getValidationAnnotationTypeSystemDescription())
          .addType("gov.va.vinci.echo.types.RefStAssessment", "", "uima.tcas.Annotation") //Assessment
          .addType("gov.va.vinci.echo.types.RefStValue", "", "uima.tcas.Annotation") // Value
          .addType("gov.va.vinci.echo.types.RefStLV_Term", "", "uima.tcas.Annotation") //LV_Systolic_Function
          .addType("gov.va.vinci.echo.types.RefStOthers", "", "uima.tcas.Annotation") // Other ref st annotations
          .addType(regexFtsd)
          .addType(patternFtsd)
          .addType(templateFtsd)
          .addType(mappingFtsd)
          .addType("gov.va.vinci.echo.types.Anatomy", "", regexParent)
          .addType("gov.va.vinci.echo.types.Measurement", "", regexParent)
          .addType("gov.va.vinci.echo.types.ExcludeValue", "", regexParent)
          .addType("gov.va.vinci.echo.types.MiddleStuff", "", regexParent)
          .addType("gov.va.vinci.echo.types.ExcludeConcept", "", regexParent)
          .addType("gov.va.vinci.echo.types.Concept", "", regexParent)
          .addType("gov.va.vinci.echo.types.ConceptCollection", "", regexParent)
          .addType("gov.va.vinci.echo.types.QValue", "", regexParent)
          .addType("gov.va.vinci.echo.types.NumericValue", "", regexParent)
          .addType("gov.va.vinci.echo.types.Units", "", regexParent)
          .addType("gov.va.vinci.echo.types.Method", "", regexParent)
          .addType("gov.va.vinci.echo.types.Header", "", regexParent)
          .addType("gov.va.vinci.echo.types.Modifier", "", regexParent)
          .addType("gov.va.vinci.echo.types.Range", "", patternParent)
          .addType("gov.va.vinci.echo.types.RelationPattern", "", patternParent)
          .addType(relationFtsd)
          .addType(relationParent + "1", "", relationParent) // for target concepts with single mapping
          .addType(relationParent + "2", "", relationParent) // for all other concepts with single mapping
          .addType(relationParent + "3", "", relationParent) // for other concepts with multiple mappings
          .addType(relationParent + "4", "", relationParent) // for other concepts with no mappings
          .addType("gov.va.vinci.echo.types.ValidationRelation", "", relationParent);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return description;
  }

  /**
   * @param name
   * @param resourcePath
   * @param outputType
   * @param types
   * @return
   * @throws Exception
   */
  public LeoAEDescriptor createRegexAnnotator(String name,
                                              String resourcePath, String outputType, LeoTypeSystemDescription types) throws Exception {

    return new RegexAnnotator()
              .setResource(resourcePath)
              .setName(name)
              .setOutputType(outputType)
              .getLeoAEDescriptor()
              .addTypeSystemDescription(types);

  }
}
