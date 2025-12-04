package course.examples.cinepople.domain;

import java.util.List;

public class Booking {
    private String id;
    private String showtimeId;
    private String movieTitle;
    private String cinemaName;
    private String roomName;
    private String status;
    private List<String> seats;
    private double totalPrice;
    private String showtimeDate;

    // ---- GETTERS ----
    public String getId() { return id; }
    public String getShowtimeId() { return showtimeId; }
    public String getMovieTitle() { return movieTitle; }
    public String getCinemaName() { return cinemaName; }
    public String getRoomName() { return roomName; }
    public String getStatus() { return status; }
    public List<String> getSeats() { return seats; }
    public double getTotalPrice() { return totalPrice; }
    public String getShowtimeDate() { return showtimeDate; }

    // ---- SETTERS (NEEDED FOR RETROFIT) ----
    public void setId(String id) { this.id = id; }
    public void setShowtimeId(String showtimeId) { this.showtimeId = showtimeId; }
    public void setMovieTitle(String movieTitle) { this.movieTitle = movieTitle; }
    public void setCinemaName(String cinemaName) { this.cinemaName = cinemaName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }
    public void setStatus(String status) { this.status = status; }
    public void setSeats(List<String> seats) { this.seats = seats; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public void setShowtimeDate(String showtimeDate) { this.showtimeDate = showtimeDate; }
}
