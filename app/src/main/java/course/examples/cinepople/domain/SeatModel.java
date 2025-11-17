package course.examples.cinepople.domain;

// UI Model
public class SeatModel {
    private String id;
    private double price;
    private boolean isOccupied;
    private boolean isChosen;

    public SeatModel(String id, double price, boolean isOccupied, boolean isChosen) {
        this.id = id;
        this.price = price;
        this.isOccupied = isOccupied;
        this.isChosen = isChosen;
    }

    // Getters
    public String getId() { return id; }
    public double getPrice() { return price; }
    public boolean isOccupied() { return isOccupied; }
    public boolean isChosen() { return isChosen; }

    public void setOccupied(boolean occupied) { isOccupied = occupied; }
    public void setChosen(boolean chosen) { isChosen = chosen; }
}