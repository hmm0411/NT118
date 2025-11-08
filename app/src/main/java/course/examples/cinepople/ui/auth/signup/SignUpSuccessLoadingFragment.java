package course.examples.cinepople.ui.auth.signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import course.examples.cinepople.databinding.FragmentAuthSignUpSuccessBinding; // <--- THAY ĐỔI TÊN NÀY

import course.examples.cinepople.ui.main.MainActivity;

public class SignUpSuccessLoadingFragment extends Fragment {

    private FragmentAuthSignUpSuccessBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 3. Khởi tạo binding trong onCreateView
        binding = FragmentAuthSignUpSuccessBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 4. Thiết lập sự kiện click cho button
        // Giả sử ID của button là "btn_go_to_main"
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gọi hàm để chuyển màn hình
                goToMainActivity();
            }
        });
    }

    private void goToMainActivity() {
        // Kiểm tra để chắc chắn Activity vẫn còn tồn tại
        if (getActivity() == null) {
            return;
        }

        // Tạo Intent để chuyển sang MainActivity
        Intent intent = new Intent(getActivity(), MainActivity.class);

        // (Rất quan trọng) Thêm cờ này để xóa hết các màn hình đăng nhập/đăng ký
        // khỏi "lịch sử" (back stack).
        // Người dùng sẽ không thể nhấn "Back" để quay lại màn hình này.
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);

        // Đóng AuthenticationActivity (hoặc Activity đang chứa Fragment này)
        getActivity().finish();
    }

    // 5. Hủy binding để tránh rò rỉ bộ nhớ
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}