package listeners

import gov.va.vinci.echo.types.Relation1
import gov.va.vinci.leo.listener.SimpleCsvListener
import gov.va.vinci.leo.tools.LeoUtils


String csvDir =   "src" + File.separator + "test" + File.separator +"resources"+ File.separator +"output"+ File.separator +"simple_output.csv" // path to output
if(!(new File(csvDir)).exists()) (new File(csvDir).getParentFile()).mkdirs()

listener = new SimpleCsvListener(new File(csvDir), false, Relation1.canonicalName)