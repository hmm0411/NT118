package course.examples.cinepople.home;

// <<< SỬA 1: Import Fragment và các thư viện liên quan
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment; // <<< Import class Fragment
import androidx.viewpager2.widget.ViewPager2;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import course.examples.cinepople.adapter.TopMoviesSliderAdapter;
import course.examples.cinepople.adapter.MoviePosterAdapter;
import course.examples.cinepople.data.Movie;
import course.examples.cinepople.R;

// <<< SỬA 2: Kế thừa từ Fragment, KHÔNG phải AppCompatActivity
public class HomeFragment extends Fragment {

    // --- Các biến của bạn (Giữ nguyên) ---
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

    // <<< SỬA 3: Dùng onCreateView để "gắn" layout
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate (nạp) layout XML cho Fragment này
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view; // Trả về view đã được nạp
    }

    // <<< SỬA 4: Dùng onViewCreated để chạy logic
    // (Toàn bộ code trong "onCreate" cũ của bạn sẽ được chuyển vào đây)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // <<< SỬA 5: Dùng "view.findViewById" để tìm các thành phần
        viewPagerTopMovies = view.findViewById(R.id.viewpager_top_movies);
        recyclerNowPlaying = view.findViewById(R.id.recycler_now_playing);
        recyclerComingSoon = view.findViewById(R.id.recycler_coming_soon);

        tvMovieTitle = view.findViewById(R.id.tv_movie_title);
        tvMovieDuration = view.findViewById(R.id.tv_movie_duration);
        btnBookMovie = view.findViewById(R.id.btn_book_movie);

        // Tải dữ liệu
        loadDummyData();

        // --- Setup ViewPager "Top Movies" ---
        // <<< SỬA 6: Dùng "getContext()" thay cho "this"
        sliderAdapter = new TopMoviesSliderAdapter(getContext(), topMoviesList);
        viewPagerTopMovies.setAdapter(sliderAdapter);
        setupSliderTransformer();
        setupPageChangeListener();

        // --- Setup RecyclerView "Now Playing" ---
        // <<< SỬA 6: Dùng "getContext()" thay cho "this"
        nowPlayingAdapter = new MoviePosterAdapter(getContext(), nowPlayingList);
        recyclerNowPlaying.setAdapter(nowPlayingAdapter);
        recyclerNowPlaying.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );

        // --- Setup RecyclerView "Coming Soon" ---
        // <<< SỬA 6: Dùng "getContext()" thay cho "this"
        comingSoonAdapter = new MoviePosterAdapter(getContext(), comingSoonList);
        recyclerComingSoon.setAdapter(comingSoonAdapter);
        LinearLayoutManager horizontalLayoutManager2 =
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerComingSoon.setLayoutManager(horizontalLayoutManager2);

        if (topMoviesList != null && !topMoviesList.isEmpty()) {
            updateTopMovieInfo(0);
        }
    }

    // --- Các hàm logic (Không thay đổi nhiều) ---

    private void setupPageChangeListener() {
        viewPagerTopMovies.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
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

        tvMovieTitle.setText(selectedMovie.getTitle());
        tvMovieDuration.setText(selectedMovie.getDuration());

        btnBookMovie.setOnClickListener(v -> {
            // <<< SỬA 7: Dùng "getContext()" thay cho "HomeFragment.this"
            Toast.makeText(getContext(), "Đặt vé: " + selectedMovie.getTitle(), Toast.LENGTH_SHORT).show();

            // TODO: Bạn có thể bắt đầu một Activity mới (ví dụ: BookingActivity) từ đây
            // Intent intent = new Intent(getContext(), BookingActivity.class);
            // intent.putExtra("MOVIE_TITLE", selectedMovie.getTitle());
            // startActivity(intent);
        });
    }

    private void setupSliderTransformer() {
        // (Không cần thay đổi gì)
        viewPagerTopMovies.setOffscreenPageLimit(3);
        viewPagerTopMovies.setPageTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            float scale = 0.85f + r * 0.15f;
            page.setScaleY(scale);
            page.setScaleX(scale);
        });
    }

    private void loadDummyData() {
        // (Không cần thay đổi gì)
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