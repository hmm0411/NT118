package course.examples.cinepople.domain;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;

public class Region implements Serializable {

    @DocumentId
    private String id;
    private String name;
    public Region() {}

    public String getId() { return id; }
    public String getName() { return name; }
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
}