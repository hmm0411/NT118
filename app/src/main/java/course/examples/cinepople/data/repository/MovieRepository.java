package course.examples.cinepople.data.repository;

import java.util.List;

import course.examples.cinepople.data.remote.ApiService;
import course.examples.cinepople.domain.Movie;
import retrofit2.Call;
import retrofit2.Callback;

public class MovieRepository {

    private ApiService apiService;

    public MovieRepository() {
        this.apiService = ApiClient.getClient().create(ApiService.class);
    }

    // (Các hàm cũ như searchMoviesApi...)

    // --- CÁC HÀM MỚI ---

    public void getTopMoviesApi(Callback<List<Movie>> callback) {
        apiService.getTopMovies().enqueue(callback);
    }

    public void getNowPlayingMoviesApi(Callback<List<Movie>> callback) {
        apiService.getNowPlayingMovies().enqueue(callback);
    }

    public void getComingSoonMoviesApi(Callback<List<Movie>> callback) {
        apiService.getComingSoonMovies().enqueue(callback);
    }
}