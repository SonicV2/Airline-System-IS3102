package DCS.ManagedBean;


import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author HOULIANG
 */
@ManagedBean
@SessionScoped
public class seatManagedBean {

    private ArrayList<String> A330seatArrange = new ArrayList<String>();
    private ArrayList<String> B777_200seatArrange = new ArrayList<String>();
    private ArrayList<String> B777_200ERseatArrange = new ArrayList<String>();
    private ArrayList<String> B777_300seatArrange = new ArrayList<String>();
    private ArrayList<String> B777_300ERseatArrange = new ArrayList<String>();
    private String choose;

    @PostConstruct
    public void init() {
        addSeatA330();
        addSeatB777_200();
        addSeatB777_200ER();
        addSeatB777_300();
        addSeatB777_300ER();
    }

    public void addSeatA330() {
        for (int i = 1; i < 9; i++) {  // 30 business seats
            if (i == 1) {
                A330seatArrange.add(" ");
                A330seatArrange.add(" ");
                A330seatArrange.add(" ");
                A330seatArrange.add(" ");
                A330seatArrange.add("\uD83D\uDCBA" + "B" + i);
                A330seatArrange.add("\uD83D\uDCBA" + "C" + i);
                A330seatArrange.add(" ");
                A330seatArrange.add(" ");
                A330seatArrange.add(" ");
                A330seatArrange.add(" ");
                i++;
            }
            A330seatArrange.add("\uD83D\uDCBA" + "A" + i);
            A330seatArrange.add(" ");
            A330seatArrange.add(" ");
            A330seatArrange.add(" ");
            A330seatArrange.add("\uD83D\uDCBA" + "B" + i);
            A330seatArrange.add("\uD83D\uDCBA" + "C" + i);
            A330seatArrange.add(" ");
            A330seatArrange.add(" ");
            A330seatArrange.add(" ");
            A330seatArrange.add("\uD83D\uDCBA" + "D" + i);
        }
        for (int i = 1; i < 11; i++) { // seperation row eg toliet   
            A330seatArrange.add(" ");
        }
        for (int i = 9; i < 41; i++) { //255 economic seats
            if (i == 40) {
                A330seatArrange.add("\uD83D\uDCBA" + "A" + i);
                A330seatArrange.add("\uD83D\uDCBA" + "B" + i);
                A330seatArrange.add(" ");
                A330seatArrange.add(" ");
                A330seatArrange.add("\uD83D\uDCBA" + "D" + i);
                A330seatArrange.add("\uD83D\uDCBA" + "E" + i);
                A330seatArrange.add("\uD83D\uDCBA" + "F" + i);
                A330seatArrange.add(" ");
                A330seatArrange.add("\uD83D\uDCBA" + "G" + i);
                A330seatArrange.add("\uD83D\uDCBA" + "H" + i);
                break;
            }
            A330seatArrange.add("\uD83D\uDCBA" + "A" + i);
            A330seatArrange.add("\uD83D\uDCBA" + "B" + i);
            A330seatArrange.add(" ");
            A330seatArrange.add("\uD83D\uDCBA" + "C" + i);
            A330seatArrange.add("\uD83D\uDCBA" + "D" + i);
            A330seatArrange.add("\uD83D\uDCBA" + "E" + i);
            A330seatArrange.add("\uD83D\uDCBA" + "F" + i);
            A330seatArrange.add(" ");
            A330seatArrange.add("\uD83D\uDCBA" + "G" + i);
            A330seatArrange.add("\uD83D\uDCBA" + "H" + i);
            if (i == 25) {   // seperation row eg toliet
                for (int j = 1; j < 11; j++) {
                    A330seatArrange.add(" ");
                }
            }

        }
    }

    public void chooseSeatA330() {
        if (A330seatArrange.contains("\uD83D\uDCBA" + choose.toUpperCase())) {
            A330seatArrange.set(A330seatArrange.indexOf("\uD83D\uDCBA" + choose.toUpperCase()), "\n" + "\u26D4" + "\n" + choose.toUpperCase());

        } else {

        }
    }

    //----------------------------------------------------------------------------------------------------   
    public void addSeatB777_200() {
        for (int i = 1; i < 11; i++) {  // 38 business seats
            if (i == 1) {
                B777_200seatArrange.add(" ");
                B777_200seatArrange.add(" ");
                B777_200seatArrange.add(" ");
                B777_200seatArrange.add(" ");
                B777_200seatArrange.add("\uD83D\uDCBA" + "B" + i);
                B777_200seatArrange.add("\uD83D\uDCBA" + "C" + i);
                B777_200seatArrange.add(" ");
                B777_200seatArrange.add(" ");
                B777_200seatArrange.add(" ");
                B777_200seatArrange.add(" ");
                i++;
            }
            B777_200seatArrange.add("\uD83D\uDCBA" + "A" + i);
            B777_200seatArrange.add(" ");
            B777_200seatArrange.add(" ");
            B777_200seatArrange.add(" ");
            B777_200seatArrange.add("\uD83D\uDCBA" + "B" + i);
            B777_200seatArrange.add("\uD83D\uDCBA" + "C" + i);
            B777_200seatArrange.add(" ");
            B777_200seatArrange.add(" ");
            B777_200seatArrange.add(" ");
            B777_200seatArrange.add("\uD83D\uDCBA" + "D" + i);
        }
        for (int i = 1; i < 11; i++) { // seperation row eg toliet   
            B777_200seatArrange.add(" ");
        }
        for (int i = 11; i < 40; i++) { //228 economic seats
            if (i == 39) {
            B777_200seatArrange.add("\u26D4");
            B777_200seatArrange.add("\u26D4");
            B777_200seatArrange.add(" ");
            B777_200seatArrange.add("\uD83D\uDCBA" + "C" + i);
            B777_200seatArrange.add("\uD83D\uDCBA" + "D" + i);
            B777_200seatArrange.add("\uD83D\uDCBA" + "E" + i);
            B777_200seatArrange.add("\uD83D\uDCBA" + "F" + i);
            B777_200seatArrange.add(" ");
            B777_200seatArrange.add("\u26D4");
            B777_200seatArrange.add("\u26D4");
                break;
            }
            B777_200seatArrange.add("\uD83D\uDCBA" + "A" + i);
            B777_200seatArrange.add("\uD83D\uDCBA" + "B" + i);
            B777_200seatArrange.add(" ");
            B777_200seatArrange.add("\uD83D\uDCBA" + "C" + i);
            B777_200seatArrange.add("\uD83D\uDCBA" + "D" + i);
            B777_200seatArrange.add("\uD83D\uDCBA" + "E" + i);
            B777_200seatArrange.add("\uD83D\uDCBA" + "F" + i);
            B777_200seatArrange.add(" ");
            B777_200seatArrange.add("\uD83D\uDCBA" + "G" + i);
            B777_200seatArrange.add("\uD83D\uDCBA" + "H" + i);
            if (i == 27) {   // seperation row eg toliet
                for (int j = 1; j < 11; j++) {
                    B777_200seatArrange.add(" ");
                }
            }

        }
    }

    public void chooseSeatB777_200() {
        if (B777_200seatArrange.contains("\uD83D\uDCBA" + choose.toUpperCase())) {
            B777_200seatArrange.set(B777_200seatArrange.indexOf("\uD83D\uDCBA" + choose.toUpperCase()), "\n" + "\u26D4" + "\n" + choose.toUpperCase());

        } else {

        }
    }
    
//-------------------------------------------------------------------------------------------------------
    
       public void addSeatB777_200ER() {
           for (int i = 1; i < 9; i++) {  // 30 business seats
            if (i == 1) {
                B777_200ERseatArrange.add(" ");
                B777_200ERseatArrange.add(" ");
                B777_200ERseatArrange.add(" ");
                B777_200ERseatArrange.add(" ");
                B777_200ERseatArrange.add("\uD83D\uDCBA" + "B" + i);
                B777_200ERseatArrange.add("\uD83D\uDCBA" + "C" + i);
                B777_200ERseatArrange.add(" ");
                B777_200ERseatArrange.add(" ");
                B777_200ERseatArrange.add(" ");
                B777_200ERseatArrange.add(" ");
                i++;
            }
            B777_200ERseatArrange.add("\uD83D\uDCBA" + "A" + i);
            B777_200ERseatArrange.add(" ");
            B777_200ERseatArrange.add(" ");
            B777_200ERseatArrange.add(" ");
            B777_200ERseatArrange.add("\uD83D\uDCBA" + "B" + i);
            B777_200ERseatArrange.add("\uD83D\uDCBA" + "C" + i);
            B777_200ERseatArrange.add(" ");
            B777_200ERseatArrange.add(" ");
            B777_200ERseatArrange.add(" ");
            B777_200ERseatArrange.add("\uD83D\uDCBA" + "D" + i);
        }
        for (int i = 1; i < 11; i++) { // seperation row eg toliet   
            B777_200ERseatArrange.add(" ");
        }
        for (int i = 9; i < 41; i++) { //255 economic seats
            if (i == 40) {
                B777_200ERseatArrange.add("\uD83D\uDCBA" + "A" + i);
                B777_200ERseatArrange.add("\uD83D\uDCBA" + "B" + i);
                B777_200ERseatArrange.add(" ");
                B777_200ERseatArrange.add(" ");
                B777_200ERseatArrange.add("\uD83D\uDCBA" + "D" + i);
                B777_200ERseatArrange.add("\uD83D\uDCBA" + "E" + i);
                B777_200ERseatArrange.add("\uD83D\uDCBA" + "F" + i);
                B777_200ERseatArrange.add(" ");
                B777_200ERseatArrange.add("\uD83D\uDCBA" + "G" + i);
                B777_200ERseatArrange.add("\uD83D\uDCBA" + "H" + i);
                break;
            }
            B777_200ERseatArrange.add("\uD83D\uDCBA" + "A" + i);
            B777_200ERseatArrange.add("\uD83D\uDCBA" + "B" + i);
            B777_200ERseatArrange.add(" ");
            B777_200ERseatArrange.add("\uD83D\uDCBA" + "C" + i);
            B777_200ERseatArrange.add("\uD83D\uDCBA" + "D" + i);
            B777_200ERseatArrange.add("\uD83D\uDCBA" + "E" + i);
            B777_200ERseatArrange.add("\uD83D\uDCBA" + "F" + i);
            B777_200ERseatArrange.add(" ");
            B777_200ERseatArrange.add("\uD83D\uDCBA" + "G" + i);
            B777_200ERseatArrange.add("\uD83D\uDCBA" + "H" + i);
            if (i == 25) {   // seperation row eg toliet
                for (int j = 1; j < 11; j++) {
                    B777_200ERseatArrange.add(" ");
                }
            }

        }
    }

    public void chooseSeatB777_200ER() {
        if (B777_200ERseatArrange.contains("\uD83D\uDCBA" + choose.toUpperCase())) {
            B777_200ERseatArrange.set(B777_200ERseatArrange.indexOf("\uD83D\uDCBA" + choose.toUpperCase()), "\n" + "\u26D4" + "\n" + choose.toUpperCase());

        } else {

        }
    }
//-------------------------------------------------------------------------------------------------------
    public void addSeatB777_300() {
           for (int i = 1; i < 3; i++) {  // 8 first class seats
   
           B777_300seatArrange.add("\uD83D\uDCBA" + "A" + i);
           B777_300seatArrange.add(" ");
           B777_300seatArrange.add(" ");
           B777_300seatArrange.add(" ");
           B777_300seatArrange.add("\uD83D\uDCBA" + "B" + i);
           B777_300seatArrange.add("\uD83D\uDCBA" + "C" + i);
           B777_300seatArrange.add(" ");
           B777_300seatArrange.add(" ");
           B777_300seatArrange.add(" ");
           B777_300seatArrange.add("\uD83D\uDCBA" + "D" + i);
        }
        for (int i = 1; i < 11; i++) { // seperation row eg toliet   
           B777_300seatArrange.add(" ");
        }
        
         for (int i = 3; i < 12; i++) {  // 50 business seats
            if (i == 3) {
                B777_300seatArrange.add(" ");
                B777_300seatArrange.add(" ");
                B777_300seatArrange.add(" ");
                B777_300seatArrange.add(" ");
                B777_300seatArrange.add("\uD83D\uDCBA" + "E" + i);
                B777_300seatArrange.add("\uD83D\uDCBA" + "F" + i);
                B777_300seatArrange.add(" ");
                B777_300seatArrange.add(" ");
                B777_300seatArrange.add(" ");
                B777_300seatArrange.add(" ");
                i++;
            }
           B777_300seatArrange.add("\uD83D\uDCBA" + "A" + i);
           B777_300seatArrange.add("\uD83D\uDCBA" + "B" + i);
           B777_300seatArrange.add(" ");
           B777_300seatArrange.add(" ");
           B777_300seatArrange.add("\uD83D\uDCBA" + "E" + i);
           B777_300seatArrange.add("\uD83D\uDCBA" + "F" + i);
           B777_300seatArrange.add(" ");
           B777_300seatArrange.add(" ");
           B777_300seatArrange.add("\uD83D\uDCBA" + "J" + i);
           B777_300seatArrange.add("\uD83D\uDCBA" + "K" + i);
        }
        for (int i = 1; i < 11; i++) { // seperation row eg toliet   
           B777_300seatArrange.add(" ");
        }
        
        
        for (int i = 12; i < 41; i++) { //226 economic seats
            if (i == 40) {
                B777_300seatArrange.add("\uD83D\uDCBA" + "C" + i);
                B777_300seatArrange.add("\uD83D\uDCBA" + "D" + i);
                
                B777_300seatArrange.add(" ");
                B777_300seatArrange.add("\u26D4");
                B777_300seatArrange.add("\u26D4");
                B777_300seatArrange.add("\u26D4");
                B777_300seatArrange.add("\u26D4");
                B777_300seatArrange.add(" ");
                B777_300seatArrange.add("\u26D4");
                B777_300seatArrange.add("\u26D4");
                break;
            }
           B777_300seatArrange.add("\uD83D\uDCBA" + "A" + i);
           B777_300seatArrange.add("\uD83D\uDCBA" + "B" + i);
           B777_300seatArrange.add(" ");
           B777_300seatArrange.add("\uD83D\uDCBA" + "C" + i);
           B777_300seatArrange.add("\uD83D\uDCBA" + "D" + i);
           B777_300seatArrange.add("\uD83D\uDCBA" + "E" + i);
           B777_300seatArrange.add("\uD83D\uDCBA" + "F" + i);
           B777_300seatArrange.add(" ");
           B777_300seatArrange.add("\uD83D\uDCBA" + "G" + i);
           B777_300seatArrange.add("\uD83D\uDCBA" + "H" + i);
            if (i == 25) {   // seperation row eg toliet
                for (int j = 1; j < 11; j++) {
                   B777_300seatArrange.add(" ");
                }
            }

        }
    }

    public void chooseSeatB777_300() {
        if (B777_300seatArrange.contains("\uD83D\uDCBA" + choose.toUpperCase())) {
           B777_300seatArrange.set(B777_300seatArrange.indexOf("\uD83D\uDCBA" + choose.toUpperCase()), "\n" + "\u26D4" + "\n" + choose.toUpperCase());

        } else {

        }
    }
       
    
//-------------------------------------------------------------------------------------------------------    
    public void addSeatB777_300ER() {
           for (int i = 1; i < 2; i++) {  // 4 first class seats
   
           B777_300ERseatArrange.add("\uD83D\uDCBA" + "A" + i);
           B777_300ERseatArrange.add(" ");
           B777_300ERseatArrange.add(" ");
           B777_300ERseatArrange.add(" ");
           B777_300ERseatArrange.add("\uD83D\uDCBA" + "B" + i);
           B777_300ERseatArrange.add("\uD83D\uDCBA" + "C" + i);
           B777_300ERseatArrange.add(" ");
           B777_300ERseatArrange.add(" ");
           B777_300ERseatArrange.add(" ");
           B777_300ERseatArrange.add("\uD83D\uDCBA" + "D" + i);
        }
        for (int i = 1; i < 11; i++) { // seperation row eg toliet   
           B777_300ERseatArrange.add(" ");
        }
        
         for (int i = 2; i < 10; i++) {  // 48 business seats

           B777_300ERseatArrange.add("\uD83D\uDCBA" + "A" + i);
           B777_300ERseatArrange.add("\uD83D\uDCBA" + "B" + i);
           B777_300ERseatArrange.add(" ");
           B777_300ERseatArrange.add(" ");
           B777_300ERseatArrange.add("\uD83D\uDCBA" + "E" + i);
           B777_300ERseatArrange.add("\uD83D\uDCBA" + "F" + i);
           B777_300ERseatArrange.add(" ");
           B777_300ERseatArrange.add(" ");
           B777_300ERseatArrange.add("\uD83D\uDCBA" + "J" + i);
           B777_300ERseatArrange.add("\uD83D\uDCBA" + "K" + i);
        }
        for (int i = 1; i < 11; i++) { // seperation row eg toliet   
           B777_300ERseatArrange.add(" ");
        }
        
        
        for (int i = 10; i < 39; i++) { //232 economic seats

           B777_300ERseatArrange.add("\uD83D\uDCBA" + "A" + i);
           B777_300ERseatArrange.add("\uD83D\uDCBA" + "B" + i);
           B777_300ERseatArrange.add(" ");
           B777_300ERseatArrange.add("\uD83D\uDCBA" + "C" + i);
           B777_300ERseatArrange.add("\uD83D\uDCBA" + "D" + i);
           B777_300ERseatArrange.add("\uD83D\uDCBA" + "E" + i);
           B777_300ERseatArrange.add("\uD83D\uDCBA" + "F" + i);
           B777_300ERseatArrange.add(" ");
           B777_300ERseatArrange.add("\uD83D\uDCBA" + "G" + i);
           B777_300ERseatArrange.add("\uD83D\uDCBA" + "H" + i);
            if (i == 25) {   // seperation row eg toliet
                for (int j = 1; j < 11; j++) {
                   B777_300ERseatArrange.add(" ");
                }
            }

        }
    }

    public void chooseSeatB777_300ER() {
        if (B777_300ERseatArrange.contains("\uD83D\uDCBA" + choose.toUpperCase())) {
           B777_300ERseatArrange.set(B777_300ERseatArrange.indexOf("\uD83D\uDCBA" + choose.toUpperCase()), "\n" + "\u26D4" + "\n" + choose.toUpperCase());

        } else {

        }
    }
    
    
    
    
    
//-------------------------------------------------------------------------------------------------------    
    public seatManagedBean() {
    }

    public ArrayList<String> getA330seatArrange() {
        return A330seatArrange;
    }

    public void setA330seatArrange(ArrayList<String> A330seatArrange) {
        this.A330seatArrange = A330seatArrange;
    }

    public String getChoose() {
        return choose;
    }

    public void setChoose(String choose) {
        this.choose = choose;
    }

    public ArrayList<String> getB777_200seatArrange() {
        return B777_200seatArrange;
    }

    public void setB777_200seatArrange(ArrayList<String> B777_200seatArrange) {
        this.B777_200seatArrange = B777_200seatArrange;
    }

    public ArrayList<String> getB777_200ERseatArrange() {
        return B777_200ERseatArrange;
    }

    public void setB777_200ERseatArrange(ArrayList<String> B777_200ERseatArrange) {
        this.B777_200ERseatArrange = B777_200ERseatArrange;
    }

    public ArrayList<String> getB777_300seatArrange() {
        return B777_300seatArrange;
    }

    public void setB777_300seatArrange(ArrayList<String> B777_300seatArrange) {
        this.B777_300seatArrange = B777_300seatArrange;
    }

    public ArrayList<String> getB777_300ERseatArrange() {
        return B777_300ERseatArrange;
    }

    public void setB777_300ERseatArrange(ArrayList<String> B777_300ERseatArrange) {
        this.B777_300ERseatArrange = B777_300ERseatArrange;
    }

}
