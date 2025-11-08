package course.examples.cinepople.data;

import com.google.firebase.firestore.Exclude;
import java.util.List;

public class Movie {

    // --- Document ID ---
    // Firestore sẽ không lưu trường này vào document,
    // nhưng chúng ta dùng nó để biết ID của phim là gì.
    @Exclude
    private String id;

    // --- Core Info (Dùng ở Trang chủ, Tìm kiếm) ---
    private String title;
    private String posterUrl;
    private String duration;

    private String bannerImageUrl;  // Link ẢNH tĩnh cho banner
    private String trailerVideoUrl; // Link VIDEO cho nút Play
    private String description;
    private List<String> genres;      // Danh sách thể loại [ "Action", "Sci-Fi" ]
    private String releaseDate;
    private String language;
    private Double imdbRating;      // Dùng kiểu Double để có thể là null
    private String ageRating;
    private String ageRatingDesc;

    // --- Filtering (Dùng để lọc ở Trang chủ) ---
    private Boolean isTopMovie;     // Dùng kiểu Boolean để có thể là null
    private Boolean isNowPlaying;
    private Boolean isComingSoon;

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
    public String getDuration() { return duration; }
    public String getBannerImageUrl() { return bannerImageUrl; }
    public String getTrailerVideoUrl() { return trailerVideoUrl; }
    public String getDescription() { return description; }
    public List<String> getGenres() { return genres; }
    public String getReleaseDate() { return releaseDate; }
    public String getLanguage() { return language; }
    public Double getImdbRating() { return imdbRating; }
    public String getAgeRating() { return ageRating; }
    public String getAgeRatingDesc() { return ageRatingDesc; }
    public Boolean getIsTopMovie() { return isTopMovie; }
    public Boolean getIsNowPlaying() { return isNowPlaying; }
    public Boolean getIsComingSoon() { return isComingSoon; }

    @Exclude
    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }
}