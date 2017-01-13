package gov.va.vinci.echo.ae;

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

import java.io.File;
import java.io.FileOutputStream;
import java.util.prefs.Preferences;

import gov.va.vinci.echo.pipeline.Pipeline;
import org.apache.commons.lang3.StringUtils;
import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.cas.impl.XmiCasSerializer;
import org.apache.uima.jcas.JCas;
import org.apache.uima.tools.AnnotationViewerMain;
import org.apache.uima.util.FileUtils;
import org.junit.Before;
import org.junit.Test;

import gov.va.vinci.leo.descriptors.LeoAEDescriptor;
import gov.va.vinci.leo.descriptors.LeoTypeSystemDescription;

public class EchoTest {
  protected LeoAEDescriptor aggregate = null;
  protected LeoTypeSystemDescription types = null;
  protected String inputDir = "input" + File.separator;
  protected String outputDir = "output" + File.separator;;
  //protected String outputDir = "src/test/resources/xmi";
  protected boolean launchView = false;

  @Before
  public void setUp() throws Exception {
  }

  @Before
  public void setup() throws Exception {
    Pipeline bs = new Pipeline();
    types = bs.getLeoTypeSystemDescription();
    aggregate = bs.getPipeline();
    File o = new File(outputDir);
    if (!o.exists()) {
      o.mkdirs();
    }//if
  }//setup

  @Test
  public void testXmi() throws Exception {
    String types = "";
    String docText = "";
    AnalysisEngine ae = UIMAFramework.produceAnalysisEngine(aggregate.getAnalysisEngineDescription());
    String filename = "template3.txt";
    String filePath = this.inputDir;

    try {
      docText = FileUtils.file2String(new File(filePath));
    } catch (Exception e) {
      System.out.println("Missing file!!");
    }
    if (StringUtils.isBlank(docText)) {
      System.out.println("Blank file!!");
    }

    JCas jcas = ae.newJCas();
    jcas.setDocumentText(docText);
    ae.process(jcas);
    try {
      File xmio = new File(outputDir, filename + ".xmi");
      XmiCasSerializer.serialize(jcas.getCas(), new FileOutputStream(xmio));
    } catch (Exception e) {

    }

    if (launchView) launchViewer();
    System.out.println(types);
    if (launchView) System.in.read();
  }//testXmi method

  protected void launchViewer() throws Exception {
    if (aggregate == null) { throw new RuntimeException("Aggregate is null, unable to generate descriptor for viewing xmi"); }
    aggregate.toXML(outputDir);
    String aggLoc = aggregate.getDescriptorLocator().substring(5);
    Preferences prefs = Preferences.userRoot().node("org/apache/uima/tools/AnnotationViewer");
    if (aggLoc != null) {
      prefs.put("taeDescriptorFile", aggLoc);
    }//if mAggDesc != null
    if (outputDir != null) {
      prefs.put("inDir", outputDir);
    }//if mOutputDir != null
    AnnotationViewerMain avm = new AnnotationViewerMain();
    avm.setBounds(0, 0, 1000, 225);
    avm.setVisible(true);
  }//launchViewer method

  /**
  @After
  **/
  public void cleanup() throws Exception {
    File o = new File(outputDir);
    if (o.exists()) {
      FileUtils.deleteRecursive(o);
    }//if
  }//cleanup method
}//EchoTest class
