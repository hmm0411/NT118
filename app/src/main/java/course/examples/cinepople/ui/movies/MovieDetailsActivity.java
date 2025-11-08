package course.examples.cinepople.ui.movies; // Thay đổi package của bạn

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import course.examples.cinepople.ui.session.SelectSessionActivity; // <-- Thêm import
//import course.examples.cinepople.adapter.ActorAdapter;
import course.examples.cinepople.databinding.ActivityMovieDetailsBinding;
import course.examples.cinepople.data.Actor;

import java.util.List;
import java.util.Locale;

public class MovieDetailsActivity extends AppCompatActivity {

    private static final String TAG = "MovieDetailsActivity";
    public static final String MOVIE_ID_KEY = "movie_id"; // Key để nhận ID phim từ Intent

    private ActivityMovieDetailsBinding binding; // View Binding
    private FirebaseFirestore db;

    // Sửa: Dùng Adapter tiêu chuẩn
    //private ActorAdapter actorAdapter;
    private List<Actor> actorList; // <-- Thêm: Danh sách chứa diễn viên

    private String movieId;
    private String trailerUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();

        movieId = getIntent().getStringExtra(MOVIE_ID_KEY);
        if (movieId == null || movieId.isEmpty()) {
            Log.e(TAG, "Movie ID not found in Intent");
            Toast.makeText(this, "Error: Movie not found", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        setupToolbar();
        loadMovieData();
        //setupActorRecyclerView(); // Cài đặt RecyclerView diễn viên

        binding.imagePlayButton.setOnClickListener(v -> onPlayTrailerClicked());
        binding.buttonSelectSession.setOnClickListener(v -> onSelectSessionClicked());
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    /**
     * Tải dữ liệu chính của phim từ document
     */
    private void loadMovieData() {
        DocumentReference movieRef = db.collection("movies").document(movieId);

        movieRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    populateUi(document); // Điền thông tin lên giao diện
                } else {
                    Log.d(TAG, "No such document");
                    Toast.makeText(this, "Movie details not found.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
                Toast.makeText(this, "Failed to load movie.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Điền dữ liệu từ DocumentSnapshot lên các View
     */
    private void populateUi(DocumentSnapshot doc) {
        binding.toolbar.setTitle(doc.getString("title"));

        // Tải ảnh poster
        Glide.with(this).load(doc.getString("posterUrl")).into(binding.imagePoster);

        // --- SỬA: Tải ẢNH banner ---
        Glide.with(this).load(doc.getString("bannerImageUrl")).into(binding.imageBanner);

        // Thông tin cơ bản
        binding.textMovieTitle.setText(doc.getString("title"));
        List<String> genres = (List<String>) doc.get("genres");
        if (genres != null && !genres.isEmpty()) {
            binding.textGenre.setText(String.join(", ", genres));
        }

        // Rating
        binding.textAgeRating.setText(doc.getString("ageRating"));
        binding.textAgeRatingDesc.setText(doc.getString("ageRatingDesc"));
        Double rating = doc.getDouble("imdbRating");
        if (rating != null) {
            binding.textImdbRating.setText(String.format(Locale.US, "%.1f", rating));
        }

//        // Chi tiết
//        binding.textReleaseDateValue.setText(doc.getString("releaseDate"));
//        binding.textDurationValue.setText(doc.getString("duration"));
//        binding.textLanguageValue.setText(doc.getString("language"));
//        binding.textDescriptionBody.setText(doc.getString("description"));

        // --- SỬA: Lấy link VIDEO trailer ---
        trailerUrl = doc.getString("trailerVideoUrl");
        binding.imagePlayButton.setVisibility(trailerUrl != null && !trailerUrl.isEmpty() ? View.VISIBLE : View.GONE);
    }

    /**
     * Cài đặt RecyclerView cho diễn viên (dùng Adapter tiêu chuẩn)
     */
//    private void setupActorRecyclerView() {
//        // 1. Khởi tạo danh sách và Adapter
//        actorList = new ArrayList<>();
//        // (Giả sử ActorAdapter của bạn có constructor là (Context, List<Actor>)
//        // giống như MoviePosterAdapter)
//        actorAdapter = new ActorAdapter(this, actorList);
//
//        // 2. Cài đặt LayoutManager và Adapter cho RecyclerView
//        binding.recyclerActors.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        binding.recyclerActors.setAdapter(actorAdapter);
//
//        // 3. Truy vấn dữ liệu từ subcollection 'actors'
//        db.collection("movies").document(movieId)
//                .collection("actors")
//                .orderBy("name")
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful() && task.getResult() != null) {
//                        actorList.clear(); // Xóa dữ liệu cũ
//                        // Lặp qua kết quả và thêm vào danh sách
//                        for (DocumentSnapshot doc : task.getResult()) {
//                            Actor actor = doc.toObject(Actor.class);
//                            if (actor != null) {
//                                actorList.add(actor);
//                            }
//                        }
//                        // Báo cho adapter biết dữ liệu đã thay đổi
//                        actorAdapter.notifyDataSetChanged();
//                    } else {
//                        Log.w(TAG, "Error getting actors list.", task.getException());
//                    }
//                });
//    }

    /**
     * Xử lý khi nhấn nút Play
     */
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

    /**
     * Xử lý khi nhấn nút "Select session"
     */
    private void onSelectSessionClicked() {
        // --- SỬA: Chuyển sang SelectSessionActivity ---
        Intent intent = new Intent(MovieDetailsActivity.this, SelectSessionActivity.class);
        intent.putExtra(MOVIE_ID_KEY, movieId); // Gửi movieId sang Activity tiếp theo
        startActivity(intent);
    }

    // --- XÓA: onStart() và onStop() ---
    // (Không cần thiết khi dùng Adapter tiêu chuẩn)
}