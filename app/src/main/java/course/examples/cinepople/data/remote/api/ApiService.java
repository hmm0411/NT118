package course.examples.cinepople.data.remote.api;

import java.util.List;

import course.examples.cinepople.data.remote.request.BookingRequest;
import course.examples.cinepople.data.remote.request.ChatRequest;
import course.examples.cinepople.data.remote.response.BookingResponse;
import course.examples.cinepople.data.remote.response.ChatResponse;
import course.examples.cinepople.domain.Booking;
import course.examples.cinepople.domain.Movie;
import course.examples.cinepople.domain.Showtime;
import course.examples.cinepople.domain.Region;
import course.examples.cinepople.data.remote.response.ApiResponse; // 🟢 Import ApiResponse

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Path;

public interface ApiService {

    // 🟢 SỬA LẠI: Trả về List trực tiếp (vì Server trả vềArray)
    @GET("api/movies")
    Call<List<Movie>> getAllMovies();

    // Các hàm khác giữ nguyên nếu chúng trả về object bọc
    @GET("api/movies/{id}")
    Call<Movie> getMovieById(@Path("id") String id);

    // ------------------- SHOWTIMES ENDPOINT -------------------
    // 🟢 SỬA LẠI: Trả về ApiResponse chứa List<Showtime>
    @GET("api/showtimes")
    Call<ApiResponse<List<Showtime>>> getShowtimes(
            @Query("movieId") String movieId,
            @Query("regionId") String regionId,
            @Query("date") String date
    );

    @GET("api/showtimes/{id}")
    Call<ApiResponse<Showtime>> getShowtimeById(@Path("id") String id);

    // ------------------- REGION ENDPOINT -------------------
    // 🟢 SỬA LẠI: Trả về ApiResponse chứa List<Region>
    @GET("api/regions")
    Call<ApiResponse<List<Region>>> getAllRegions();

    @POST("api/booking")
    Call<ApiResponse<Booking>> createBooking(@Body BookingRequest request);

    @GET("api/booking")
    Call<BookingResponse> getMyBookings(@Header("Authorization") String token);

    /**
     * Sends a message to the chatbot and gets a reply.
     */
    @POST("api/chatbot") // Matches your backend router path
    Call<ChatResponse> sendMessageToBot(@Body ChatRequest request);
}