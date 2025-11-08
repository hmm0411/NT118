package course.examples.cinepople.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView; // <<< SỬA: DÙNG RecyclerView.Adapter

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import course.examples.cinepople.data.Movie;
import course.examples.cinepople.R;

// <<< SỬA: ViewPager2 dùng RecyclerView.Adapter
public class TopMoviesSliderAdapter extends RecyclerView.Adapter<TopMoviesSliderAdapter.SliderViewHolder> {

    private Context context;
    private List<Movie> movieList;
    private OnMovieClickListener listener; // <-- 1. Thêm biến listener

    // --- 2. Định nghĩa Interface ---
    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }
    // ---

    // --- 3. Cập nhật Constructor ---
    public TopMoviesSliderAdapter(Context context, List<Movie> movieList, OnMovieClickListener listener) {
        this.context = context;
        this.movieList = movieList;
        this.listener = listener; // Gán listener
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Giả sử layout item của bạn là item_top_movie_card.xml
        View view = LayoutInflater.from(context).inflate(R.layout.item_top_movie_card, parent, false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        Glide.with(context)
                .load(movie.getPosterUrl())
                // Giả sử bo góc 20
                .apply(new RequestOptions().transform(new RoundedCorners(20)))
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

    public static class SliderViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPoster;
        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            // Giả sử ID của poster trong item_top_movie_card.xml là img_poster_card
            imgPoster = itemView.findViewById(R.id.img_poster_card);
        }
    }
}