package course.examples.cinepople.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import course.examples.cinepople.data.repository.MovieRepository;
import course.examples.cinepople.domain.Movie;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsViewModel extends ViewModel {

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
        movieRepository.getMovieDetailApi(movieId, new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    movieDetails.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to load movie details");
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.postValue(t.getMessage());
            }
        });
    }
}