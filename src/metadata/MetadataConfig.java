package metadata;

import file.TMPLReader;

public class MetadataConfig {
    
    public static String getTableNameColumn(String language , String databaseConfigurationFile) throws Exception
    {
        try {
            return TMPLReader.getValueFromFile(databaseConfigurationFile, "TableNameColumn",language).trim();
            
        } catch (Exception e) {
                        return TMPLReader.getValueFromFile(databaseConfigurationFile, "TableNameColumn","All").trim();
        }
    }

    public static String getTypeNameColumn(String language , String databaseConfigurationFile) throws Exception
    {
        try {
            return TMPLReader.getValueFromFile(databaseConfigurationFile, "TypeName",language).trim();
        } catch (Exception e) {
            return TMPLReader.getValueFromFile(databaseConfigurationFile, "TypeName","All").trim();
        }
    }

    public static String getColumnNameColumn(String language , String databaseConfigurationFile) throws Exception
    {
        try {
            return TMPLReader.getValueFromFile(databaseConfigurationFile, "ColumnName",language).trim();
        } catch (Exception e) {
            return TMPLReader.getValueFromFile(databaseConfigurationFile, "ColumnName","All").trim();                        
        }
    }

    public static void main(String[] args) throws Exception {
        String filePath = "C:\\Users\\ratia\\Desktop\\workspace\\S5\\MrNaina\\ProjectBuilder2.0\\configurations\\sql.tmpl";
        String lang = "All";
    
        try {
            // Appeler la fonction getTableNameColumn
            String tableNameColumn = getTableNameColumn(lang, filePath);
            System.out.println("TableNameColumn:\n" + tableNameColumn);
    
            // Appeler la fonction getTypeNameColumn
            String typeNameColumn = getTypeNameColumn(lang, filePath);
            System.out.println("TypeNameColumn:\n" + typeNameColumn);
    
            // Appeler la fonction getColumnNameColumn
            String columnNameColumn = getColumnNameColumn(lang, filePath);
            System.out.println("ColumnNameColumn:\n" + columnNameColumn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
