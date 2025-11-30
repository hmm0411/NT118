package course.examples.cinepople.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;
import course.examples.cinepople.data.repository.MovieRepository;
import course.examples.cinepople.domain.Movie;

public class HomeViewModel extends ViewModel {

    private MovieRepository movieRepository;

    // LiveData chứa danh sách ĐÃ ĐƯỢC LỌC để Fragment quan sát
    private MutableLiveData<List<Movie>> topMovies = new MutableLiveData<>();
    private MutableLiveData<List<Movie>> nowPlayingMovies = new MutableLiveData<>();
    private MutableLiveData<List<Movie>> comingSoonMovies = new MutableLiveData<>();

    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public HomeViewModel() {
        this.movieRepository = new MovieRepository();
    }

    // Getters
    public LiveData<List<Movie>> getTopMovies() { return topMovies; }
    public LiveData<List<Movie>> getNowPlayingMovies() { return nowPlayingMovies; }
    public LiveData<List<Movie>> getComingSoonMovies() { return comingSoonMovies; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<String> getErrorMessage() { return errorMessage; }

    public void fetchAllHomeData() {
        isLoading.setValue(true);

        // 🟢 1. Tạo một LiveData tạm để hứng dữ liệu thô từ Repository
        // Chúng ta override hàm postValue để biết khi nào có dữ liệu trả về
        MutableLiveData<List<Movie>> rawDataReceiver = new MutableLiveData<List<Movie>>() {
            @Override
            public void postValue(List<Movie> allMovies) {
                super.postValue(allMovies);
                // Khi Repository trả dữ liệu về đây -> Tiến hành lọc
                if (allMovies != null) {
                    filterMovies(allMovies);
                }
                isLoading.postValue(false); // Tắt loading
            }
        };

        // 🟢 2. Tạo một LiveData tạm để hứng lỗi
        MutableLiveData<String> errorReceiver = new MutableLiveData<String>() {
            @Override
            public void postValue(String error) {
                super.postValue(error);
                // Truyền lỗi ra ngoài cho Fragment
                errorMessage.postValue(error);
                isLoading.postValue(false); // Tắt loading
            }
        };

        // 🟢 3. Gọi Repository (Không dùng Callback nữa)
        movieRepository.getAllMovies(rawDataReceiver, errorReceiver);
    }

    private void filterMovies(List<Movie> allMovies) {
        List<Movie> topList = new ArrayList<>();
        List<Movie> nowList = new ArrayList<>();
        List<Movie> soonList = new ArrayList<>();

        for (Movie movie : allMovies) {
            // 1. TOP MOVIES (Tối đa 5 phim)
            if (movie.isTopMovie() && topList.size() < 5) {
                topList.add(movie);
            }

            // 2. NOW PLAYING (Tối đa 6 phim)
            if ("now_showing".equals(movie.getStatus()) && nowList.size() < 6) {
                nowList.add(movie);
            }

            // 3. COMING SOON (Tối đa 6 phim)
            if ("coming_soon".equals(movie.getStatus()) && soonList.size() < 6) {
                soonList.add(movie);
            }
        }

        // Đẩy dữ liệu đã lọc về các LiveData chính
        topMovies.postValue(topList);
        nowPlayingMovies.postValue(nowList);
        comingSoonMovies.postValue(soonList);
    }
}