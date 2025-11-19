package course.examples.cinepople.fragment.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate; // Giữ lại import quan trọng này
import androidx.fragment.app.Fragment;

import course.examples.cinepople.R;
import course.examples.cinepople.activity.auth.LoginActivity;
import course.examples.cinepople.activity.auth.SignUpActivity;

import course.examples.cinepople.databinding.FragmentMainProfileBinding;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    private FragmentMainProfileBinding binding;
    private boolean isLoggedIn = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        isLoggedIn = true; // Mặc định: CHƯA ĐĂNG NHẬP

        // --- KHÔNG CẦN loadDarkModeState() NỮA ---

        // Đảm bảo Switch hiển thị đúng trạng thái hệ thống hiện tại
        setInitialSwitchState();

        updateProfileUI();
        setupListeners();
    }

    // --- LOGIC DARK MODE ĐƯỢC ĐƠN GIẢN HÓA ---

    private void setInitialSwitchState() {
        // Thiết lập Switch dựa trên chế độ đang hoạt động của ứng dụng
        int currentMode = AppCompatDelegate.getDefaultNightMode();
        boolean isDark = (currentMode == AppCompatDelegate.MODE_NIGHT_YES ||
                currentMode == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        if (binding.switchDarkMode != null) {
            binding.switchDarkMode.setChecked(isDark);
        }
    }

    private void applyDarkMode(boolean isDark) {
        // Chỉ áp dụng chế độ mới, không lưu trữ
        int mode = isDark ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
        AppCompatDelegate.setDefaultNightMode(mode);
    }

    private void setupListeners() {
        if (binding == null) return;

        // --- LISTENER CHO DARK MODE SWITCH ---
        if (binding.switchDarkMode != null) {
            binding.switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
                applyDarkMode(isChecked);
                // Vì không dùng SharedPreferences, trạng thái này sẽ bị mất khi ứng dụng thoát
            });
        }

        // --- Listeners cho trạng thái CHƯA ĐĂNG NHẬP (Giữ nguyên) ---
        binding.btnGoToLogin.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        });

        binding.btnGoToSignup.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), SignUpActivity.class));
        });

        // --- Listener cho trạng thái ĐÃ ĐĂNG NHẬP (Giữ nguyên) ---
        binding.btnLogout.setOnClickListener(v -> {
            logout();
        });
    }

    private void updateProfileUI() {
        if (binding == null) return;

        if (isLoggedIn) {
            // Logic ĐÃ ĐĂNG NHẬP (Giữ nguyên)
            binding.scrollView.setVisibility(View.VISIBLE);
            //binding.user_info_layout.setVisibility(View.VISIBLE);
            binding.loggedOutView.setVisibility(View.GONE);
            binding.optionWatchlist.setVisibility(View.VISIBLE);
            binding.optionPaymentMethods.setVisibility(View.VISIBLE);
            binding.optionPersonalInfo.setVisibility(View.VISIBLE);
            binding.optionSecurity.setVisibility(View.VISIBLE);
            binding.btnLogout.setVisibility(View.VISIBLE);
        } else {
            // Logic CHƯA ĐĂNG NHẬP (Giữ nguyên)
            binding.scrollView.setVisibility(View.GONE);
            //binding.user_info_layout.setVisibility(View.GONE);
            binding.loggedOutView.setVisibility(View.VISIBLE);
            binding.optionWatchlist.setVisibility(View.GONE);
            binding.optionPaymentMethods.setVisibility(View.GONE);
            binding.optionPersonalInfo.setVisibility(View.GONE);
            binding.optionSecurity.setVisibility(View.GONE);
            binding.btnLogout.setVisibility(View.GONE);
        }
    }

    public void setIsLoggedIn(boolean status) {
        this.isLoggedIn = status;
        updateProfileUI();
    }

    private void logout() {
        isLoggedIn = false;
        updateProfileUI();
        Toast.makeText(getContext(), "Đã đăng xuất thành công!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}