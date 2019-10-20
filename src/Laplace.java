import java.util.HashMap;

public class Laplace {

    public static HashMap<String, Double> addone(String unifile, String bifile, String trifile){
        HashMap<Character, Double> uni = Util.getMapUNI(unifile);
        HashMap<String, Double> bi = Util.getMapBI(bifile);
        HashMap<String, Double> tri = Util.getMapBI(trifile);

        HashMap<String, Double> laplace = new HashMap<>();
        for(String s: bi.keySet()){
            for(char c: uni.keySet()){
                laplace.put(s + c, tri.getOrDefault(s + c, 0.0));
            }
        }

        boolean flag = false;
        for(String s: bi.keySet()){
            for(char c: uni.keySet()){
                if(laplace.get(s + c) == 0){
                    flag = true;
                    break;
                }
            }
            if(flag){
                for(char c: uni.keySet()){
                    laplace.put(s + c, laplace.getOrDefault(s + c, 0.0) + 1.0);
                }
            }
            flag = false;
        }

        return laplace;
    }

    public static HashMap<String, Double> computePro(HashMap<String, Double> laplace, String unifile, String bifile){
        HashMap<String, Double> pro = new HashMap<>();
        HashMap<Character, Double> uni = Util.getMapUNI(unifile);
        HashMap<String, Double> bi = Util.getMapBI(bifile);

        for(char c: uni.keySet()){
            for(String s: bi.keySet()){
                pro.put(s + c, laplace.getOrDefault(s + c, 0.0) / bi.get(s));
            }
        }
        return pro;
    }

    public static void main(String[] args) {
        HashMap<String, Double> en = addone("UNI-EN-COUNT","BI-EN","TRI-EN");
        Util.writeMapBI(en, "Laplace-EN");
        Util.writePro3(computePro(en,"UNI-EN-COUNT","BI-EN"), "Laplace-EN-PRO");
//        System.out.println(Util.getMapBI("Laplace-EN"));

        HashMap<String, Double> de = addone("UNI-DE-COUNT","BI-DE","TRI-DE");
        Util.writeMapBI(de, "Laplace-DE");
//        System.out.println(Util.getMapBI("Laplace-DE"));

        HashMap<String, Double> es = addone("UNI-ES-COUNT","BI-ES","TRI-ES");
        Util.writeMapBI(es, "Laplace-ES");
//        System.out.println(Util.getMapBI("Laplace-ES"));
    }
}
