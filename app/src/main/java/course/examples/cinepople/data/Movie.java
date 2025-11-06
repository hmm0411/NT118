package course.examples.cinepople.data;

public class Movie {
    private String title;
    private String posterUrl;
    private String duration;

    public Movie() {
    }

    public Movie(String title, String posterUrl, String duration) {
        this.title = title;
        this.posterUrl = posterUrl;
        this.duration = duration;
    }


    public String getTitle() {
        return title;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getDuration() {
        return duration;
    }
}