package readers

import gov.va.vinci.leo.cr.BatchDatabaseCollectionReader;
String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver"
String url = "jdbc:sqlserver://<dbs_engine>:1433;databasename=<dbs_name>;integratedSecurity=true"
String query = "select docid, reporttext, rowno from input_table where rowno > {min} and row_no < {max}"

int start_row_index = 0
int end_row_index = 1000000
int batch_size

reader = new BatchDatabaseCollectionReader(
        driver,
        url,
        "", "",
        query,
        "docid","reporttext",
        start_row_index , end_row_index
        , batch_size )
