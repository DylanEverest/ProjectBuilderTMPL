package file;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * TMPLReader
 */
public class TMPLReader {


    private static String format(String x){
        while (x.contains("  ")) {
            x=x.replace("  ", "");
        }
        while (x.contains("$")) {
            x=x.replace("$", "");
        }
        return x.trim();
    }
    private static String simpleFormat(String x){
        while (x.contains("$")) {
           x= x.replace("$", ""); 
        }  
        return x ;
    }

    public static String getValueFromFile(String filePath, String tag) throws Exception {

        String text = readFromFile(filePath);
        text = text.substring(text.indexOf(tag));

        text= text.replaceFirst(tag, "");
        int end = 1;
        int indexA=  text.indexOf("{") +1;
        int i ;
    
        for ( i= indexA; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c=='{') {
                end ++;
            }
            else if(c=='}'){
                end --;
            }
            if (end ==0) {
                break;
            }
        }

        return format(text.substring(indexA, i)) ;

    }


    public static String getValueFromFile(String filePath ,String alternative , String [] tags) throws Exception
    {
        try {
            return simpleFormat(getValueFromFile(filePath, tags)) ;
        } catch (Exception e) {
            tags[tags.length-1] = alternative;
            return simpleFormat(getValueFromFile(filePath, tags)) ;
        }
    }

    public static String getValueFromFile(String filePath, String ... tag) throws Exception {
        String text = readFromFile(filePath);

        for (int j = 0; j < tag.length; j++) {
            try {
                text = text.substring(text.indexOf(tag[j]));
            } catch (Exception e) {
                throw new Exception("Tag not found: "+tag[j]);
            }

            text= text.replaceFirst(tag[j], "");
            int end = 1;
            int indexA=  text.indexOf("{") +1;
            int i ;
        
            for ( i= indexA; i < text.length(); i++) {
                char c = text.charAt(i);
                if (c=='{') {
                   try {
                        if (text.charAt(i-1)!='$') {
                            end ++;
                        }
                    } catch (Exception e) {
                        end++;
                    }
                }
                else if(c=='}'){
                    try {
                        if (text.charAt(i-1)!='$') {
                            end --;
                        }
                    } catch (Exception e) {
                        end--;
                    }
                }
                if (end ==0) {
                    break;
                }
            }            
            text = (text.substring(indexA, i)) ;
        }
        return text; 
    }

    public static String getValue(String text, String tag){
        text = text.substring(text.indexOf(tag));

        text= text.replaceFirst(tag, "");
        int end = 1;
        int indexA=  text.indexOf("{") +1;
        int i ;
    
        for ( i= indexA; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c=='{') {
                end ++;
            }
            else if(c=='}'){
                end --;
            }
            if (end ==0) {
                break;
            }
        }

        return format(text.substring(indexA, i)) ;
    }

    public static String readFromFile(String filePath) {
        StringBuilder content = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content.toString();
    }



    public static void main(String[] args) throws Exception {
        String filePath = "C:\\Users\\ratia\\Desktop\\workspace\\S5\\MrNaina\\ProjectBuilder2.0\\configurations\\sql.tmpl";
        String value = getValueFromFile(filePath, "TableNameColumn", "Allo" );
        System.out.println("Value for " + "hihi" + ":\n" + value);
    }
    
}