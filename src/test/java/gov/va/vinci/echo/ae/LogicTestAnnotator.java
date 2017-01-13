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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import gov.va.vinci.echo.pipeline.Pipeline;
import junit.framework.Assert;

import org.apache.commons.lang3.StringUtils;
import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.cas.impl.XmiCasSerializer;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.util.FileUtils;
import org.junit.Before;
import org.junit.Test;
import gov.va.vinci.echo.types.*;

import gov.va.vinci.leo.descriptors.LeoAEDescriptor;
import gov.va.vinci.leo.descriptors.LeoTypeSystemDescription;
import gov.va.vinci.leo.tools.XmlFilter;
import gov.va.vinci.leo.AnnotationLibrarian;

public class LogicTestAnnotator {
//TODO: change if you want to run without checks
	public boolean assess = true;
	
	protected LeoAEDescriptor aggregate = null;
	protected LeoTypeSystemDescription types = null;
	protected String outputDir = "output" + File.separator + "xmi" + File.separator;
	protected String filePath = "input" + File.separator;

	protected void compareFileToRefSt(JCas jcas, HashSet<TestUnit> refSt, int type) {
		Collection<Annotation> annotations = AnnotationLibrarian.getAllAnnotationsOfType(jcas, type, true);
		// go through annotations
		// check if the annotation is in refst and if it is correct

		HashSet<TestUnit> currDocItems = new HashSet<TestUnit>();
		for (Annotation a : annotations) {
			String term = ((Relation) a).getTerm();
			String mapping = (((Relation) a).getMapping().get(0));
			String assessment = ((Relation) a).getAssessment();
			String valueString = ((Relation) a).getValueString();
			String unit = ((Relation) a).getUnit();
			TestUnit currRelation = new TestUnit(assessment, mapping, term, unit, valueString);
			currDocItems.add(currRelation);
			boolean found = false;
			for (TestUnit item : refSt) {
				if (item.equals(currRelation)) {
					found = true;
					System.out.println("Matched  : " + item.toString());
				}
			}
			if (!found)
				System.out.println(currRelation.toString());
			if (assess)
				Assert.assertTrue(found);

		}
		if (currDocItems.size() != refSt.size()) {
		System.out.println("Size mismatch! False negatives:" +currDocItems.size()+"-"+refSt.size() +"="+ (currDocItems.size() - refSt.size())) ;
			for (TestUnit rfU : refSt) {
				boolean foundU = false;
				for (TestUnit crU : currDocItems) {
					if (rfU.equals(crU)) {
						foundU = true;
					}
				}
				if(!foundU) System.out.println("Missing : " + rfU.toString());
			}
		}
		if (assess)
			Assert.assertEquals(annotations.size(), refSt.size());

	}

	public String filter(String text) {
		if (StringUtils.isBlank(text)) {
			return text;
		}//if
		return XmlFilter.toXml10(text);
	}//filter method String input

	/**
	 * TODO: template0.txt
	 * 
	 * @return
	 */
	private HashSet<TestUnit> loadTemp0() {

		HashSet<TestUnit> temp = new HashSet<TestUnit>();
		//TODO: False positive - unsolved bug. I think it is solved now
		//		temp.add(new TestUnit("","pulmonary artery pressure","pa acc time","sec","0.11"));
		temp.add(new TestUnit("", "interventricular septum dimension at end diastole", "ivsd", "cm", "1.0"));
		temp.add(new TestUnit("", "left ventricular dimension at end diastole", "lvidd", "cm", "5.5"));
		temp.add(new TestUnit("", "left ventricular dimension at end systole", "lvids", "cm", "3.3"));
		temp.add(new TestUnit("", "left ventricular posterior wall thickness at end diastole", "lvpwd", "cm",
		    "1.0"));
		temp.add(new TestUnit("", "left ventricular ejection fraction", "ef(teich)", "%", "69.4"));
		temp.add(new TestUnit("", "left ventricular ejection fraction", "ef(mod sp4)", "%", "48.9"));
		temp.add(new TestUnit("", "interventricular septum dimension at end diastole", "% ivs thick", "%", "35.0"));
		temp.add(new TestUnit("", "left atrium size at end systole", "la dimension", "cm", "4.3"));
		temp.add(new TestUnit("", "mitral valve orifice area", "mva(p1/2t)", "cm2", "2.8"));
		temp.add(new TestUnit("", "aortic valve max pressure gradient", "ao max pg", "mmhg", "9.3"));
		temp.add(new TestUnit("", "aortic valve mean gradient", "ao mean pg", "mmhg", "5.0"));
		temp.add(new TestUnit("", "aortic valve orifice area", "ava(i d)", "cm2", "2.1"));
		temp.add(new TestUnit("", "aortic valve orifice area", "ava(v d)", "cm2", "2.3"));
		temp.add(new TestUnit("", "mitral valve regurgitation peak velocity", "mr max vel", "cm/sec", "301.0"));
		temp.add(new TestUnit("", "tricuspid valve regurgitation peak velocity", "tr max vel", "cm/sec", "137.5"));
		temp.add(new TestUnit("", "tricuspid valve mean gradient", "tr max pg", "mmhg", "7.6"));
		temp.add(new TestUnit("", "mitral valve mean gradient", "mr max pg", "mmhg", "36.4"));
		temp.add(new TestUnit("", "pulmonary artery pressure", "pa pr(accel)", "mmhg", "28.0"));

		return temp;
	}

	/**
	 * TODO: template1.txt
	 * 
	 * @return
	 */
	private HashSet<TestUnit> loadTemp1() {
		HashSet<TestUnit> temp = new HashSet<TestUnit>();
		temp.add(new TestUnit("", "aortic valve max pressure gradient", "aortic max grad", "mm hg", "0.0"));
		temp.add(new TestUnit("", "interventricular septum dimension at end diastole", "septum", "mm", "10"));
		temp.add(new TestUnit("", "left atrium size at end systole", "lt atrium", "mm", "44"));
		temp.add(new TestUnit("", "left ventricular dimension at end diastole", "lv diastole", "mm", "82"));
		temp.add(new TestUnit("", "left ventricular dimension at end systole", "lv systole", "mm", "71"));
		temp.add(new TestUnit("", "left ventricular ejection fraction", "ef", "%", "0"));
		temp.add(new TestUnit("", "left ventricular ejection fraction", "ef", "%", "20"));
		temp.add(new TestUnit("", "mitral valve mean gradient", "mitral max grad", "mm hg", "0.0"));
		temp.add(new TestUnit("", "mitral valve orifice area", "mitral valve area(dopp)", "cm sq", "0.0"));
		temp.add(new TestUnit("", "pulmonary artery pressure", "pa systolic", "mm hg", "48"));
		temp.add(new TestUnit("", "right atrial pressure", "ra pressure", "", "10"));
		temp.add(new TestUnit("abnormality", "interventricular septum dimension at end diastole", "septal", "",
		    ""));
		temp.add(new TestUnit("dilated", "left atrium size at end systole", "dilated la chamber", "", ""));
		// False negtives
		//temp.add(new TestUnit("dilated", "left ventricular size", "lv chamber", "", ""));
		//temp.add(new TestUnit("hypertension", "pulmonary artery pressure", "pulmonary hypertension", "", ""));
		temp.add(new TestUnit("mild", "tricuspid valve regurgitation", "tricuspid regurgitation", "", ""));
		temp.add(new TestUnit("mild", "aortic valve max pressure gradient", "aortic max grad", "", ""));
		temp.add(new TestUnit("moderate to severe", "mitral valve regurgitation", "mitral regurgitation", "", ""));
		temp.add(new TestUnit("moderate", "mitral valve regurgitation", "mitral regurgitation", "", ""));
		temp.add(new TestUnit("moderate","mitral valve regurgitation","mitral regurgitation","",""));
		temp.add(new TestUnit("moderate", "tricuspid valve regurgitation", "tricuspid regurgitation", "", ""));

		return temp;
	}

	private HashSet<TestUnit> loadTemp2() {
		HashSet<TestUnit> temp = new HashSet<TestUnit>();
		// False positive
		temp.add(new TestUnit("normal", "interventricular septum dimension at end diastole", "septal", "", ""));

		temp.add(new TestUnit("","interventricular septum dimension at end diastole","ivs","mm","15"));

		temp.add(new TestUnit("", "left atrium size at end systole", "la apical", "mm", "51"));
		temp.add(new TestUnit("", "left ventricular dimension at end diastole", "lvedd", "mm", "50"));
		temp.add(new TestUnit("", "left ventricular dimension at end systole", "lvesd", "mm", "32"));
		temp.add(new TestUnit("", "left ventricular posterior wall thickness at end diastole", "lvpw", "mm", "13"));
		temp.add(new TestUnit("", "left ventricular ejection fraction", "left ventricular ejection fraction",
		    "%", "65 +/ 5"));
		temp.add(new TestUnit("", "right atrial pressure", "ra pressure", "mm hg", "5"));
		temp.add(new TestUnit("trace", "mitral valve regurgitation", "mitral regurgitation", "", ""));
		temp.add(new TestUnit("trace", "tricuspid valve regurgitation", "tricuspid regurgitation", "", ""));
		temp.add(new TestUnit("normal", "left ventricular ejection fraction",
		    "left ventricular systolic function", "", ""));
		temp.add(new TestUnit("trace", "mitral valve regurgitation", "mitral regurgitation", "", ""));
		temp.add(new TestUnit("trace", "tricuspid valve regurgitation", "tricuspid regurgitation", "", ""));
		temp.add(new TestUnit("", "left ventricular ejection fraction", "left ventricular ejection fraction",
		    "%", "65 +/ 5"));
		temp.add(new TestUnit("", "tricuspid valve regurgitation peak velocity", "tr vel", "m/sec", "2.5"));
		temp.add(new TestUnit("", "pulmonary artery pressure", "pulmonary systolic pressure", "mm hg", "30"));
		temp.add(new TestUnit("enlargement; mild", "left atrium size at end systole", "la enlargement", "", ""));

		return temp;
	}

	private HashSet<TestUnit> loadTemp3() {
		HashSet<TestUnit> temp = new HashSet<TestUnit>();

		temp.add(new TestUnit("", "interventricular septum dimension at end diastole", "ivsd", "cm", "0.92"));
		temp.add(new TestUnit("", "left ventricular dimension at end diastole", "lvidd", "cm", "6.31"));
		temp.add(new TestUnit("", "left ventricular posterior wall thickness at end diastole", "lvpwd", "cm", ".90"));
		temp.add(new TestUnit("hypertrophy; eccentric", "left ventricular hypertrophy", "hypertrophy", "", ""));
		temp.add(new TestUnit("", "left ventricular dimension at end systole", "lvids", "cm", "5.5"));
		temp.add(new TestUnit("", "left atrium size at end systole", "la dimension", "cm", "2.14"));
		temp.add(new TestUnit("", "left ventricular ejection fraction", "lvef", "%", "10"));
		temp.add(new TestUnit("", "pulmonary artery pressure", "mean pa pressure", "mmhg", "22.8"));
		temp.add(new TestUnit("trivial", "aortic valve regurgitation", "aortic regurgitation", "", ""));
		temp.add(new TestUnit("dilatation", "left ventricular size", "lv dilatation", "", ""));
		temp.add(new TestUnit("dilatation","left atrium size at end systole","la","",""));
		return temp;
	}

	private HashSet<TestUnit> loadTemp4_1() {
		HashSet<TestUnit> temp = new HashSet<TestUnit>();
		// False positive:
		temp.add(new TestUnit("", "aortic valve regurgitation", "ar pht", "msec", "574"));

		temp.add(new TestUnit("", "left atrium size at end systole", "la diam", "cm", "5"));
		temp.add(new TestUnit("", "left ventricular ejection fraction", "ejection fraction", "%", "60 65%"));
		temp.add(new TestUnit("hypertrophy; moderate concentric", "left ventricular hypertrophy",
		    "left ventricular hypertrophy", "", ""));
		temp.add(new TestUnit("mild", "aortic valve regurgitation", "aortic regurgitation", "", ""));
		temp.add(new TestUnit("mild", "mitral valve regurgitation", "mitral regurgitation", "", ""));
		temp.add(new TestUnit("mild", "tricuspid valve regurgitation", "tricuspid regurgitation", "", ""));
		temp.add(new TestUnit("evidence of moderate; hypertension", "pulmonary artery pressure",
		    "pulmonary hypertension", "", ""));
		temp.add(new TestUnit("normal", "left ventricular size", "left ventricular chamber size", "", ""));
		temp.add(new TestUnit("hypertrophy; moderate concentric", "left ventricular hypertrophy",
		    "left ventricular hypertrophy", "", ""));
		temp.add(new TestUnit("", "left ventricular ejection fraction", "ejection fraction", "%", "60 65%"));
		temp.add(new TestUnit("mild to moderately dilated", "left atrium size at end systole", "left atrium", "",
		    ""));
		temp.add(new TestUnit("mild", "aortic valve regurgitation", "aortic regurgitation", "", ""));
		temp.add(new TestUnit("mild", "mitral valve regurgitation", "mitral regurgitation", "", ""));
		temp.add(new TestUnit("mild", "tricuspid valve regurgitation", "tricuspid regurgitation", "", ""));
		temp.add(new TestUnit("evidence of moderate; hypertension", "pulmonary artery pressure",
		    "pulmonary hypertension", "", ""));
		temp.add(new TestUnit("", "interventricular septum dimension at end diastole", "ivsd", "cm", "1.5"));
		temp.add(new TestUnit("", "left ventricular dimension at end diastole", "lvidd", "cm", "5.4"));
		temp.add(new TestUnit("", "left ventricular posterior wall thickness at end diastole", "lvpwd", "cm",
		    "1.4"));
		temp.add(new TestUnit("", "left ventricular dimension at end systole", "lvids", "cm", "3.1"));
		temp.add(new TestUnit("", "aortic valve max pressure gradient", "av pg", "mmhg", "11"));
		temp.add(new TestUnit("", "aortic valve mean gradient", "av mg", "mmhg", "5")); 
		temp.add(new TestUnit("", "tricuspid valve regurgitation peak velocity", "tr peak", "m/sec", "2.9"));

		return temp;
	}

	private HashSet<TestUnit> loadTemp5_1() {
		HashSet<TestUnit> temp = new HashSet<TestUnit>();
		// false positive
		temp.add(new TestUnit("normal","left ventricular dimension at end diastole","lvid ed","",""));
		temp.add(new TestUnit("normal","left atrium size at end systole","left atrium","",""));
		
		//True positives
		temp.add(new TestUnit("","interventricular septum dimension at end diastole","ivs ed","mm","11"));
		temp.add(new TestUnit("","left ventricular dimension at end diastole","lvid ed","mm","50"));
		temp.add(new TestUnit("","left ventricular dimension at end systole","lvid es","mm","33"));
		temp.add(new TestUnit("","left ventricular ejection fraction","left ventricular ejection fraction","%","50 % to 55 %"));
		temp.add(new TestUnit("","left ventricular ejection fraction","left ventricular ejection fraction","%","50 % to 55 %"));
		temp.add(new TestUnit("","left ventricular posterior wall thickness at end diastole","lvpwt ed","mm","11"));
		temp.add(new TestUnit("mild to moderately dilated","left atrium size at end systole","left atrium","",""));
		temp.add(new TestUnit("mild to moderately dilated","left atrium size at end systole","left atrium","",""));
		temp.add(new TestUnit("mildly increased","left ventricular posterior wall thickness at end diastole","left ventricular wall thickness","",""));
		temp.add(new TestUnit("mildly increased","left ventricular posterior wall thickness at end diastole","left ventricular wall thickness","",""));
		temp.add(new TestUnit("no evidence for","aortic valve stenosis","aortic valve stenosis","",""));
		temp.add(new TestUnit("no significant","mitral valve regurgitation","mitral valvular regurgitation","",""));
		temp.add(new TestUnit("no significant","tricuspid valve regurgitation","tricuspid valvular regurgitation","",""));
		temp.add(new TestUnit("normal","left ventricular size","left ventricular size","",""));

		return temp;
	}

	private HashSet<TestUnit> loadTemp6() {
		HashSet<TestUnit> temp = new HashSet<TestUnit>();
		// False positive
		temp.add(new TestUnit("hypertension; mild","pulmonary artery pressure","pulmonary hypertension","",""));
		temp.add(new TestUnit("mild", "interventricular septum dimension at end diastole", "septal", "", ""));
		temp.add(new TestUnit("", "interventricular septum dimension at end diastole", "septum", "cm", "1.2"));
		temp.add(new TestUnit("", "left atrium size at end systole", "left atrium", "cm", "3.4"));
		temp.add(new TestUnit("", "left ventricular dimension at end diastole", "lv diastole", "cm", "4.6"));
		temp.add(new TestUnit("", "left ventricular ejection fraction", "ef", "%", "50 55%"));
		temp.add(new TestUnit("", "left ventricular ejection fraction", "ef", "%", "50 55%"));
		temp.add(new TestUnit("", "left ventricular posterior wall thickness at end diastole", "posterior wall", "cm", "1.2"));
		temp.add(new TestUnit("","left ventricular dimension at end systole","lv systole","cm","3.6"));
		temp.add(new TestUnit("abnormal; mild", "mitral valve regurgitation", "mr", "", ""));
		temp.add(new TestUnit("mild", "aortic valve regurgitation", "ai", "", ""));
		temp.add(new TestUnit("mild", "interventricular septum dimension at end diastole", "septal", "", ""));
		temp.add(new TestUnit("mild", "left ventricular hypertrophy", "lvh", "", ""));
		temp.add(new TestUnit("mild", "mitral valve regurgitation", "mr", "", ""));
		temp.add(new TestUnit("mild", "tricuspid valve regurgitation", "tr", "", ""));
		temp.add(new TestUnit("mild", "tricuspid valve regurgitation", "tr", "", ""));
		temp.add(new TestUnit("mild; none", "aortic valve regurgitation", "ai", "", ""));
		temp.add(new TestUnit("normal size; normal", "left atrium size at end systole", "left atrium", "", ""));
		temp.add(new TestUnit("preserved", "left ventricular ejection fraction", "lv systolic function", "", ""));
		temp.add(new TestUnit("preserved", "left ventricular ejection fraction", "lv systolic function", "", ""));


		return temp;
	}

	private HashSet<TestUnit> loadTemp9() {
		HashSet<TestUnit> temp = new HashSet<TestUnit>();
		temp.add(new TestUnit("normal", "left ventricular ejection fraction", "systolic function", "", ""));
		temp.add(new TestUnit("hypertrophy; no", "left ventricular hypertrophy", "hypertrophy", "", ""));
		temp.add(new TestUnit("hypertension", "pulmonary artery pressure", "pulmonary hypertension", "", ""));
		temp.add(new TestUnit("lower limits of normal", "left ventricular ejection fraction", "lv function", "", ""));
		temp.add(new TestUnit("", "left ventricular ejection fraction", "ef", "%", "around 50"));
		temp.add(new TestUnit("", "left ventricular dimension at end diastole", "lv internal dimension (diastole)", "cm", "6.2"));
		temp.add(new TestUnit("", "left ventricular dimension at end systole", "lv internal dimension (systole)", "cm", "4.5"));
		temp.add(new TestUnit("", "interventricular septum dimension at end diastole", "septal thickness (diastole)", "cm", "1.0"));
		temp.add(new TestUnit("", "left atrium size at end systole", "left atrial dimension", "cm", "4.4"));
		temp.add(new TestUnit("none", "aortic valve regurgitation", "aortic valve regurgitation", "", ""));
		temp.add(new TestUnit("trivial", "mitral valve regurgitation", "mitral valve regurgitation", "", ""));
		temp.add(new TestUnit("none", "tricuspid valve regurgitation", "tricuspid valve regurgitation", "", ""));
		temp.add(new TestUnit("mild dilation", "left atrium size at end systole", "left atrial size", "", ""));

		return temp;
	}

	private HashSet<TestUnit> loadTemp10() {
		HashSet<TestUnit> temp = new HashSet<TestUnit>();
		temp.add(new TestUnit("", "left atrium size at end systole", "left atrium", "cm", "4.5"));
		temp.add(new TestUnit("", "interventricular septum dimension at end diastole", "iv septum", "cm", "2.0"));
		temp.add(new TestUnit("", "left ventricular posterior wall thickness at end diastole",
		    "lv posterior wall", "cm", "1.8"));
		temp.add(new TestUnit("", "left ventricular dimension at end diastole", "lvid (diastolic)", "cm", "6.7"));
		temp.add(new TestUnit("abnormal", "left ventricular ejection fraction", "left ventricular function", "",
		    ""));
		temp.add(new TestUnit("hypertrophy; concentric", "left ventricular hypertrophy",
		    "left ventricular hypertrophy", "", ""));
		temp.add(new TestUnit("", "left ventricular ejection fraction", "left ventricular ejection fraction",
		    "%", "30 35%"));
		temp.add(new TestUnit("enlargement; mild", "left atrium size at end systole", "left atrial enlargement",
		    "", ""));
		temp.add(new TestUnit("trace", "mitral valve regurgitation", "mitral regurgitation", "", ""));
		temp.add(new TestUnit("mild", "tricuspid valve regurgitation", "tricuspid regurgitation", "", ""));

		return temp;
	}

	private HashSet<TestUnit> loadTemp12() {
		HashSet<TestUnit> temp = new HashSet<TestUnit>();
		temp.add(new TestUnit("","left atrium size at end systole","left atrium","","40"));
		temp.add(new TestUnit("","left ventricular dimension at end diastole","left ventricular diastole","","48"));
		temp.add(new TestUnit("","left ventricular dimension at end systole","left ventricular systole","","32"));
		temp.add(new TestUnit("","left ventricular posterior wall thickness at end diastole","posterior wall","","11"));
		temp.add(new TestUnit("","interventricular septum dimension at end diastole","iv septum","","12"));
		temp.add(new TestUnit("trace","mitral valve regurgitation","mitral regurgitation","",""));
		temp.add(new TestUnit("normal","pulmonary artery pressure","pa pressures","",""));
		temp.add(new TestUnit("trace","tricuspid valve regurgitation","tricuspid regurgitation","",""));
		temp.add(new TestUnit("","left ventricular ejection fraction","left ventricular ejection fraction","%","65"));
		temp.add(new TestUnit("hypertrophy; concentric","left ventricular hypertrophy","hypertrophy","",""));
		temp.add(new TestUnit("preserved","left ventricular ejection fraction","left ventricular systolic function","",""));


		return temp;
	}

	private HashSet<TestUnit> loadTemp16() {
		HashSet<TestUnit> temp = new HashSet<TestUnit>();
		temp.add(new TestUnit("","interventricular septum dimension at end diastole","interventricular septal thickness diastolic","mm","12"));
		temp.add(new TestUnit("","left ventricular dimension at end diastole","left ventricular dimension diastolic","mm","52"));
		temp.add(new TestUnit("","left ventricular dimension at end systole","left ventricular dimension systolic","mm","40"));
		temp.add(new TestUnit("","left atrium size at end systole","left atrium end systolic","mm","38"));
		temp.add(new TestUnit("","left ventricular ejection fraction","left ventricular ejection fraction","%","more than 55"));
		temp.add(new TestUnit("normal","left ventricular ejection fraction","left ventricular systolic function","",""));

		return temp;
	}

	private HashSet<TestUnit> loadTemp18() {
		HashSet<TestUnit> temp = new HashSet<TestUnit>();
		temp.add(new TestUnit("", "left ventricular dimension at end diastole", "lv diastolic dimension", "cm",
		    "5.2"));
		temp.add(new TestUnit("", "left ventricular dimension at end systole", "lv systolic dimension", "cm",
		    "3.1"));
		temp.add(new TestUnit("", "interventricular septum dimension at end diastole",
		    "iv septum wall thickness", "cm", "1.7"));
		temp.add(new TestUnit("", "left ventricular posterior wall thickness at end diastole",
		    "lv posterior wall thickness", "cm", "1.0"));
		temp.add(new TestUnit("", "left ventricular ejection fraction", "ejection fraction", "%", "63"));
		temp.add(new TestUnit("", "left atrium size at end systole", "left atrial dimension", "cm", "3.6"));
		temp.add(new TestUnit("normal in size", "left ventricular size", "left ventricle is normal in size", "",
		    ""));
		temp.add(new TestUnit("normal", "left ventricular ejection fraction",
		    "left ventricular systolic function", "", ""));
		temp.add(new TestUnit("normal in size", "left atrium size at end systole", "left atrium", "", ""));
		temp.add(new TestUnit("trace", "mitral valve regurgitation", "regurgitation of the mitral valve", "", ""));
		temp.add(new TestUnit("trace", "aortic valve regurgitation", "regurgitation of the aortic valve", "", ""));
		temp.add(new TestUnit("trace", "tricuspid valve regurgitation", "regurgitation of the tricuspid valve",
		    "", ""));
		temp.add(new TestUnit("hypertrophy; concentric", "left ventricular hypertrophy", "hypertrophy", "", ""));

		return temp;
	}

	private HashSet<TestUnit> loadTemp19() {
		HashSet<TestUnit> temp = new HashSet<TestUnit>();
		temp.add(new TestUnit("", "left atrium size at end systole", "left atrium", "cm", "3.6"));
		temp.add(new TestUnit("", "interventricular septum dimension at end diastole", "iv septum", "cm", "1.0"));
		temp.add(new TestUnit("", "left ventricular posterior wall thickness at end diastole", "lv post wall",
		    "cm", "1.0"));
		temp.add(new TestUnit("normal", "left ventricular ejection fraction", "lv systolic function", "", ""));
		temp.add(new TestUnit("", "left ventricular ejection fraction", "lv ejection fraction", "%", "60"));
		temp.add(new TestUnit("trace", "mitral valve regurgitation", "mitral regurgitation", "", ""));
		temp.add(new TestUnit("mild", "tricuspid valve regurgitation", "tricuspid regurgitation", "", ""));
		temp.add(new TestUnit("", "pulmonary artery pressure", "pasp", "mmhg", "26"));
		temp.add(new TestUnit("no; hypertension", "pulmonary artery pressure", "pulmonary hypertension", "", ""));

		return temp;
	}

	private HashSet<TestUnit> loadTemp27() {
		HashSet<TestUnit> temp = new HashSet<TestUnit>();
		// false negative
		// 3.7__mmHg  Aortic Valve... Max Press Grad -- split term
		// False positive

		temp.add(new TestUnit("", "left ventricular dimension at end systole", "lv systole", "%", "32"));
		// True positive
		temp.add(new TestUnit("mild", "left ventricular hypertrophy", "lvh", "", ""));
		temp.add(new TestUnit("hypertrophy", "left ventricular hypertrophy", "posterior wall hypertrophy", "", ""));
		temp.add(new TestUnit("mild", "mitral valve regurgitation", "mr", "", ""));
		temp.add(new TestUnit("wnl", "pulmonary artery pressure", "pap", "", ""));
		temp.add(new TestUnit("normal", "left atrium size at end systole", "left atrium", "cm", "3.7"));
		temp.add(new TestUnit("normal", "left ventricular posterior wall thickness at end diastole", "lv post wall", "cm", "1.3"));
		temp.add(new TestUnit("normal", "left ventricular dimension at end diastole", "lv diastole", "cm", "5.7"));
		temp.add(new TestUnit("", "left ventricular dimension at end systole", "lv systole", "cm", "3.9"));
		temp.add(new TestUnit("", "left ventricular ejection fraction", "est. ejection fraction", "%", "59"));
		temp.add(new TestUnit("normal", "left ventricular ejection fraction", "est. ejection fraction", "", ""));
		temp.add(new TestUnit("", "pulmonary artery pressure", "pa pressure", "mmhg", "26"));
		temp.add(new TestUnit("", "tricuspid valve regurgitation peak velocity", "tr vel", "", "2.0"));

		return temp;
	}

	private HashSet<TestUnit> loadTemp() {
		HashSet<TestUnit> temp = new HashSet<TestUnit>();
		return temp;
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
	public void testTemplate() throws Exception {
		File dir = new File(filePath);
		File[] temps = dir.listFiles();
		//TODO: add more test cases
		HashMap<String, HashSet<TestUnit>> referenceStandard = new HashMap<String, HashSet<TestUnit>>();
		//referenceStandard.put("temp.txt", loadTemp0());
		referenceStandard.put("template0.txt", loadTemp0());
		referenceStandard.put("template1.txt", loadTemp1());
		referenceStandard.put("template2.txt", loadTemp2());
		referenceStandard.put("template3.txt", loadTemp3());

		referenceStandard.put("template4_1.txt", loadTemp4_1());
		referenceStandard.put("template5_1.txt", loadTemp5_1());
		referenceStandard.put("template6.txt", loadTemp6());
		referenceStandard.put("template9.txt", loadTemp9());
		referenceStandard.put("template10.txt", loadTemp10());
		referenceStandard.put("template12.txt", loadTemp12());
		referenceStandard.put("template16.txt", loadTemp16());
		referenceStandard.put("template18.txt", loadTemp18());
		referenceStandard.put("template19.txt", loadTemp19());
		referenceStandard.put("template27.txt", loadTemp27());

		AnalysisEngine ae = UIMAFramework.produceAnalysisEngine(
		    aggregate.getAnalysisEngineDescription());
		//TODO
		// If test all, comment out lines below.
		/**
		 String tempFile = filePath;
		  //tempFile= "input" + File.seperator;
		  temps = new File[]{new File(tempFile+"template27.txt")};
		  
		 /**/
		for (File currFile : temps) {
			//String filePath = "input" + File.seperator + t;
			//String docText = FileUtils.file2String(new File(filePath));
			String docText = FileUtils.file2String(currFile);

			if (StringUtils.isBlank(docText)) {
				System.out.println("The loaded document is blank at " + filePath);
			} else {
				// Step 1: process the file
				System.out.println("\r\n------  Start  " + currFile.getName() + " -------\r\n");
				JCas jcas = ae.newJCas();
				jcas.setDocumentText(filter(docText));
				ae.process(jcas);
				try {
					File xmio = new File(outputDir, currFile.getName() + ".xmi");
					XmiCasSerializer.serialize(jcas.getCas(), new FileOutputStream(xmio));
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(e.getMessage());
				}
				// Step 2: compare Relation1 to referenceStandard
				HashSet<TestUnit> currentRefSt = referenceStandard.get(currFile.getName());
				if (currentRefSt != null) {
					compareFileToRefSt(jcas, currentRefSt, Relation1.type);
				}

				System.out.println("\r\n------  End  " + currFile.getName() + " -------\r\n");
			}//end else

		}//end for
	}//test

}

class TestUnit {
	String term;
	String mapping;
	String assessment;
	String valueString;
	String unit;

	/**
	 * 
	 * @param t
	 * @param m
	 * @param a
	 * @param v
	 * @param u
	 */
	TestUnit(String a, String m, String t, String u, String v) {
		term = t;
		mapping = m;
		assessment = a;
		valueString = v;
		unit = u;
	}

	public boolean equals(TestUnit tu) {
		boolean isEqual = true;
		if (isEqual)
			isEqual = this.term.equals(tu.term);
		if (isEqual)
			isEqual = this.mapping.equals(tu.mapping);
		if (isEqual)
			isEqual = this.assessment.equals(tu.assessment);
		if (isEqual)
			isEqual = this.valueString.equals(tu.valueString);
		if (isEqual)
			isEqual = this.unit.equals(tu.unit);

		return isEqual;
	}

	public String toString() {
		return "temp.add(new TestUnit(\"" + assessment + "\",\"" + mapping + "\",\"" + term + "\",\""
		    + unit + "\",\"" + valueString + "\"));";
	}
}
