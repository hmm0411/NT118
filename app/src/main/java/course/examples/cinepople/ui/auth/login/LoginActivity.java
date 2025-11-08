package course.examples.cinepople.ui.auth.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import course.examples.cinepople.R;
import course.examples.cinepople.ui.auth.forgot.ForgotActivity;
import course.examples.cinepople.ui.auth.signup.SignUpActivity;
import course.examples.cinepople.ui.main.MainActivity;
import android.content.Context; // <-- THÊM IMPORT
import android.content.Intent;
import android.content.SharedPreferences; // <-- THÊM IMPORT
import android.os.Bundle;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser; // <-- THÊM IMPORT

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginFragment";
    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    private TextView textViewSignUp, textViewForgotPassword;
    private FirebaseAuth mAuth;
    // Tên file SharedPreferences
    public static final String APP_PREFERENCES = "AppSession";
    public static final String KEY_USER_ID = "USER_ID";
    public static final String KEY_USER_EMAIL = "USER_EMAIL";
    private static final int DIALOG_DISPLAY_TIME = 1000;

    private static final String titleSuccess = "Login in Successfull!";
    private static final String messageSuccess = "Please wait...\n" +
            "You will be directed to the homepage";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_login);

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.ed_email);
        editTextPassword = findViewById(R.id.ed_password);
        buttonLogin = findViewById(R.id.btn_login);
        textViewSignUp = findViewById(R.id.tv_signup);
        textViewForgotPassword = findViewById(R.id.tv_forgotPassword);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 Intent intent = new Intent(LoginActivity.this, ForgotActivity.class);
                 startActivity(intent);
            }
        });
    }

    private void loginUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Email Unfill.");
            editTextEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Password Unfill.");
            editTextPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!isFinishing() && !isDestroyed()) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "LoginWithEmail:success");

                                // --- BẮT ĐẦU SỬA ĐỔI ---
                                // Lấy thông tin người dùng vừa đăng nhập
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    // Lưu UID và Email vào SharedPreferences
                                    saveUserSession(user.getUid(), user.getEmail());
                                }
                                // --- KẾT THÚC SỬA ĐỔI ---

                                showLoginSuccessDialog(titleSuccess, messageSuccess);
                            } else {
                                Log.w(TAG, "LoginWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "AuthFail: " + task.getException().getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    private void saveUserSession(String uid, String email) {
        SharedPreferences sharedPref = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KEY_USER_ID, uid);
        editor.putString(KEY_USER_EMAIL, email);
        editor.apply(); // Lưu
    }

    private void showLoginSuccessDialog(String title, String message) {
        if (isFinishing() || isDestroyed()) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_auth_success, null);

        TextView tvMessage = dialogView.findViewById(R.id.tv_dialog_message);
        TextView tvSubtext = dialogView.findViewById(R.id.tv_dialog_subtext);

        if (tvMessage != null) {
            tvMessage.setText(title);
        }

        if (tvSubtext != null) {
            tvSubtext.setText(message);
        }

        builder.setView(dialogView);
        builder.setCancelable(false);

        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing() && !isDestroyed()) {
                    dialog.dismiss();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        }, DIALOG_DISPLAY_TIME);
    }
}