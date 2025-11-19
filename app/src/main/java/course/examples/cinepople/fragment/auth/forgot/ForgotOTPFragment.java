package course.examples.cinepople.fragment.auth.forgot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import course.examples.cinepople.activity.auth.ForgotActivity;
import course.examples.cinepople.databinding.FragmentAuthForgotOtpBinding;

public class ForgotOTPFragment extends Fragment {

    private FragmentAuthForgotOtpBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAuthForgotOtpBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Chỉ giữ lại sự kiện click vào nút Verify
        binding.btnVerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Kiểm tra xem Activity có tồn tại không
                if (getActivity() instanceof ForgotActivity) {
                    // Gọi hàm loadFragment từ SignUpActivity
                    ((ForgotActivity) getActivity())
                            .loadFragment(new ForgotResetFragment(), true);
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