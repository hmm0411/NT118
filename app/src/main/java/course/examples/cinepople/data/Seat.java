//package course.examples.cinepople.ui.seats;
//
//import java.util.Objects;
//
//public class Seat {
//    private String id; // Tên ghế, ví dụ: "A1", "E5"
//    private SeatStatus status;
//    private double price;
//
//    public Seat(String id, SeatStatus status, double price) {
//        this.id = id;
//        this.status = status;
//        this.price = price;
//    }
//
//    // Getters
//    public String getId() { return id; }
//    public SeatStatus getStatus() { return status; }
//    public double getPrice() { return price; }
//
//    // Setter
//    public void setStatus(SeatStatus status) {
//        this.status = status;
//    }
//
//    // Dùng cho DiffUtil trong Adapter
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Seat seat = (Seat) o;
//        return Objects.equals(id, seat.id) && status == seat.status;
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, status);
//    }
//}