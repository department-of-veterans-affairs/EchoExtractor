package listeners

import gov.va.vinci.echo.listeners.BasicDatabaseListener
import gov.va.vinci.leo.tools.LeoUtils

int batchSize = 1000
String url = "jdbc:sqlserver://<dbs_engine>:1433;databasename=<dbs_name>;integratedSecurity=true"
String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver"
String dbUser = ""
String dbPwd = ""

String dbsName = "dbs_name"
String tableName = "[schema].[table_name]"

incomingTypes = "gov.va.vinci.echo.types.Relation1"

fieldList = [
        ["DocID", "0", "varchar(500)"],
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

boolean dropExisting = false;
listener = BasicDatabaseListener.createNewListener(
        driver,
        url,
        dbUser,
        dbPwd,
        dbsName,
        tableName,
        batchSize,
        fieldList,
        incomingTypes)

// Comment out the statement below if you want to add to the existing table
listener.createTable(dropExisting);

