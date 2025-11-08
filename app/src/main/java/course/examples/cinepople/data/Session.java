package course.examples.cinepople.data;

import com.google.firebase.firestore.DocumentId;
import java.util.List;

public class Session {

    @DocumentId
    private String id; // ID của suất chiếu, ví dụ: "session_xyz_123"

    private String movieId;
    private String provinceName;
    private String date; // "YYYY-MM-DD"
    private String time; // "19:00"

    private String movieTitle;
    private String cinemaName;
    private String cinemaAddress;
    private String hallName; // Tên phòng (ví dụ: "Phòng 3")
    private List<String> seatMap;
    private List<String> disabledSeats; // Mảng ghế hỏng/lối đi
    private List<String> bookedSeats;   // Mảng ghế ĐÃ ĐẶT cho suất này

    public Session() {}

    // Getters (BẮT BUỘC)
    public String getId() { return id; }
    public String getMovieId() { return movieId; }
    public String getProvinceName() { return provinceName; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getMovieTitle() { return movieTitle; }
    public String getCinemaName() { return cinemaName; }
    public String getCinemaAddress() { return cinemaAddress; }
    public String getHallName() { return hallName; }
    public List<String> getSeatMap() { return seatMap; }
    public List<String> getDisabledSeats() { return disabledSeats; }
    public List<String> getBookedSeats() { return bookedSeats; }

    // Setters
    public void setId(String id) { this.id = id; }
}