package course.examples.cinepople.data.repository;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import course.examples.cinepople.data.remote.api.ApiClient;
import course.examples.cinepople.data.remote.api.ApiService;
import course.examples.cinepople.data.remote.request.BookingRequest;
import course.examples.cinepople.data.remote.response.ApiResponse;
import course.examples.cinepople.domain.Booking;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingRepository {
    private ApiService apiService;

    public BookingRepository() {
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    public void createBooking(String showtimeId, List<String> seats,
                              MutableLiveData<Booking> bookingData,
                              MutableLiveData<String> errorData) {

        BookingRequest request = new BookingRequest(showtimeId, seats);

        apiService.createBooking(request).enqueue(new Callback<ApiResponse<Booking>>() {
            @Override
            public void onResponse(Call<ApiResponse<Booking>> call, Response<ApiResponse<Booking>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        bookingData.postValue(response.body().getData());
                    } else {
                        errorData.postValue(response.body().getMessage());
                    }
                } else {
                    errorData.postValue("Lỗi đặt vé: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Booking>> call, Throwable t) {
                errorData.postValue("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
}