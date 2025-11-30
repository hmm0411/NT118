package course.examples.cinepople.activity.booking;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

// Import các lớp Activity, Binding
import course.examples.cinepople.R;
import course.examples.cinepople.activity.main.MainActivity; // 🟢 Cần Import MainActivity
import course.examples.cinepople.databinding.ActivityPaymentBinding;
// ... (Các imports khác)

public class PaymentActivity extends AppCompatActivity {

    // ... (các biến khác) ...

    // 🟢 HẰNG SỐ NAVIGATION
    public static final String NAV_TARGET_TAB_KEY = "NAV_TAB";
    public static final int TAB_HOME = 0;    // Giả sử Home là tab index 0
    public static final int TAB_TICKETS = 2; // Giả sử Tickets là tab index 2

    private ActivityPaymentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // ... (Code setup khác giữ nguyên) ...

        // 3. Xử lý nút thanh toán MoMo (Thay cho logic thanh toán thật)
        binding.btnPaymentMomo.setOnClickListener(v -> handleMomoClick());
    }

    private void handleMomoClick() {
        // ... (Logic gọi MoMo SDK / API giữ nguyên) ...

        // 🟢 GIẢ LẬP KẾT QUẢ THÀNH CÔNG (Sau khi API S2S báo OK)
        showSuccessDialog(); // ⬅️ Gọi dialog thành công
    }

    // --- NAVIGATION HELPERS ---

    private void navigateToMainTab(int tabIndex) {
        Intent intent = new Intent(this, MainActivity.class);

        // Cờ quan trọng: Xóa Activity Stack (SelectSeats, ReviewSummary, Payment)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        // Truyền index tab muốn mở
        intent.putExtra(NAV_TARGET_TAB_KEY, tabIndex);

        startActivity(intent);
        finish(); // Đóng Activity Payment
    }

    // --- DIALOGS ---

    /**
     * Hiển thị Dialog khi thanh toán thành công (Theo hình)
     */
    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_payment_success, null);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();

        // 1. Logic nút "View My Ticket"
        dialogView.findViewById(R.id.btn_view_ticket).setOnClickListener(v -> {
            dialog.dismiss();
            navigateToMainTab(TAB_TICKETS);
        });

        // 2. Logic nút "Go Home"
        dialogView.findViewById(R.id.btn_go_home).setOnClickListener(v -> {
            dialog.dismiss();
            navigateToMainTab(TAB_HOME);
        });

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        dialog.setCancelable(false);
        dialog.show();
    }

    /**
     * Hiển thị Dialog khi thanh toán thất bại (Không thành công)
     */
    private void showFailureDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_payment_failure, null);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();

        // Gán thông báo lỗi
        TextView tvMessage = dialogView.findViewById(R.id.tv_failure_message);
        tvMessage.setText(message);

        // Nút Cancel/Dismiss
        dialogView.findViewById(R.id.btn_cancel_dismiss).setOnClickListener(v -> {
            dialog.dismiss();
        });

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        dialog.setCancelable(true); // Cho phép hủy
        dialog.show();
    }
}