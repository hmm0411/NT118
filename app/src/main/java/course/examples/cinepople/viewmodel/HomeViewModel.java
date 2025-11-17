package course.examples.cinepople.viewmodel;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;
import course.examples.cinepople.data.repository.MovieRepository;
import course.examples.cinepople.domain.Movie;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {

    private static final String TAG = "HomeViewModel";
    private MovieRepository movieRepository;

    private MutableLiveData<List<Movie>> topMovies = new MutableLiveData<>();
    private MutableLiveData<List<Movie>> nowPlayingMovies = new MutableLiveData<>();
    private MutableLiveData<List<Movie>> comingSoonMovies = new MutableLiveData<>();

    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public HomeViewModel() {
        this.movieRepository = new MovieRepository();
    }

    // --- Getters để Fragment theo dõi ---
    public LiveData<List<Movie>> getTopMovies() { return topMovies; }
    public LiveData<List<Movie>> getNowPlayingMovies() { return nowPlayingMovies; }
    public LiveData<List<Movie>> getComingSoonMovies() { return comingSoonMovies; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<String> getErrorMessage() { return errorMessage; }


    /**
     * Hàm gọi tất cả API cho trang chủ
     */
    public void fetchAllHomeData() {
        isLoading.setValue(true);

        // 1. Tải Top Movies
        movieRepository.getTopMoviesApi(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful()) {
                    topMovies.postValue(response.body());
                } else {
                    errorMessage.postValue("API Error (Top): " + response.message());
                }
                // (Chỉ tắt loading sau khi tất cả API hoàn thành - đây là cách đơn giản)
                // isLoading.setValue(false);
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Log.e(TAG, "onFailure: getTopMoviesApi", t);
                errorMessage.postValue("Network Error (Top): " + t.getMessage());
                // isLoading.setValue(false);
            }
        });

        // 2. Tải Now Playing
        movieRepository.getNowPlayingMoviesApi(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful()) {
                    nowPlayingMovies.postValue(response.body());
                } else {
                    errorMessage.postValue("API Error (Now): " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Log.e(TAG, "onFailure: getNowPlayingMoviesApi", t);
                errorMessage.postValue("Network Error (Now): " + t.getMessage());
            }
        });

        // 3. Tải Coming Soon
        movieRepository.getComingSoonMoviesApi(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful()) {
                    comingSoonMovies.postValue(response.body());
                } else {
                    errorMessage.postValue("API Error (Soon): " + response.message());
                }
                isLoading.setValue(false); // Tắt loading sau khi API cuối cùng hoàn thành
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Log.e(TAG, "onFailure: getComingSoonMoviesApi", t);
                errorMessage.postValue("Network Error (Soon): " + t.getMessage());
                isLoading.setValue(false); // Tắt loading sau khi API cuối cùng hoàn thành (dù lỗi)
            }
        });
    }
}