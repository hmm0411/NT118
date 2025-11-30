package course.examples.cinepople.domain;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class Showtime implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("movieId")
    private String movieId;

    @SerializedName("movieTitle")
    private String movieTitle;

    @SerializedName("cinemaId")
    private String cinemaId;

    @SerializedName("cinemaName")
    private String cinemaName;

    @SerializedName("roomName")
    private String roomName;

    @SerializedName("startTime")
    private String startTime;

    @SerializedName("endTime")
    private String endTime;

    @SerializedName("totalSeats")
    private int totalSeats;

    // Map chứa thông tin ghế: Key là mã ghế (A1), Value là object Seat
    // Trường này có thể null khi gọi API lấy danh sách list, nhưng có dữ liệu khi gọi detail
    @SerializedName("seatMap")
    private Map<String, Seat> seatMap;

    public Showtime() {
    }

    // ------------------- GETTERS -------------------

    public String getId() { return id; }
    public String getMovieId() { return movieId; }
    public String getMovieTitle() { return movieTitle; }
    public String getCinemaId() { return cinemaId; }
    public String getCinemaName() { return cinemaName; }
    public String getRoomName() { return roomName; }
    public String getStartTimeRaw() { return startTime; } // Lấy chuỗi gốc
    public String getEndTime() { return endTime; }
    public int getTotalSeats() { return totalSeats; }
    public Map<String, Seat> getSeatMap() { return seatMap; }

    public String getDate() {
        return formatIsoDate(startTime, "yyyy-MM-dd");
    }

    public String getTime() {
        return formatIsoDate(startTime, "HH:mm");
    }

    private String formatIsoDate(String isoDate, String pattern) {
        if (isoDate == null || isoDate.isEmpty()) return "";
        try {
            // Định dạng ISO 8601 từ server (UTC)
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
            isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            // Nếu server trả về không có mili giây (.SSS), dùng pattern này:
            // SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);

            Date date = isoFormat.parse(isoDate);

            // Định dạng mong muốn (Local Time của điện thoại)
            SimpleDateFormat outputFormat = new SimpleDateFormat(pattern, Locale.getDefault());
            return outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}