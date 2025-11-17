package course.examples.cinepople.domain;

import java.util.Objects;

public class Cinema {
    private String id; // ID của rạp (ví dụ: "cine_pople_1")
    private String name;
    private String address;

    // Constructor rỗng (Bắt buộc cho Firestore)
    public Cinema() {}

    public Cinema(String id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }

    // BẮT BUỘC: Để HashSet lọc trùng lặp
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cinema cinema = (Cinema) o;
        // So sánh bằng ID rạp
        return Objects.equals(id, cinema.id);
    }

    @Override
    public int hashCode() {
        // Dùng ID rạp
        return Objects.hash(id);
    }
}