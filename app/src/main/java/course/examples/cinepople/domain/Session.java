package course.examples.cinepople.domain;

import com.google.firebase.firestore.DocumentId;
import java.util.List;

public class Session {

    @DocumentId
    private String id;
    private String movieId;
    private String regionName;
    private String regionID;
    private String date;
    private String cinemaId;

    private String cinemaName;
    private String cinemaAddress;

    private List<String> showtimes;
    private List<String> bookedSeats;
    private List<String> seatMap;
    public Session() {}

    public String getId() { return id; }
    public String getMovieId() { return movieId; }
    public String getRegionID() {return regionID; }
    public String getRegionName() { return regionName; }
    public String getDate() { return date; }
    public String getCinemaId() { return cinemaId; }
    public String getCinemaName() { return cinemaName; }
    public String getCinemaAddress() { return cinemaAddress; }
    public List<String> getShowtimes() { return showtimes; }

    public List<String> getSeatMap() {
        return seatMap;
    }

    public List<String> getBookedSeats() {
        return bookedSeats;
    }
}