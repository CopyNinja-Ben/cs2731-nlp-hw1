import java.io.*;
import java.util.HashMap;

public class Unigram {
    static HashMap<Character, Double> mapEN = new HashMap<>();
    static HashMap<Character, Double> mapDE = new HashMap<>();
    static HashMap<Character, Double> mapES = new HashMap<>();
    static HashMap<Character, Double> probEN = new HashMap<>();
    static HashMap<Character, Double> probDE = new HashMap<>();
    static HashMap<Character, Double> probES = new HashMap<>();

    public Unigram(){
    }

    public static int readFile(String fileName, HashMap<Character, Double> map){
        File file = new File(fileName);
        BufferedReader reader = null;
        int total = 0;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                char[] temp = tempString.toCharArray();
                for(char c: temp){
                    map.put(c, map.getOrDefault(c, 0.0) + 1.0);
                    total++;
                }
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return total;
    }

    public static void countPro(int totalNumber, HashMap<Character, Double> map, HashMap<Character, Double> prob) throws IllegalAccessException {
        for(char c: map.keySet()){
            prob.put(c, map.get(c) / totalNumber);
        }
    }

    public static void main(String[] args) throws IllegalAccessException {
        //EN
        int totalEN = readFile(Util.fileEN, mapEN);
        countPro(totalEN, mapEN, probEN);
        Util.writeMapUNI(mapEN, "UNI-EN-COUNT");
        Util.writeMapUNI(probEN, "UNI-EN-PRO");
        Util.writePro(probEN, "UNI-EN-PRO-Readable");

        //DE
        int totalDE = readFile(Util.fileDE, mapDE);
        countPro(totalDE, mapDE, probDE);
        Util.writeMapUNI(mapDE, "UNI-DE-COUNT");
        Util.writeMapUNI(probDE, "UNI-DE-PRO");
        Util.writePro(probDE, "UNI-DE-PRO-Readable");

        //ES
        int totalES = readFile(Util.fileES, mapES);
        countPro(totalES, mapES, probES);
        Util.writeMapUNI(mapES, "UNI-ES-COUNT");
        Util.writeMapUNI(probES, "UNI-ES-PRO");
        Util.writePro(probES, "UNI-ES-PRO-Readable");
    }
}


