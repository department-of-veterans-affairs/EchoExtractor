

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

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Wed Jan 11 09:29:43 MST 2017
 * XML source: /var/folders/9p/6qt9dlgd1ks8d036hkqb9kz00000gn/T/leoTypeDescription_42fa7ddf-ce58-47c5-bbbd-f2621b629d817793577776427376679.xml
 * @generated */
public class AnnotationPattern extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(AnnotationPattern.class);
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
  protected AnnotationPattern() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public AnnotationPattern(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public AnnotationPattern(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public AnnotationPattern(JCas jcas, int begin, int end) {
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
  //* Feature: anchor

  /** getter for anchor - gets 
   * @generated
   * @return value of the feature 
   */
  public Annotation getAnchor() {
    if (AnnotationPattern_Type.featOkTst && ((AnnotationPattern_Type)jcasType).casFeat_anchor == null)
      jcasType.jcas.throwFeatMissing("anchor", "gov.va.vinci.echo.types.AnnotationPattern");
    return (Annotation)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((AnnotationPattern_Type)jcasType).casFeatCode_anchor)));}
    
  /** setter for anchor - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setAnchor(Annotation v) {
    if (AnnotationPattern_Type.featOkTst && ((AnnotationPattern_Type)jcasType).casFeat_anchor == null)
      jcasType.jcas.throwFeatMissing("anchor", "gov.va.vinci.echo.types.AnnotationPattern");
    jcasType.ll_cas.ll_setRefValue(addr, ((AnnotationPattern_Type)jcasType).casFeatCode_anchor, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: anchorPattern

  /** getter for anchorPattern - gets 
   * @generated
   * @return value of the feature 
   */
  public String getAnchorPattern() {
    if (AnnotationPattern_Type.featOkTst && ((AnnotationPattern_Type)jcasType).casFeat_anchorPattern == null)
      jcasType.jcas.throwFeatMissing("anchorPattern", "gov.va.vinci.echo.types.AnnotationPattern");
    return jcasType.ll_cas.ll_getStringValue(addr, ((AnnotationPattern_Type)jcasType).casFeatCode_anchorPattern);}
    
  /** setter for anchorPattern - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setAnchorPattern(String v) {
    if (AnnotationPattern_Type.featOkTst && ((AnnotationPattern_Type)jcasType).casFeat_anchorPattern == null)
      jcasType.jcas.throwFeatMissing("anchorPattern", "gov.va.vinci.echo.types.AnnotationPattern");
    jcasType.ll_cas.ll_setStringValue(addr, ((AnnotationPattern_Type)jcasType).casFeatCode_anchorPattern, v);}    
   
    
  //*--------------*
  //* Feature: target

  /** getter for target - gets 
   * @generated
   * @return value of the feature 
   */
  public Annotation getTarget() {
    if (AnnotationPattern_Type.featOkTst && ((AnnotationPattern_Type)jcasType).casFeat_target == null)
      jcasType.jcas.throwFeatMissing("target", "gov.va.vinci.echo.types.AnnotationPattern");
    return (Annotation)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((AnnotationPattern_Type)jcasType).casFeatCode_target)));}
    
  /** setter for target - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setTarget(Annotation v) {
    if (AnnotationPattern_Type.featOkTst && ((AnnotationPattern_Type)jcasType).casFeat_target == null)
      jcasType.jcas.throwFeatMissing("target", "gov.va.vinci.echo.types.AnnotationPattern");
    jcasType.ll_cas.ll_setRefValue(addr, ((AnnotationPattern_Type)jcasType).casFeatCode_target, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: targetPattern

  /** getter for targetPattern - gets 
   * @generated
   * @return value of the feature 
   */
  public String getTargetPattern() {
    if (AnnotationPattern_Type.featOkTst && ((AnnotationPattern_Type)jcasType).casFeat_targetPattern == null)
      jcasType.jcas.throwFeatMissing("targetPattern", "gov.va.vinci.echo.types.AnnotationPattern");
    return jcasType.ll_cas.ll_getStringValue(addr, ((AnnotationPattern_Type)jcasType).casFeatCode_targetPattern);}
    
  /** setter for targetPattern - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setTargetPattern(String v) {
    if (AnnotationPattern_Type.featOkTst && ((AnnotationPattern_Type)jcasType).casFeat_targetPattern == null)
      jcasType.jcas.throwFeatMissing("targetPattern", "gov.va.vinci.echo.types.AnnotationPattern");
    jcasType.ll_cas.ll_setStringValue(addr, ((AnnotationPattern_Type)jcasType).casFeatCode_targetPattern, v);}    
   
    
  //*--------------*
  //* Feature: pattern

  /** getter for pattern - gets 
   * @generated
   * @return value of the feature 
   */
  public String getPattern() {
    if (AnnotationPattern_Type.featOkTst && ((AnnotationPattern_Type)jcasType).casFeat_pattern == null)
      jcasType.jcas.throwFeatMissing("pattern", "gov.va.vinci.echo.types.AnnotationPattern");
    return jcasType.ll_cas.ll_getStringValue(addr, ((AnnotationPattern_Type)jcasType).casFeatCode_pattern);}
    
  /** setter for pattern - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setPattern(String v) {
    if (AnnotationPattern_Type.featOkTst && ((AnnotationPattern_Type)jcasType).casFeat_pattern == null)
      jcasType.jcas.throwFeatMissing("pattern", "gov.va.vinci.echo.types.AnnotationPattern");
    jcasType.ll_cas.ll_setStringValue(addr, ((AnnotationPattern_Type)jcasType).casFeatCode_pattern, v);}    
  }

    