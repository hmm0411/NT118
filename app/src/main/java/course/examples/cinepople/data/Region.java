package course.examples.cinepople.data;

public class Region {
    private String name;
    // (Thêm các trường khác nếu có)

    // ---> THÊM CONSTRUCTOR RỖNG NÀY VÀO <---
    public Region() {
        // Constructor rỗng là BẮT BUỘC để Firestore
        // gọi hàm .toObject()
    }

    // (Bạn có thể giữ các constructor khác)
    public Region(String name) {
        this.name = name;
    }

    // Getter (BẮT BUỘC)
    public String getName() {
        return name;
    }

    // Setter (Cũng nên có)
    public void setName(String name) {
        this.name = name;
    }
}