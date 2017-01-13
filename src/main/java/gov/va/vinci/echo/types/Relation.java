

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
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.StringArray;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Wed Jan 11 09:29:43 MST 2017
 * XML source: /var/folders/9p/6qt9dlgd1ks8d036hkqb9kz00000gn/T/leoTypeDescription_42fa7ddf-ce58-47c5-bbbd-f2621b629d817793577776427376679.xml
 * @generated */
public class Relation extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Relation.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated
   * @return index of the type  
   */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Relation() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public Relation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public Relation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public Relation(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** 
   * <!-- begin-user-doc -->
   * Write your own initialization here
   * <!-- end-user-doc -->
   *
   * @generated modifiable 
   */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: Term

  /** getter for Term - gets 
   * @generated
   * @return value of the feature 
   */
  public String getTerm() {
    if (Relation_Type.featOkTst && ((Relation_Type)jcasType).casFeat_Term == null)
      jcasType.jcas.throwFeatMissing("Term", "gov.va.vinci.echo.types.Relation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Relation_Type)jcasType).casFeatCode_Term);}
    
  /** setter for Term - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setTerm(String v) {
    if (Relation_Type.featOkTst && ((Relation_Type)jcasType).casFeat_Term == null)
      jcasType.jcas.throwFeatMissing("Term", "gov.va.vinci.echo.types.Relation");
    jcasType.ll_cas.ll_setStringValue(addr, ((Relation_Type)jcasType).casFeatCode_Term, v);}    
   
    
  //*--------------*
  //* Feature: Value

  /** getter for Value - gets 
   * @generated
   * @return value of the feature 
   */
  public String getValue() {
    if (Relation_Type.featOkTst && ((Relation_Type)jcasType).casFeat_Value == null)
      jcasType.jcas.throwFeatMissing("Value", "gov.va.vinci.echo.types.Relation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Relation_Type)jcasType).casFeatCode_Value);}
    
  /** setter for Value - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setValue(String v) {
    if (Relation_Type.featOkTst && ((Relation_Type)jcasType).casFeat_Value == null)
      jcasType.jcas.throwFeatMissing("Value", "gov.va.vinci.echo.types.Relation");
    jcasType.ll_cas.ll_setStringValue(addr, ((Relation_Type)jcasType).casFeatCode_Value, v);}    
   
    
  //*--------------*
  //* Feature: Value2

  /** getter for Value2 - gets 
   * @generated
   * @return value of the feature 
   */
  public String getValue2() {
    if (Relation_Type.featOkTst && ((Relation_Type)jcasType).casFeat_Value2 == null)
      jcasType.jcas.throwFeatMissing("Value2", "gov.va.vinci.echo.types.Relation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Relation_Type)jcasType).casFeatCode_Value2);}
    
  /** setter for Value2 - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setValue2(String v) {
    if (Relation_Type.featOkTst && ((Relation_Type)jcasType).casFeat_Value2 == null)
      jcasType.jcas.throwFeatMissing("Value2", "gov.va.vinci.echo.types.Relation");
    jcasType.ll_cas.ll_setStringValue(addr, ((Relation_Type)jcasType).casFeatCode_Value2, v);}    
   
    
  //*--------------*
  //* Feature: ValueString

  /** getter for ValueString - gets 
   * @generated
   * @return value of the feature 
   */
  public String getValueString() {
    if (Relation_Type.featOkTst && ((Relation_Type)jcasType).casFeat_ValueString == null)
      jcasType.jcas.throwFeatMissing("ValueString", "gov.va.vinci.echo.types.Relation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Relation_Type)jcasType).casFeatCode_ValueString);}
    
  /** setter for ValueString - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setValueString(String v) {
    if (Relation_Type.featOkTst && ((Relation_Type)jcasType).casFeat_ValueString == null)
      jcasType.jcas.throwFeatMissing("ValueString", "gov.va.vinci.echo.types.Relation");
    jcasType.ll_cas.ll_setStringValue(addr, ((Relation_Type)jcasType).casFeatCode_ValueString, v);}    
   
    
  //*--------------*
  //* Feature: Mapping

  /** getter for Mapping - gets 
   * @generated
   * @return value of the feature 
   */
  public StringArray getMapping() {
    if (Relation_Type.featOkTst && ((Relation_Type)jcasType).casFeat_Mapping == null)
      jcasType.jcas.throwFeatMissing("Mapping", "gov.va.vinci.echo.types.Relation");
    return (StringArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Relation_Type)jcasType).casFeatCode_Mapping)));}
    
  /** setter for Mapping - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setMapping(StringArray v) {
    if (Relation_Type.featOkTst && ((Relation_Type)jcasType).casFeat_Mapping == null)
      jcasType.jcas.throwFeatMissing("Mapping", "gov.va.vinci.echo.types.Relation");
    jcasType.ll_cas.ll_setRefValue(addr, ((Relation_Type)jcasType).casFeatCode_Mapping, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for Mapping - gets an indexed value - 
   * @generated
   * @param i index in the array to get
   * @return value of the element at index i 
   */
  public String getMapping(int i) {
    if (Relation_Type.featOkTst && ((Relation_Type)jcasType).casFeat_Mapping == null)
      jcasType.jcas.throwFeatMissing("Mapping", "gov.va.vinci.echo.types.Relation");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Relation_Type)jcasType).casFeatCode_Mapping), i);
    return jcasType.ll_cas.ll_getStringArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Relation_Type)jcasType).casFeatCode_Mapping), i);}

  /** indexed setter for Mapping - sets an indexed value - 
   * @generated
   * @param i index in the array to set
   * @param v value to set into the array 
   */
  public void setMapping(int i, String v) { 
    if (Relation_Type.featOkTst && ((Relation_Type)jcasType).casFeat_Mapping == null)
      jcasType.jcas.throwFeatMissing("Mapping", "gov.va.vinci.echo.types.Relation");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Relation_Type)jcasType).casFeatCode_Mapping), i);
    jcasType.ll_cas.ll_setStringArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Relation_Type)jcasType).casFeatCode_Mapping), i, v);}
   
    
  //*--------------*
  //* Feature: Assessment

  /** getter for Assessment - gets 
   * @generated
   * @return value of the feature 
   */
  public String getAssessment() {
    if (Relation_Type.featOkTst && ((Relation_Type)jcasType).casFeat_Assessment == null)
      jcasType.jcas.throwFeatMissing("Assessment", "gov.va.vinci.echo.types.Relation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Relation_Type)jcasType).casFeatCode_Assessment);}
    
  /** setter for Assessment - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setAssessment(String v) {
    if (Relation_Type.featOkTst && ((Relation_Type)jcasType).casFeat_Assessment == null)
      jcasType.jcas.throwFeatMissing("Assessment", "gov.va.vinci.echo.types.Relation");
    jcasType.ll_cas.ll_setStringValue(addr, ((Relation_Type)jcasType).casFeatCode_Assessment, v);}    
   
    
  //*--------------*
  //* Feature: Unit

  /** getter for Unit - gets 
   * @generated
   * @return value of the feature 
   */
  public String getUnit() {
    if (Relation_Type.featOkTst && ((Relation_Type)jcasType).casFeat_Unit == null)
      jcasType.jcas.throwFeatMissing("Unit", "gov.va.vinci.echo.types.Relation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Relation_Type)jcasType).casFeatCode_Unit);}
    
  /** setter for Unit - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setUnit(String v) {
    if (Relation_Type.featOkTst && ((Relation_Type)jcasType).casFeat_Unit == null)
      jcasType.jcas.throwFeatMissing("Unit", "gov.va.vinci.echo.types.Relation");
    jcasType.ll_cas.ll_setStringValue(addr, ((Relation_Type)jcasType).casFeatCode_Unit, v);}    
   
    
  //*--------------*
  //* Feature: Modifier

  /** getter for Modifier - gets 
   * @generated
   * @return value of the feature 
   */
  public String getModifier() {
    if (Relation_Type.featOkTst && ((Relation_Type)jcasType).casFeat_Modifier == null)
      jcasType.jcas.throwFeatMissing("Modifier", "gov.va.vinci.echo.types.Relation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Relation_Type)jcasType).casFeatCode_Modifier);}
    
  /** setter for Modifier - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setModifier(String v) {
    if (Relation_Type.featOkTst && ((Relation_Type)jcasType).casFeat_Modifier == null)
      jcasType.jcas.throwFeatMissing("Modifier", "gov.va.vinci.echo.types.Relation");
    jcasType.ll_cas.ll_setStringValue(addr, ((Relation_Type)jcasType).casFeatCode_Modifier, v);}    
   
    
  //*--------------*
  //* Feature: ConceptType

  /** getter for ConceptType - gets 
   * @generated
   * @return value of the feature 
   */
  public String getConceptType() {
    if (Relation_Type.featOkTst && ((Relation_Type)jcasType).casFeat_ConceptType == null)
      jcasType.jcas.throwFeatMissing("ConceptType", "gov.va.vinci.echo.types.Relation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Relation_Type)jcasType).casFeatCode_ConceptType);}
    
  /** setter for ConceptType - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setConceptType(String v) {
    if (Relation_Type.featOkTst && ((Relation_Type)jcasType).casFeat_ConceptType == null)
      jcasType.jcas.throwFeatMissing("ConceptType", "gov.va.vinci.echo.types.Relation");
    jcasType.ll_cas.ll_setStringValue(addr, ((Relation_Type)jcasType).casFeatCode_ConceptType, v);}    
  }

    