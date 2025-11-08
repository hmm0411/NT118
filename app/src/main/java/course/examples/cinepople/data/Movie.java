package course.examples.cinepople.data;

import com.google.firebase.firestore.Exclude;
import java.util.List;

public class Movie {

    @Exclude
    private String id;

    private String title;
    private String posterUrl;
   // private Long duration;

    private String bannerImageUrl;
    private String trailerUrl;
    private String description;
    private List<String> genres;      // Danh sách thể loại [ "Action", "Sci-Fi" ]
    private String releaseDate;
    private String language;
    private Double imdbRating;      // Dùng kiểu Double để có thể là null
    private String ageRating;

    // --- Filtering (Dùng để lọc ở Trang chủ) ---
  private String status;

    // --- CONSTRUCTOR ---

    /**
     * !!! RẤT QUAN TRỌNG !!!
     * Cần một constructor rỗng, public
     * để Firestore có thể tự động tạo đối tượng Movie khi lấy dữ liệu.
     */
    public Movie() {}

    @Exclude
    public String getId() { return id; }

    public String getTitle() { return title; }
    public String getPosterUrl() { return posterUrl; }
    //public Long  getDuration() { return duration; }
    public String getBannerImageUrl() { return bannerImageUrl; }
    public String getTrailerUrl() { return trailerUrl; }
    public String getDescription() { return description; }
    public List<String> getGenres() { return genres; }
    public String getReleaseDate() { return releaseDate; }
    public String getLanguage() { return language; }
    public Double getImdbRating() { return imdbRating; }
    public String getAgeRating() { return ageRating; }

    public String getStatus() { return status; }

    @Exclude
    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }
}