package course.examples.cinepople.data.repository;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import java.util.List;

import course.examples.cinepople.data.remote.api.ApiClient;
import course.examples.cinepople.data.remote.api.ApiService;
import course.examples.cinepople.domain.Movie;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {

    private static final String TAG = "MovieRepository";
    private ApiService apiService;

    public MovieRepository() {
        // Khởi tạo Retrofit Service
        this.apiService = ApiClient.getClient().create(ApiService.class);
    }

    // ----------------------------------------------------------------
    // 1. LẤY DANH SÁCH PHIM (API trả về mảng JSON trực tiếp)
    // ----------------------------------------------------------------
    public void getAllMovies(MutableLiveData<List<Movie>> moviesLiveData, MutableLiveData<String> errorLiveData) {

        apiService.getAllMovies().enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> movies = response.body();
                    moviesLiveData.postValue(movies);
                } else {
                    Log.e(TAG, "getAllMovies Error: " + response.code());
                    errorLiveData.postValue("Lỗi tải phim: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Log.e(TAG, "getAllMovies Failure: " + t.getMessage());
                errorLiveData.postValue("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    // ----------------------------------------------------------------
    // 2. LẤY CHI TIẾT PHIM (API trả về object JSON trực tiếp)
    // ----------------------------------------------------------------
    public void getMovieDetail(String movieId, MutableLiveData<Movie> movieLiveData, MutableLiveData<String> errorLiveData) {

        // Gọi API trả về trực tiếp đối tượng Movie (không qua ApiResponse)
        apiService.getMovieById(movieId).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                Log.d(TAG, "getMovieDetail Code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    Movie movie = response.body();
                    Log.d(TAG, "Thành công! Tên phim: " + movie.getTitle());
                    movieLiveData.postValue(movie);
                } else {
                    String msg = "Lỗi tải chi tiết: " + response.message() + " (Code: " + response.code() + ")";
                    Log.e(TAG, msg);
                    errorLiveData.postValue(msg);
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.e(TAG, "getMovieDetail Failure: " + t.getMessage());
                errorLiveData.postValue("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
}