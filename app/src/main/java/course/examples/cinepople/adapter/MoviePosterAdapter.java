package course.examples.cinepople.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import course.examples.cinepople.data.Movie;
import course.examples.cinepople.R;

public class MoviePosterAdapter extends RecyclerView.Adapter<MoviePosterAdapter.PosterViewHolder> {

    private Context context;
    private List<Movie> movieList;
    private OnMovieClickListener listener; // <-- 1. Thêm biến listener

    // --- 2. Định nghĩa Interface ---
    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }
    // ---

    // --- 3. Cập nhật Constructor ---
    public MoviePosterAdapter(Context context, List<Movie> movieList, OnMovieClickListener listener) {
        this.context = context;
        this.movieList = movieList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PosterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Giả sử layout item của bạn là item_movie_poster.xml
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie_poster, parent, false);
        return new PosterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PosterViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        Glide.with(context)
                .load(movie.getPosterUrl())
                // Giả sử bo góc 16
                .apply(new RequestOptions().transform(new RoundedCorners(16)))
                .into(holder.imgPoster);

        // --- 4. Gán sự kiện click ---
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMovieClick(movie);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (movieList != null) {
            return movieList.size();
        }
        return 0;
    }

    public static class PosterViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPoster;
        public PosterViewHolder(@NonNull View itemView) {
            super(itemView);
            // Giả sử ID của poster trong item_movie_poster.xml là img_poster
            imgPoster = itemView.findViewById(R.id.img_poster);
        }
    }
}