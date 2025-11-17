package course.examples.cinepople.domain;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;
import java.util.List;

public class Booking {

    @DocumentId
    private String id;

    private String userId;
    private String sessionId;
    private List<String> seatsBooked;
    private double totalPrice;
    private String status; // "paid" hoặc "unpaid"

    private String movieTitle;
    private String cinemaName;
    private String dateTime;

    @ServerTimestamp
    private Date createdAt;

    public Booking() {}

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getSessionId() { return sessionId; }
    public List<String> getSeatsBooked() { return seatsBooked; }
    public double getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }
    public String getMovieTitle() { return movieTitle; }
    public String getCinemaName() { return cinemaName; }
    public String getDateTime() { return dateTime; }
    public Date getCreatedAt() { return createdAt; }
}