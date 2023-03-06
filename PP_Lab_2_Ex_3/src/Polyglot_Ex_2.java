import org.graalvm.polyglot.*;
import java.util.*;

public class Polyglot_Ex_2 {

    private static void InputPython(List<Integer> l){
        //construim un context care ne permite sa folosim elemente din PYTHON
        Context polyglot = Context.newBuilder().allowAllAccess(true).build();

        Value temp1 = polyglot.eval("python", "n = int(input('Introduceti numarul de aruncari: '))");
        Value n = polyglot.eval("python", "n");
        Value temp2 = polyglot.eval("python", "x = int(input('Introduceti numarul x: '))");
        Value x = polyglot.eval("python", "x");
        int nInt = n.asInt();
        int xInt = x.asInt();
        l.add(nInt);
        l.add(xInt);
        // inchidem contextul Polyglot
        polyglot.close();
    }

    //functia MAIN
    public static void main(String[] args) {
        //construim un context pentru evaluare elemente JS
        Context polyglot = Context.create();

        List<Integer> input = new ArrayList<Integer> ();
        InputPython(input);

        Context polyglotR = Context.newBuilder().allowAllAccess(true).build();
        Value binDist = polyglotR.eval("R", "pbinom(" + input.get(1) + ", " + input.get(0) + ", 0.5" +")");
        double binDistDouble = binDist.asDouble();
        polyglotR.close();

        System.out.println("Probabilitatea de a obtine cel mult " + input.get(1) + "ori pajura din " + input.get(0) + " aruncari este : " + binDistDouble);

        // inchidem contextul Polyglot
        polyglot.close();
    }
}
