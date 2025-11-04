package course.examples.cinepople;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import course.examples.cinepople.databinding.ActivityForgotPasswordBinding;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ActivityForgotPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Sự kiện click nút "Return"
        binding.btnReturnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Đóng Activity này và quay lại Login
            }
        });

        // Sự kiện click nút "Submit"
        binding.btnSubmitForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Thêm logic gửi email khôi phục mật khẩu ở đây

                Toast.makeText(ForgotPasswordActivity.this, "Gửi yêu cầu...", Toast.LENGTH_SHORT).show();

                // (Sau khi gửi, có thể chuyển sang màn hình OTP)
                // Intent intent = new Intent(ForgotPasswordActivity.this, OtpActivity.class);
                // startActivity(intent);
            }
        });
    }
}
