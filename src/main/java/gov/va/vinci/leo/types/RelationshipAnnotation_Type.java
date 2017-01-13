
/* First created by JCasGen Wed Jan 11 09:29:43 MST 2017 */
package gov.va.vinci.leo.types;

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

/** RelationshipAnnotation Annotation
 * Updated by JCasGen Wed Jan 11 09:29:43 MST 2017
 * @generated */
public class RelationshipAnnotation_Type extends Annotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (RelationshipAnnotation_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = RelationshipAnnotation_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new RelationshipAnnotation(addr, RelationshipAnnotation_Type.this);
  			   RelationshipAnnotation_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new RelationshipAnnotation(addr, RelationshipAnnotation_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = RelationshipAnnotation.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("gov.va.vinci.leo.types.RelationshipAnnotation");
 
  /** @generated */
  final Feature casFeat_Source;
  /** @generated */
  final int     casFeatCode_Source;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getSource(int addr) {
        if (featOkTst && casFeat_Source == null)
      jcas.throwFeatMissing("Source", "gov.va.vinci.leo.types.RelationshipAnnotation");
    return ll_cas.ll_getRefValue(addr, casFeatCode_Source);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setSource(int addr, int v) {
        if (featOkTst && casFeat_Source == null)
      jcas.throwFeatMissing("Source", "gov.va.vinci.leo.types.RelationshipAnnotation");
    ll_cas.ll_setRefValue(addr, casFeatCode_Source, v);}
    
  
 
  /** @generated */
  final Feature casFeat_Target;
  /** @generated */
  final int     casFeatCode_Target;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getTarget(int addr) {
        if (featOkTst && casFeat_Target == null)
      jcas.throwFeatMissing("Target", "gov.va.vinci.leo.types.RelationshipAnnotation");
    return ll_cas.ll_getRefValue(addr, casFeatCode_Target);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setTarget(int addr, int v) {
        if (featOkTst && casFeat_Target == null)
      jcas.throwFeatMissing("Target", "gov.va.vinci.leo.types.RelationshipAnnotation");
    ll_cas.ll_setRefValue(addr, casFeatCode_Target, v);}
    
   /** @generated
   * @param addr low level Feature Structure reference
   * @param i index of item in the array
   * @return value at index i in the array 
   */
  public int getTarget(int addr, int i) {
        if (featOkTst && casFeat_Target == null)
      jcas.throwFeatMissing("Target", "gov.va.vinci.leo.types.RelationshipAnnotation");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_Target), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_Target), i);
	return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_Target), i);
  }
   
  /** @generated
   * @param addr low level Feature Structure reference
   * @param i index of item in the array
   * @param v value to set
   */ 
  public void setTarget(int addr, int i, int v) {
        if (featOkTst && casFeat_Target == null)
      jcas.throwFeatMissing("Target", "gov.va.vinci.leo.types.RelationshipAnnotation");
    if (lowLevelTypeChecks)
      ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_Target), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_Target), i);
    ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_Target), i, v);
  }
 



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public RelationshipAnnotation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_Source = jcas.getRequiredFeatureDE(casType, "Source", "uima.tcas.Annotation", featOkTst);
    casFeatCode_Source  = (null == casFeat_Source) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Source).getCode();

 
    casFeat_Target = jcas.getRequiredFeatureDE(casType, "Target", "uima.cas.FSArray", featOkTst);
    casFeatCode_Target  = (null == casFeat_Target) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Target).getCode();

  }
}



    