import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Main {
    public static void main(String[] args) {




        ListGraph<Place> test = new ListGraph<>();
        Place lisabon= new Place(1,1,"Lisabon");
        test.add(lisabon);
        var madrid = new Place(1,1,"Madrid");
        test.add(madrid);
        var paris = new Place(1,1,"Paris");
        test.add(paris);
        var london = new Place(1,1,"London");
        test.add(london);
        var dublin = new Place(1,1,"Dublin");
        test.add(dublin);
        var amsterdam = new Place(1,1,"Amsterdam");
        test.add(amsterdam);
        var berlin = new Place(1,1,"Berlin");
        test.add(berlin);

        test.connect(lisabon,madrid,"a",3);
        test.connect(madrid,paris,"b",10);
        test.connect(paris,london,"c",2);
        test.connect(london, dublin, "xd",1);
        test.connect(paris,amsterdam,"weed",1);
        test.connect(paris,berlin,"wall",1);
        test.getPath(madrid,london).forEach(o -> System.out.println(o));









        /*
        a => c connection 10, b OtherConnection 12
        b => a OtherConnection 12, c yetanotherconnection 15
        c => a connection 10, b yetanotherconnection 15
         */
    }
}