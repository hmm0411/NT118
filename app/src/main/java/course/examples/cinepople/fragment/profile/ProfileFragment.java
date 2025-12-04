package course.examples.cinepople.fragment.profile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import course.examples.cinepople.R;
import course.examples.cinepople.activity.auth.LoginActivity;
import course.examples.cinepople.activity.auth.SignUpActivity;
import course.examples.cinepople.databinding.FragmentMainProfileBinding;
import course.examples.cinepople.utility.SessionManager;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    private FragmentMainProfileBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Cập nhật giao diện (Login/Logout view)
        updateProfileUI();

        // 2. Thiết lập trạng thái ban đầu cho Switch
        setInitialSwitchState();

        // 3. Cài đặt các sự kiện click khác (Login, Logout...)
        setupClickListeners();
    }

    /**
     * Thiết lập trạng thái bật/tắt cho Switch và gán sự kiện lắng nghe
     * Hàm này được gọi ở onViewCreated và onResume để đảm bảo đồng bộ
     */
    private void setInitialSwitchState() {
        if (binding.switchDarkMode != null && getActivity() != null) {
            // Lấy trạng thái đã lưu
            boolean isDarkSaved = SessionManager.isDarkMode(getActivity());

            // ⚠️ QUAN TRỌNG: Gỡ bỏ listener trước khi setChecked để tránh kích hoạt sự kiện không mong muốn
            binding.switchDarkMode.setOnCheckedChangeListener(null);

            // Set trạng thái hiển thị
            binding.switchDarkMode.setChecked(isDarkSaved);

            // Gán lại listener sau khi đã set xong trạng thái
            setupDarkModeListener();
        }
    }

    /**
     * Tách riêng logic lắng nghe của Dark Mode
     */
    private void setupDarkModeListener() {
        binding.switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (getActivity() == null) return;

            // 1. Lưu trạng thái
            SessionManager.setDarkMode(getActivity(), isChecked);

            // 2. Cài đặt chế độ
            int mode = isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
            AppCompatDelegate.setDefaultNightMode(mode);

            // 3. Xử lý chuyển đổi mượt mà
            // Sử dụng Handler để đợi nút Switch gạt xong mới reload -> Tránh bị khựng (freeze)
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (getActivity() != null) {
                    // Áp dụng Style Animation chúng ta vừa tạo ở Bước 1
                    getActivity().getWindow().setWindowAnimations(R.style.WindowAnimationFade);

                    // Tải lại Activity hiện tại (Giữ nguyên vị trí Fragment)
                    getActivity().recreate();
                }
            }, 200); // Delay 200ms
        });
    }

    /**
     * Thiết lập các sự kiện click cho các nút khác
     */
    private void setupClickListeners() {
        if (binding == null) return;

        // --- Listeners cho trạng thái CHƯA ĐĂNG NHẬP ---
        binding.btnGoToLogin.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        });

        binding.btnGoToSignup.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), SignUpActivity.class));
        });

        // --- Listener cho trạng thái ĐÃ ĐĂNG NHẬP ---
        binding.btnLogout.setOnClickListener(v -> {
            logout();
        });
    }

    private void updateProfileUI() {
        if (binding == null) return;

        boolean isLoggedIn = false;
        if (getActivity() != null) {
            isLoggedIn = SessionManager.isLoggedIn(getActivity());
        }

        if (isLoggedIn) {
            binding.scrollView.setVisibility(View.VISIBLE);
            binding.loggedOutView.setVisibility(View.GONE);
        } else {
            binding.scrollView.setVisibility(View.GONE);
            binding.loggedOutView.setVisibility(View.VISIBLE);
        }
    }

    private void logout() {
        // 1. Đăng xuất Firebase
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().signOut();
        }

        // 2. Xóa phiên cục bộ (Chỉ xóa trạng thái đăng nhập, không xóa setting Dark Mode nếu không muốn)
        if (getActivity() != null) {
            // Giả sử clearSession chỉ xóa tShông tin user, giữ lại setting app
            SessionManager.clearSession(getActivity());
//            SessionManager.clearSession(requireContext());
        }

        // 3. Cập nhật giao diện
        updateProfileUI();
        Toast.makeText(getContext(), "Đã đăng xuất thành công!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Cập nhật lại UI mỗi khi quay lại Fragment
        updateProfileUI();

        // Đảm bảo Switch hiển thị đúng trạng thái (quan trọng sau khi recreate)
        setInitialSwitchState();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}