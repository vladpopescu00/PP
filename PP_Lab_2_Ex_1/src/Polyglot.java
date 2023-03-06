//import libraria principala polyglot din graalvm
import org.graalvm.polyglot.*;
import java.util.*;

//clasa principala - aplicatie JAVA
class Polyglot {
    //metoda privata pentru conversie low-case -> up-case folosind functia toupper() din R
    private static String RToUpper(String token){
        //construim un context care ne permite sa folosim elemente din R
        Context polyglot = Context.newBuilder().allowAllAccess(true).build();
        //folosim o variabila generica care va captura rezultatul excutiei funcitiei R, toupper(String)
        //pentru aexecuta instructiunea I din limbajul X, folosim functia graalvm polyglot.eval("X", "I");
        Value result = polyglot.eval("R", "toupper(\""+token+"\");");
        //utilizam metoda asString() din variabila incarcata cu output-ul executiei pentru a mapa valoarea generica la un String
        String resultString = result.asString();
        // inchidem contextul Polyglot
        polyglot.close();

        return resultString;
    }

    //metoda privata pentru evaluarea unei sume de control simple a literelor unui text ASCII, folosind PYTHON
    private static int SumCRC(String token){
        //construim un context care ne permite sa folosim elemente din PYTHON
        Context polyglot = Context.newBuilder().allowAllAccess(true).build();
        //folosim o variabila generica care va captura rezultatul excutiei functiei PYTHON, sum()
        //avem voie sa inlocuim anumite elemente din scriptul pe care il construim spre evaluare, aici token provine din JAVA, dar va fi interpretat de PYTHON
        Value result = polyglot.eval("python", "sum(ord(ch) for ch in '" + token + "')");
        //utilizam metoda asInt() din variabila incarcata cu output-ul executiei, pentru a mapa valoarea generica la un Int
        int resultInt = result.asInt();
        // inchidem contextul Polyglot
        polyglot.close();

        return resultInt;
    }

    //functia MAIN
    public static void main(String[] args) {
        //construim un context pentru evaluare elemente JS
        Context polyglot = Context.create();
        //construim un array de string-uri, folosind cuvinte din pagina web:  https://chrisseaton.com/truffleruby/tenthings/
        Value array = polyglot.eval("js", "[\"DDD\", \"CDE\", \"123\", \"CC\", \"BD\", \"RTU\"];");
        //pentru fiecare cuvant, convertim la upcase folosind R si calculam suma de control folosind PYTHON

        List<String> words = new ArrayList<String> ();
        List<Integer> checksums = new ArrayList<Integer> ();
        for (int i = 0; i < array.getArraySize();i++){
            String element = array.getArrayElement(i).asString();
            String upper = RToUpper(element);
            int crc = SumCRC(upper);
            words.add(upper);
            checksums.add(crc);
            System.out.println(upper + " -> " + crc);

        }

        while (!words.isEmpty())
        {
            int index = 0;
            int sum = checksums.get(0);
            checksums.remove(0);
            System.out.print(" " + words.get(0) + " " + sum);
            words.remove(0);
            for (int i = 0; i < words.size(); i++)
            {
                if (sum == checksums.get(i))
                {
                    System.out.print(" " + words.get(i) + " " + checksums.get(i));
                    checksums.remove(i);
                    words.remove(i);
                }
            }
            System.out.println();
        }
        // inchidem contextul Polyglot
        polyglot.close();
    }
}

