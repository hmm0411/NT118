package course.examples.cinepople.ui.search; // Đảm bảo đúng package

//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.inputmethod.EditorInfo;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//
//// Import các lớp cần thiết cho tìm kiếm
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.Query;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import course.examples.cinepople.MovieDetailsActivity;
//import course.examples.cinepople.adapter.MovieSearchAdapter; // TODO: Bạn sẽ cần tạo Adapter này
//import course.examples.cinepople.data.Movie;
//import course.examples.cinepople.databinding.FragmentSearchBinding; // Tự động tạo

public class SearchFragment extends Fragment {

//    private FragmentSearchBinding binding;
//    private FirebaseFirestore db;
//
//    private MovieSearchAdapter searchAdapter; // TODO: Tạo Adapter này
//    private List<Movie> searchResultList = new ArrayList<>();
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        // Inflate layout bằng ViewBinding
//        binding = FragmentSearchBinding.inflate(inflater, container, false);
//        return binding.getRoot();
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        db = FirebaseFirestore.getInstance();
//
//        // 1. Cài đặt RecyclerView
//        setupRecyclerView();
//
//        // 2. Cài đặt trình lắng nghe (listener) cho thanh tìm kiếm
//        setupSearchListener();
//    }
//
//    private void setupRecyclerView() {
//        // Khởi tạo Adapter
//        searchAdapter = new MovieSearchAdapter(getContext(), searchResultList, movie -> {
//            // Xử lý khi click vào một item kết quả
//            Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
//            intent.putExtra(MovieDetailsActivity.MOVIE_ID_KEY, movie.getId());
//            startActivity(intent);
//        });
//
//        binding.recyclerSearchResults.setLayoutManager(new LinearLayoutManager(getContext()));
//        binding.recyclerSearchResults.setAdapter(searchAdapter);
//    }
//
//    private void setupSearchListener() {
//        binding.searchEditText.setOnEditorActionListener((v, actionId, event) -> {
//            // Kiểm tra nếu người dùng nhấn nút "Search" (Tìm kiếm) trên bàn phím
//            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                String query = binding.searchEditText.getText().toString().trim();
//                if (!query.isEmpty()) {
//                    performSearch(query);
//                }
//                return true;
//            }
//            return false;
//        });
//    }
//
//    /**
//     * Thực hiện truy vấn tìm kiếm trên Firestore
//     * @param query Từ khóa tìm kiếm
//     */
//    private void performSearch(String query) {
//        binding.textNoResults.setVisibility(View.GONE); // Ẩn thông báo cũ
//        binding.progressBar.setVisibility(View.VISIBLE); // Hiển thị vòng xoay
//
//        // TODO: Tùy chỉnh logic tìm kiếm này
//        // Logic ví dụ: Tìm phim có 'title' >= query VÀ 'title' <= query + '\uf8ff'
//        // Đây là cách phổ biến để tìm kiếm "bắt đầu bằng" (prefix search) trong Firestore
//        db.collection("movies")
//                .orderBy("title")
//                .startAt(query)
//                .endAt(query + "\uf8ff")
//                .limit(20)
//                .get()
//                .addOnCompleteListener(task -> {
//                    binding.progressBar.setVisibility(View.GONE); // Ẩn vòng xoay
//                    if (task.isSuccessful()) {
//                        searchResultList.clear();
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            Movie movie = document.toObject(Movie.class);
//                            movie.setId(document.getId());
//                            searchResultList.add(movie);
//                        }
//                        searchAdapter.notifyDataSetChanged();
//
//                        // Hiển thị thông báo nếu không tìm thấy kết quả
//                        if (searchResultList.isEmpty()) {
//                            binding.textNoResults.setVisibility(View.VISIBLE);
//                        }
//
//                    } else {
//                        // Xử lý lỗi
//                        binding.textNoResults.setText("Error loading results.");
//                        binding.textNoResults.setVisibility(View.VISIBLE);
//                    }
//                });
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null; // Tránh memory leak
//    }
}