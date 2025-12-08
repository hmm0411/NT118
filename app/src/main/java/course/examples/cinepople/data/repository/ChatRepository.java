package course.examples.cinepople.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import course.examples.cinepople.data.remote.api.ApiClient;
import course.examples.cinepople.data.remote.api.ApiService;
// Correct the import paths for ChatRequest and ChatResponse
import course.examples.cinepople.data.remote.request.ChatRequest;
import course.examples.cinepople.data.remote.response.ChatResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatRepository {
    private ApiService apiService;
    private static final String TAG = "ChatRepository";

    public ChatRepository() {
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    public LiveData<ChatResponse> getBotReply(String message) {
        final MutableLiveData<ChatResponse> data = new MutableLiveData<>();
        ChatRequest request = new ChatRequest(message);
        Log.d(TAG, "Đang gửi tin nhắn tới API: " + message);
        apiService.sendMessageToBot(request).enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                if (response.isSuccessful()) {
                    ChatResponse chatResponse = response.body();
                    if (chatResponse != null && chatResponse.getResponse() != null) {
                        Log.d(TAG, "API trả lời thành công: " + chatResponse.getResponse());
                        data.setValue(chatResponse);
                    } else {
                        // PHÁT HIỆN LỖI: Server trả lời mã 200 OK nhưng body là null hoặc nội dung null
                        Log.e(TAG, "LỖI: Phản hồi thành công nhưng body hoặc nội dung reply là NULL");
                        data.setValue(null);
                    }
                } else {
                    // PHÁT HIỆN LỖI: Server trả lời lỗi (mã 4xx, 5xx)
                    // Log.e là để in ra lỗi (error log), nó sẽ có màu đỏ trong Logcat
                    Log.e(TAG, "LỖI: API trả lời không thành công. Mã lỗi: " + response.code());
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                data.setValue(null); // Xử lý error
            }
        });
        return data;
    }
}
