package course.examples.cinepople.ui.auth;

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
import com.google.firebase.auth.FirebaseUser; // <-- Thêm import

// (Bỏ import HomeFragment vì chúng ta không đi đến đó nữa)
// import course.examples.cinepople.home.HomeFragment;
import course.examples.cinepople.databinding.FragmentSignupBinding;

public class SignUpFragment extends Fragment {
    private static final String TAG = "SignUpFragment";
    private FragmentSignupBinding binding;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSignupBinding.inflate(inflater, container, false);
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
            }
        });

        binding.tvSignInLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    private void signUpUser() {
        String email = binding.edEmail.getText().toString().trim();
        String password = binding.edPassword.getText().toString().trim();
        String confirmPassword = binding.edConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || !password.equals(confirmPassword))
        {
            if (!password.equals(confirmPassword))
            {
                binding.edConfirmPassword.setError("Mật khẩu không khớp.");
                binding.edConfirmPassword.requestFocus();
            }
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
                        if (!isAdded()) {return;}

                        if (task.isSuccessful())
                        {
                            Log.d(TAG, "createUserWithEmail:success");

                            FirebaseUser user = mAuth.getCurrentUser();

                            if (user != null)
                            {
                                sendEmailVerification(user);
                            } else
                            {
                                Toast.makeText(getContext(), "Đăng ký thành công nhưng không lấy được thông tin user.", Toast.LENGTH_SHORT).show();
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
                                    "Đăng ký thành công. Vui lòng kiểm tra email để xác thực tài khoản.",
                                    Toast.LENGTH_LONG).show();

                            mAuth.signOut();

                            getActivity().getSupportFragmentManager().popBackStack();

                        } else {
                            Log.e(TAG, "sendEmailVerification:failure", task.getException());
                            Toast.makeText(getContext(),
                                    "Gửi email xác thực thất bại: " + task.getException().getMessage(),
                                    Toast.SHORT).show();

                            binding.btnNext.setEnabled(true); // Kích hoạt lại nút
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