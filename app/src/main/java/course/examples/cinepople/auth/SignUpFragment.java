package course.examples.cinepople.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import course.examples.cinepople.home.HomeFragment;
import course.examples.cinepople.databinding.FragmentSignupBinding;

public class SignUpFragment extends Fragment {
    private static final String TAG = "SignUpFragment";
    private FragmentSignupBinding binding;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // "Thổi phồng" layout bằng View Binding
        binding = FragmentSignupBinding.inflate(inflater, container, false);

        // Trả về View gốc của layout
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 2. Khởi tạo Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // 3. Gán sự kiện click cho nút "Next"
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Khi nhấn "Next", gọi hàm đăng ký
                registerUser();
            }
        });

        // 4. Gán sự kiện click cho link "Sign In" (quay lại Login)
        binding.tvSignInLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Yêu cầu Activity cha quay lại Fragment trước đó
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // (Tùy chọn: Bạn cũng có thể gán sự kiện cho tv_terms_link để mở trang web)
    }

    /**
     * Hàm xử lý logic đăng ký tài khoản
     */
    private void registerUser() {
        // Lấy dữ liệu từ các EditText
        String email = binding.edEmail.getText().toString().trim();
        String password = binding.edPassword.getText().toString().trim();
        String confirmPassword = binding.edConfirmPassword.getText().toString().trim();

        // === 5. Kiểm tra (Validate) dữ liệu đầu vào ===
        if (TextUtils.isEmpty(email)) {
            binding.edEmail.setError("Email không được để trống.");
            binding.edEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            binding.edPassword.setError("Mật khẩu không được để trống.");
            binding.edPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            binding.edPassword.setError("Mật khẩu phải có ít nhất 6 ký tự.");
            binding.edPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            binding.edConfirmPassword.setError("Vui lòng xác nhận mật khẩu.");
            binding.edConfirmPassword.requestFocus();
            return;
        }

        // Kiểm tra xem 2 mật khẩu có khớp nhau không
        if (!password.equals(confirmPassword)) {
            binding.edConfirmPassword.setError("Mật khẩu không khớp.");
            binding.edConfirmPassword.requestFocus();
            return;
        }

        // Kiểm tra xem người dùng đã đồng ý điều khoản chưa
        if (!binding.cbAgreeTerms.isChecked()) {
            Toast.makeText(getContext(), "Bạn phải đồng ý với điều khoản sử dụng.", Toast.LENGTH_SHORT).show();
            return;
        }

        // (Tùy chọn: Hiển thị ProgressBar ở đây)

        // === 6. Gọi Firebase để tạo tài khoản ===
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Đảm bảo Fragment vẫn đang hoạt động
                        if (!isAdded()) {
                            return;
                        }

                        if (task.isSuccessful()) {
                            // Đăng ký thành công!
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(getContext(), "Tạo tài khoản thành công.", Toast.LENGTH_SHORT).show();

                            // Tự động đăng nhập và chuyển sang HomeActivity
                            Intent intent = new Intent(getActivity(), HomeFragment.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                            // Đóng AuthenticationActivity
                            getActivity().finish();

                        } else {
                            // Đăng ký thất bại
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());

                            // Hiển thị lỗi (ví dụ: Email đã tồn tại)
                            Toast.makeText(getContext(), "Đăng ký thất bại: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}