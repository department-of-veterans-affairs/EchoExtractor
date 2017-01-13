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
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.cas.impl.XmiCasSerializer;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.FileUtils;
import org.junit.Before;
import org.junit.Test;

import gov.va.vinci.leo.descriptors.LeoAEDescriptor;
import gov.va.vinci.leo.descriptors.LeoTypeSystemDescription;
import gov.va.vinci.leo.tools.XmlFilter;

public class LocalLogicTestAnnotator {
  protected LeoAEDescriptor aggregate = null;
  protected LeoTypeSystemDescription types = null;
  protected String outputDir = "src/test/resources/output/xmi";

  @Before
  public void setup() throws Exception {
    gov.va.vinci.echo.pipeline.Pipeline bs = new gov.va.vinci.echo.pipeline.Pipeline();
    types = bs.getLeoTypeSystemDescription();
    aggregate = bs.getPipeline();
    File o = new File(outputDir);
    if (!o.exists()) {
      o.mkdirs();
    }//if
  }//setup

  @Test
  public void testTemplate() throws Exception {
    String[] temps = {
        "Exercise8_01.txt",
        "Exercise8_02.txt",
        "Exercise8_03.txt",
        "Exercise8_04.txt",
        "Exercise8_05.txt",
        "Exercise8_06.txt",
        "Exercise8_07.txt",
        "Exercise8_08.txt",
        "Exercise8_09.txt",
        "Exercise8_10.txt",
        "Exercise8_11.txt",
        "Exercise8_12.txt" };
    //temps = new String[]{ "Exercise1.txt"};
    //TODO: add more test cases
    HashMap<String, HashMap<String, String>> resultsToCompare = new HashMap<String, HashMap<String, String>>();
    /*   *
    resultsToCompare.put("Exercise8_01.txt", loadTemp0());
    
    resultsToCompare.put("Exercise8_02.txt", loadTemp1());
    resultsToCompare.put("Exercise8_03.txt", loadTemp3_1());
    resultsToCompare.put("Exercise8_04.txt", loadTemp4_2());
    resultsToCompare.put("Exercise8_05.txt", loadTemp5_2());
    resultsToCompare.put("Exercise8_06.txt", loadTemp9());
        /*    */
    int[] counts = {
        56, 39, 43, 47, // 0 ... 0_3
        21, 20, 14, 44, 32, 7, // 1 ... 1_5 
        27, // 2 
        13, 18, 17, 17, // 3 ... 3_3
        8, 33, 32, // 4 ... 4_2 
        18, 21, 38, 44, // 5 ... 5_3  
        16, // 6  
        17, 20, 19, 5, // 7 ... 7_3
        6, // 8
        22, //9
        14, 20 };
    AnalysisEngine ae = UIMAFramework.produceAnalysisEngine(
        aggregate.getAnalysisEngineDescription());
    int index = 0;
    //TODO
    // If test all, comment out lines below.
    /**
      temps = new String[]{"template9.txt"};
      counts = new int[] {22};
     
     /**/
    for (String t : temps) {

      String filePath = "src/test/resources/examples/" + t;
      String docText = FileUtils.file2String(new File(filePath));
      if (StringUtils.isBlank(docText)) {
        System.out.println("The loaded document is blank at " + filePath);
      } else {
        HashMap<String, String> types = new HashMap<String, String>();
        JCas jcas = ae.newJCas();
        jcas.setDocumentText(filter(docText));
        ae.process(jcas);
        try {
          File xmio = new File(outputDir, t + ".xmi");
          XmiCasSerializer.serialize(jcas.getCas(), new FileOutputStream(xmio));
        } catch (Exception e) {
          e.printStackTrace();
          System.out.println(e.getMessage());
        }
        //types.putAll(logicTypes(jcas));

        System.out.println("\r\n------ Start " + t + " -------\r\n");
        System.out.println("Number of concept-value paris: " + types.size());
        for (String extractedConcept : types.keySet())
          System.out.println(extractedConcept + " " + types.get(extractedConcept));
        System.out.println("Number of concept-value paris: " + types.size());
        /*  *
        			//	Assert.assertEquals(types.size(), counts[index]);
        				HashMap refSt = resultsToCompare.get(t);
        				if (refSt != null) {
        			////		Assert.assertEquals(types.size(), refSt.size());
        					for (String extractedConcept : types.keySet()) {
        						System.out.print(extractedConcept);
        			//			Assert.assertNotNull(refSt.get(extractedConcept));
        						System.out.println(": ref="
        						    + refSt.get(extractedConcept) + " sys=" +
        						    types.get(extractedConcept));
        				//		Assert.assertEquals(refSt.get(extractedConcept),
        				//		    types.get(extractedConcept));
        					}
        					System.out.println();
        				}
        				/*  */
        System.out.println("\r\n------  End  " + t + " -------\r\n");
        index++;
      }
    }
    //assertTrue(typeResults.equals(types));
  }//test

  /**
   * TODO: template0.txt
   * 
   * @return
   */
  private HashMap<String, String> loadTemp0() {

    HashMap<String, String> temp = new HashMap<String, String>();
    temp.put("% ivs thick", "35.0%");
    temp.put("acs", "2.1cm");
    temp.put("ao max pg", "9.3mmhg");
    temp.put("ao mean pg", "5.0mmhg");
    temp.put("ao root area", "10.8cm2");
    temp.put("ao root diam", "3.7cm");
    temp.put("ao v2 max", "152.5cm/sec");
    temp.put("ao v2 mean", "104.0cm/sec");
    temp.put("ao v2 vti", "29.6cm");
    temp.put("ava(id)", "2.1cm2");
    temp.put("ava(vd)", "2.3cm2");
    temp.put("co(mod-sp4)", "4.7l/min");
    temp.put("edv(mod-sp4)", "137.0ml");
    temp.put("edv(teich)", "144.4ml");
    temp.put("ef(mod-sp4)", "48.9%");
    temp.put("ef(teich)", "69.4%");
    temp.put("esv(mod-sp4)", "70.0ml");
    temp.put("esv(teich)", "44.1ml");
    temp.put("fs", "39.4%");
    temp.put("ivsd", "1.0cm");
    temp.put("ivss", "1.4cm");
    temp.put("la dimension", "4.3cm");
    temp.put("lv ivrt", "0.06sec");
    temp.put("lv mass(c)d", "209.9grams");
    temp.put("lv v1 max pg", "4.0mmhg");
    temp.put("lv v1 max", "100.0cm/sec");
    temp.put("lv v1 mean pg", "2.0mmhg");
    temp.put("lv v1 mean", "62.5cm/sec");
    temp.put("lv v1 vti", "17.9cm");
    temp.put("lvidd", "5.5cm");
    temp.put("lvids", "3.3cm");
    temp.put("lvld ap4", "8.4cm");
    temp.put("lvls ap4", "7.2cm");
    temp.put("lvot area", "3.5cm2");
    temp.put("lvot diam", "2.1cm");
    temp.put("lvpwd", "1.0cm");
    temp.put("lvpws", "1.5cm");
    temp.put("mm hr", "70.0bpm");
    temp.put("mm r-r int", "0.86sec");
    temp.put("mr max pg", "36.4mmhg");
    temp.put("mr max vel", "301.0cm/sec");
    temp.put("mv a max vel", "73.4cm/sec");
    temp.put("mv dec slope", "222.0cm/sec2");
    temp.put("mv dec time", "0.26sec");
    temp.put("mv e max vel", "58.6cm/sec");
    temp.put("mv e/a", "0.80");
    temp.put("mv p1/2t max vel", "59.0cm/sec");
    temp.put("mv p1/2t", "77.8msec");
    temp.put("mva(p1/2t)", "2.8cm2");
    temp.put("pa acc time", "0.11sec");
    temp.put("pa pr(accel)", "28.0mmhg");
    temp.put("rvdd", "2.7cm");
    temp.put("sv(lvot)", "62.0ml");
    temp.put("sv(mod-sp4)", "67.0ml");
    temp.put("tr max pg", "7.6mmhg");
    temp.put("tr max vel", "137.5cm/sec");

    return temp;
  }

  /**
   * TODO: template1.txt
   * 
   * @return
   */
  private HashMap<String, String> loadTemp1() {

    HashMap<String, String> temp = new HashMap<String, String>();
    temp.put("% fract short", "13%");
    temp.put("aortic max grad", "0.0mmhg");
    temp.put("aortic root", "36mm");
    temp.put("calculated ef", "0%");
    temp.put("e pnt sep spn", "35mm");
    temp.put("ef descriptor", "severely depressed");
    temp.put("estimated ef", "20%");
    temp.put("la chamber", "dilated");
    temp.put("lt atrium", "44mm");
    temp.put("lv chamber", "dilated");
    temp.put("lv diastole", "82mm");
    temp.put("lv mass", "0");
    temp.put("lv systole", "71mm");
    temp.put("mitral max grad", "0.0mmhg");
    temp.put("mitral regurgitation", "moderate");
    temp.put("mitral regurgitation1", "moderate");
    temp.put("mitral valve area(dopp)", "0.0cm-sq");
    temp.put("pa systolic", "48mmhg");
    temp.put("rv chamber", "normal");
    temp.put("septum", "10mm");
    temp.put("tricuspid regurgitation", "mild");

    return temp;
  }

  /**
   * TODO: template3_1.txt
   * 
   * @return
   */
  private HashMap<String, String> loadTemp3_1() {

    HashMap<String, String> temp = new HashMap<String, String>();
    temp.put("aortic root diameter", "3.8cm");
    temp.put("aortic valve area", "1.2cm2");
    temp.put("a-wave", "0.9m/s");
    temp.put("e-wave", "0.7m/s");
    temp.put("left atrial dimension", "4.1cm");
    temp.put("lv diastolic dimension", "4.9cm");
    temp.put("lv systolic dimension", "3.0cm");
    temp.put("lvot diameter", "2.5cm");
    temp.put("lvot peak velocity", "1.0m/s");
    temp.put("lvpw diastolic thickness", "1.2cm");
    temp.put("mean gradient", "61mmhg");
    temp.put("peak gradient", "91mmhg");
    temp.put("peak velocity", "4.8m/s");
    temp.put("pressure half-time", "496ms");
    temp.put("rv dimension", "2.8cm");
    temp.put("septum diastolic thickness", "1.2cm");
    temp.put("vti", "121cm");
    temp.put("vti1", "28cm");

    return temp;
  }

  /**
   * TODO: template4_2.txt
   * 
   * @return
   */
  private HashMap<String, String> loadTemp4_2() {

    HashMap<String, String> temp = new HashMap<String, String>();
    temp.put("a dur", "126msec");
    temp.put("a peak", "63cm/sec");
    temp.put("ao asc", "3.35cm");
    temp.put("ao diam", "3.8cm");
    temp.put("av mg", "3.27mmhg");
    temp.put("av pg", "6.18mmhg");
    temp.put("dt", "164msec");
    temp.put("e peak", "87cm/sec");
    temp.put("e/a ratio", "1.38ratio");
    temp.put("e1", "8cm/s");
    temp.put("ivrt", "140msec");
    temp.put("ivsd", "0.9cm");
    temp.put("lv fs", "48%");
    temp.put("lv mass (m-m)", "191.63gm");
    temp.put("lv vol s", "32ml");
    temp.put("lvidd", "5.22cm");
    temp.put("lvids", "2.9cm");
    temp.put("lvot mg", "2.31mmhg");
    temp.put("lvot pg", "5.07mmhg");
    temp.put("lvot vti", "24.7cm");
    temp.put("lvpwd", "0.9cm");
    temp.put("pv a dur", "126msec");
    temp.put("pv pg", "6.83mmhg");
    temp.put("pv vti", "16.6cm");
    temp.put("pvd", "0.62m/sec");
    temp.put("pvs1", "0.62m/sec");
    temp.put("tr peak", "2.42m/sec");
    temp.put("v1 (rvot vel)", "0.65m/sec");
    temp.put("v1 (lvot vel)", "1.13m/sec");
    temp.put("v2(ao vel)", "1.24m/sec");
    temp.put("v2(pv vel)", "1.31m/sec");
    temp.put("v2/v1", "0.91ratio");

    return temp;
  }

  /**
   * TODO: template5_2.txt
   * 
   * @return
   */
  private HashMap<String, String> loadTemp5_2() {

    HashMap<String, String> temp = new HashMap<String, String>();
    temp.put("aao ap diam ed", "32mm");
    temp.put("aor diam (2d)", "3.5cm");
    temp.put("area es a4c", "15.1cm");
    temp.put("cv orifice area cont eq by vti", "3.1cm"); //TODO: fix this: cm^2
    temp.put("ivsd (2d)", "1.4cm");
    temp.put("lv peak dias tissue vel during atrial systole lateral ma (tdi)", "9.8cm/s");
    temp.put("lv peak dias tissue vel during atrial systole medial ma (tdi)", "14cm/s");
    temp.put("lv e/a tissue vel lateral ma (tdi)", "1"); //TODO: fix
    temp.put("lv e/a tissue vel medial ma (tdi)", "0.5"); //TODO: fix
    temp.put("lv peak early dias tissue vel lateral ma (tdi)", "10.2cm/s");
    temp.put("lv peak early dias tissue vel medial ma (tdi)", "7.6cm/s");
    temp.put("lv peak sys tissue vel lateral ma (tdi)", "7.7cm/s");
    temp.put("lv peak sys tissue vel medial ma (tdi)", "7.6cm/s");
    temp.put("lvidd (2d)", "4.9cm");
    temp.put("lvids (2d)", "3cm");
    temp.put("lvot diam", "2.1cm");
    temp.put("lvot mean grad", "2mm[hg]");
    temp.put("lvot mean vel", "72.1cm/s");
    temp.put("lvot vti", "22.4cm");
    temp.put("lvpwd (2d)", "1.1cm");
    temp.put("mean grad antegrade flow", "2mm[hg]");
    temp.put("mean vel antegrade flow", "68cm/s");
    temp.put("mv e/a", "1");
    temp.put("mv peak a vel", "57.3cm/s");
    temp.put("mv peak e vel antegrade flow", "59.7cm/s");
    temp.put("mv peak vel/lv peak tissue vel e-wave lateral ma", "5.9");
    temp.put("mv peak vel/lv peak tissue vel e-wave medial ma", "7.9");
    temp.put("peak grad mean antegrade flow", "6mm[hg]"); //TODO: fix
    temp.put("peak grad mean antegrade flow2", "2mm[hg]"); // TODO: fix this
    temp.put("prox ivc diam", "21mm");
    temp.put("rvot mean grad", "1mm[hg]");
    temp.put("rvot mean vel", "41.4cm/s");
    temp.put("rvot vti", "13.4cm");
    temp.put("vmax antegrade flow", "77.2cm/s");
    temp.put("vmax mean antegrade flow", "119cm/s");
    temp.put("vti antegrade flow", "24.7cm");
    temp.put("vti antegrade flow1", "21.7cm");
    temp.put("vti mean antegrade flow", "21.7cm");
    return temp;
  }

  /**
   * TODO: template9.txt
   * 
   * @return
   */
  private HashMap<String, String> loadTemp9() {
    //TODO: fix parsing
    HashMap<String, String> temp = new HashMap<String, String>();
    temp.put("aortic valve regurgitation", "none");
    temp.put("left atrial size", "mild dilation");
    temp.put("mitral valve regurgitation", "trivial");
    temp.put("rv systolic function", "normal");
    temp.put("tricuspid valve regurgitation", "none");
    temp.put("aortic valve thickening", "none");
    temp.put("mitral valve thickening", "none");
    temp.put("pericardial effusion", "none");
    temp.put("right atrial size", "mildly dilation");
    temp.put("tricuspid valve thickening", "none");
    temp.put("aortic valve motion", "normal");
    temp.put("mitral valve motion", "normal");
    temp.put("tricuspid valve motion", "normal");
    temp.put("lv internal dimension (diastole)", "6.2cm");
    temp.put("lv internal dimension (systole)", "4.5cm");
    temp.put("fractional shortening (%)", "27.4%");
    temp.put("pulmonic valve regurgitation", "none");
    temp.put("septal thickness (diastole)", "1.0cm");
    temp.put("posterior wall thickness (diastole)", "0.7cm");
    temp.put("left atrial dimension", "4.4cm");
    temp.put("aortic root dimension", "3.8cm");
    temp.put("right ventricular dimension", "2.0cm");

    return temp;
  }

  /**
   * TODO: marking the start of logicTypes method
   * @param jcas
   * @return
   *
  protected HashMap<String, String> logicTypes(JCas jcas) {
    HashMap<String, String> types = new HashMap<String, String>();
    Collection<Annotation> annotations = AnnotationLibrarian.getAllAnnotationsOfType(jcas, Logic.type);
    int i = 1;
    for (Annotation a : annotations) {
      Logic bw = (Logic) a;
      String s = bw.getConcept();
      if (types.get(s) != null) {
        s = s + i;
        i++;
      }
      types.put(s, bw.getValue() + bw.getUnit());
    }
    return types;
  }//windowTypes method
/***/
  public String filter(String text) {
    if (StringUtils.isBlank(text)) { return text; }//if
    return XmlFilter.toXml10(text);
  }//filter method String input

}
