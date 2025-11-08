package course.examples.cinepople.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import androidx.recyclerview.widget.LinearLayoutManager;

// Imports cho Firebase
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import course.examples.cinepople.ui.session.SelectSessionActivity;
import course.examples.cinepople.ui.movies.MovieDetailsActivity;
import course.examples.cinepople.adapter.TopMoviesSliderAdapter;
import course.examples.cinepople.adapter.MoviePosterAdapter;
import course.examples.cinepople.data.Movie;
import course.examples.cinepople.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment
        implements TopMoviesSliderAdapter.OnMovieClickListener,
        MoviePosterAdapter.OnMovieClickListener {

    private static final String TAG = "HomeFragment";
    private FragmentHomeBinding binding;

    // --- Firebase ---
    private FirebaseFirestore db;

    // --- Dữ liệu & Adapters ---
    private TopMoviesSliderAdapter sliderAdapter;
    private List<Movie> topMoviesList;

    private MoviePosterAdapter nowPlayingAdapter;
    private List<Movie> nowPlayingList;

    private MoviePosterAdapter comingSoonAdapter;
    private List<Movie> comingSoonList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Khởi tạo Firebase
        db = FirebaseFirestore.getInstance();

        // Cài đặt các Views
        setupViews();

        // Tải tất cả dữ liệu từ Firebase
        loadAllMovieData();
    }

    private void setupViews() {
        // --- Top Movies ---
        topMoviesList = new ArrayList<>();
        sliderAdapter = new TopMoviesSliderAdapter(getContext(), topMoviesList, this);
        binding.viewpagerTopMovies.setAdapter(sliderAdapter);
        setupSliderTransformer();
        setupPageChangeListener();

        // --- Now Playing ---
        nowPlayingList = new ArrayList<>();
        nowPlayingAdapter = new MoviePosterAdapter(getContext(), nowPlayingList, this);
        binding.recyclerNowPlaying.setAdapter(nowPlayingAdapter);
        binding.recyclerNowPlaying.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );

        // --- Coming Soon ---
        comingSoonList = new ArrayList<>();
        comingSoonAdapter = new MoviePosterAdapter(getContext(), comingSoonList, this);
        binding.recyclerComingSoon.setAdapter(comingSoonAdapter);
        binding.recyclerComingSoon.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );
    }

    /**
     * Gọi các hàm để tải dữ liệu cho từng danh sách
     */
    private void loadAllMovieData() {
        loadTopMovies();
        loadNowPlayingMovies();
        loadComingSoonMovies();
    }

    // --- CÁC HÀM TẢI DỮ LIỆU TỪ FIREBASE ---

    private void loadTopMovies() {
        db.collection("movies")
                .whereEqualTo("isTopMovie", true)
                .limit(3)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        topMoviesList.clear(); // Xóa dữ liệu cũ
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Chuyển document thành đối tượng Movie
                            Movie movie = document.toObject(Movie.class);
                            movie.setId(document.getId());
                            topMoviesList.add(movie);
                        }
                        sliderAdapter.notifyDataSetChanged();

                        if (!topMoviesList.isEmpty()) {
                            updateTopMovieInfo(0);
                        }
                    } else {
                        Log.w(TAG, "Error getting top movies.", task.getException());
                    }
                });
    }

    private void loadNowPlayingMovies() {
        db.collection("movies")
                .whereEqualTo("isNowPlaying", true) // Lọc theo trường "isNowPlaying"
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        nowPlayingList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Movie movie = document.toObject(Movie.class);
                            movie.setId(document.getId());
                            nowPlayingList.add(movie);
                        }
                        nowPlayingAdapter.notifyDataSetChanged(); // Cập nhật RecyclerView
                    } else {
                        Log.w(TAG, "Error getting now playing movies.", task.getException());
                    }
                });
    }

    private void loadComingSoonMovies() {
        db.collection("movies")
                .whereEqualTo("isComingSoon", true)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        comingSoonList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Movie movie = document.toObject(Movie.class);
                            movie.setId(document.getId());
                            comingSoonList.add(movie);
                        }
                        comingSoonAdapter.notifyDataSetChanged();
                    } else {
                        Log.w(TAG, "Error getting coming soon movies.", task.getException());
                    }
                });
    }

    @Override
    public void onMovieClick(Movie movie) {
        Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
        intent.putExtra(MovieDetailsActivity.MOVIE_ID_KEY, movie.getId());
        startActivity(intent);
    }

    private void setupPageChangeListener() {
        binding.viewpagerTopMovies.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateTopMovieInfo(position);
            }
        });
    }

    private void updateTopMovieInfo(int position) {
        if (topMoviesList == null || topMoviesList.isEmpty() || position >= topMoviesList.size()) {
            return;
        }
        Movie selectedMovie = topMoviesList.get(position);

        // Hiển thị tên và thời lượng (như yêu cầu của bạn)
        binding.tvMovieTitle.setText(selectedMovie.getTitle());
        binding.tvMovieDuration.setText(selectedMovie.getDuration());

        // Gán sự kiện cho nút "Book Movie"
        binding.btnBookMovie.setOnClickListener(v -> {
            // Chuyển sang Activity chọn suất chiếu
            Intent intent = new Intent(getActivity(), SelectSessionActivity.class);
            intent.putExtra(MovieDetailsActivity.MOVIE_ID_KEY, selectedMovie.getId());
            startActivity(intent);
        });
    }

    private void setupSliderTransformer() {
        binding.viewpagerTopMovies.setOffscreenPageLimit(3);
        binding.viewpagerTopMovies.setPageTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            float scale = 0.85f + r * 0.15f;
            page.setScaleY(scale);
            page.setScaleX(scale);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}