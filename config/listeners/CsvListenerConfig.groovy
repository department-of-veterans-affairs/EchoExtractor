package listeners

import gov.va.vinci.echo.listeners.CsvListener

String filePath = "output" + File.separator + "output.csv";  // output table
fieldList = [
        ["DocID", "0", "bigint"],
        ["Mapping", "-1", "varchar(500)"],
        ["Value", "-1", "varchar(100)"],
        ["Value2", "-1", "varchar(100)"],
        ["Unit", "-1", "varchar(25)"],
        ["ValueString", "-1", "varchar(100)"],
        ["Assessment", "-1", "varchar(100)"],
        ["InstanceID", "-1", "int"],
        ["Term", "-1", "varchar(100)"],
        ["Snippets", "-1", "varchar(8000)"],
        ["SpanStart", "-1", "int"],
        ["SpanEnd", "-1", "int"]
]
incomingTypes = "gov.va.vinci.echo.types.Relation1"
listener = CsvListener.createNewListener(filePath, fieldList, incomingTypes);
listener.writeHeaders();