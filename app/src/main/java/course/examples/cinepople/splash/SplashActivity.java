package course.examples.cinepople.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.appcompat.app.AppCompatActivity;

import course.examples.cinepople.R;
import course.examples.cinepople.home.HomeActivity;
import course.examples.cinepople.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {
    // Khai báo biến binding
    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Gán layout cho Activity
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Animation logoMoveUp = AnimationUtils.loadAnimation(this, R.anim.logo_move_up);

        Animation textSlideInUp = AnimationUtils.loadAnimation(this, R.anim.text_slide_in_up);

        binding.logoSplash.startAnimation(logoMoveUp);

        binding.nameAppSplash.startAnimation(textSlideInUp);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2500);
    }

}