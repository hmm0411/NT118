package course.examples.cinepople.data.remote.request;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class BookingRequest {
    @SerializedName("showtimeId")
    private String showtimeId;

    @SerializedName("seats")
    private List<String> seats;

    @SerializedName("voucher")
    private String voucher; // Optional

    public BookingRequest(String showtimeId, List<String> seats) {
        this.showtimeId = showtimeId;
        this.seats = seats;
    }

    // Nếu có voucher
    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }
}