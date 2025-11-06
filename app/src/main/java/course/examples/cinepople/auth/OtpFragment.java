package course.examples.cinepople.auth;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

// Đảm bảo import đúng lớp ViewBinding
// (ví dụ: FragmentOtpBinding nếu tệp layout là fragment_otp.xml)
import course.examples.cinepople.CreateProfileActivity;
import course.examples.cinepople.databinding.FragmentOtpBinding;

public class OtpFragment extends Fragment {

    private FragmentOtpBinding binding;

    // Mảng chứa các ô EditText
    private EditText[] otpFields;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOtpBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Khởi tạo mảng EditText
        otpFields = new EditText[]{
                binding.etOtp1,
                binding.etOtp2,
                binding.etOtp3,
                binding.etOtp4
        };

        // 1. Cài đặt tự động nhảy ô và xóa lùi
        setupOtpTextWatchers();

        // 2. Tự động mở bàn phím và focus vào ô đầu tiên
        showKeyboard(binding.etOtp1);

        // 3. Xử lý nút "Verify OTP"
        binding.btnVerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyOtp();
            }
        });

        // 4. Xử lý nút "Resend OTP"
        binding.tvResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thêm logic gửi lại OTP tại đây
                Toast.makeText(getContext(), "Đang gửi lại OTP...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Hàm tự động mở bàn phím và focus vào ô
     */
    private void showKeyboard(EditText editText) {
        if (isAdded() && getActivity() != null) {
            editText.requestFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }

    /**
     * Hàm cài đặt auto-advance và backspace cho các ô OTP
     */
    private void setupOtpTextWatchers() {
        for (int i = 0; i < otpFields.length; i++) {
            final int index = i;

            // Xử lý auto-advance (tự động nhảy ô)
            otpFields[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 1 && index < otpFields.length - 1) {
                        // Tự động nhảy sang ô tiếp theo
                        otpFields[index + 1].requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            // Xử lý nút Backspace (Xóa lùi)
            otpFields[i].setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (index > 0 && otpFields[index].getText().toString().isEmpty()) {
                            // Nếu ô hiện tại rỗng và nhấn xóa,
                            // focus vào ô trước đó
                            otpFields[index - 1].requestFocus();
                        }
                    }
                    return false;
                }
            });
        }
    }

    /**
     * Hàm xử lý khi nhấn nút "Verify OTP"
     */
    // Bên trong OtpFragment.java, hàm verifyOtp()
    private void verifyOtp() {
        // ... (Code lấy OTP và kiểm tra độ dài) ...

        // (Đây là nơi bạn sẽ gọi Firebase để xác thực OTP)
        // ...

        // === PHẦN THAY ĐỔI ===

        // Giả sử xác thực thành công:
        Toast.makeText(getContext(), "Xác thực OTP thành công!", Toast.LENGTH_SHORT).show();

        // Đảm bảo Fragment vẫn đang hoạt động
        if (!isAdded() || getActivity() == null) {
            return;
        }

        // 1. Khởi chạy ProfileActivity MỚI
        Intent intent = new Intent(getActivity(), CreateProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        // 2. Đóng Activity "vỏ bọc" (AuthenticationActivity)
        getActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}