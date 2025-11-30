package course.examples.cinepople.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import course.examples.cinepople.data.repository.MovieRepository;
import course.examples.cinepople.domain.Movie;

public class ReviewSummaryViewModel extends ViewModel {

    private static final String TAG = "ReviewSummaryViewModel";
    private final MovieRepository repository;

    // LiveData chứa thông tin phim (để hiển thị Poster, Genre...)
    private final MutableLiveData<Movie> movieData = new MutableLiveData<>();

    // LiveData trạng thái loading
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    // LiveData lỗi
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public ReviewSummaryViewModel() {
        this.repository = new MovieRepository();
    }

    // --- Getters ---
    public LiveData<Movie> getMovieData() {
        return movieData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void loadMovieInfo(String movieId) {
        if (movieId == null || movieId.isEmpty()) {
            Log.e(TAG, "loadMovieInfo: MovieID is null/empty");
            return;
        }

        isLoading.setValue(true);
        Log.d(TAG, "loadMovieInfo: Bắt đầu tải thông tin phim ID: " + movieId);

        MutableLiveData<Movie> successProxy = new MutableLiveData<Movie>() {
            @Override
            public void postValue(Movie value) {
                super.postValue(value);
                movieData.postValue(value); // Đẩy dữ liệu ra UI
                isLoading.postValue(false); // Tắt loading
            }

            @Override
            public void setValue(Movie value) {
                super.setValue(value);
                movieData.setValue(value);
                isLoading.setValue(false);
            }
        };

        // 🟢 2. Tạo Proxy hứng dữ liệu LỖI
        MutableLiveData<String> errorProxy = new MutableLiveData<String>() {
            @Override
            public void postValue(String value) {
                super.postValue(value);
                errorMessage.postValue(value);
                isLoading.postValue(false);
            }

            @Override
            public void setValue(String value) {
                super.setValue(value);
                errorMessage.setValue(value);
                isLoading.setValue(false);
            }
        };

        // 🟢 3. Gọi Repository
        repository.getMovieDetail(movieId, successProxy, errorProxy);
    }
}