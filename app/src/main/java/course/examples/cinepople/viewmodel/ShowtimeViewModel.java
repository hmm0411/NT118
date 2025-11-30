// package course.examples.cinepople.viewmodel.ShowtimeViewModel.java

package course.examples.cinepople.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;

import course.examples.cinepople.domain.Showtime;
import course.examples.cinepople.data.repository.ShowtimeRepository;

public class ShowtimeViewModel extends ViewModel {
    private final ShowtimeRepository repository;
    private final MutableLiveData<List<Showtime>> showtimes = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public ShowtimeViewModel() {
        this.repository = new ShowtimeRepository();
    }

    public LiveData<List<Showtime>> getShowtimes() {
        return showtimes;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    // 🟢 HÀM CHÍNH GỌI API SHOWTIMES
    public void fetchShowtimes(String movieId, String regionId, String date) {
        // Bạn có thể thêm logic kiểm tra isLoading ở đây nếu cần

        repository.fetchShowtimes(movieId, regionId, date, showtimes, errorMessage);
    }
}