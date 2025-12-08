package course.examples.cinepople.fragment.chat;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import course.examples.cinepople.R;
import course.examples.cinepople.adapter.ChatAdapter;
import course.examples.cinepople.model.ChatMessage;
import course.examples.cinepople.viewmodel.ChatViewModel;

public class ChatFragment extends DialogFragment {

    private ChatViewModel chatViewModel;
    private RecyclerView recyclerViewChat;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> messageList;
    private EditText etMessageInput;
    private ImageButton btnSendMessage;

    private static final String TAG = "ChatFragment";

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                window.setBackgroundDrawableResource(android.R.color.transparent);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ánh xạ các view từ layout
        recyclerViewChat = view.findViewById(R.id.recycler_view_chat);
        etMessageInput = view.findViewById(R.id.et_message_input);
        btnSendMessage = view.findViewById(R.id.btn_send_message);

        // Khởi tạo ViewModel
        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);

        // Khởi tạo danh sách tin nhắn và Adapter
        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageList);

        // Cấu hình RecyclerView
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewChat.setAdapter(chatAdapter);

        // Thêm tin nhắn chào mừng ban đầu
        addMessageToList(new ChatMessage("Chào bạn, tôi là trợ lý ảo CinePople. Tôi có thể giúp gì cho bạn?", ChatMessage.Type.RECEIVED));

        // Xử lý sự kiện gửi tin nhắn
        btnSendMessage.setOnClickListener(v -> {
            String messageText = etMessageInput.getText().toString().trim();
            if (!messageText.isEmpty()) {
                handleSendMessage(messageText);
                etMessageInput.setText(""); // Xóa nội dung input
            }
        });
    }
    private void handleSendMessage(String messageText) {
        // 1. Thêm tin nhắn của người dùng vào UI ngay lập tức
        addMessageToList(new ChatMessage(messageText, ChatMessage.Type.SENT));

        // 2. Gọi ViewModel để lấy phản hồi và "lắng nghe" kết quả
        chatViewModel.sendMessage(messageText).observe(getViewLifecycleOwner(), chatResponse -> {
            if (chatResponse != null && chatResponse.getResponse() != null) {
                // 3. Nếu có phản hồi, thêm tin nhắn của bot vào UI
                Log.d(TAG, "Nhận được phản hồi: " + chatResponse.getResponse());
                addMessageToList(new ChatMessage(chatResponse.getResponse(), ChatMessage.Type.RECEIVED));
            } else {
                // 4. (Tùy chọn) Xử lý khi server không trả lời hoặc trả về null
                Log.e(TAG, "Phản hồi từ bot là null hoặc không hợp lệ");
                addMessageToList(new ChatMessage("Xin lỗi, tôi đang gặp sự cố. Vui lòng thử lại sau.", ChatMessage.Type.RECEIVED));
            }
        });
    }

    private void addMessageToList(ChatMessage message) {
        if (chatAdapter != null && recyclerViewChat != null) {
            messageList.add(message);
            chatAdapter.notifyItemInserted(messageList.size() - 1);
            recyclerViewChat.scrollToPosition(messageList.size() - 1);
        }
    }
}
