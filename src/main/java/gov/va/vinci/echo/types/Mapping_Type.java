
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
public class Mapping_Type extends Annotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Mapping_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Mapping_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Mapping(addr, Mapping_Type.this);
  			   Mapping_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Mapping(addr, Mapping_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = Mapping.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("gov.va.vinci.echo.types.Mapping");
 
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
      jcas.throwFeatMissing("Mapping", "gov.va.vinci.echo.types.Mapping");
    return ll_cas.ll_getRefValue(addr, casFeatCode_Mapping);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setMapping(int addr, int v) {
        if (featOkTst && casFeat_Mapping == null)
      jcas.throwFeatMissing("Mapping", "gov.va.vinci.echo.types.Mapping");
    ll_cas.ll_setRefValue(addr, casFeatCode_Mapping, v);}
    
   /** @generated
   * @param addr low level Feature Structure reference
   * @param i index of item in the array
   * @return value at index i in the array 
   */
  public String getMapping(int addr, int i) {
        if (featOkTst && casFeat_Mapping == null)
      jcas.throwFeatMissing("Mapping", "gov.va.vinci.echo.types.Mapping");
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
      jcas.throwFeatMissing("Mapping", "gov.va.vinci.echo.types.Mapping");
    if (lowLevelTypeChecks)
      ll_cas.ll_setStringArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_Mapping), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_Mapping), i);
    ll_cas.ll_setStringArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_Mapping), i, v);
  }
 



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public Mapping_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_Mapping = jcas.getRequiredFeatureDE(casType, "Mapping", "uima.cas.StringArray", featOkTst);
    casFeatCode_Mapping  = (null == casFeat_Mapping) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Mapping).getCode();

  }
}



    