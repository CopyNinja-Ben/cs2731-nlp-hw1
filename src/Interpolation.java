import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Interpolation {
    public static ArrayList<Double> interpolation(String uniprofile, String unifile, String bifile, String trifile, String fileName){
        HashMap<Character, Double> unipro = Util.getMapUNI(uniprofile);
        HashMap<Character, Double> unimap = Util.getMapUNI(unifile);
        HashMap<String, Double> bimap = Util.getMapBI(bifile);
        HashMap<String, Double> trimap = Util.getMapBI(trifile);
        HashMap<String, Double> interpro = new HashMap<>();

        File file = new File(fileName);
        double perp = 1;
        double t1 = 0;
        double t2 = 0;
        double t3 = 0;
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
                        t1 = bimap.getOrDefault("<" + temp[i],0.0) / bimap.get("<");
                        t2= unipro.getOrDefault(temp[i], 0.0);
                        perp *= 1.0/3.0 * (2 * t1 + t2);
                        interpro.put("<<" + temp[i], perp);
                    }
                    else if(i == 1){
                        key = "<" + (temp[i-1] + "") + (temp[i] + "");
                        if(!bimap.containsKey("<" + temp[i - 1])){
                            t1 = 0;
                        }
                        else{
                            t1 = trimap.getOrDefault(key, 0.0) / bimap.get("<" + temp[i - 1]);
                        }
                        t2 = bimap.getOrDefault((temp[i-1] + "") + (temp[i] + ""),0.0) / unimap.get(temp[i - 1]);
                        t3 = unipro.getOrDefault(temp[i],0.0);
                        perp *= 1.0/3.0 * (t1 + t2 + t3);
                        interpro.put(key, 1.0/3.0 * (t1 + t2 + t3));
                    }
                    else{
                        key = (temp[i-1] + "") + (temp[i] + "");
                        if(trimap.containsKey((temp[i-2] + "") + key) && bimap.containsKey((temp[i-2] + "") + (temp[i-1] + ""))){
                            t1 = trimap.get((temp[i-2] + "") + key) / bimap.get((temp[i-2] + "") + (temp[i-1] + ""));
                        }
                        else
                            t1 = 0;
                        if(unimap.containsKey(temp[i-1])){
                            t2 = bimap.getOrDefault(key,0.0) / unimap.get(temp[i-1]);
                        }
                        else{
                            t2 = 0;
                        }
                        t3 = unipro.getOrDefault(temp[i],0.0);
                        perp *= 1.0/3.0 * (t1 + t2 + t3);
                        interpro.put((temp[i-2] + "") + key, 1.0/3.0 * (t1 + t2 + t3));
                    }
                    if(i == lineLength - 1){
                        key = (temp[i-1] + "") + (temp[i] + "");
                        if(!bimap.containsKey(key)){
                            t1 = 0;
                        }
                        else{
                            t1 = trimap.getOrDefault(key + ">",0.0) / bimap.get(key);
                        }
                        if(unimap.containsKey(temp[i])){
                            t2 = bimap.getOrDefault(temp[i] + ">",0.0) / unimap.get(temp[i]);
                        }
                        else{
                            t2 = 0;
                        }
                        t3 = bimap.get(">");
                        perp *= 1.0/3.0 * (t1 + t2 + t3);
                        interpro.put(key + ">", 1.0/3.0 * (t1 + t2 + t3));
                    }
                }
                perp = perp == 0? Double.POSITIVE_INFINITY : Math.pow(perp, -1.0/(double)lineLength);
                perplexity.add(perp);
            }
            Util.writePro3(interpro,"Interpolation-EN-PRO");
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return perplexity;

    }

    public static void main(String[] args) {
        Util.writePP(interpolation("UNI-DE-PRO","UNI-DE-COUNT","BI-DE","TRI-DE", Util.TEST),
                "perplexity/de_interpolation_pp.txt");
        Util.writePP(interpolation("UNI-ES-PRO","UNI-ES-COUNT","BI-ES","TRI-ES", Util.TEST),
                "perplexity/es_interpolation_pp.txt");
        Util.writePP(interpolation("UNI-EN-PRO","UNI-EN-COUNT","BI-EN","TRI-EN", Util.TEST),
                "perplexity/en_interpolation_pp.txt");
    }
}
