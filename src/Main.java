import java.util.ArrayList;
import java.util.Iterator;

public class Main {
    public static void main(String[] args) {
        ArrayList<Integer> a = new ArrayList<>();
        for(int i = 0; i<5; i++) {
            a.add(i);
        }
        Iterator<Integer> it = a.iterator();


        /*
        ListGraph<String> test = new ListGraph<>();
        String a= "a";
        String b = "b";
        String c = "c";
        String q = "q";
        String d = "d";
        String e = "e";
        test.add(a);
        test.add(b);
        test.add(c);
        test.add(d);
        test.add(e);
        test.add(q);

        test.connect(a,b,"a => b", 10);
        test.connect(a,c,"a => c", 10);
        test.connect(b,c,"b => c",10);
        test.connect(b,d,"b => d",10);
        test.connect(d,e,"d => e",10);
        test.connect(e,q,"e => q", 10);
        test.getPath(a,q).forEach(t -> System.out.println(t));


         */

        /*
        a => c connection 10, b OtherConnection 12
        b => a OtherConnection 12, c yetanotherconnection 15
        c => a connection 10, b yetanotherconnection 15
         */
    }
}