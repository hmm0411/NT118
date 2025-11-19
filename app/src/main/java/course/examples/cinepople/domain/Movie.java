package course.examples.cinepople.domain;

import com.google.firebase.firestore.Exclude;
import java.util.List;

public class Movie {
    @Exclude
    private String id;
    private String title;
    private String posterUrl;
    private String duration;
    private String bannerImageUrl;
    private String trailerUrl;
    private String description;
    private List<String> genres;
    private String releaseDate;
    private String language;
    private Double imdbRating;
    private String ageRating;
    private String status;
    private boolean isTopMovie;

    public Movie() {}

    @Exclude
    public String getId() { return id; }

    public String getTitle() { return title; }
    public String getPosterUrl() { return posterUrl; }
    public String  getDuration() { return duration; }
    public String getBannerImageUrl() { return bannerImageUrl; }
    public String getTrailerUrl() { return trailerUrl; }
    public String getDescription() { return description; }
    public List<String> getGenres() { return genres; }
    public String getReleaseDate() { return releaseDate; }
    public String getLanguage() { return language; }
    public Double getImdbRating() { return imdbRating; }
    public String getAgeRating() { return ageRating; }
    public boolean isTopMovie(){ return isTopMovie;}

    public String getStatus() { return status; }

    @Exclude
    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }
}