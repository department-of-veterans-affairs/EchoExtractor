
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
public class Template_Type extends Annotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Template_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Template_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Template(addr, Template_Type.this);
  			   Template_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Template(addr, Template_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = Template.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("gov.va.vinci.echo.types.Template");
 
  /** @generated */
  final Feature casFeat_TemplateType;
  /** @generated */
  final int     casFeatCode_TemplateType;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getTemplateType(int addr) {
        if (featOkTst && casFeat_TemplateType == null)
      jcas.throwFeatMissing("TemplateType", "gov.va.vinci.echo.types.Template");
    return ll_cas.ll_getIntValue(addr, casFeatCode_TemplateType);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setTemplateType(int addr, int v) {
        if (featOkTst && casFeat_TemplateType == null)
      jcas.throwFeatMissing("TemplateType", "gov.va.vinci.echo.types.Template");
    ll_cas.ll_setIntValue(addr, casFeatCode_TemplateType, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public Template_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_TemplateType = jcas.getRequiredFeatureDE(casType, "TemplateType", "uima.cas.Integer", featOkTst);
    casFeatCode_TemplateType  = (null == casFeat_TemplateType) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_TemplateType).getCode();

  }
}



    