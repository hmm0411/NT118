package course.examples.cinepople.data;

public class Actor {
    private String name;
    private String headshotUrl;

    public Actor() {}

    public Actor(String name, String headshotUrl) {
        this.name = name;
        this.headshotUrl = headshotUrl;
    }

    public String getName() {
        return name;
    }

    public String getHeadshotUrl() {
        return headshotUrl;
    }
}