package course.examples.cinepople.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;
import course.examples.cinepople.data.repository.MovieRepository;
import course.examples.cinepople.domain.Movie;

public class SearchViewModel extends ViewModel {

    private static final String TAG = "SearchViewModel";
    private MovieRepository movieRepository;

    private MutableLiveData<List<Movie>> searchResults = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public SearchViewModel() {
        this.movieRepository = new MovieRepository();
    }

    public LiveData<List<Movie>> getSearchResults() { return searchResults; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<String> getErrorMessage() { return errorMessage; }

    public void fetchAllMovies() {
        isLoading.setValue(true);

        // 🟢 1. Tạo Receiver hứng dữ liệu thành công
        MutableLiveData<List<Movie>> dataReceiver = new MutableLiveData<List<Movie>>() {
            @Override
            public void postValue(List<Movie> movies) {
                super.postValue(movies);
                if (movies != null) {
                    searchResults.postValue(movies);
                }
                isLoading.postValue(false); // Tắt loading khi có dữ liệu
            }
        };

        // 🟢 2. Tạo Receiver hứng lỗi
        MutableLiveData<String> errorReceiver = new MutableLiveData<String>() {
            @Override
            public void postValue(String error) {
                super.postValue(error);
                errorMessage.postValue(error);
                isLoading.postValue(false); // Tắt loading khi có lỗi
            }
        };

        // 🟢 3. Gọi Repository (Sử dụng hàm mới getAllMovies không có hậu tố Api)
        movieRepository.getAllMovies(dataReceiver, errorReceiver);
    }
}