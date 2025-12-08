package course.examples.cinepople.viewmodel;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import course.examples.cinepople.data.remote.response.ChatResponse;
import course.examples.cinepople.data.repository.ChatRepository;

public class ChatViewModel extends ViewModel {
    private ChatRepository chatRepository;
    private static final String TAG = "ChatRepository";
    public ChatViewModel() {
        chatRepository = new ChatRepository();
    }

    public LiveData<ChatResponse> sendMessage(String message) {
        return chatRepository.getBotReply(message);
    }
}
