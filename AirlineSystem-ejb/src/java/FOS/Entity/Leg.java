package FOS.Entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Leg implements Comparable,Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int line;
    private String origin;
    private String destination;
    private int startHour;
    private int finishHour;
    private String date1;
    
    public Leg(){super();}

    public Leg(int line, String origin, String destination, int startHour, int finishHour, String date1) {
        super();
        this.line = line;
        this.origin = origin;
        this.destination = destination;
        this.startHour = startHour;
        this.finishHour = finishHour;
        this.date1 = date1;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

 
    /**
     * @return the line
     */
    public int getLine() {
        return line;
    }

 
    public void setLine(int line) {
        this.line = line;
    }

    /**
     * @return the origin
     */
    public String getOrigin() {
        return origin;
    }

  
    public void setOrigin(String origin) {
        this.origin = origin;
    }

    /**
     * @return the destination
     */
    public String getDestination() {
        return destination;
    }

 
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * @return the startHour
     */
    public int getStartHour() {
        return startHour;
    }

   
    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    /**
     * @return the finishHour
     */
    public int getFinishHour() {
        return finishHour;
    }

    /**
     * @param finishHour the finishHour to set
     */
    public void setFinishHour(int finishHour) {
        this.finishHour = finishHour;
    }

    /**
     * @return the date
     */
    public String getDate1() {
        return date1;
    }

    
    public void setDate1(String date1) {
        this.date1 = date1;
    }
    
    public Long getFormatted(String da){
		String d1 = da.split("/")[0];
		String d2 = da.split("/")[1];
		String d3 = da.split("/")[2];
		String format = d3+d2+d1;
		Long day = Long.parseLong(format);
		return day;
	}


    @Override
    public int compareTo(Object o) {
        Leg leg = (Leg) o;
		if(this.startHour == leg.getStartHour()){
			
			return 0;
		}
		else if(getFormatted(this.date1) > getFormatted(leg.getDate1())){
			System.out.println("this Date: " + getFormatted(this.date1) );
			System.out.println("leg date: "+ getFormatted(leg.getDate1()));
			return 2;
		}
		else if(this.startHour <leg.getStartHour()){
			System.out.println("this Hour: " + this.startHour);
			System.out.println("leg Hour: "+ leg.getStartHour());
			return -1;
		}
		else
			return 1;
		
	}
   
}
