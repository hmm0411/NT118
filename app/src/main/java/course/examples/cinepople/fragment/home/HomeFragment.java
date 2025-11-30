package course.examples.cinepople.fragment.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import course.examples.cinepople.activity.movie.MovieDetailsActivity;
import course.examples.cinepople.adapter.TopMoviesAdapter;
import course.examples.cinepople.adapter.MoviePosterAdapter;
import course.examples.cinepople.domain.Movie;
import course.examples.cinepople.databinding.FragmentMainHomeBinding;
import course.examples.cinepople.viewmodel.HomeViewModel;

public class HomeFragment extends Fragment
        implements TopMoviesAdapter.OnMovieClickListener,
        MoviePosterAdapter.OnMovieClickListener {

    private static final String TAG = "HomeFragment";
    private FragmentMainHomeBinding binding;
    private HomeViewModel viewModel;

    // Adapters & Lists
    private TopMoviesAdapter sliderAdapter;
    private List<Movie> topMovieList;
    private MoviePosterAdapter nowPlayingAdapter;
    private List<Movie> nowPlayingList;
    private MoviePosterAdapter comingSoonAdapter;
    private List<Movie> comingSoonList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Init ViewModel
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // 2. Setup UI
        setupViews();

        // 3. Observe Data (Fragment chỉ việc nhận data đã lọc và hiển thị)
        observeViewModel();

        // 4. Call API
        viewModel.fetchAllHomeData();
    }

    private void setupViews() {
        Context context = requireContext();

        // Top Movies Slider
        topMovieList = new ArrayList<>();
        sliderAdapter = new TopMoviesAdapter(context, topMovieList, this);
        binding.viewpagerTopMovies.setAdapter(sliderAdapter);
        setupPageChangeListener();
        setupSliderTransformer();

        // Now Playing Recycler
        nowPlayingList = new ArrayList<>();
        nowPlayingAdapter = new MoviePosterAdapter(context, nowPlayingList, this);
        binding.recyclerNowPlaying.setAdapter(nowPlayingAdapter);
        binding.recyclerNowPlaying.setLayoutManager(
                new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        );

        // Coming Soon Recycler
        comingSoonList = new ArrayList<>();
        comingSoonAdapter = new MoviePosterAdapter(context, comingSoonList, this);
        binding.recyclerComingSoon.setAdapter(comingSoonAdapter);
        binding.recyclerComingSoon.setLayoutManager(
                new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        );
    }

    private void observeViewModel() {
        // ViewModel đã lọc sẵn (Top=3, Now=6, Soon=6), Fragment chỉ việc hiển thị
        viewModel.getTopMovies().observe(getViewLifecycleOwner(), movies -> {
            if (movies != null) {
                topMovieList.clear();
                topMovieList.addAll(movies);
                sliderAdapter.notifyDataSetChanged();
                if (!topMovieList.isEmpty()) updateTopMovieInfo(0);
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
            if (binding == null) return;
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
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
        if (topMovieList == null || topMovieList.isEmpty() || position >= topMovieList.size()) return;
        Movie selectedMovie = topMovieList.get(position);
        if (binding.tvMovieTitle != null) binding.tvMovieTitle.setText(selectedMovie.getTitle());
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