package course.examples.cinepople.fragment.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;
import androidx.recyclerview.widget.LinearLayoutManager;


import java.util.ArrayList;
import java.util.List;

import course.examples.cinepople.activity.auth.LoginActivity;
import course.examples.cinepople.activity.movie.MovieDetailsActivity;
import course.examples.cinepople.adapter.TopMoviesSliderAdapter;
import course.examples.cinepople.adapter.MoviePosterAdapter;
import course.examples.cinepople.domain.Movie;
import course.examples.cinepople.databinding.FragmentMainHomeBinding;
import course.examples.cinepople.viewmodel.HomeViewModel; // <-- THÊM

public class HomeFragment extends Fragment
        implements TopMoviesSliderAdapter.OnMovieClickListener,
        MoviePosterAdapter.OnMovieClickListener {

    private static final String TAG = "HomeFragment";
    private FragmentMainHomeBinding binding;

    private HomeViewModel viewModel;

    private TopMoviesSliderAdapter sliderAdapter;
    private List<Movie> topMovieList;
    private MoviePosterAdapter nowPlayingAdapter;
    private List<Movie> nowPlayingList;
    private MoviePosterAdapter comingSoonAdapter;
    private List<Movie> comingSoonList;

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

        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        loadUserSession();
        setupViews();

        observeViewModel();
        viewModel.fetchAllHomeData();
    }

    private void observeViewModel() {

        viewModel.getTopMovies().observe(getViewLifecycleOwner(), movies -> {
            if (movies != null) {
                topMovieList.clear();
                topMovieList.addAll(movies);
                sliderAdapter.notifyDataSetChanged();

                if (!topMovieList.isEmpty()) {
                    updateTopMovieInfo(0);
                }
            }
        });

        viewModel.getNowPlayingMovies().observe(getViewLifecycleOwner(), movies -> {
            if (movies != null) {
                nowPlayingList.clear();
                nowPlayingList.addAll(movies);
                nowPlayingAdapter.notifyDataSetChanged();
            }
        });

        viewModel.getComingSoonMovies().observe(getViewLifecycleOwner(), movies -> {
            if (movies != null) {
                comingSoonList.clear();
                comingSoonList.addAll(movies);
                comingSoonAdapter.notifyDataSetChanged();
            }
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                // binding.progressBar.setVisibility(View.VISIBLE);
                // binding.mainContent.setVisibility(View.GONE); // (Ẩn nội dung)
            } else {
                // binding.progressBar.setVisibility(View.GONE);
                // binding.mainContent.setVisibility(View.VISIBLE);
            }
        });

        // 5. Lắng nghe Lỗi
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "API Error: " + error);
            }
        });
    }

    // --- (Các hàm loadUserSession và setupViews giữ nguyên) ---
    private void loadUserSession() {
        if (getContext() == null) return;
        SharedPreferences sharedPref = getContext().getSharedPreferences(
                LoginActivity.APP_PREFERENCES,
                Context.MODE_PRIVATE
        );
        currentUserId = sharedPref.getString(LoginActivity.KEY_USER_ID, null);
        currentUserEmail = sharedPref.getString(LoginActivity.KEY_USER_EMAIL, "Guest");
    }

    private void setupViews() {
        // (Không thay đổi gì ở hàm này)
        topMovieList = new ArrayList<>();
        sliderAdapter = new TopMoviesSliderAdapter(getContext(), topMovieList, this);
        binding.viewpagerTopMovies.setAdapter(sliderAdapter);
        setupSliderTransformer();
        setupPageChangeListener();

        nowPlayingList = new ArrayList<>();
        nowPlayingAdapter = new MoviePosterAdapter(getContext(), nowPlayingList, this);
        binding.recyclerNowPlaying.setAdapter(nowPlayingAdapter);
        binding.recyclerNowPlaying.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );

        comingSoonList = new ArrayList<>();
        comingSoonAdapter = new MoviePosterAdapter(getContext(), comingSoonList, this);
        binding.recyclerComingSoon.setAdapter(comingSoonAdapter);
        binding.recyclerComingSoon.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );
    }

    // --- XÓA TOÀN BỘ CÁC HÀM "load...Movies()" ---
    // private void loadAllMovieData() { ... }
    // private void loadTopMovies() { ... }
    // private void loadNowPlayingMovies() { ... }
    // private void loadComingSoonMovies() { ... }

    // --- (Các hàm sự kiện và UI còn lại giữ nguyên) ---
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
        if (topMovieList == null || topMovieList.isEmpty() || position >= topMovieList.size()) {
            return;
        }
        Movie selectedMovie = topMovieList.get(position);
        binding.tvMovieTitle.setText(selectedMovie.getTitle());
        //binding.tvMovieDuration.setText(selectedMovie.getDuration());
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