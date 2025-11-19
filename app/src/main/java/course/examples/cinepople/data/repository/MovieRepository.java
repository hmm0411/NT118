package course.examples.cinepople.data.repository;

import java.util.List;

import course.examples.cinepople.data.remote.ApiClient;
import course.examples.cinepople.data.remote.ApiService;
import course.examples.cinepople.domain.Movie;
import retrofit2.Call;
import retrofit2.Callback;

public class MovieRepository {

    private ApiService apiService;

    public MovieRepository() {
        this.apiService = ApiClient.getClient().create(ApiService.class);
    }
    public void getAllMoviesApi(Callback<List<Movie>> callback) {
        apiService.getAllMovies().enqueue(callback);
    }
    public void getMovieDetailApi(String movieId, Callback<Movie> callback) {
        apiService.getMovieById(movieId).enqueue(callback);
    }
}