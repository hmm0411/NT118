package course.examples.cinepople.fragment.search;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

// --- BẠN CÓ THỂ COMMENT CÁC IMPORT CỦA FIREBASE ---
// import com.google.firebase.firestore.FirebaseFirestore;
// import com.google.firebase.firestore.Query;
// import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import course.examples.cinepople.R;
import course.examples.cinepople.activity.movie.MovieDetailsActivity;
import course.examples.cinepople.adapter.MovieSearchAdapter;
import course.examples.cinepople.domain.Movie;
import course.examples.cinepople.databinding.FragmentMainSearchBinding;

public class SearchFragment extends Fragment implements MovieSearchAdapter.OnMovieClickListener {

    private static final String TAG = "SearchFragment";
    private FragmentMainSearchBinding binding;

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
        setupRecyclerView();
        setupSearchListener();
        setupChipListeners();
    }

    private void setupRecyclerView() {
        searchAdapter = new MovieSearchAdapter(getContext(), searchResultList, this);
        binding.recyclerSearchResults.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerSearchResults.setAdapter(searchAdapter);
    }

    private void setupSearchListener() {
        binding.searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                Log.d(TAG, "Search clicked (Data loading disabled)");
                return true;
            }
            return false;
        });
    }

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

            // --- COMMENT CÁC HÀM GỌI DATA ---
            // loadMoviesByFilter();
            Log.d(TAG, "Filter changed (Data loading disabled)");
            binding.searchEditText.setText("");
        });
    }

    private void loadMoviesByFilter() {
        // binding.textNoResults.setVisibility(View.GONE);
        // binding.progressBar.setVisibility(View.VISIBLE);
        // ... (Toàn bộ code Firebase) ...
    }

    private void performSearch(String searchText) {
        // binding.textNoResults.setVisibility(View.GONE);
        // binding.progressBar.setVisibility(View.VISIBLE);
        // ... (Toàn bộ code Firebase) ...
    }

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