package course.examples.cinepople.domain;


public class SeatStatus {

    private String seatId;
    private boolean isBooked;

    public SeatStatus() {}

    public SeatStatus(String seatId, boolean isBooked) {
        this.seatId = seatId;
        this.isBooked = isBooked;
    }

    public String getSeatId() { return seatId; }
    public boolean isBooked() { return isBooked; }

    public void setSeatId(String seatId) { this.seatId = seatId; }
    public void setBooked(boolean booked) { isBooked = booked; }
}