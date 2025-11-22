package course.examples.cinepople.fragment.search;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast; // THÊM IMPORT
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider; // THÊM IMPORT
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import course.examples.cinepople.R;
import course.examples.cinepople.activity.movie.MovieDetailsActivity;
import course.examples.cinepople.adapter.MovieSearchAdapter;
import course.examples.cinepople.domain.Movie;
import course.examples.cinepople.databinding.FragmentMainSearchBinding;
import course.examples.cinepople.viewmodel.SearchViewModel;

public class SearchFragment extends Fragment implements MovieSearchAdapter.OnMovieClickListener {

    private static final String TAG = "SearchFragment";
    private FragmentMainSearchBinding binding;

    private SearchViewModel viewModel;

    private MovieSearchAdapter searchAdapter;
    private List<Movie> searchResultList = new ArrayList<>();

    private String currentFilterStatus = "all";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. KHỞI TẠO VIEWMODEL
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        setupRecyclerView();
        setupSearchListener();
        setupChipListeners();

        // 2. BẮT ĐẦU LẮNG NGHE DỮ LIỆU
        observeViewModel();

        // 3. GỌI API ĐỂ TẢI TẤT CẢ PHIM NGAY KHI MỞ TRANG
        viewModel.fetchAllMovies();
    }

    // --- HÀM MỚI: LẮNG NGHE LIVEDATA TỪ VIEWMODEL ---
    private void observeViewModel() {
        // Lắng nghe danh sách kết quả
        viewModel.getSearchResults().observe(getViewLifecycleOwner(), movies -> {
            if (movies != null) {
                searchResultList.clear();
                searchResultList.addAll(movies);
                searchAdapter.notifyDataSetChanged();

                // Hiển thị thông báo nếu không có kết quả
                binding.textNoResults.setVisibility(movies.isEmpty() ? View.VISIBLE : View.GONE);

                // Cập nhật trạng thái loading
                binding.progressBar.setVisibility(View.GONE);
            }
        });

        // Lắng nghe trạng thái Loading
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (binding == null) return;
            // Hiển thị ProgressBar khi tải
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        // Lắng nghe Lỗi
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                Log.e(TAG, "API Error: " + error);
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }


    private void setupRecyclerView() {
        searchAdapter = new MovieSearchAdapter(getContext(), searchResultList, this);
        binding.recyclerSearchResults.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerSearchResults.setAdapter(searchAdapter);
    }

    private void setupSearchListener() {
        binding.searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                // TODO: Triển khai performSearch(query) sau
                Log.d(TAG, "Search action triggered");
                return true;
            }
            return false;
        });
    }

    private void setupChipListeners() {
        binding.chipGroupFilters.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) return;

            // TODO: Triển khai loadMoviesByFilter() sau
            Log.d(TAG, "Filter changed (data loading disabled)");
            binding.searchEditText.setText("");
        });
    }

    // --- CÁC HÀM NÀY GIỮ NGUYÊN (CHỈ LÀ SKELETON) ---
    private void loadMoviesByFilter() {
        // ... (Sẽ dùng ViewModel.fetchFilteredMovies() sau)
    }

    private void performSearch(String searchText) {
        // ... (Sẽ dùng ViewModel.fetchSearchQuery() sau)
    }
    // ----------------------------------------------------

    @Override
    public void onMovieClick(Movie movie) {
        Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
        intent.putExtra(MovieDetailsActivity.MOVIE_ID_KEY, movie.getId());
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}