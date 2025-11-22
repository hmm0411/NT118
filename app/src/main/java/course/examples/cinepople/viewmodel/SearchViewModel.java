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
        movieRepository.getAllMoviesApi(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    searchResults.postValue(response.body());
                } else {
                    errorMessage.postValue("API Error: Failed to load movies (" + response.message() + ")");
                }
                isLoading.postValue(false);
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Log.e(TAG, "Network Error: ", t);
                errorMessage.postValue("Network connection failed: " + t.getMessage());
                isLoading.postValue(false);
            }
        });
    }
}