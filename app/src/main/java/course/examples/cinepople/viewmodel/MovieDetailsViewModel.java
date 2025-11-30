package course.examples.cinepople.viewmodel;

import android.util.Log; // Thêm Log để debug

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import course.examples.cinepople.data.repository.MovieRepository;
import course.examples.cinepople.domain.Movie;

public class MovieDetailsViewModel extends ViewModel {

    private static final String TAG = "MovieDetailsViewModel"; // Tag log
    private MovieRepository movieRepository;

    private MutableLiveData<Movie> movieDetails = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public MovieDetailsViewModel() {
        this.movieRepository = new MovieRepository();
    }

    public LiveData<Movie> getMovieDetails() { return movieDetails; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<String> getErrorMessage() { return errorMessage; }

    public void fetchMovieDetail(String movieId) {
        isLoading.setValue(true);
        Log.d(TAG, "fetchMovieDetail: Bắt đầu tải phim ID: " + movieId);

        // 🟢 1. Tạo LiveData Proxy cho Thành công (Hứng cả setValue và postValue)
        MutableLiveData<Movie> successReceiver = new MutableLiveData<Movie>() {
            @Override
            public void postValue(Movie value) {
                Log.d(TAG, "Nhận dữ liệu (Background Thread): " + (value != null ? value.getTitle() : "null"));
                movieDetails.postValue(value); // Đẩy sang LiveData chính
                isLoading.postValue(false);    // Tắt loading
            }

            @Override
            public void setValue(Movie value) {
                Log.d(TAG, "Nhận dữ liệu (Main Thread): " + (value != null ? value.getTitle() : "null"));
                movieDetails.setValue(value);  // Đẩy sang LiveData chính
                isLoading.setValue(false);     // Tắt loading
            }
        };

        // 🟢 2. Tạo LiveData Proxy cho Lỗi (Hứng cả setValue và postValue)
        MutableLiveData<String> errorReceiver = new MutableLiveData<String>() {
            @Override
            public void postValue(String value) {
                Log.e(TAG, "Nhận lỗi (Background Thread): " + value);
                errorMessage.postValue(value);
                isLoading.postValue(false);
            }

            @Override
            public void setValue(String value) {
                Log.e(TAG, "Nhận lỗi (Main Thread): " + value);
                errorMessage.setValue(value);
                isLoading.setValue(false);
            }
        };

        // 🟢 3. Gọi Repository
        movieRepository.getMovieDetail(movieId, successReceiver, errorReceiver);
    }
}