package course.examples.cinepople.domain;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Seat implements Serializable {

    @SerializedName("code")
    private String code; // Ví dụ: "A1"

    @SerializedName("row")
    private String row; // Ví dụ: "A"

    @SerializedName("col")
    private int col; // Ví dụ: 1

    @SerializedName("type")
    private String type; // enum: standard, vip, couple

    @SerializedName("price")
    private double price;

    @SerializedName("status")
    private String status; // enum: available, held, sold, locked

    @SerializedName("userId")
    private String userId; // ID người đang giữ ghế (nếu có)

    public Seat() {
    }

    // Getters
    public String getCode() { return code; }
    public String getRow() { return row; }
    public int getCol() { return col; }
    public String getType() { return type; }
    public double getPrice() { return price; }
    public String getStatus() { return status; }
    public String getUserId() { return userId; }

    // Helpers
    public boolean isAvailable() {
        return "available".equalsIgnoreCase(status);
    }

    public boolean isSold() {
        return "sold".equalsIgnoreCase(status);
    }
}