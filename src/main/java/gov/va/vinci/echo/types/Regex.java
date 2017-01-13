

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
public class Regex extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Regex.class);
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
  protected Regex() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public Regex(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public Regex(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public Regex(JCas jcas, int begin, int end) {
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
  //* Feature: Pattern

  /** getter for Pattern - gets 
   * @generated
   * @return value of the feature 
   */
  public String getPattern() {
    if (Regex_Type.featOkTst && ((Regex_Type)jcasType).casFeat_Pattern == null)
      jcasType.jcas.throwFeatMissing("Pattern", "gov.va.vinci.echo.types.Regex");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Regex_Type)jcasType).casFeatCode_Pattern);}
    
  /** setter for Pattern - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setPattern(String v) {
    if (Regex_Type.featOkTst && ((Regex_Type)jcasType).casFeat_Pattern == null)
      jcasType.jcas.throwFeatMissing("Pattern", "gov.va.vinci.echo.types.Regex");
    jcasType.ll_cas.ll_setStringValue(addr, ((Regex_Type)jcasType).casFeatCode_Pattern, v);}    
   
    
  //*--------------*
  //* Feature: Groups

  /** getter for Groups - gets 
   * @generated
   * @return value of the feature 
   */
  public StringArray getGroups() {
    if (Regex_Type.featOkTst && ((Regex_Type)jcasType).casFeat_Groups == null)
      jcasType.jcas.throwFeatMissing("Groups", "gov.va.vinci.echo.types.Regex");
    return (StringArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Regex_Type)jcasType).casFeatCode_Groups)));}
    
  /** setter for Groups - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setGroups(StringArray v) {
    if (Regex_Type.featOkTst && ((Regex_Type)jcasType).casFeat_Groups == null)
      jcasType.jcas.throwFeatMissing("Groups", "gov.va.vinci.echo.types.Regex");
    jcasType.ll_cas.ll_setRefValue(addr, ((Regex_Type)jcasType).casFeatCode_Groups, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for Groups - gets an indexed value - 
   * @generated
   * @param i index in the array to get
   * @return value of the element at index i 
   */
  public String getGroups(int i) {
    if (Regex_Type.featOkTst && ((Regex_Type)jcasType).casFeat_Groups == null)
      jcasType.jcas.throwFeatMissing("Groups", "gov.va.vinci.echo.types.Regex");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Regex_Type)jcasType).casFeatCode_Groups), i);
    return jcasType.ll_cas.ll_getStringArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Regex_Type)jcasType).casFeatCode_Groups), i);}

  /** indexed setter for Groups - sets an indexed value - 
   * @generated
   * @param i index in the array to set
   * @param v value to set into the array 
   */
  public void setGroups(int i, String v) { 
    if (Regex_Type.featOkTst && ((Regex_Type)jcasType).casFeat_Groups == null)
      jcasType.jcas.throwFeatMissing("Groups", "gov.va.vinci.echo.types.Regex");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Regex_Type)jcasType).casFeatCode_Groups), i);
    jcasType.ll_cas.ll_setStringArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Regex_Type)jcasType).casFeatCode_Groups), i, v);}
  }

    