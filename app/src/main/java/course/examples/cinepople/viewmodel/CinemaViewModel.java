package course.examples.cinepople.viewmodel;

import android.util.Log; // 🟢 Import Log

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;

import course.examples.cinepople.domain.Cinema;
import course.examples.cinepople.repository.CinemaRepository;

public class CinemaViewModel extends ViewModel {

    // 🟢 Định nghĩa TAG để dễ dàng tìm kiếm trong Logcat
    private static final String TAG = "CinemaViewModel";

    private final CinemaRepository repository;
    private final MutableLiveData<List<Cinema>> cinemas;
    private final MutableLiveData<String> errorMessage;

    public CinemaViewModel() {
        this.repository = new CinemaRepository();
        this.cinemas = new MutableLiveData<>();
        this.errorMessage = new MutableLiveData<>();
        Log.d(TAG, "Constructor: CinemaViewModel đã được khởi tạo."); // 🟢 Log khởi tạo
    }

    // ------------------- LIVE DATA GETTERS -------------------

    public LiveData<List<Cinema>> getCinemas() {
        return cinemas;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    // ------------------- DATA LOADING METHODS -------------------

    /**
     * Bắt đầu quá trình tải danh sách TẤT CẢ các rạp chiếu phim từ Repository.
     */
    public void fetchAllCinemas() {
        Log.d(TAG, "fetchAllCinemas: Hàm được gọi."); // 🟢 Log bắt đầu hàm

        // Có thể thêm logic kiểm tra nếu dữ liệu đã tồn tại để tránh gọi API lặp lại
        if (cinemas.getValue() != null && !cinemas.getValue().isEmpty()) {
            Log.d(TAG, "fetchAllCinemas: Dữ liệu đã có sẵn (" + cinemas.getValue().size() + " rạp). Bỏ qua gọi API."); // 🟢 Log check cache
            return;
        }

        Log.d(TAG, "fetchAllCinemas: Đang gọi Repository để tải danh sách rạp..."); // 🟢 Log gọi Repo
        // Gọi Repository để lấy dữ liệu
        repository.fetchAllCinemas(cinemas, errorMessage);
    }

    /**
     * Tải danh sách các rạp chiếu phim dựa trên danh sách các ID cụ thể.
     * @param cinemaIds Danh sách ID của các rạp cần tải.
     */
    public void fetchCinemasByIds(List<String> cinemaIds) {
        if (cinemaIds == null || cinemaIds.isEmpty()) {
            Log.w(TAG, "fetchCinemasByIds: Danh sách ID rạp bị Null hoặc Rỗng."); // 🟢 Log cảnh báo
            return;
        }

        Log.d(TAG, "fetchCinemasByIds: Đang tải thông tin cho " + cinemaIds.size() + " rạp."); // 🟢 Log số lượng cần tải

        repository.fetchCinemasByIds(cinemaIds, cinemas, errorMessage);
    }
}