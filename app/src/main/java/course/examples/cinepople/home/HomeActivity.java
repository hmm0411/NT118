package course.examples.cinepople.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import course.examples.cinepople.adapter.TopMoviesSliderAdapter;
import course.examples.cinepople.adapter.MoviePosterAdapter;
import course.examples.cinepople.data.Movie;
import course.examples.cinepople.R;

public class HomeActivity extends AppCompatActivity {

    private ViewPager2 viewPagerTopMovies;
    private TopMoviesSliderAdapter sliderAdapter;
    private List<Movie> topMoviesList;
    private RecyclerView recyclerNowPlaying;
    private MoviePosterAdapter nowPlayingAdapter;
    private List<Movie> nowPlayingList;
    private RecyclerView recyclerComingSoon;
    private MoviePosterAdapter comingSoonAdapter;
    private List<Movie> comingSoonList;

    private TextView tvMovieTitle;
    private TextView tvMovieDuration;
    private MaterialButton btnBookMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        viewPagerTopMovies = findViewById(R.id.viewpager_top_movies);
        recyclerNowPlaying = findViewById(R.id.recycler_now_playing);
        recyclerComingSoon = findViewById(R.id.recycler_coming_soon);

        tvMovieTitle = findViewById(R.id.tv_movie_title);
        tvMovieDuration = findViewById(R.id.tv_movie_duration);
        btnBookMovie = findViewById(R.id.btn_book_movie);

        // Tải dữ liệu
        loadDummyData();

        // --- Setup ViewPager "Top Movies" ---
        sliderAdapter = new TopMoviesSliderAdapter(this, topMoviesList);
        viewPagerTopMovies.setAdapter(sliderAdapter);
        setupSliderTransformer();
        // <<< THÊM VÀO: Lắng nghe sự kiện lướt (swipe)
        setupPageChangeListener();

        // --- Setup RecyclerView "Now Playing" ---
        nowPlayingAdapter = new MoviePosterAdapter(this, nowPlayingList);
        recyclerNowPlaying.setAdapter(nowPlayingAdapter);
        recyclerNowPlaying.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );

        // --- Setup RecyclerView "Coming Soon" ---
        comingSoonAdapter = new MoviePosterAdapter(this, comingSoonList);
        recyclerComingSoon.setAdapter(comingSoonAdapter);
        // <<< SỬA LẠI: Dùng biến mới cho LayoutManager
        LinearLayoutManager horizontalLayoutManager2 =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerComingSoon.setLayoutManager(horizontalLayoutManager2);

        if (topMoviesList != null && !topMoviesList.isEmpty()) {
            updateTopMovieInfo(0);
        }
    }

    private void setupPageChangeListener() {
        viewPagerTopMovies.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // Cập nhật thông tin khi lướt đến trang (phim) mới
                updateTopMovieInfo(position);
            }
        });
    }

    // <<< THÊM VÀO: Hàm cập nhật Tên, Thời lượng, Nút Book
    private void updateTopMovieInfo(int position) {
        if (topMoviesList == null || topMoviesList.isEmpty() || position >= topMoviesList.size()) {
            return; // Đảm bảo an toàn, tránh crash nếu list rỗng
        }

        Movie selectedMovie = topMoviesList.get(position);

        // Cập nhật văn bản
        tvMovieTitle.setText(selectedMovie.getTitle());
        tvMovieDuration.setText(selectedMovie.getDuration());

        // Cập nhật sự kiện click cho nút Book
        btnBookMovie.setOnClickListener(v -> {
            // Xử lý khi nhấn nút "Book"
            Toast.makeText(HomeActivity.this, "Đặt vé: " + selectedMovie.getTitle(), Toast.LENGTH_SHORT).show();
        });
    }

    private void setupSliderTransformer() {
        viewPagerTopMovies.setOffscreenPageLimit(3);
        // Padding và ClipChildren đã được set trong XML

        viewPagerTopMovies.setPageTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            float scale = 0.85f + r * 0.15f;
            page.setScaleY(scale);
            page.setScaleX(scale); // Thêm scaleX cho đẹp
        });
    }

    private void loadDummyData() {
        topMoviesList = new ArrayList<>();
        topMoviesList.add(new Movie("Avatar 2", "https://image.tmdb.org/t/p/w500/t6HIqrRAclMCA60NsSmeqe9RmNV.jpg", "3h 12m"));
        topMoviesList.add(new Movie("Oppenheimer", "https://image.tmdb.org/t/p/w500/8GQu4CjGagE6PfoS4i6Yf2eiZoU.jpg", "3h 0m"));
        topMoviesList.add(new Movie("Spider-Man", "https://image.tmdb.org/t/p/w500/uJYYizSuA9Y3DCs0qS4qWvHfZg4.jpg", "2h 28m"));

        nowPlayingList = new ArrayList<>();
        nowPlayingList.add(new Movie("Dune: Part Two", "https://image.tmdb.org/t/p/w500/8b8R8l8FjHkGkS8rQ9oA2eHVb0c.jpg", "2h 46m"));
        nowPlayingList.add(new Movie("Interstellar", "https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6eSjR3i22fM6A.jpg", "2h 49m"));
        nowPlayingList.add(new Movie("Oppenheimer", "https://image.tmdb.org/t/p/w500/8GQu4CjGagE6PfoS4i6Yf2eiZoU.jpg", "3h 0m"));
        nowPlayingList.add(new Movie("The Dark Knight", "https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6I0WY8G7Hax.jpg", "2h 32m"));

        comingSoonList = new ArrayList<>();
        comingSoonList.add(new Movie("Avatar 2", "https://image.tmdb.org/t/p/w500/t6HIqrRAclMCA60NsSmeqe9RmNV.jpg", "3h 12m"));
        comingSoonList.add(new Movie("Spider-Man", "https://image.tmdb.org/t/p/w500/uJYYizSuA9Y3DCs0qS4qWvHfZg4.jpg", "2h 28m"));
    }
}