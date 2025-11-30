package course.examples.cinepople.data.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import course.examples.cinepople.data.remote.api.ApiService;
import course.examples.cinepople.data.remote.api.ApiClient;
import course.examples.cinepople.data.remote.response.ApiResponse;
import course.examples.cinepople.domain.Showtime;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowtimeRepository {

    private static final String TAG = "ShowtimeRepository";
    private final ApiService apiService;

    public ShowtimeRepository() {
        // Khởi tạo ApiService từ RetrofitClient
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    public void fetchShowtimes(String movieId, String regionId, String date,
                               MutableLiveData<List<Showtime>> showtimesLiveData,
                               MutableLiveData<String> errorLiveData) {

        Log.d(TAG, "fetchShowtimes: Bắt đầu gọi Retrofit...");
        Log.d(TAG, "Params: movieId=" + movieId + ", regionId=" + regionId + ", date=" + date);

        apiService.getShowtimes(movieId, regionId, date).enqueue(new Callback<ApiResponse<List<Showtime>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Showtime>>> call, Response<ApiResponse<List<Showtime>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 🟢 Lấy wrapper ra trước
                    ApiResponse<List<Showtime>> apiResponse = response.body();

                    // 🟢 Kiểm tra logic success từ server
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        // Lấy dữ liệu thật sự từ biến .data
                        List<Showtime> result = apiResponse.getData();
                        Log.d(TAG, "onResponse: Thành công! Nhận được " + result.size() + " suất chiếu.");
                        showtimesLiveData.postValue(result);
                    } else {
                        Log.e(TAG, "onResponse: Server trả về lỗi logic - " + apiResponse.getMessage());
                        errorLiveData.postValue(apiResponse.getMessage());
                    }
                } else {
                    Log.e(TAG, "onResponse: Lỗi HTTP " + response.code());
                    errorLiveData.postValue("Lỗi hệ thống: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Showtime>>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                errorLiveData.postValue("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
    public void getShowtimeDetail(String id, MutableLiveData<Showtime> liveData, MutableLiveData<String> errorMsg) {
        apiService.getShowtimeById(id).enqueue(new Callback<ApiResponse<Showtime>>() {
            @Override
            public void onResponse(Call<ApiResponse<Showtime>> call, Response<ApiResponse<Showtime>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    liveData.postValue(response.body().getData());
                } else {
                    errorMsg.postValue("Lỗi tải sơ đồ ghế: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Showtime>> call, Throwable t) {
                errorMsg.postValue("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
}