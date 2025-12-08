package course.examples.cinepople.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import course.examples.cinepople.R;
import course.examples.cinepople.model.ChatMessage;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageViewHolder> {

    // Các hằng số này định nghĩa hai loại giao diện (view) mà chúng ta sẽ có trong danh sách.
    private static final int VIEW_TYPE_SENT = 1; // Số 1 đại diện cho tin nhắn GỬI ĐI
    private static final int VIEW_TYPE_RECEIVED = 2; // Số 2 đại diện cho tin nhắn NHẬN VỀ

    private final List<ChatMessage> messages;

    // Hàm khởi tạo (Constructor)
    public ChatAdapter(List<ChatMessage> messages) {
        this.messages = messages; // Nhận danh sách tin nhắn từ ChatFragment
    }

    /**
     * Phương thức này kiểm tra loại của tin nhắn tại một vị trí (position) nhất định
     * và trả về kiểu giao diện tương ứng (SENT hoặc RECEIVED).
     * Phương thức onCreateViewHolder sẽ dùng kết quả này để quyết định xem nên "thổi phồng" (inflate) layout XML nào.
     */
    @Override
    public int getItemViewType(int position) {
        ChatMessage message = messages.get(position);
        if (message.getType() == ChatMessage.Type.SENT) { // <-- So sánh với enum
            return VIEW_TYPE_SENT; // Trả về 1
        } else {
            return VIEW_TYPE_RECEIVED; // Trả về 2
        }
    }

    /**
     * Phương thức này được RecyclerView gọi để tạo ra một ViewHolder mới (một khung chứa giao diện cho 1 tin nhắn).
     * Nó sử dụng viewType (mà đã xác định ở hàm getItemViewType) để "thổi phồng" file layout XML chính xác.
     */
    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_SENT) { // nếu gửi (1)
            // layout dành cho tin nhắn người dùng gửi.
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_message_sent, parent, false);
        } else { // Nếu là tin nhắn nhận về (số 2)
            // layout dành cho tin nhắn bot gửi
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_message_received, parent, false);
        }
        return new MessageViewHolder(view);
    }

    /**
     * Gắn dữ liệu từ một đối tượng ChatMessage vào các view bên trong ViewHolder.
     * Nói cách khác, nó lấy nội dung text từ tin nhắn và đặt vào TextView.
     */
    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        holder.tvMessageText.setText(message.getText());
    }

    @Override
    public int getItemCount() {
        return messages.size(); // Trả về tổng số tin nhắn có trong danh sách
    }

    /**
     * Lớp ViewHolder chứa các view (thành phần giao diện) cho một item duy nhất trong danh sách.
     */
    static class MessageViewHolder extends RecyclerView.ViewHolder {
        // This TextView must exist in BOTH item_chat_message_sent.xml and item_chat_message_received.xml
        TextView tvMessageText;

        MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessageText = itemView.findViewById(R.id.tv_message_text);
        }

        void bind(ChatMessage message) {
            if (tvMessageText != null) {
                tvMessageText.setText(message.getText());
            }
        }
    }
}