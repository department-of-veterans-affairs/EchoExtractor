
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
public class Regex_Type extends Annotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Regex_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Regex_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Regex(addr, Regex_Type.this);
  			   Regex_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Regex(addr, Regex_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = Regex.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("gov.va.vinci.echo.types.Regex");
 
  /** @generated */
  final Feature casFeat_Pattern;
  /** @generated */
  final int     casFeatCode_Pattern;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getPattern(int addr) {
        if (featOkTst && casFeat_Pattern == null)
      jcas.throwFeatMissing("Pattern", "gov.va.vinci.echo.types.Regex");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Pattern);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setPattern(int addr, String v) {
        if (featOkTst && casFeat_Pattern == null)
      jcas.throwFeatMissing("Pattern", "gov.va.vinci.echo.types.Regex");
    ll_cas.ll_setStringValue(addr, casFeatCode_Pattern, v);}
    
  
 
  /** @generated */
  final Feature casFeat_Groups;
  /** @generated */
  final int     casFeatCode_Groups;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getGroups(int addr) {
        if (featOkTst && casFeat_Groups == null)
      jcas.throwFeatMissing("Groups", "gov.va.vinci.echo.types.Regex");
    return ll_cas.ll_getRefValue(addr, casFeatCode_Groups);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setGroups(int addr, int v) {
        if (featOkTst && casFeat_Groups == null)
      jcas.throwFeatMissing("Groups", "gov.va.vinci.echo.types.Regex");
    ll_cas.ll_setRefValue(addr, casFeatCode_Groups, v);}
    
   /** @generated
   * @param addr low level Feature Structure reference
   * @param i index of item in the array
   * @return value at index i in the array 
   */
  public String getGroups(int addr, int i) {
        if (featOkTst && casFeat_Groups == null)
      jcas.throwFeatMissing("Groups", "gov.va.vinci.echo.types.Regex");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getStringArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_Groups), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_Groups), i);
	return ll_cas.ll_getStringArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_Groups), i);
  }
   
  /** @generated
   * @param addr low level Feature Structure reference
   * @param i index of item in the array
   * @param v value to set
   */ 
  public void setGroups(int addr, int i, String v) {
        if (featOkTst && casFeat_Groups == null)
      jcas.throwFeatMissing("Groups", "gov.va.vinci.echo.types.Regex");
    if (lowLevelTypeChecks)
      ll_cas.ll_setStringArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_Groups), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_Groups), i);
    ll_cas.ll_setStringArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_Groups), i, v);
  }
 



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public Regex_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_Pattern = jcas.getRequiredFeatureDE(casType, "Pattern", "uima.cas.String", featOkTst);
    casFeatCode_Pattern  = (null == casFeat_Pattern) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Pattern).getCode();

 
    casFeat_Groups = jcas.getRequiredFeatureDE(casType, "Groups", "uima.cas.StringArray", featOkTst);
    casFeatCode_Groups  = (null == casFeat_Groups) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Groups).getCode();

  }
}



    