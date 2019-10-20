import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Trigram {
    static HashMap<String, Double> mapEN = new HashMap<>();
    static HashMap<String, Double> mapDE = new HashMap<>();
    static HashMap<String, Double> mapES = new HashMap<>();

    // unsmoothed
    public static void readFile(String fileName, HashMap<String, Double> map){
        File file = new File(fileName);
        BufferedReader reader = null;
        String key;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            while ((tempString = reader.readLine()) != null) {
                char[] temp = tempString.toCharArray();
                for(int i = 0; i < temp.length - 1; i++){
                    if(i == 1){
                        map.put("<" + temp[i - 1] + temp[i], map.getOrDefault("<" + temp[i - 1] + temp[i], 0.0) + 1.0);
                    }
                    if(i == temp.length - 2){
                        key = (temp[i] + "") + (temp[i + 1] + "") + ">";
                        map.put(key, map.getOrDefault(key, 0.0) + 1.0);
                    }
                    else{
                        key = (temp[i] + "") + (temp[i + 1] + "") + (temp[i + 2] + "");
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

        Util.writeMapBI(mapEN, "TRI-EN");
        Util.writeMapBI(mapDE, "TRI-DE");
        Util.writeMapBI(mapES, "TRI-ES");

//        System.out.println(Util.getMapBI("TRI-EN").keySet());
    }
}
