package NativeLibraries;

import java.io.BufferedReader;
import java.io.File;


import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A class for parsing JSON<br>
 * WARNING! it uses hard syntax, and write your JSON mod file as this:
 * <blockquote><pre>
 *     {
 *         "name": "Amod"
 *         "desc": "an example mod by JvVireo"
 *         "require":["midde",
 *         "FireAnim","MDA"]
 *     }
 * </blockquote></pre>
 * Using in java:
 * * <blockquote><pre>
 *  public ModMetaData GetMeta(String pack) {
 *         JsonManager JM = new JsonManager();
 *         JM.setTargetFile(new File(pack + "/modData.json"));
 *
 *         return new ModMetaData(JM.getField("name"),JM.getField("desc"),JM.getArrayField("require")));
 *     }
 *  * </blockquote></pre>
 */
public class JsonManager {
    private File TargetFile;
    public  void setTargetFile(File path){
           TargetFile = path;
    }
    public String getField(String field){

        try (BufferedReader br = new BufferedReader(new FileReader(TargetFile))) {
            String line;
            while ((line = br.readLine()) == "{") {
                 line = br.readLine().replace(",","");
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String currentKey = parts[0].trim().replace("\"", "");
                    String currentValue = parts[1].trim().replace("\"", "");

                    if (currentKey.equals(field)) {
                        return currentValue;
                    }
            }
        } } catch (IOException e) {
            System.err.println("Error reading JSON" + e.getMessage());
        }
        return field;
    }
    public ArrayList<String> getArrayField(String Array) {

        ArrayList<String> outArray = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader(TargetFile))) {
            String line;
            while ((line = br.readLine()) == "{") {
                while ((line = br.readLine()) == "\"" + Array + "\":[") {
                    line = br.readLine().replace("]", "");
                    String[] parts = line.split(",");

                        for(String part : parts){

                          outArray.add(part.replace("\"", ""));

                    }

                }
            }
        } catch(IOException e){
                System.err.println("Error reading JSON" + e.getMessage());
            }
        return outArray;
    }}

