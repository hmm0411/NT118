package course.examples.cinepople;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.appcompat.app.AppCompatActivity;

import course.examples.cinepople.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Tải hoạt ảnh
        final Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        // Chạy hoạt ảnh cho TextView
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.nameAppSplash.setVisibility(View.VISIBLE);
                binding.nameAppSplash.startAnimation(fadeInAnimation);
            }
        }, 500); // 0.5 giây

        // Chuyển màn hình
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000); // 2 giây
    }
}