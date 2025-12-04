package course.examples.cinepople.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import course.examples.cinepople.domain.Booking;
import course.examples.cinepople.data.repository.BookingRepository;

public class BookingViewModel extends ViewModel {

    private static final String TAG = "BookingViewModel";

    private final BookingRepository repository;

    // LiveData chứa kết quả đặt vé thành công
    private final MutableLiveData<Booking> bookingResult = new MutableLiveData<>();

    private final MutableLiveData<List<Booking>> myBookings = new MutableLiveData<>();


    // LiveData trạng thái loading
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    // LiveData thông báo lỗi
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public BookingViewModel() {
        this.repository = new BookingRepository();
    }

    // --- Getters ---
    public LiveData<Booking> getBookingResult() {
        return bookingResult;
    }

    public LiveData<List<Booking>> getMyBookings() {
        return myBookings;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    /**
     * Gọi API tạo Booking (Giữ ghế)
     *
     * @param showtimeId ID suất chiếu
     * @param seats      Danh sách mã ghế (A1, A2...)
     */
    public void createBooking(String showtimeId, List<String> seats) {
        isLoading.setValue(true);
        Log.d(TAG, "createBooking: Bắt đầu giữ ghế cho suất " + showtimeId);

        // 🟢 1. Proxy hứng dữ liệu THÀNH CÔNG
        MutableLiveData<Booking> successProxy = new MutableLiveData<Booking>() {
            @Override
            public void postValue(Booking value) {
                super.postValue(value);
                Log.d(TAG, "Success (Background): " + value.getId());
                bookingResult.postValue(value); // Đẩy dữ liệu ra UI
                isLoading.postValue(false);     // Tắt loading
            }

            @Override
            public void setValue(Booking value) {
                super.setValue(value);
                Log.d(TAG, "Success (Main): " + value.getId());
                bookingResult.setValue(value);
                isLoading.setValue(false);
            }
        };

        // 🟢 2. Proxy hứng dữ liệu LỖI
        MutableLiveData<String> errorProxy = new MutableLiveData<String>() {
            @Override
            public void postValue(String value) {
                super.postValue(value);
                Log.e(TAG, "Error (Background): " + value);
                errorMessage.postValue(value);
                isLoading.postValue(false);
            }

            @Override
            public void setValue(String value) {
                super.setValue(value);
                Log.e(TAG, "Error (Main): " + value);
                errorMessage.setValue(value);
                isLoading.setValue(false);
            }
        };

        // 🟢 3. Gọi Repository
        repository.createBooking(showtimeId, seats, successProxy, errorProxy);
    }

    public void loadMyBookings(String token) {

        MutableLiveData<List<Booking>> successProxy = new MutableLiveData<>() {
            @Override
            public void postValue(List<Booking> bookings) {
                super.postValue(bookings);
                myBookings.postValue(bookings);
            }
        };

        MutableLiveData<String> errorProxy = new MutableLiveData<>() {
            @Override
            public void postValue(String msg) {
                super.postValue(msg);
                errorMessage.postValue(msg);
            }
        };
        repository.getMyBookings(token, successProxy, errorProxy);
    }
}