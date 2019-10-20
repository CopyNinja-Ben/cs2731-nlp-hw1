import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Backoff {

    public static ArrayList<Double> backoff(String uniprofile, String unifile, String bifile, String trifile, String fileName){
        HashMap<Character, Double> unipro = Util.getMapUNI(uniprofile);
        HashMap<Character, Double> unimap = Util.getMapUNI(unifile);
        HashMap<String, Double> bimap = Util.getMapBI(bifile);
        HashMap<String, Double> trimap = Util.getMapBI(trifile);
        HashMap<String, Double> backpro = new HashMap<>();

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
                        if(bimap.containsKey("<" + temp[i])){
                            perp *= bimap.get("<" + temp[i]) / bimap.get("<");
                            backpro.put("<<" + temp[i], perp);
                        }
                        else if(unipro.containsKey(temp[i])){
                            perp *= unipro.get(temp[i]);
                            backpro.put("<<" + temp[i], perp);
                        }
                        else{
                            perp = Double.POSITIVE_INFINITY;
                            break;
                        }
                    }
                    else if(i == 1){
                        key = "<" + (temp[i-1] + "") + (temp[i] + "");
                        if(trimap.containsKey(key)){
                            perp *= trimap.get(key) / bimap.get("<" + temp[i - 1]);
                            backpro.put(key, trimap.get(key) / bimap.get("<" + temp[i - 1]));
                        }
                        else if(bimap.containsKey((temp[i-1] + "") + (temp[i] + ""))){
                            perp *= bimap.get((temp[i-1] + "") + (temp[i] + "")) / unimap.get(temp[i - 1]);
                            backpro.put(key, bimap.get((temp[i-1] + "") + (temp[i] + "")) / unimap.get(temp[i - 1]));
                        }
                        else if (unipro.containsKey(temp[i])){
                            perp *= unipro.get(temp[i]);
                            backpro.put(key, unipro.get(temp[i]));
                        }
                        else{
                            perp = Double.POSITIVE_INFINITY;
                            break;
                        }
                    }
                    else{
                        key = (temp[i-1] + "") + (temp[i] + "");
                        if(trimap.containsKey((temp[i-2] + "") + key) && bimap.containsKey((temp[i-2] + "") + (temp[i-1] + ""))){
                            perp *= trimap.get((temp[i-2] + "") + key) / bimap.get((temp[i-2] + "") + (temp[i-1] + ""));
                            backpro.put((temp[i-2] + "") + key, trimap.get((temp[i-2] + "") + key) / bimap.get((temp[i-2] + "") + (temp[i-1] + "")));
                        }
                        else if(bimap.containsKey(key)){
                            perp *= bimap.get(key) / unimap.get(temp[i-1]);
                            backpro.put((temp[i-2] + "") + key, bimap.get(key) / unimap.get(temp[i-1]));
                        }
                        else if(unipro.containsKey(temp[i])){
                            perp *= unipro.get(temp[i]);
                            backpro.put((temp[i-2] + "") + key, unipro.get(temp[i]));
                        }
                        else{
                            perp = Double.POSITIVE_INFINITY;
                            break;
                        }
                    }
                    if(i == lineLength - 1){
                        key = (temp[i-1] + "") + (temp[i] + "");
                        if(trimap.containsKey(key + ">")){
                            perp *= trimap.get(key + ">") / bimap.get(key);
                            backpro.put(key + ">", trimap.get(key + ">") / bimap.get(key));
                        }
                        else if(bimap.containsKey(temp[i] + ">")){
                            perp *= bimap.get(temp[i] + ">") / unimap.get(temp[i]);
                            backpro.put(key + ">", bimap.get(temp[i] + ">") / unimap.get(temp[i]));
                        }
                        else{
                            perp *= bimap.get(">");
                            backpro.put(key + ">", bimap.get(">"));
                        }
                    }
                }
                if(perp != Double.POSITIVE_INFINITY){
                    perp = Math.pow(perp, -1.0/(double)lineLength);
                }
                perplexity.add(perp);
            }

            Util.writePro3(backpro, "Backoff-EN-PRO");
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return perplexity;

    }

    public static void main(String[] args) {

        ArrayList<Double> depp = backoff("UNI-DE-PRO","UNI-DE-COUNT","BI-DE","TRI-DE", Util.TEST);
        Util.writePP(depp, "perplexity/de_backoff_perplexity.txt");

        ArrayList<Double> espp = backoff("UNI-ES-PRO","UNI-ES-COUNT","BI-ES","TRI-ES", Util.TEST);
        Util.writePP(espp, "perplexity/es_backoff_perplexity.txt");

        ArrayList<Double> enpp = backoff("UNI-EN-PRO","UNI-EN-COUNT","BI-EN","TRI-EN", Util.TEST);
        Util.writePP(enpp, "perplexity/en_backoff_perplexity.txt");
    }
}
