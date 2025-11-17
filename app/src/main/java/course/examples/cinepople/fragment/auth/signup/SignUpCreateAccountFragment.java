package course.examples.cinepople.fragment.auth.signup;

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
import com.google.firebase.auth.FirebaseUser;

import course.examples.cinepople.activity.auth.SignUpActivity;
import course.examples.cinepople.databinding.FragmentAuthSignUpCreateAccountBinding;

public class SignUpCreateAccountFragment extends Fragment {
    private static final String TAG = "SignUpFragment";
    private FragmentAuthSignUpCreateAccountBinding binding;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAuthSignUpCreateAccountBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpUser();
                ((SignUpActivity) getActivity())
                        .loadFragment(new SignUpOTPFragment(), true);
            }
        });

        binding.tvSignInLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });
    }

    private void signUpUser() {
        String email = binding.edEmail.getText().toString().trim();
        String password = binding.edPassword.getText().toString().trim();
        String confirmPassword = binding.edConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edEmail.setError("Vui lòng nhập email hợp lệ.");
            binding.edEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password) || password.length() < 6) {
            binding.edPassword.setError("Mật khẩu phải có ít nhất 6 ký tự.");
            binding.edPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(confirmPassword) || !password.equals(confirmPassword))
        {
            binding.edConfirmPassword.setError("Mật khẩu không khớp.");
            binding.edConfirmPassword.requestFocus();
            return;
        }

        if (!binding.cbAgreeTerms.isChecked())
        {
            Toast.makeText(getContext(), "Bạn phải đồng ý với điều khoản sử dụng.", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.btnNext.setEnabled(false);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        // Kiểm tra xem fragment có còn tồn tại không
                        if (!isAdded()) {return;}

                        if (task.isSuccessful())
                        {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            if (user != null)
                            {
                                // Gửi email xác thực
                                sendEmailVerification(user);
                            } else
                            {
                                Toast.makeText(getContext(), "Đăng ký thành công nhưng không lấy được thông tin user.", Toast.LENGTH_SHORT).show();
                                binding.btnNext.setEnabled(true);
                            }

                        } else
                        {
                            // Đăng ký thất bại
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Đăng ký thất bại: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                            binding.btnNext.setEnabled(true);
                        }
                    }
                });
    }

    private void sendEmailVerification(FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (!isAdded()) {return;}

                        if (task.isSuccessful())
                        {
                            Log.d(TAG, "Email verification sent.");
                            Toast.makeText(getContext(),
                                    "Đăng ký thành công. Vui lòng kiểm tra email để xác thực.",
                                    Toast.LENGTH_LONG).show();

                            if (getActivity() instanceof SignUpActivity) {
                                ((SignUpActivity) getActivity())
                                        .loadFragment(new SignUpOTPFragment(), true);
                            }


                        } else {
                            Log.e(TAG, "sendEmailVerification:failure", task.getException());
                            Toast.makeText(getContext(),
                                    "Gửi email xác thực thất bại: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();

                            binding.btnNext.setEnabled(true);
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