package course.examples.cinepople.domain;

import java.io.Serializable;

public class DateModel implements Serializable {

    // Hiển thị lên giao diện (Ví dụ: "Th 2", "CN")
    private String dayOfWeek;

    // Hiển thị lên giao diện (Ví dụ: "30/11")
    private String date;

    // Trạng thái: Có đang được chọn hay không (để đổi màu nền)
    private boolean isSelected;

    // 🟢 QUAN TRỌNG: Dùng để lọc dữ liệu API (Ví dụ: "2025-11-30")
    private String fullDate;

    // Constructor rỗng (Cần thiết cho một số thư viện)
    public DateModel() {
    }

    // Constructor đầy đủ
    public DateModel(String dayOfWeek, String date, boolean isSelected, String fullDate) {
        this.dayOfWeek = dayOfWeek;
        this.date = date;
        this.isSelected = isSelected;
        this.fullDate = fullDate;
    }

    // Constructor rút gọn (Nếu bạn tạo object trước rồi setFullDate sau)
    public DateModel(String dayOfWeek, String date, boolean isSelected) {
        this.dayOfWeek = dayOfWeek;
        this.date = date;
        this.isSelected = isSelected;
        this.fullDate = ""; // Mặc định rỗng để tránh null
    }

    // ------------------- GETTERS -------------------
    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public String getDate() {
        return date;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public String getFullDate() {
        return fullDate;
    }

    // ------------------- SETTERS -------------------
    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setFullDate(String fullDate) {
        this.fullDate = fullDate;
    }
}