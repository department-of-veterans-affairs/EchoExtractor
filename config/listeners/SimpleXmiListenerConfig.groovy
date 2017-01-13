package listeners

import gov.va.vinci.leo.listener.SimpleXmiListener
import gov.va.vinci.leo.tools.LeoUtils

String xmiDir = "src"+ File.separator +"test"+ File.separator +"resources"+ File.separator +"output"+ File.separator +"xmi"  // path to output directory

if(!(new File(xmiDir)).exists()) (new File(xmiDir)).mkdirs()

listener = new SimpleXmiListener(new File(xmiDir), true)
listener.setAnnotationTypeFilter("gov.va.vinci.echo.types.Relation1")
