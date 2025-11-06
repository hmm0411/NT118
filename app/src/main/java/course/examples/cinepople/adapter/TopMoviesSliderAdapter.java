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

public class TopMoviesSliderAdapter extends RecyclerView.Adapter<TopMoviesSliderAdapter.SliderViewHolder> {

    private Context context;
    private List<Movie> movieList;

    public TopMoviesSliderAdapter(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_top_movie_card, parent, false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        Glide.with(context)
                .load(movie.getPosterUrl())
                .apply(new RequestOptions().transform(new RoundedCorners(20))) // Bo góc 20dp
                .into(holder.imgPoster);

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
            imgPoster = itemView.findViewById(R.id.img_poster_card);
        }
    }
}