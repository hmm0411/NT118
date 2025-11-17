package course.examples.cinepople.data.remote;

import java.util.List;
import course.examples.cinepople.domain.LoginRequest;
import course.examples.cinepople.domain.LoginResponse;
import course.examples.cinepople.domain.Movie;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("movies")
    Call<List<Movie>> getHomeMovies();

    @GET("movies/search")
    Call<List<Movie>> searchMovies(@Query("q") String query);
}