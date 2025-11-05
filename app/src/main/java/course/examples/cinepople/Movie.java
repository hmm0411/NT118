package course.examples.cinepople;


public class Movie {

    public String name;
    public String duration;
    public String imageUrl;

    public Movie(String name, String duration, String imageUrl) {
        this.name = name;
        this.duration = duration;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getDuration() {
        return duration;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

