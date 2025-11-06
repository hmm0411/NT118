package course.examples.cinepople.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;
import course.examples.cinepople.data.Movie;
import course.examples.cinepople.R;

public class MoviePosterAdapter extends RecyclerView.Adapter<MoviePosterAdapter.PosterViewHolder> {

    private Context context;
    private List<Movie> movieList;
    public MoviePosterAdapter(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public PosterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie_poster, parent, false);
        return new PosterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PosterViewHolder holder, int position) {

        Movie movie = movieList.get(position);

        Glide.with(context)
                .load(movie.getPosterUrl())
                .into(holder.imgPoster);

        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(context, "Đang mở: " + movie.getTitle(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class PosterViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPoster;

        public PosterViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.img_poster_small);
        }
    }
}