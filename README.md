EchoExtractor
=============

Project description:
--------------------

The goal of the current application is to extract Concept-Value pairs for metrics measured during an echocardiogram study. The input is a text document to be processed. The output is a dataset where each record represent a Concept-Value relationship. 

### Definitions
<dl>
<dt> String </dt>
<dd>a sequence of characters found in text. </dd>
<dd>For example, "lvef", "e:e", "5 mg", "50-55%", "calculated lv ejection fraction was 50-55%", "Attending: Dr. DoLittle"   are strings.</dd>
<dt>Term</dt>
<dd>a string that has a specific meaning, which may or may not be identified.</dd>
<dd>For example, "lvef" is a string, but it is also a term because it represents a clinical variable.</dd>
<dt>Concept</dt>
<dd>a specific meaning.</dd>
<dd>For example, terms "ef", "lvef", "lv ej frac", "ejection fraction", "edjection fractian"  are all terms (the last one is a misspelling) that represent the same concept "left ventricular ejection fraction"</dd>
<dt>Mapping</dt>
<dd>a link between a term and a concept.</dd>
<dd>For example, "lv ef" maps to "left ventricular ejection fraction”</dd>
<dt>Value</dt>
<dd>ValueString - a string that represents a numeric value of the target concept.</dd>
<dt>Unit</dt>
<dd>is a string that represents a unit for measure for the numeric value of the target concept</dd>
<dd>For example, in a phrase "calculated lv ejection fraction was 50-55%”, ValueString is 50-55 with associated Unit % for Term “lv ejection fraction”</dd>
<dt>Assessment</dt>
<dd> a string that represents a qualitative assessment of the target concept</dd>
<dd>For example, in a phrase “trace mitral regurgitation” Assessment is “trace” for Term “mitral regurgitation”</dd>
<dt>Concept-Value pair</dt>
<dd>an association between a term, which is mapped to a concept, and a value (qualitative or quantitate) found in text. Creating a concept-value pair hinges on correct identification of the strings that represent term, numeric value and unit, and also correct mapping of the term to the dictionary entry for the concept.</dd>
<dd>For example, in a sentence "The ejection fraction was visually estimated in a range of 50% to 55%.”
* Term is “ejection fraction”
* ValueString is  “50% to 55%”
* Unit for the value is "%”
* Mapping is  “left ventricular ejection fraction”
</dd>
</dl>


System Description
---------------------

### The pipeline has the following modules:

1. ConceptAnnotator - regex to identify unusual combination of characters that describe a concept [MVA(p1/2t), AVA(i,d), ...]
1. AnatomyAnnotator - regex to identify strings that represent heart anatomy [left, right, ventricle, atrium, systole, ...]
1. MeasurementAnnotator - regex to identify strings that represent measurements [size, diameter, velocity, mean, peak, ...]
1. QValueAnnoator - regex to identify strings that represent qualitative measurements [normal, mild, severe, dysfunction, ...]
1. NumericValueAnnotator - regex to identify stings that represent numeric values for the measurements [2, 2.5, about 2, > 55 ,...]
1. UnitAnnotator - regex to identify strings that represent units of measure for the numeric value [mm, hgmm, m/sec, cm^2, ... ]
1. MiddleStuffAnnotator - regex to identify text commonly used to link concepts and their values [was, is calculated at, found to be, ...]
1. MethodAnnotator - regex to identify strings that represent method or mode of measurement acquisition [mmode, doppler, Simpson's, ...]
1. HeaderAnnotator - regex to identify strings that most often represent subsection headers in Echo reports. Used to identify scope for anatomy.
1. ExcludeValueAnnotator - regex to identify special cases when a numeric or qualitative value should be ignored [`\d+\/\d+\/\d+` because it is a date...]
1. ExcludeConceptAnnotator - regex to identify cases when a term should be disregarded for further processing because it does not represent a valid concept when is mentioned by itself [time, velocity, date, ...]
1. ModifierAnnotator - regex to identify strings that provide more insight into the context of the term [visually estimated, biplane, ...]
1. AnnotationFilter1 - a custom annotator that removes smallest annotation when overlaps with another annotation of the same type or some other types.
1. RangeAnnotator - an APA to combine multiple NumericValue annotations into one that represent a range.
1. ConceptCollectorAnnotator - a custom annotator that combines sequences of Anatomy and Measurement annotations into one annotation.
1. AnnotationFiler2 - a custom annotator that removes smallest annotation when it overlaps with other annotation of the same type or some other type.
1. ConceptMapping - a custom annotator that extends LookupAnnotator that includes additional logic for flexible concept mapping to include most frequent mapping. Results in Mapping annotations.
1. ConceptDisambiguation - a custom annotator that extends ConceptMapping that changes flexible mapping logic to include all mappings for the terms. Changes Mapping annotations.
1. RelationshipPatternAnnotator - APA that combines all other annotations into relations. Results in RelationPattern annotations.
1. AnnotationFilter3 - a custom annotator that removes the smallest annotation when it overlaps with other annotations of RelationPattern type.
1. RelationAnnotator - a custom annotator that contains the main logic for determining Concept-Value pairs. Results in the following annotations: 
    * Relation1 - for terms that were unambiguously mapped to a single target concept
    * Relation2 - for terms that were unambiguously mapped to a single concept that is not one of the target concepts
    * Relation3 - for terms that were mapped ambiguously (had more than 1 mappings)

The output into a database or csv file has the following columns:

1. DocID - varchar(25) - document id. TIUDocumentSID for TIU docs, EchoSID for Echo691 docs, concat([RadNucMedReportIEN],'_',Sta3n) for RadiologyNotes.
2. PatientID - bigint - patient id. PatientSID for TIU docs and Echo691, ScrSSN for RadiologyNotes.
3. ReferenceDate - date - ReferenceDate from TIU docs, Datetime from Echo691, ExamDateTime for RadiologyNotes
4. InstanceID - int - sequential number of the instance in the document
5. SpanStart - int - Relation span start
6. SpanEnd - int - Relation span end
7. Snippets - varchar(1000) - Relation covered text
8. Term - varchar(500) - Term covered text
9. ValueString - varchar(1000) - NumericValue covered text
1. Value - float - first numeric value of NumericValue covered text. Represents a lower bound of the range if the NumericValue is a range.
1. Value2 - float - second numeric value of NumericValue covered text. Represents an upper bound of the range if the NumericValue is a range.
1. Unit - varchar(25) - Units covered text
1. Assessment - varchar(1000) - QValue covered text
1. ConceptType - pattern used to create the RelationPattern that was used to create the Relation. Used to filter unused patterns.
1.  Mapping - varchar(5000) - a pipe-delimited numbered list of mappings. Numbering starts at 0. If Mapping not like '%1%' that means the  field does not have a second mapping therefore there is just one mapping for that term.
1. Modifier - varchar(1000) - additional text that provides more context for the term. Can potentially be used in post-processing.


## Target concepts:
<table border="1">
<tr><th> Concept </th><th>Definition</th><th>Example</th></tr>
<tr><td>aortic valve mean gradient </td><td>The difference between the ventricular pressure and the recovered aortic pressure averaged across multiple measurements. [mmHg] </td><td> AV PG <br> AV mean grad <br> aortic mean gradient </td></tr>
<tr><td>aortic valve orifice area</td><td> Area of the aortic valve opening measured at systole. [mm^2]	</td><td>AV area</td></tr>
<tr><td>aortic valve regurgitation</td><td>(aka aortic insufficiency) - a condition when aortic valve does not close tightly. Measured qualitatively [trace, mild, severe..]) or on a scale  [0..4+] </td><td>	ai 1+ <br> ar <br> aortic insufficiency </td></tr>
<tr><td>aortic valve regurgitation peak velocity	</td><td> Velocity of the regurgitant jet [m/sec]</td><td>aortic pk vel</td></tr>
<tr><td>aortic valve stenosis</td><td>Valve disease in which the opening of the aortic valve is narrowed.  [mild, severe...]	</td><td> AS <br> AV stenosis</td></tr>
<tr><td>e/e prime ratio	</td><td>The ratio of mitral peak velocity of early filling (E) to early diastolic mitral annular velocity (E') (E/E' ratio). Used to detect left ventricular diastolic dysfunction. Normal value is > 8.	</td><td> e:e’<br>e to e prime<br>E/Ea ratio</td></tr>
<tr><td>inter-ventricular septum dimension at end diastole</td><td>Inter-ventricular septal wall thickness [mm]	</td><td>ivs ed<br>IVS(ED)<br>IVSd</td></tr>
<tr><td>left atrium size at end systole</td><td>	diameter of the left atrium measured at the end-systole, when the LA chamber is at its greatest dimension. Normal 28-40 mm [mm]</td><td>	LA dimension<br>dilated LA<br>left atrium<br>LA dilatation<br>LA chamber size<br>LA</td></tr>
<tr><td>left ventricular dimension at end diastole</td><td>The diameter across a ventricle at the end of diastole. [mm]</td><td>LVEDD<br>LVIDD<br>LVED<br>LVD ed<br>end diastolic lv diameter</td></tr>
<tr><td>left ventricular dimension at end systole</td><td>similar to the end-diastolic dimension, but is measured at the end of systole (after the ventricles have pumped out blood) rather than at the end of diastole. [mm]	</td><td>LVESD<br>LV systole</td></tr>
<tr><td>left ventricular size</td><td>general description of size of the left ventricle [normal, dilated, enlarged]	</td><td>LV size<br>dilated left ventricle</td></tr>
<tr><td>left ventricular ejection fraction</td><td>the percentage of blood pumped out of a heart chamber with each contraction  [%, preserved, reduced]<br>Same as LV systolic function or dysfunction [normal, reduced]<br>Same as LV contractility [normal, low, reduced]</td><td>LVEF <br>EF<br>systolic dysfunction</td></tr>
<tr><td>left ventricular posterior wall thickness at end diastole</td><td>	The thickness of the posterior left ventricular wall. [mm]</td><td>LVPWd<br>post LV wall</td></tr>
<tr><td>mitral valve mean gradient</td><td>The pressure gradient across the mitral valve in mitral stenosis is determined by measurement of the maximum recorded velocity of the mitral jet at end-. [mmHg]</td><td>MV PG</td></tr>
<tr><td>mitral valve orifice area </td><td>The normal area of the mitral valve orifice is about 4 to 6 cm2</td><td>MVA</td></tr>
<tr><td>mitral valve regurgitation</td><td>(aka mitral insufficiency) is defined as the abnormal flow of blood through the mitral valve from the left ventricle to the left atrium during systole. [mild, severe, or scale 0...4]</td><td>MR 2-3+<br>1+ MI<br>trace MI<br>MV insufficiency </td></tr>
<tr><td>mitral valve regurgitation peak velocity</td><td>peak mitral regurgitant velocity <br>Mitral Regurgitation jet Vmax [m/s]	</td><td>mr jet vel</td></tr>
<tr><td> mitral valve stenosis</td><td>narrowing of the orifice of the mitral valve of the heart.[mild, severe, is present, no evidence of]	</td><td>MS<br>mitral stenosis</td></tr>
<tr><td>pulmonary artery pressure</td><td>(aka PA pressure) is a measure of the blood pressure found in the pulmonary artery, usually measured during systole. Mean pulmonary arterial pressure is normally 9 - 18 mmHg [mmHg, hypertension]	</td><td>PAP<br>PASP<br>PA systolic pressure</td></tr>
<tr><td>right atrial pressure</td><td>The pressure in the thoracic vena cava near the right atrium</td><td>RAP<br>RA pressure</td></tr>
<tr><td>tricuspid valve mean gradient</td><td>mean diastolic gradient across the tricuspid valve [mmHg]</td><td>TR mean grad</td></tr>
<tr><td>tricuspid valve orifice area	</td><td>tricuspid valve orifice area (TOA) [mm]</td><td>TR area</td></tr>
<tr><td>tricuspid valve regurgitation</td><td>a disorder in which the heart's tricuspid valve does not close properly, causing blood to flow backward (leak) into the right upper heart chamber (atrium) when the right lower heart chamber (ventricle) contracts [trace, mild, 3-4+]</td><td>TR<br>TI<br>TV insufficiency</td></tr>
<tr><td>tricuspid valve regurgitation peak velocity</td><td>	(aka tricuspid regurgitant jet velocity) is measured in order to estimate the right ventricular and pulmonary pressure. [m/s]</td><td>TR jet velocity<br>TR max vel<br>Tricuspid regurgitant vel</td></tr>
</table>


----------------------

Performance stats:
1. with 3 services, 10 instances, 500 cas pool each:
1.1 Client finished in: 0:59:30.659 after processing 46701 records: INSERT INTO ORD_Joseph_201409027D.[nlp].[nlpOut_Sta3n_575_20150325]
1.1 Client finished in: 0:24:21.624 after processing 37934 records: INSERT INTO ORD_Joseph_201409027D.[nlp].[nlpOut_Sta3n_358_20150325]
1.1 Client finished in: 1:00:05.541 after processing 47122 records: INSERT INTO ORD_Joseph_201409027D.[nlp].[nlpOut_Sta3n_687_20150325]
1.1 Client finished in: 1:13:03.345 after processing 55441 records: INSERT INTO ORD_Joseph_201409027D.[nlp].[nlpOut_Sta3n_679_20150325]