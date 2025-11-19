package course.examples.cinepople.data.remote;

import java.util.List;
import course.examples.cinepople.domain.Movie;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Path;

public interface ApiService {

    @GET("movies")
    Call<List<Movie>> getAllMovies();
    @GET("movies/{id}")
    Call<Movie> getMovieById(@Path("id") String id);
}