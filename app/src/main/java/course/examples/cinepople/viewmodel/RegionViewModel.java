package course.examples.cinepople.viewmodel;

import android.util.Log; // 🟢 Import Log

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import course.examples.cinepople.domain.Region;
import course.examples.cinepople.data.repository.RegionRepository;

public class RegionViewModel extends ViewModel {

    // 🟢 Thêm TAG để lọc trong Logcat
    private static final String TAG = "RegionViewModel";

    private final RegionRepository repository;
    private final MutableLiveData<List<Region>> regions;
    private final MutableLiveData<String> errorMessage;

    public RegionViewModel() {
        this.repository = new RegionRepository();
        this.regions = new MutableLiveData<>();
        this.errorMessage = new MutableLiveData<>();
    }

    // Các hàm getter công khai
    public LiveData<List<Region>> getRegions() {
        return regions;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    /**
     * Bắt đầu quá trình tải danh sách Regions.
     */
    public void fetchRegions() {
        Log.d(TAG, "fetchRegions: Hàm được gọi."); // 🟢 Log kiểm tra 1

        // Có thể thêm logic kiểm tra nếu dữ liệu đã tồn tại để tránh gọi API lặp lại
        if (regions.getValue() != null && !regions.getValue().isEmpty()) {
            Log.d(TAG, "fetchRegions: Dữ liệu đã có sẵn (" + regions.getValue().size() + " phần tử). Không gọi lại API."); // 🟢 Log kiểm tra 2
            return;
        }

        Log.d(TAG, "fetchRegions: Đang gọi Repository để lấy dữ liệu mới..."); // 🟢 Log kiểm tra 3

        // Gọi Repository để lấy dữ liệu
        repository.fetchRegions(regions, errorMessage);
    }
}