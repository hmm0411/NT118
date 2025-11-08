package course.examples.cinepople.ui.main.search;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log; // <-- THÊM IMPORT NÀY
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot; // <-- THÊM IMPORT NÀY

import java.util.ArrayList;
import java.util.List;

import course.examples.cinepople.R; // <-- THÊM IMPORT NÀY
import course.examples.cinepople.ui.movies.MovieDetailsActivity;
import course.examples.cinepople.adapter.MovieSearchAdapter;
import course.examples.cinepople.data.Movie;
import course.examples.cinepople.databinding.FragmentMainSearchBinding;

// SỬA: Implement interface click của Adapter
public class SearchFragment extends Fragment implements MovieSearchAdapter.OnMovieClickListener {

    private static final String TAG = "SearchFragment"; // Thêm TAG để debug
    private FragmentMainSearchBinding binding;
    private FirebaseFirestore db;

    private MovieSearchAdapter searchAdapter;
    private List<Movie> searchResultList = new ArrayList<>();

    // SỬA: Thêm biến để quản lý bộ lọc
    private String currentFilterStatus = "all"; // "all", "now_showing", "coming_soon"

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();

        setupRecyclerView();
        setupSearchListener();

        // SỬA: Thêm hàm lắng nghe các Chip
        setupChipListeners();

        // SỬA: Tải tất cả phim (mặc định) ngay khi mở
        loadMoviesByFilter();
    }

    private void setupRecyclerView() {
        // SỬA: Truyền "this" làm listener
        searchAdapter = new MovieSearchAdapter(getContext(), searchResultList, this);

        binding.recyclerSearchResults.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerSearchResults.setAdapter(searchAdapter);
    }

    private void setupSearchListener() {
        // SỬA: Đổi ID cho đúng với XML của bạn
        binding.searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String query = binding.searchEditText.getText().toString().trim();

                // Nếu ô tìm kiếm trống, tải lại theo filter chip
                if (query.isEmpty()) {
                    loadMoviesByFilter();
                } else {
                    // Nếu có chữ, thực hiện tìm kiếm
                    performSearch(query);
                }
                return true;
            }
            return false;
        });
    }

    /**
     * SỬA: Hàm mới để lắng nghe các Chip
     */
    private void setupChipListeners() {
        binding.chipGroupFilters.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) return;

            int checkedId = checkedIds.get(0);

            if (checkedId == R.id.chip_all) {
                currentFilterStatus = "all";
            } else if (checkedId == R.id.chip_now_playing) {
                currentFilterStatus = "now_showing";
            } else if (checkedId == R.id.chip_coming_soon) {
                currentFilterStatus = "coming_soon";
            }

            // Tải lại danh sách phim theo bộ lọc mới
            loadMoviesByFilter();
            // Xóa text trong ô tìm kiếm
            binding.searchEditText.setText("");
        });
    }

    /**
     * SỬA: Hàm này giờ sẽ tải phim dựa trên Chip đang chọn
     */
    private void loadMoviesByFilter() {
        binding.textNoResults.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);

        Query query = db.collection("movies");

        // Dòng QUAN TRỌNG: Thêm điều kiện lọc vào truy vấn
        if (!"all".equals(currentFilterStatus)) {
            query = query.whereEqualTo("status", currentFilterStatus);
        }

        query = query.orderBy("title").limit(20);

        // Lấy dữ liệu đã lọc
        query.get().addOnCompleteListener(task -> {
            binding.progressBar.setVisibility(View.GONE);
            if (isAdded() && task.isSuccessful()) {
                searchResultList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Movie movie = document.toObject(Movie.class);
                    movie.setId(document.getId());
                    searchResultList.add(movie);
                }
                searchAdapter.notifyDataSetChanged(); // Cập nhật danh sách

                if (searchResultList.isEmpty()) {
                    binding.textNoResults.setVisibility(View.VISIBLE);
                }
            } else if (isAdded()) {
                Log.w(TAG, "Error loading filtered movies: ", task.getException());
                binding.textNoResults.setText("Error loading results.");
                binding.textNoResults.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * Hàm này chỉ dùng khi người dùng GÕ CHỮ vào thanh tìm kiếm
     */
    private void performSearch(String searchText) {
        binding.textNoResults.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);

        // Bỏ chọn tất cả các Chip khi đang tìm kiếm bằng chữ
        binding.chipGroupFilters.clearCheck();

        db.collection("movies")
                .orderBy("title")
                .startAt(searchText)
                .endAt(searchText + "\uf8ff")
                .limit(20)
                .get()
                .addOnCompleteListener(task -> {
                    binding.progressBar.setVisibility(View.GONE);
                    if (isAdded() && task.isSuccessful()) {
                        searchResultList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Movie movie = document.toObject(Movie.class);
                            movie.setId(document.getId());
                            searchResultList.add(movie);
                        }
                        searchAdapter.notifyDataSetChanged();

                        if (searchResultList.isEmpty()) {
                            binding.textNoResults.setVisibility(View.VISIBLE);
                        }
                    } else if (isAdded()) {
                        Log.w(TAG, "Error performing search: ", task.getException());
                        binding.textNoResults.setText("Error loading results.");
                        binding.textNoResults.setVisibility(View.VISIBLE);
                    }
                });
    }

    /**
     * SỬA: Hàm bắt buộc của Interface
     * Đây là nơi xử lý khi người dùng click vào 1 item phim
     */
    @Override
    public void onMovieClick(Movie movie) {
        Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
        intent.putExtra(MovieDetailsActivity.MOVIE_ID_KEY, movie.getId());
        intent.putExtra(MovieDetailsActivity.MOVIE_TITLE_KEY, movie.getTitle());
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}