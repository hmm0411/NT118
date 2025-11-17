package course.examples.cinepople.fragment.home;

import android.content.Context; // SỬA: Thêm import
import android.content.Intent;
import android.content.SharedPreferences; // SỬA: Thêm import
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

// SỬA: Import LoginActivity để dùng hằng số
import course.examples.cinepople.activity.auth.LoginActivity;
import course.examples.cinepople.activity.movie.MovieDetailsActivity;
import course.examples.cinepople.adapter.TopMoviesSliderAdapter;
import course.examples.cinepople.adapter.MoviePosterAdapter;
import course.examples.cinepople.domain.Movie;
import course.examples.cinepople.databinding.FragmentMainHomeBinding;

public class HomeFragment extends Fragment
        implements TopMoviesSliderAdapter.OnMovieClickListener,
        MoviePosterAdapter.OnMovieClickListener {

    private static final String TAG = "HomeFragment";
    private FragmentMainHomeBinding binding;

    // --- Firebase ---
    private FirebaseFirestore db;

    // --- Dữ liệu & Adapters ---
    private TopMoviesSliderAdapter sliderAdapter;
    private List<Movie> topMovieList;

    private MoviePosterAdapter nowPlayingAdapter;
    private List<Movie> nowPlayingList;

    private MoviePosterAdapter comingSoonAdapter;
    private List<Movie> comingSoonList;

    // SỬA: Thêm biến để giữ thông tin user
    private String currentUserId;
    private String currentUserEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMainHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();

        // SỬA: Tải thông tin phiên đăng nhập (UID/Email) từ SharedPreferences
        loadUserSession();

        setupViews();


        // Tải dữ liệu phim (các hàm này tự động dùng token)
        loadAllMovieData();
    }

    /**
     * SỬA: Hàm mới - Lấy UID/Email từ SharedPreferences
     */
    private void loadUserSession() {
        if (getContext() == null) return;

        SharedPreferences sharedPref = getContext().getSharedPreferences(
                LoginActivity.APP_PREFERENCES, // Dùng hằng số từ LoginActivity
                Context.MODE_PRIVATE
        );

        currentUserId = sharedPref.getString(LoginActivity.KEY_USER_ID, null);
        currentUserEmail = sharedPref.getString(LoginActivity.KEY_USER_EMAIL, "Guest");
    }

    private void setupViews() {
        // --- Top Movies ---
        topMovieList = new ArrayList<>();
        sliderAdapter = new TopMoviesSliderAdapter(getContext(), topMovieList, this);
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

    private void loadAllMovieData() {
        loadTopMovies();
        loadNowPlayingMovies();
        loadComingSoonMovies();
    }

    private void loadTopMovies() {
        db.collection("movies")
                .whereEqualTo("status", "now_showing")
                .limit(3) // Lấy 3 phim
                .get()
                .addOnCompleteListener(task -> {
                    if (isAdded() && task.isSuccessful() && task.getResult() != null) {
                        topMovieList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Movie movie = document.toObject(Movie.class);
                            movie.setId(document.getId());
                            topMovieList.add(movie);
                        }
                        sliderAdapter.notifyDataSetChanged();

                        if (!topMovieList.isEmpty()) {
                            updateTopMovieInfo(0);
                        }
                    } else if (isAdded()) {
                        Log.w(TAG, "Error getting top movies.", task.getException());
                    }
                });
        // comingSoonList.clear(); // <-- SỬA: Xóa dòng này, đây là bug
    }

    private void loadNowPlayingMovies() {
        db.collection("movies")
                .whereEqualTo("status", "now_showing")
                .get()
                .addOnCompleteListener(task -> {
                    if (isAdded() && task.isSuccessful() && task.getResult() != null) {
                        nowPlayingList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Movie movie = document.toObject(Movie.class);
                            movie.setId(document.getId());
                            nowPlayingList.add(movie);
                        }
                        nowPlayingAdapter.notifyDataSetChanged();
                    } else if (isAdded()) {
                        Log.w(TAG, "Error getting now playing movies.", task.getException());
                    }
                });
    }

    private void loadComingSoonMovies() {
        db.collection("movies")
                .whereEqualTo("status", "coming_soon")
                .get()
                .addOnCompleteListener(task -> {
                    if (isAdded() && task.isSuccessful() && task.getResult() != null) {
                        comingSoonList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Movie movie = document.toObject(Movie.class);
                            movie.setId(document.getId());
                            comingSoonList.add(movie);
                        }
                        comingSoonAdapter.notifyDataSetChanged();
                    } else if (isAdded()) {
                        Log.w(TAG, "Error getting coming soon movies.", task.getException());
                    }
                });
    }

    @Override
    public void onMovieClick(Movie movie) {
        // (Code này đã đúng)
        Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
        intent.putExtra(MovieDetailsActivity.MOVIE_ID_KEY, movie.getId());
        startActivity(intent);
    }

    private void setupPageChangeListener() {
        // (Code này đã đúng)
        binding.viewpagerTopMovies.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateTopMovieInfo(position);
            }
        });
    }

    private void updateTopMovieInfo(int position) {
        // (Code này đã đúng)
        if (topMovieList == null || topMovieList.isEmpty() || position >= topMovieList.size()) {
            return;
        }
        Movie selectedMovie = topMovieList.get(position);
        binding.tvMovieTitle.setText(selectedMovie.getTitle());
        //binding.tvMovieDuration.setText(selectedMovie.getDuration());
    }

    private void setupSliderTransformer() {
        // (Code này đã đúng)
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