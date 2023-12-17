package code;

import file.TMPLReader;
import file.FilesWriter;
import metadata.MetadataHandler;

public class CodeGenerator {

    private MetadataHandler metadataHandler ;

 
    private String progFile ;
    private String progLang ;

    private String pathTarget;
    private String packageName;


    public CodeGenerator(String jdbcUrl, String username, String password , 
                            String databaseConfigurationFile ,String databaseLanguage ,
                            String programmingLanguageConfFile ,String programmingLanguage ,
                            String pathTarget , String packageName
                            ) throws Exception
    {
        metadataHandler = new MetadataHandler(jdbcUrl, username, password, databaseConfigurationFile, databaseLanguage);
        setProgFile(programmingLanguageConfFile);
        setProgLang(programmingLanguage);        
        setPathTarget(pathTarget);
        setPackageName(packageName); 
    }


       public static void main(String[] args) throws Exception {
        String jdbcurl = "jdbc:postgresql://localhost:5432/society";
        String username = "postgres";
        String password = "post";
        String databaseConf = "C:\\Users\\ratia\\Desktop\\workspace\\S5\\MrNaina\\ProjectBuilder2.0\\configurations\\sql.tmpl";
        String progFileConf ="C:\\Users\\ratia\\Desktop\\workspace\\S5\\MrNaina\\ProjectBuilder2.0\\configurations\\template\\lang.tmpl";
        String progLang = "java";
        String databaseLang ="Postgres" ;
        String pathTarget = "C:\\Users\\ratia\\Desktop\\workspace\\S5\\MrNaina\\ProjectBuilder2.0\\configurations";
        String packageName ="models";
        // String jdbcurl = args[0];
        // String username = args[1];
        // String password = args[2];
        // String databaseConf = args[3];

        // String progFileConf = args[4];
        // String progLang = args[5];
        // String databaseLang = args[6];

        // String pathTarget = args [7];
        // String packageName = args [8];
        // CodeGen code = new CodeGen(jdbcurl, username, password, databaseConf, "Postgres", progFileConf, progLang, "C:\\Users\\ratia\\Desktop\\workspace\\S5\\MrNaina\\ProjectBuilder\\src\\projectBuilder", "models");
        CodeGenerator code = new CodeGenerator(jdbcurl, username, password, databaseConf, databaseLang, progFileConf, progLang, pathTarget, packageName);
        code.generateStandardCodes();


    }


    public void generateStandardCodes() throws Exception {  
        String [] classes = metadataHandler.getListTables(null);
        String [] codes =  getStandardCodes(classes);

        for (int i = 0; i < codes.length; i++) {
            FilesWriter.writeStringToFile(getPathTarget()+"\\"+classes[i].substring(0,1).toUpperCase()+classes[i].substring(1)+"."+getProgLang().toLowerCase(), codes[i]);
        }
    }

    public String [] getStandardCodes(String [] classes) throws Exception {
        String [] codes = new String[classes.length];
        for (int i = 0; i < classes.length; i++) {
            codes[i]= getStandardCode(classes[i]);            
        }
        return codes;
    }


    public String getStandardCode(String className) throws Exception
    {
        StringBuilder str = new StringBuilder() ;

        // init package
        str.append(getPackage(className));
        // adding imports
        str.append(getImport(className));
        // adding class declaration
        str.append(getClassStructure(className));

        // prepare fields name and type 
        // from database
        String [][] fieldsNameAndType = getFieldsNameAndType(className);

        // adding private fields
        str.append(getFields(fieldsNameAndType)) ;

        // adding setters
        str.append(getSetters(fieldsNameAndType));

        // adding getters
        str.append(getGetters(fieldsNameAndType)) ;

        str.append(getEndline());

        return str.toString();

    }


    private String getEndline() throws Exception{
        return TMPLReader.getValueFromFile(getProgFile(),"All", new String[] {"endLine",getProgLang()});
    }


    private String getGetters(String[][] fieldsNameAndType) throws Exception {
        StringBuilder result = new StringBuilder();
        String models = TMPLReader.getValueFromFile(getProgFile(), "All", new String[]{"gettersDeclaration",getProgLang()} );

        for (int i = 0; i < fieldsNameAndType.length; i++) {
            String newGetters = models.replaceAll("#type#", fieldsNameAndType[i][1]);
            newGetters = newGetters.replaceAll("#Fields#", fieldsNameAndType[i][0]);

            result.append(newGetters);
        }
        return result.toString();
    }

    private String getSetters(String[][] fieldsNameAndType) throws Exception {
        StringBuilder result = new StringBuilder();
        String models = TMPLReader.getValueFromFile(getProgFile(),  "All", new String[]{"settersDeclaration",getProgLang()});

        for (int i = 0; i < fieldsNameAndType.length; i++) {
            String newGetters = models.replaceAll("#type#", fieldsNameAndType[i][1]);
            newGetters = newGetters.replaceAll("#Fields#", fieldsNameAndType[i][0]);
            result.append(newGetters);
        }
        return result.toString();
    }

    public String getFields(String [][] fieldsAndType) throws Exception{
        
        StringBuilder result = new StringBuilder();
        
        String fieldsStructure = TMPLReader.getValueFromFile(getProgFile(), "All", new String[]{"privateFieldsDeclaration",getProgLang()}) ;
        for (int i = 0; i < fieldsAndType.length; i++) {
            String toAppend = fieldsStructure.replaceFirst("#type#", fieldsAndType[i][1]);
            toAppend = toAppend.replaceFirst("#fieldName#", fieldsAndType[i][0]); 
            result.append(toAppend);
        }
        return result.toString() ;
    }

    public String [][] getFieldsNameAndType (String className) throws Exception{

        String [][] fieldsNameAndType = getMetadataHandler().getColumnsAndTypes(null, className);
        
        for (int i = 0; i < fieldsNameAndType.length; i++) {
            
            String types = TMPLReader.getValueFromFile(getProgFile(),"All", new String[]{ fieldsNameAndType[i][1],getProgLang()});  
            
            if(!types.contains(";"))
                fieldsNameAndType[i][1]= types.trim();
            else
                fieldsNameAndType[i][1]= types.strip().split(";")[0].trim();
        }

        return fieldsNameAndType ;
    }

    public String getClassStructure(String className) throws Exception{
        
        return TMPLReader.getValueFromFile(getProgFile(), "All", new String[]{ "classDeclaration",getProgLang()}).replaceFirst("#className#", className.substring(0, 1).toUpperCase()+className.substring(1));

    }

    public StringBuilder getImport(String className) throws Exception{

        StringBuilder imports = new StringBuilder();

        

        String [][] fieldsNameAndType = getMetadataHandler().getColumnsAndTypes(null, className);

        for (int i = 0; i < fieldsNameAndType.length; i++) {
            
            String types =TMPLReader.getValueFromFile(getProgFile(), "All", new String[]{ fieldsNameAndType[i][1],getProgLang()});    
            if(types.contains(";"))
            {
                
                String importModel = TMPLReader.getValueFromFile(getProgFile(),"All", new String[]{ types.strip().split(";")[1],getProgLang()});
                imports.append(importModel);
            }

        }

        return imports;

    }

    public String getPackage(String className) throws Exception{

        String packageName = TMPLReader.getValueFromFile(getProgFile(),"All", new String[]{ "Package",getProgLang()}) ;
        return packageName.replaceFirst("#packageName#", getPackageName()).trim();
        
    }


    public MetadataHandler getMetadataHandler() {
        return metadataHandler;
    }

    public void setMetadataHandler(MetadataHandler metadataHandler) {
        this.metadataHandler = metadataHandler;
    }


    public String getProgFile() {
        return progFile;
    }

    public void setProgFile(String progFile) {
        this.progFile = progFile;
    }

    public String getProgLang() {
        return progLang;
    }

    public void setProgLang(String progLang) {
        this.progLang = progLang;
    }

    public String getPathTarget() {
        return pathTarget;
    }

    public void setPathTarget(String pathTarget) {
        this.pathTarget = pathTarget;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

}
