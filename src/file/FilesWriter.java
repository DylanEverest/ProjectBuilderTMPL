package file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FilesWriter {

    public static void writeStringToFile(String filePath, String content) {
        try {
            // Créez un objet File avec le chemin spécifié
            File file = new File(filePath);
    
            // Créez le fichier s'il n'existe pas
            if (!file.exists()) {
                file.createNewFile();
            }
    
            // Utilisez un FileWriter avec un deuxième argument pour spécifier que le flux ne doit pas être ajouté à la fin
            FileWriter fileWriter = new FileWriter(file, false);
    
            // Créez un objet BufferedWriter pour écrire dans le fichier
            BufferedWriter writer = new BufferedWriter(fileWriter);
    
            // Écrivez la chaîne dans le fichier
            writer.write(content);
    
            // Fermez le BufferedWriter
            writer.close();
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
