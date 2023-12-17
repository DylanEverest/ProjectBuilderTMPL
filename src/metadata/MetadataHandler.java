package metadata;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MetadataHandler {

    // Connection information
    private String jdbcUrl ;
    private String username ;
    private String password ;


    // databases syntax
    private String tableNameColumn ; 
    private String columnNameColumn ;
    private String typeNameColumn ;


    public MetadataHandler() {
    }

    public MetadataHandler(String jdbcUrl, String username, String password , String databaseConfigurationFile ,String language) throws Exception {
        setJdbcUrl(jdbcUrl);
        setUsername(username);
        setPassword(password);
        setTableNameColumn(databaseConfigurationFile,language);
        setColumnNameColumn(databaseConfigurationFile,language);
        setTypeNameColumn(databaseConfigurationFile,language);
    }

    // get the list of tables in the database


    public String [] getListTables (Connection connection) throws Exception
    {
        // CONNECT AND GET ALL TABLES
        if (connection == null) {
            connection = DriverManager.getConnection(getJdbcUrl(), getUsername(), getPassword()) ;           
        }
        DatabaseMetaData metaData = connection.getMetaData() ;
        ResultSet tables  = metaData.getTables(null, null, "%", new String[] {"TABLE" ,"VIEW"}) ;


        // PREPARE RESULT
        int tableCount =0 ;
        
        while (tables.next()) {
            tableCount ++;
        }

        tables.beforeFirst();
        String [] result = new String[tableCount];
        int i = 0 ;

        while (tables.next()) {
            result[i] = tables.getString(getTableNameColumn());
            i++;
        }

        return result;
    }

    public String [][] getColumnsAndTypes(Connection connection ,String table) throws Exception
    {
        // CONNECT AND GET ALL TABLES
        if (connection == null) {
            connection = DriverManager.getConnection(getJdbcUrl(), getUsername(), getPassword()) ;           
        }
        DatabaseMetaData metaData = connection.getMetaData() ;
        ResultSet columns  = metaData.getColumns(null, null, table, null) ;


        // PREPARE RESULT

        int columnsCount = 0;
        while (columns.next()) {
            columnsCount++ ;
        }

        columns.beforeFirst();
        String [][] columnsAndTypes = new String[columnsCount][2] ;
        int i = 0;

        while (columns.next()) {
            columnsAndTypes[i][0] = columns.getString(getColumnNameColumn()) ;
            columnsAndTypes[i][1] = columns.getString(getTypeNameColumn()) ;
            i++ ;          
        }

        return columnsAndTypes ;


    }





    // GETTERS AND SETTERS
    public String getJdbcUrl() {
        return jdbcUrl;
    }
    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setTableNameColumn(String databaseConfigurationFile , String language) throws Exception {
        tableNameColumn = MetadataConfig.getTableNameColumn(language, databaseConfigurationFile);
    }
    
    public void setColumnNameColumn(String databaseConfigurationFile , String language) throws Exception {
        columnNameColumn = MetadataConfig.getColumnNameColumn(language, databaseConfigurationFile);
    }
    
    public void setTypeNameColumn(String databaseConfigurationFile , String language) throws Exception {
        typeNameColumn = MetadataConfig.getTypeNameColumn(language, databaseConfigurationFile);        
    }
    
    public String getColumnNameColumn() {
        return columnNameColumn;
    }
    
    public String getTypeNameColumn() {
        return typeNameColumn;
    }
    
    public String getTableNameColumn() {
        return tableNameColumn;
    }

public static void main(String[] args) throws Exception {
        
        String jdbcurl = "jdbc:postgresql://localhost:5432/society";
        String username = "postgres";
        String password = "post";
        String databaseConf = "C:\\Users\\ratia\\Desktop\\workspace\\S5\\MrNaina\\ProjectBuilder2.0\\configurations\\sql.tmpl";
        String language = "All";

        MetadataHandler meta = new MetadataHandler(jdbcurl, username, password, databaseConf, language);

        // Get a connection
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(jdbcurl, username, password);
            
            // Test getListTables
            String[] tables = meta.getListTables(connection);
            System.out.println("List of Tables:");
            for (String table : tables) {
                System.out.println(table);
            }

            // Test getColumnsAndTypes for the first table in the list
            if (tables.length > 0) {
                String[][] columnsAndTypes = meta.getColumnsAndTypes(connection, tables[0]);
                System.out.println("\nColumns and Types for " + tables[0] + ":");
                for (String[] columnAndType : columnsAndTypes) {
                    System.out.println(columnAndType[0] + " - " + columnAndType[1]);
                }
            } else {
                System.out.println("No tables found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the connection in a finally block to ensure it's always closed
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
}
