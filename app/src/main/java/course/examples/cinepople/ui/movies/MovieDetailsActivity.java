package course.examples.cinepople.ui.movies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import course.examples.cinepople.ui.session.SelectSessionActivity;
import course.examples.cinepople.adapter.ActorAdapter;
import course.examples.cinepople.databinding.ActivityMovieDetailsBinding;
import course.examples.cinepople.data.Actor;

import java.util.List;
import java.util.Locale;

public class MovieDetailsActivity extends AppCompatActivity {

    private static final String TAG = "MovieDetailsActivity";
    public static final String MOVIE_ID_KEY = "movie_id";
    public static final String MOVIE_TITLE_KEY = "movie_title";
    private ActivityMovieDetailsBinding binding;
    private FirebaseFirestore db;

    private ActorAdapter actorAdapter;
    private String currentMovieTitle;

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
        setupActorRecyclerView();

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

    private void loadMovieData() {
        DocumentReference movieRef = db.collection("movies").document(movieId);
        movieRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    populateUi(document);
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

    private void populateUi(DocumentSnapshot doc) {
        currentMovieTitle = doc.getString("title");
        binding.toolbar.setTitle(doc.getString("title"));
        Glide.with(this).load(doc.getString("posterUrl")).into(binding.imagePoster);
        Glide.with(this).load(doc.getString("bannerImageUrl")).into(binding.imageBanner);
        binding.textMovieTitle.setText(doc.getString("title"));
        List<String> genres = (List<String>) doc.get("genres");
        if (genres != null && !genres.isEmpty()) {
            binding.textGenre.setText(String.join(", ", genres));
        }
        binding.textAgeRating.setText(doc.getString("ageRating"));
        binding.textAgeRatingDesc.setText(doc.getString("ageRatingDesc"));
        Double rating = doc.getDouble("imdbRating");
        if (rating != null) {
            binding.textImdbRating.setText(String.format(Locale.US, "%.1f", rating));
        }
        binding.textReleaseDateValue.setText(doc.getString("releaseDate"));
        binding.textDurationValue.setText(doc.getString("duration"));
        binding.textLanguageValue.setText(doc.getString("language"));
        binding.textDescriptionBody.setText(doc.getString("description"));
        trailerUrl = doc.getString("trailerVideoUrl");
        binding.imagePlayButton.setVisibility(trailerUrl != null && !trailerUrl.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void setupActorRecyclerView() {
        Query query = db.collection("movies").document(movieId)
                .collection("actors")
                .orderBy("name");

        FirestoreRecyclerOptions<Actor> options = new FirestoreRecyclerOptions.Builder<Actor>()
                .setQuery(query, Actor.class)
                .build();

        actorAdapter = new ActorAdapter(options);

        binding.recyclerActors.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerActors.setAdapter(actorAdapter);
    }

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

    private void onSelectSessionClicked() {
        Intent intent = new Intent(MovieDetailsActivity.this, SelectSessionActivity.class);
        intent.putExtra(MOVIE_ID_KEY, movieId);
        intent.putExtra(MOVIE_TITLE_KEY, currentMovieTitle);
        startActivity(intent);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (actorAdapter != null) {
            actorAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (actorAdapter != null) {
            actorAdapter.stopListening();
        }
    }
}