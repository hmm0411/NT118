package course.examples.cinepople.adapter;

import android.content.Context;
import android.text.TextUtils; // SỬA: Thêm import
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView; // SỬA: Thêm import

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.button.MaterialButton; // SỬA: Thêm import

import java.util.List;

import course.examples.cinepople.domain.Movie;
import course.examples.cinepople.R;

public class MovieSearchAdapter extends RecyclerView.Adapter<MovieSearchAdapter.MovieViewHolder> {

    private Context context;
    private List<Movie> movieList;
    private OnMovieClickListener listener;

    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }

    public MovieSearchAdapter(Context context, List<Movie> movieList, OnMovieClickListener listener) {
        this.context = context;
        this.movieList = movieList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Sử dụng layout item_movie_search (bạn cần tạo file này)
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie_search, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.bind(movie, listener);
    }

    @Override
    public int getItemCount() {
        return (movieList != null) ? movieList.size() : 0;
    }

    // --- ViewHolder ---
    public class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPoster;
        TextView tvTitle, tvGenre, tvDescription;
        MaterialButton btnBooking, btnInformation;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            // Tìm các View từ layout item_movie_search.xml
            imgPoster = itemView.findViewById(R.id.img_movie_poster);
            tvTitle = itemView.findViewById(R.id.tv_movie_title);
            tvGenre = itemView.findViewById(R.id.tv_movie_genre);
            tvDescription = itemView.findViewById(R.id.tv_movie_description);
            btnBooking = itemView.findViewById(R.id.btn_booking);
            btnInformation = itemView.findViewById(R.id.btn_information);
        }

        public void bind(final Movie movie, final OnMovieClickListener listener) {
            // 1. Điền dữ liệu
            tvTitle.setText(movie.getTitle());
            tvDescription.setText(movie.getDescription());

            // Nối các thể loại (genres) lại với nhau
            if (movie.getGenres() != null && !movie.getGenres().isEmpty()) {
                tvGenre.setText(TextUtils.join(", ", movie.getGenres()));
            } else {
                tvGenre.setText("N/A");
            }

            // 2. Tải ảnh Poster
            Glide.with(itemView.getContext())
                    .load(movie.getPosterUrl()) // Dùng poster (ảnh dọc)
                    .apply(new RequestOptions().transform(new RoundedCorners(16)))
                    .into(imgPoster);

            // 3. Logic hiển thị nút (Booking vs Information)
            // Giả sử trường 'status' là "now_showing" hoặc "coming_soon"
            if ("now_showing".equals(movie.getStatus())) {
                btnBooking.setVisibility(View.VISIBLE);
                btnInformation.setVisibility(View.GONE);
            } else {
                // Mặc định cho "coming_soon" hoặc các trạng thái khác
                btnBooking.setVisibility(View.GONE);
                btnInformation.setVisibility(View.VISIBLE);
            }

            // 4. Gán sự kiện click (cho cả item và các nút)
            // Fragment của bạn chỉ định nghĩa 1 click duy nhất (để mở Details)
            View.OnClickListener clickListener = v -> listener.onMovieClick(movie);

            itemView.setOnClickListener(clickListener);
            btnBooking.setOnClickListener(clickListener);
            btnInformation.setOnClickListener(clickListener);
        }
    }
}