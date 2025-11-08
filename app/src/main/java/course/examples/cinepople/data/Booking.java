package course.examples.cinepople.data;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;
import java.util.List;

public class Booking {

    @DocumentId
    private String id;

    private String userId;
    private String sessionId; // ID của suất chiếu đã đặt
    private List<String> seatsBooked;
    private double totalPrice;
    private String status; // "paid" hoặc "unpaid"

    // Các trường sao chép để hiển thị
    private String movieTitle;
    private String cinemaName;
    private String dateTime; // "2025-11-12 19:00"

    @ServerTimestamp
    private Date createdAt;

    // BẮT BUỘC: Constructor rỗng
    public Booking() {}

    // Getters (BẮT BUỘC)
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