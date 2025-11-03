package course.examples.cinepople;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.FirebaseApp;
import course.examples.cinepople.databinding.ActivitySignupBinding;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);
        Log.d("FIREBASE", "Firebase initialized successfully!");

        // 1. Thiết lập ViewBinding
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        // 2. Set ContentView bằng root của binding
        setContentView(binding.getRoot());

        // (Xóa code EdgeToEdge nếu có)

        // 3. THÊM SỰ KIỆN: Click vào nút "Back" (btn_return_signup)
        binding.btnReturnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đóng Activity này và quay lại màn hình trước (LoginActivity)
                finish();
            }
        });

        // 4. THÊM SỰ KIỆN: Click vào text "Sign In" (tv_sign_in_link)
        binding.tvSignInLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đóng Activity này và quay lại màn hình trước (LoginActivity)
                finish();
            }
        });

        // 5. THÊM SỰ KIỆN: Click vào nút "NEXT" (btn_next_signup)
        binding.btnNextSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // (Sau này, bạn sẽ thêm logic kiểm tra email/pass/confirm pass ở đây)

                // Giả sử đăng ký thành công, chuyển sang HomeActivity
                Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);

                // Cờ này sẽ xóa LoginActivity khỏi stack (nếu nó đang mở)
                // và làm cho HomeActivity trở thành task mới.
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish(); // Đóng SignUpActivity
            }
        });
    }
}
