package course.examples.cinepople.data;

import com.google.firebase.firestore.DocumentId;

public class Cinema {

    @DocumentId
    private String id; // Lấy ID rạp

    private String name;
    private String address;
    private String provinceName;

    public Cinema() {}

    // Getters (BẮT BUỘC)
    public String getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getProvinceName() { return provinceName; }
}