package MDVRP;


import java.util.Arrays;

public class Customer {
    private int id;
    private int x;
    private int y;
    private int duration;
    private int demand;
    private Depot depot;
    private boolean onBorderline;


    public Customer(Integer id, Integer x, Integer y, Integer duration, Integer demand) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.duration = duration;
        this.demand = demand;
    }


    public void setDepot(Depot depot, boolean onBorderline) {
        this.depot = depot;
        this.onBorderline = onBorderline;
    }


    public int getId()          { return id; }
    public int getX()           { return x; }
    public int getY()           { return y; }
    public int getDuration()    { return duration; }
    public int getDemand()      { return demand; }
    public Depot getDepot()     { return depot; }
    public boolean getOnBorder(){ return onBorderline; }

    @Override
    public String toString() {
        // return customer id
        return "" + this.id;
    }

}
