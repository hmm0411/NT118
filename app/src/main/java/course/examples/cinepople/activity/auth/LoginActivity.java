package course.examples.cinepople.activity.auth;

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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;
import course.examples.cinepople.viewmodel.LoginViewModel;

import course.examples.cinepople.R;
import course.examples.cinepople.activity.main.MainActivity;
import android.content.Context;
import android.content.SharedPreferences;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    private TextView textViewSignUp, textViewForgotPassword;

    private LoginViewModel loginViewModel;

    public static final String APP_PREFERENCES = "AppSession";
    public static final String KEY_USER_ID = "USER_ID";
    public static final String KEY_USER_EMAIL = "USER_EMAIL";
    public static final String KEY_AUTH_TOKEN = "AUTH_TOKEN";

    private static final int DIALOG_DISPLAY_TIME = 1000;
    private static final String titleSuccess = "Login in Successfull!";
    private static final String messageSuccess = "Please wait...\n" + "You will be directed to the homepage";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_login);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        editTextEmail = findViewById(R.id.ed_email);
        editTextPassword = findViewById(R.id.ed_password);
        buttonLogin = findViewById(R.id.btn_login);

        textViewSignUp = findViewById(R.id.tv_signup);
        textViewForgotPassword = findViewById(R.id.tv_forgotPassword);

        observeViewModel();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { loginUser(); }
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

    private void observeViewModel() {
        loginViewModel.getLoginSuccess().observe(this, loginResponse -> {
            if (loginResponse != null) {
                Log.d(TAG, "API Login: success");

                saveUserSession(
                        loginResponse.getUserId(),
                        loginResponse.getEmail(),
                        loginResponse.getToken()
                );

                showLoginSuccessDialog(titleSuccess, messageSuccess);
            }
        });

        loginViewModel.getLoginError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Log.w(TAG, "API Login: failure: " + errorMessage);
                Toast.makeText(LoginActivity.this, "AuthFail: " + errorMessage,
                        Toast.LENGTH_LONG).show();
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

        loginViewModel.login(email, password);
    }

    private void saveUserSession(String uid, String email, String token) {
        SharedPreferences sharedPref = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KEY_USER_ID, uid);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_AUTH_TOKEN, token);
        editor.apply();
    }

    private void showLoginSuccessDialog(String title, String message) {
        if (isFinishing() || isDestroyed()) { return; }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_auth_success, null);

        TextView tvMessage = dialogView.findViewById(R.id.tv_dialog_message);
        TextView tvSubtext = dialogView.findViewById(R.id.tv_dialog_subtext);

        if (tvMessage != null) { tvMessage.setText(message); }
        if (tvSubtext != null) { tvSubtext.setText(title); }

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