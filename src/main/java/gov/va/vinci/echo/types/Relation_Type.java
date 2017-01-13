
/* First created by JCasGen Wed Jan 11 09:29:43 MST 2017 */
package gov.va.vinci.echo.types;

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

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.tcas.Annotation_Type;

/** 
 * Updated by JCasGen Wed Jan 11 09:29:43 MST 2017
 * @generated */
public class Relation_Type extends Annotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Relation_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Relation_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Relation(addr, Relation_Type.this);
  			   Relation_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Relation(addr, Relation_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = Relation.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("gov.va.vinci.echo.types.Relation");
 
  /** @generated */
  final Feature casFeat_Term;
  /** @generated */
  final int     casFeatCode_Term;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getTerm(int addr) {
        if (featOkTst && casFeat_Term == null)
      jcas.throwFeatMissing("Term", "gov.va.vinci.echo.types.Relation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Term);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setTerm(int addr, String v) {
        if (featOkTst && casFeat_Term == null)
      jcas.throwFeatMissing("Term", "gov.va.vinci.echo.types.Relation");
    ll_cas.ll_setStringValue(addr, casFeatCode_Term, v);}
    
  
 
  /** @generated */
  final Feature casFeat_Value;
  /** @generated */
  final int     casFeatCode_Value;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getValue(int addr) {
        if (featOkTst && casFeat_Value == null)
      jcas.throwFeatMissing("Value", "gov.va.vinci.echo.types.Relation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Value);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setValue(int addr, String v) {
        if (featOkTst && casFeat_Value == null)
      jcas.throwFeatMissing("Value", "gov.va.vinci.echo.types.Relation");
    ll_cas.ll_setStringValue(addr, casFeatCode_Value, v);}
    
  
 
  /** @generated */
  final Feature casFeat_Value2;
  /** @generated */
  final int     casFeatCode_Value2;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getValue2(int addr) {
        if (featOkTst && casFeat_Value2 == null)
      jcas.throwFeatMissing("Value2", "gov.va.vinci.echo.types.Relation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Value2);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setValue2(int addr, String v) {
        if (featOkTst && casFeat_Value2 == null)
      jcas.throwFeatMissing("Value2", "gov.va.vinci.echo.types.Relation");
    ll_cas.ll_setStringValue(addr, casFeatCode_Value2, v);}
    
  
 
  /** @generated */
  final Feature casFeat_ValueString;
  /** @generated */
  final int     casFeatCode_ValueString;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getValueString(int addr) {
        if (featOkTst && casFeat_ValueString == null)
      jcas.throwFeatMissing("ValueString", "gov.va.vinci.echo.types.Relation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_ValueString);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setValueString(int addr, String v) {
        if (featOkTst && casFeat_ValueString == null)
      jcas.throwFeatMissing("ValueString", "gov.va.vinci.echo.types.Relation");
    ll_cas.ll_setStringValue(addr, casFeatCode_ValueString, v);}
    
  
 
  /** @generated */
  final Feature casFeat_Mapping;
  /** @generated */
  final int     casFeatCode_Mapping;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getMapping(int addr) {
        if (featOkTst && casFeat_Mapping == null)
      jcas.throwFeatMissing("Mapping", "gov.va.vinci.echo.types.Relation");
    return ll_cas.ll_getRefValue(addr, casFeatCode_Mapping);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setMapping(int addr, int v) {
        if (featOkTst && casFeat_Mapping == null)
      jcas.throwFeatMissing("Mapping", "gov.va.vinci.echo.types.Relation");
    ll_cas.ll_setRefValue(addr, casFeatCode_Mapping, v);}
    
   /** @generated
   * @param addr low level Feature Structure reference
   * @param i index of item in the array
   * @return value at index i in the array 
   */
  public String getMapping(int addr, int i) {
        if (featOkTst && casFeat_Mapping == null)
      jcas.throwFeatMissing("Mapping", "gov.va.vinci.echo.types.Relation");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getStringArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_Mapping), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_Mapping), i);
	return ll_cas.ll_getStringArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_Mapping), i);
  }
   
  /** @generated
   * @param addr low level Feature Structure reference
   * @param i index of item in the array
   * @param v value to set
   */ 
  public void setMapping(int addr, int i, String v) {
        if (featOkTst && casFeat_Mapping == null)
      jcas.throwFeatMissing("Mapping", "gov.va.vinci.echo.types.Relation");
    if (lowLevelTypeChecks)
      ll_cas.ll_setStringArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_Mapping), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_Mapping), i);
    ll_cas.ll_setStringArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_Mapping), i, v);
  }
 
 
  /** @generated */
  final Feature casFeat_Assessment;
  /** @generated */
  final int     casFeatCode_Assessment;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getAssessment(int addr) {
        if (featOkTst && casFeat_Assessment == null)
      jcas.throwFeatMissing("Assessment", "gov.va.vinci.echo.types.Relation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Assessment);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setAssessment(int addr, String v) {
        if (featOkTst && casFeat_Assessment == null)
      jcas.throwFeatMissing("Assessment", "gov.va.vinci.echo.types.Relation");
    ll_cas.ll_setStringValue(addr, casFeatCode_Assessment, v);}
    
  
 
  /** @generated */
  final Feature casFeat_Unit;
  /** @generated */
  final int     casFeatCode_Unit;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getUnit(int addr) {
        if (featOkTst && casFeat_Unit == null)
      jcas.throwFeatMissing("Unit", "gov.va.vinci.echo.types.Relation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Unit);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setUnit(int addr, String v) {
        if (featOkTst && casFeat_Unit == null)
      jcas.throwFeatMissing("Unit", "gov.va.vinci.echo.types.Relation");
    ll_cas.ll_setStringValue(addr, casFeatCode_Unit, v);}
    
  
 
  /** @generated */
  final Feature casFeat_Modifier;
  /** @generated */
  final int     casFeatCode_Modifier;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getModifier(int addr) {
        if (featOkTst && casFeat_Modifier == null)
      jcas.throwFeatMissing("Modifier", "gov.va.vinci.echo.types.Relation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Modifier);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setModifier(int addr, String v) {
        if (featOkTst && casFeat_Modifier == null)
      jcas.throwFeatMissing("Modifier", "gov.va.vinci.echo.types.Relation");
    ll_cas.ll_setStringValue(addr, casFeatCode_Modifier, v);}
    
  
 
  /** @generated */
  final Feature casFeat_ConceptType;
  /** @generated */
  final int     casFeatCode_ConceptType;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getConceptType(int addr) {
        if (featOkTst && casFeat_ConceptType == null)
      jcas.throwFeatMissing("ConceptType", "gov.va.vinci.echo.types.Relation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_ConceptType);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setConceptType(int addr, String v) {
        if (featOkTst && casFeat_ConceptType == null)
      jcas.throwFeatMissing("ConceptType", "gov.va.vinci.echo.types.Relation");
    ll_cas.ll_setStringValue(addr, casFeatCode_ConceptType, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public Relation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_Term = jcas.getRequiredFeatureDE(casType, "Term", "uima.cas.String", featOkTst);
    casFeatCode_Term  = (null == casFeat_Term) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Term).getCode();

 
    casFeat_Value = jcas.getRequiredFeatureDE(casType, "Value", "uima.cas.String", featOkTst);
    casFeatCode_Value  = (null == casFeat_Value) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Value).getCode();

 
    casFeat_Value2 = jcas.getRequiredFeatureDE(casType, "Value2", "uima.cas.String", featOkTst);
    casFeatCode_Value2  = (null == casFeat_Value2) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Value2).getCode();

 
    casFeat_ValueString = jcas.getRequiredFeatureDE(casType, "ValueString", "uima.cas.String", featOkTst);
    casFeatCode_ValueString  = (null == casFeat_ValueString) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_ValueString).getCode();

 
    casFeat_Mapping = jcas.getRequiredFeatureDE(casType, "Mapping", "uima.cas.StringArray", featOkTst);
    casFeatCode_Mapping  = (null == casFeat_Mapping) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Mapping).getCode();

 
    casFeat_Assessment = jcas.getRequiredFeatureDE(casType, "Assessment", "uima.cas.String", featOkTst);
    casFeatCode_Assessment  = (null == casFeat_Assessment) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Assessment).getCode();

 
    casFeat_Unit = jcas.getRequiredFeatureDE(casType, "Unit", "uima.cas.String", featOkTst);
    casFeatCode_Unit  = (null == casFeat_Unit) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Unit).getCode();

 
    casFeat_Modifier = jcas.getRequiredFeatureDE(casType, "Modifier", "uima.cas.String", featOkTst);
    casFeatCode_Modifier  = (null == casFeat_Modifier) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Modifier).getCode();

 
    casFeat_ConceptType = jcas.getRequiredFeatureDE(casType, "ConceptType", "uima.cas.String", featOkTst);
    casFeatCode_ConceptType  = (null == casFeat_ConceptType) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_ConceptType).getCode();

  }
}



    