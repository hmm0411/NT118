package course.examples.cinepople.activity.movie;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider; // <-- Import ViewModel
import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Locale;

import course.examples.cinepople.activity.booking.SelectSessionActivity;
import course.examples.cinepople.databinding.ActivityMovieDetailsBinding;
import course.examples.cinepople.domain.Movie;
import course.examples.cinepople.viewmodel.MovieDetailsViewModel; // <-- Import ViewModel mới

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String MOVIE_ID_KEY = "movie_id";
    public static final String MOVIE_TITLE_KEY = "movie_title";

    private ActivityMovieDetailsBinding binding;
    private MovieDetailsViewModel viewModel;

    private String movieId;
    private String trailerUrl = "";
    private String currentMovieTitle = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 1. Lấy ID từ Intent
        movieId = getIntent().getStringExtra(MOVIE_ID_KEY);
        if (movieId == null || movieId.isEmpty()) {
            Toast.makeText(this, "Error: Movie not found", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        setupToolbar();

        // 2. Khởi tạo ViewModel
        viewModel = new ViewModelProvider(this).get(MovieDetailsViewModel.class);

        // 3. Lắng nghe dữ liệu
        observeViewModel();

        // 4. Gọi API lấy chi tiết
        viewModel.fetchMovieDetail(movieId);

        // Sự kiện click nút
        binding.imagePlayButton.setOnClickListener(v -> onPlayTrailerClicked());
        binding.buttonSelectSession.setOnClickListener(v -> onSelectSessionClicked());
    }

    private void observeViewModel() {
        // Khi có dữ liệu phim -> Cập nhật UI
        viewModel.getMovieDetails().observe(this, movie -> {
            if (movie != null) {
                populateUi(movie);
            }
        });

        // Khi đang tải -> Hiện loading (nếu bạn có ProgressBar)
        viewModel.getIsLoading().observe(this, isLoading -> {
            // binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        // Khi lỗi -> Thông báo
        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hàm cập nhật giao diện từ Object Movie (Thay vì DocumentSnapshot)
    private void populateUi(Movie movie) {
        currentMovieTitle = movie.getTitle();

        // Toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(currentMovieTitle);
        }
        // Hoặc binding.toolbar.setTitle(currentMovieTitle);

        // Load ảnh
        Glide.with(this).load(movie.getPosterUrl()).into(binding.imagePoster);
        Glide.with(this).load(movie.getBannerImageUrl()).into(binding.imageBanner);

        // Text infos
        binding.textMovieTitle.setText(movie.getTitle());

        // List Genre
        if (movie.getGenres() != null && !movie.getGenres().isEmpty()) {
            // Android O (API 26) trở lên mới dùng String.join
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                binding.textGenre.setText(String.join(", ", movie.getGenres()));
            } else {
                binding.textGenre.setText(movie.getGenres().toString());
            }
        }

        binding.textAgeRating.setText(movie.getAgeRating());
        // binding.textAgeRatingDesc.setText(movie.getAgeRatingDesc()); // Nếu model có trường này

        if (movie.getImdbRating() != null) {
            binding.textImdbRating.setText(String.format(Locale.US, "%.1f", movie.getImdbRating()));
        }

        binding.textReleaseDateValue.setText(movie.getReleaseDate());
        binding.textDurationValue.setText(movie.getDuration());
        binding.textLanguageValue.setText(movie.getLanguage());
        binding.textDescriptionBody.setText(movie.getDescription());

        trailerUrl = movie.getTrailerUrl();
        binding.imagePlayButton.setVisibility(trailerUrl != null && !trailerUrl.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void onPlayTrailerClicked() {
        if (trailerUrl != null && !trailerUrl.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl));
            try {
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "Could not open trailer", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void onSelectSessionClicked() {
        Intent intent = new Intent(MovieDetailsActivity.this, SelectSessionActivity.class);
        intent.putExtra(MOVIE_ID_KEY, movieId);
        intent.putExtra(MOVIE_TITLE_KEY, currentMovieTitle);
        startActivity(intent);
    }
}