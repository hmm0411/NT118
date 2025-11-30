package course.examples.cinepople.activity.movie;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;

import java.util.Locale;

import course.examples.cinepople.R;
import course.examples.cinepople.activity.auth.LoginActivity;
import course.examples.cinepople.activity.booking.SelectSessionActivity;
import course.examples.cinepople.databinding.ActivityMovieDetailsBinding;
import course.examples.cinepople.domain.Movie;
import course.examples.cinepople.viewmodel.MovieDetailsViewModel;
import course.examples.cinepople.utility.SessionManager;

public class MovieDetailsActivity extends AppCompatActivity {

    private static final String TAG = "MovieDetailsActivity";

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
        // 🟢 GỌI HÀM CHỈNH SỬA
        binding.buttonSelectSession.setOnClickListener(v -> onSelectSessionClicked());
    }

    // ... (Các hàm observeViewModel, populateUi, setupToolbar, onPlayTrailerClicked giữ nguyên) ...
    // [Hàm populateUi đã bỏ qua để tiết kiệm không gian]
    private void populateUi(Movie movie) {
        currentMovieTitle = movie.getTitle();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(currentMovieTitle);
        }
        Glide.with(this).load(movie.getPosterUrl()).into(binding.imagePoster);
        Glide.with(this).load(movie.getBannerImageUrl()).into(binding.imageBanner);
        binding.textToolbarTitle.setText(currentMovieTitle);
        binding.textMovieTitle.setText(movie.getTitle());

        if (movie.getGenres() != null && !movie.getGenres().isEmpty()) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                binding.textGenre.setText(String.join(", ", movie.getGenres()));
            } else {
                binding.textGenre.setText(movie.getGenres().toString());
            }
        }

        binding.textAgeRating.setText(movie.getAgeRating());

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
        // 1. KIỂM TRA TRẠNG THÁI ĐĂNG NHẬP
        if (SessionManager.isLoggedIn(this)) {
            // Đã đăng nhập: Chuyển sang màn hình chọn suất chiếu
            Intent intent = new Intent(MovieDetailsActivity.this, SelectSessionActivity.class);
            intent.putExtra(MOVIE_ID_KEY, movieId);
            intent.putExtra(MOVIE_TITLE_KEY, currentMovieTitle);
            startActivity(intent);
        } else {
            // Chưa đăng nhập: Hiển thị Dialog yêu cầu Login
            showLoginRequiredDialog();
        }
    }

    /**
     * Hiển thị hộp thoại yêu cầu người dùng đăng nhập.
     */
    private void showLoginRequiredDialog() {
        // Sử dụng builder để tạo AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(
                this,
                R.style.AlertDialogTheme
        );

        // 🟢 Gắn layout tùy chỉnh cho dialog (ví dụ: dialog_login_required.xml)
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_login_required, null);
        builder.setView(dialogView);

        // Tạo dialog
        final AlertDialog dialog = builder.create();

        // Xử lý sự kiện click nút
        dialogView.findViewById(R.id.btn_dialog_login).setOnClickListener(v -> {
            dialog.dismiss();
            // Chuyển sang LoginActivity
            Intent intent = new Intent(MovieDetailsActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        dialogView.findViewById(R.id.btn_dialog_cancel).setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: Activity is now visible");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Activity is in foreground and interactive");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: Activity is partially obscured or losing focus");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: Activity is no longer visible (in background)");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Activity is about to be destroyed");
    }
}