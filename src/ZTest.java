import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ZTest {
    public static ArrayList<Double> countPPUni(HashMap<Character, Double> map, String fileName){
        File file = new File(fileName);
        double perp = 1;
        int lineLength = 0;
        int line = 0;
        ArrayList<Double> perplexity = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                char[] temp = tempString.toCharArray();
                perp = 1;
                lineLength = temp.length;
                line++;
                for(char c: temp){
                    if(!map.containsKey(c)){
                        perp = Double.POSITIVE_INFINITY; //???
                        break;
                    }
                    perp *= map.get(c);
                }
                if(perp != Double.POSITIVE_INFINITY){
                    perp = Math.pow(perp, -1.0/(double)lineLength);
                }
                perplexity.add(perp);
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return perplexity;
    }

    public static ArrayList<Double> countPPBi(HashMap<String, Double> map, HashMap<Character, Double> Unimap,String fileName){
        File file = new File(fileName);
        double perp = 1;
        int lineLength = 0;
        ArrayList<Double> perplexity = new ArrayList<>();
        String key;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                char[] temp = tempString.toCharArray();
                perp = 1;
                lineLength = temp.length;
                for(int i = 0; i < lineLength; i++){
                    if(i == 0){
                        key = "<" + temp[i];
                        if(!map.containsKey(key)){
//                            System.out.println("Bimap not contains:" + key);
                            perp = Double.POSITIVE_INFINITY;
                            break;
                        }
                        perp *= map.get(key) / map.get("<");
                    }
                    else{
                        if(!Unimap.containsKey(temp[i - 1])){
//                            System.out.println("Unimap not contains:" + temp[i - 1]);
                            perp = 0;
                            break;
                        }
                        key = (temp[i - 1] + "") + (temp[i] + "");
                        if(!map.containsKey(key)){
//                            System.out.println("Bimap not contains:" + key);
                            perp = 0;
                            break;
                        }
                        perp *= map.get(key) / Unimap.get(temp[i - 1]);
                    }
                    if(i == lineLength - 1){
                        if(!Unimap.containsKey(temp[i])){
//                            System.out.println("Unimap not contains:" + temp[i]);
                            perp = 0;
                            break;
                        }
                        perp *= map.get(temp[i] + ">") / Unimap.get(temp[i]);
                    }
                }
                if(perp != Double.POSITIVE_INFINITY){
                    perp = Math.pow(perp, -1.0/(double)lineLength);
                }
                perplexity.add(perp);
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return perplexity;
    }

    public static ArrayList<Double> countPPTri(HashMap<String, Double> trimap, HashMap<String, Double> bimap,String fileName){
        File file = new File(fileName);
        double perp = 1;
        int lineLength = 0;
        ArrayList<Double> perplexity = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String tempString;
            String key;
            while ((tempString = reader.readLine()) != null) {
                char[] temp = tempString.toCharArray();
                perp = 1;
                lineLength = temp.length;
                for(int i = 0; i < lineLength; i++){
                    if(i == 0){
                        perp *= bimap.getOrDefault("<" + temp[i], 0.0) / bimap.get("<");
                    }
                    else if(i == 1){
                        key = "<" + (temp[i-1] + "") + (temp[i] + "");
                        if(!trimap.containsKey(key)){
                            perp = Double.POSITIVE_INFINITY;
                            break;
                        }
                        perp *= trimap.get(key) / bimap.get("<" + temp[i - 1]);
                    }
                    else{
                        key = (temp[i-2] + "") + (temp[i-1] + "");
                        if(!trimap.containsKey(key + (temp[i]+"")) || !bimap.containsKey(key)){
                            perp = Double.POSITIVE_INFINITY;
                            break;
                        }
                        perp *= trimap.get(key + (temp[i] + "")) / bimap.get(key);
                    }
                    if(i == lineLength - 1){
                        key = (temp[i-1] + "") + (temp[i] + "");
                        if(!trimap.containsKey(key + ">") || !bimap.containsKey(key)){
                            perp = Double.POSITIVE_INFINITY;
                            break;
                        }
                        perp *= trimap.get(key + ">") / bimap.get(key);
                    }
                }
                if(perp != Double.POSITIVE_INFINITY){
                    perp = Math.pow(perp, -1.0/(double)lineLength);
                }
                perplexity.add(perp);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return perplexity;
    }

    public static ArrayList<Double> countPPTrilaplace(HashMap<String, Double> laplace, HashMap<String, Double> bimap, int size,String fileName){
        File file = new File(fileName);
        double perp = 1;
        int lineLength = 0;
        ArrayList<Double> perplexity = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String tempString;
            String key;
            Double V = (double) size;
            while ((tempString = reader.readLine()) != null) {
                char[] temp = tempString.toCharArray();
                perp = 1;
                lineLength = temp.length;
                for(int i = 0; i < lineLength; i++){
                    if(i == 0){
                        key = "<" + temp[i];
                        perp *= bimap.getOrDefault(key, 1.0) / (bimap.get("<") + V);
                    }
                    else if(i == 1){
                        key = "<" + (temp[i-1] + "") + (temp[i] + "");
                        perp *= laplace.getOrDefault(key, 1.0) / (bimap.getOrDefault("<" + temp[i - 1],0.0) + V);
                    }
                    else{
                        key = (temp[i-2] + "") + (temp[i-1] + "");
                        perp *= laplace.getOrDefault(key + (temp[i] + ""), 1.0) / (bimap.getOrDefault(key, 0.0) + V);
                    }
                    if(i == lineLength - 1){
                        key = (temp[i-1] + "") + (temp[i] + "");
                        perp *= laplace.getOrDefault(key + ">", 1.0) / (bimap.getOrDefault(key,0.0) + V);
                    }
                }
                perp = Math.pow(perp, -1.0/(double)lineLength);
                perplexity.add(perp);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return perplexity;
    }


    public static void main(String[] args) {

        // en_unigram perplexity
        HashMap<Character, Double> proEN = Util.getMapUNI("UNI-EN-PRO");
        Util.writePP(countPPUni(proEN, Util.TEST), "perplexity/en_unigram_perplexity.txt");

        // de_unigram pp
        HashMap<Character, Double> proDE = Util.getMapUNI("UNI-DE-PRO");
        Util.writePP(countPPUni(proDE, Util.TEST), "perplexity/de_unigram_perplexity.txt");

        // es_unigram pp
        HashMap<Character, Double> proES = Util.getMapUNI("UNI-ES-PRO");
        Util.writePP(countPPUni(proES, Util.TEST), "perplexity/es_unigram_perplexity.txt");

        // en_bigram pp
        HashMap<String, Double> mapEN = Util.getMapBI("BI-EN");
        HashMap<Character, Double> unienCount = Util.getMapUNI("UNI-EN-COUNT");
        Util.writePP(countPPBi(mapEN, unienCount, Util.TEST), "perplexity/en_bigram_perplexity.txt");

        // de_bigram pp
        HashMap<String, Double> mapDE = Util.getMapBI("BI-DE");
        HashMap<Character, Double> unideCount = Util.getMapUNI("UNI-DE-COUNT");
        Util.writePP(countPPBi(mapDE, unideCount, Util.TEST), "perplexity/de_bigram_perplexity.txt");

        // es_bigram pp
        HashMap<String, Double> mapES = Util.getMapBI("BI-ES");
        HashMap<Character, Double> uniesCount = Util.getMapUNI("UNI-ES-COUNT");
        Util.writePP(countPPBi(mapES, uniesCount, Util.TEST), "perplexity/es_bigram_perplexity.txt");

        // de_trigram pp
        HashMap<String, Double> trimapde = Util.getMapBI("TRI-DE");
        Util.writePP(countPPTri(trimapde, mapDE, Util.TEST),"perplexity/de_trigram_perplexity.txt");
//        Util.writePP(countPPTrilaplace(trimapde, mapDE, "test"),"perplexity/de_laplace_perplexity.txt");
//
        // en_trigram pp
        HashMap<String, Double> trimapen = Util.getMapBI("TRI-EN");
        Util.writePP(countPPTri(trimapen, mapEN, Util.TEST),"perplexity/en_trigram_perplexity.txt");
//        Util.writePP(countPPTrilaplace(trimapen, mapEN, "test"),"perplexity/en_laplace_perplexity.txt");
//
        // es_trigram pp
        HashMap<String, Double> trimapes = Util.getMapBI("TRI-ES");
        Util.writePP(countPPTri(trimapes, mapES, Util.TEST),"perplexity/es_trigram_perplexity.txt");
//        Util.writePP(countPPTrilaplace(trimapes, mapES, "test"),"perplexity/es_laplace_perplexity.txt");

        // en_laplace pp
        HashMap<String, Double> laplace_en = Util.getMapBI("Laplace-EN");
        Util.writePP(countPPTrilaplace(laplace_en, mapEN, unienCount.size(),Util.TEST),"perplexity/en_laplace_perplexity.txt");

        // de_laplace pp
        HashMap<String, Double> laplace_de = Util.getMapBI("Laplace-DE");
        Util.writePP(countPPTrilaplace(laplace_de, mapDE, unideCount.size(),Util.TEST),"perplexity/de_laplace_perplexity.txt");

        // es_laplace pp
        HashMap<String, Double> laplace_es = Util.getMapBI("Laplace-ES");
        Util.writePP(countPPTrilaplace(laplace_es, mapES, uniesCount.size(),Util.TEST),"perplexity/es_laplace_perplexity.txt");

    }
}
