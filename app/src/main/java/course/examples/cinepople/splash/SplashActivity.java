package course.examples.cinepople.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.appcompat.app.AppCompatActivity;

// --- Thêm các import của Firebase ---
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import course.examples.cinepople.R;
import course.examples.cinepople.auth.AuthenticationActivity; // <-- Import Activity Đăng nhập
import course.examples.cinepople.home.HomeFragment;
import course.examples.cinepople.databinding.ActivitySplashBinding;
import course.examples.cinepople.main.MainActivity;

public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // --- Khởi tạo Firebase Auth ---
        mAuth = FirebaseAuth.getInstance();

        // (Code animation của bạn giữ nguyên, rất tốt!)
        Animation logoMoveUp = AnimationUtils.loadAnimation(this, R.anim.logo_move_up);
        Animation textSlideInUp = AnimationUtils.loadAnimation(this, R.anim.text_slide_in_up);
        binding.logoSplash.startAnimation(logoMoveUp);
        binding.nameAppSplash.startAnimation(textSlideInUp);

        // Logic chính nằm ở đây
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Kiểm tra xem người dùng đã đăng nhập chưa
                FirebaseUser currentUser = mAuth.getCurrentUser();
                Intent intent;

                if (currentUser != null) {
                    // Người dùng đã đăng nhập (phiên vẫn còn)
                    // Chuyển thẳng vào màn hình chính
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                } else {
                    // Người dùng chưa đăng nhập
                    // Chuyển đến màn hình đăng nhập/đăng ký
                    intent = new Intent(SplashActivity.this, AuthenticationActivity.class);
                }

                // Thêm cờ (flags) để ngăn người dùng nhấn Back quay lại SplashActivity
                // Điều này xóa "lịch sử" của SplashActivity khỏi stack
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
                finish(); // Đóng SplashActivity
            }
        }, 2500); // Delay 2.5 giây để chạy animation
    }
}