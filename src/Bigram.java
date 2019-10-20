import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Bigram {

    static HashMap<String, Double> mapEN = new HashMap<>();
    static HashMap<String, Double> mapDE = new HashMap<>();
    static HashMap<String, Double> mapES = new HashMap<>();

    // unsmoothed
    public static void readFile(String fileName, HashMap<String, Double> map){
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            while ((tempString = reader.readLine()) != null) {
                char[] temp = tempString.toCharArray();
                for(int i = 0; i < temp.length; i++){
                    if(i == 0){
                        map.put("<" + temp[i], map.getOrDefault("<" + temp[i],0.0) + 1.0);
                        map.put("<", map.getOrDefault("<",0.0) + 1.0);
                    }
                    else if(i == temp.length - 1){
                        map.put(temp[i] + ">", map.getOrDefault(temp[i] + ">",0.0) + 1.0);
                        map.put(">", map.getOrDefault(">",0.0) + 1.0);
                    }
                    else{
                        String key = (temp[i] + "") + (temp[i + 1] + "");
                        map.put(key, map.getOrDefault(key, 0.0) + 1.0);
                    }
                }
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        readFile(Util.fileEN, mapEN);
        readFile(Util.fileDE, mapDE);
        readFile(Util.fileES, mapES);

        Util.writeMapBI(mapEN, "BI-EN");
        Util.writeMapBI(mapDE, "BI-DE");
        Util.writeMapBI(mapES, "BI-ES");

        HashMap<Character, Double> countEN = Util.getMapUNI("UNI-EN-COUNT");
        HashMap<Character, Double> tempEN = new HashMap<>();
        for(char c: countEN.keySet()){
            int num = 0;
            for(String s: mapEN.keySet()){
                if(s.charAt(0) == c){
                    num += mapEN.get(s);
                }
            }
            tempEN.put(c, num / countEN.get(c));
        }
        Util.writePro(tempEN, "Bigram-Sumto1");
    }
}
