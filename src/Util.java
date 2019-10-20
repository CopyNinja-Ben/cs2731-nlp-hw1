import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Util {
    public static final String fileEN = "../assignment1-data/training.en";
    public static final String fileDE = "../assignment1-data/training.de";
    public static final String fileES = "../assignment1-data/training.es";
    public static final String TEST = "../assignment1-data/test";
    public static void writeMapUNI(HashMap<Character, Double> map, String fileName){
        try {
            FileOutputStream outStream = new FileOutputStream(fileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outStream);
            objectOutputStream.writeObject(map);
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeMapBI(HashMap<String, Double> map, String fileName){
        try {
            FileOutputStream outStream = new FileOutputStream(fileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outStream);
            objectOutputStream.writeObject(map);
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @SuppressWarnings("unchecked")
    public static HashMap<Character, Double> getMapUNI(String fileName){
        HashMap<Character,Double> map = new HashMap<>();
        try {
            FileInputStream freader = new FileInputStream(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(freader);
            map = (HashMap<Character, Double>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return map;
    }
    @SuppressWarnings("unchecked")
    public static HashMap<String, Double> getMapBI(String fileName){
        HashMap<String, Double> map = new HashMap<>();
        try {
            FileInputStream freader = new FileInputStream(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(freader);
            map = (HashMap<String, Double>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static void writePP(ArrayList<Double> list, String fileName){
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
            for(double d: list){
                out.write(d + "\n");
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void writePro(HashMap<Character, Double> map, String fileName) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(fileName));
            for(Map.Entry<Character, Double> entry: map.entrySet()){
                out.write(entry.getKey() + "  " + entry.getValue() + "\n");
            }

            double totalProb = 0;
            for(double f: map.values()){
                totalProb += f;
            }
            out.write("total  " + totalProb);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writePro3(HashMap<String, Double> map, String fileName) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(fileName));
            for(Map.Entry<String, Double> entry: map.entrySet()){
                out.write(entry.getKey() + "  " + entry.getValue() + "\n");
            }

            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
