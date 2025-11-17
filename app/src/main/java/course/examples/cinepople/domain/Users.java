package course.examples.cinepople.domain;

public class Users {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String dob; // day of birth
    private String gender;
    private String avtUrl;

    public Users() {}
    public Users(String name, String email, String phone, String dob, String gender, String avtUrl) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.dob = dob;
        this.gender = gender;
        this.avtUrl = avtUrl;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getAvtUrl() { return avtUrl; }
    public void setAvtUrl(String avtUrl) { this.avtUrl = avtUrl; }
}