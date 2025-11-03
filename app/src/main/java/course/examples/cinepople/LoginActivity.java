package course.examples.cinepople;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View; // Cần import View
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.FirebaseApp;
import course.examples.cinepople.databinding.ActivityLoginBinding; // Import class Binding

public class LoginActivity extends AppCompatActivity {

    // Khai báo biến binding
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);
        Log.d("FIREBASE", "Firebase initialized successfully!");

        // 1. Thiết lập ViewBinding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        // 2. Set ContentView bằng root của binding
        setContentView(binding.getRoot());

        // (Code EdgeToEdge đã được xóa, điều này là tốt)

        // 3. Thêm sự kiện click cho nút Login (btnLogin là ID trong XML)
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // (Sau này, bạn sẽ thêm logic kiểm tra email/password ở đây)

                // Chuyển sang HomeActivity
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);

                // (Tùy chọn: bạn có thể finish() LoginActivity nếu không muốn người dùng back lại)
                // finish();
            }
        });

        // 4. Thêm sự kiện click cho text "Sign up" (tvSignup là ID trong XML)
        binding.tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang SignUpActivity
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}
