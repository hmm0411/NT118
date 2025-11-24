package course.examples.cinepople.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.*;

import java.util.Arrays;

import course.examples.cinepople.R;
import course.examples.cinepople.activity.main.MainActivity;
import android.content.Context;
import android.content.SharedPreferences;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText editTextEmail, editTextPassword;
    private MaterialButton buttonLogin;
    private TextView textViewSignUp, textViewForgotPassword;

    FirebaseAuth mAuth;
    GoogleSignInClient googleSignInClient;
    CallbackManager callbackManager;

    public static final String APP_PREFERENCES = "AppSession";

    private static final int DIALOG_DISPLAY_TIME = 1000;
    private static final String titleSuccess = "Login Successful!";
    private static final String messageSuccess = "Redirecting to homepage...";

    ActivityResultLauncher<Intent> googleLauncher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_login);

        mAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();

        editTextEmail = findViewById(R.id.ed_email);
        editTextPassword = findViewById(R.id.ed_password);
        buttonLogin = findViewById(R.id.btn_login);
        textViewSignUp = findViewById(R.id.tv_signup);
        textViewForgotPassword = findViewById(R.id.tv_forgotPassword);

        setupGoogleLogin();
        setupFacebookLogin();

        buttonLogin.setOnClickListener(v -> loginUser());

        textViewSignUp.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class)));

        textViewForgotPassword.setOnClickListener(v ->
                Toast.makeText(this, "Forgot password coming soon!", Toast.LENGTH_SHORT).show());
    }

    // ------------------ EMAIL + PASSWORD ---------------------
    private void loginUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Enter email");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Enter password");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    saveUserSession(email);
                    showLoginSuccessDialog(titleSuccess, messageSuccess);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    // ------------------ GOOGLE LOGIN ---------------------
    private void setupGoogleLogin() {
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))  // Firebase Web client ID
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        googleLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getData() == null) return;
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());

                    try {
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        firebaseAuthWithGoogle(account.getIdToken());
                    } catch (Exception e) {
                        Log.e(TAG, "Google login failed", e);
                    }
                }
        );

        findViewById(R.id.login_google).setOnClickListener(v ->
                googleLauncher.launch(googleSignInClient.getSignInIntent()));
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        mAuth.signInWithCredential(credential)
                .addOnSuccessListener(authResult -> {
                    saveUserSession(mAuth.getCurrentUser().getEmail());
                    showLoginSuccessDialog(titleSuccess, messageSuccess);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Google Login failed", Toast.LENGTH_SHORT).show());
    }

    // ------------------ FACEBOOK LOGIN ---------------------
    private void setupFacebookLogin() {
        findViewById(R.id.login_facebook).setOnClickListener(v -> {
            LoginManager.getInstance().logInWithReadPermissions(
                    LoginActivity.this, Arrays.asList("email", "public_profile"));
        });

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        AuthCredential credential =
                                FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());

                        mAuth.signInWithCredential(credential)
                                .addOnSuccessListener(authResult -> {
                                    saveUserSession(mAuth.getCurrentUser().getEmail());
                                    showLoginSuccessDialog(titleSuccess, messageSuccess);
                                })
                                .addOnFailureListener(e ->
                                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
                    }

                    @Override
                    public void onCancel() { }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(LoginActivity.this, "Facebook Login failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // ------------------ SAVE SESSION ---------------------
    private void saveUserSession(String email) {
        SharedPreferences prefs = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        prefs.edit()
                .putBoolean("LOGGED_IN", true)
                .putString("USER_EMAIL", email)
                .apply();
    }

    // ------------------ SUCCESS DIALOG ---------------------
    private void showLoginSuccessDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_auth_success, null);

        ((TextView) dialogView.findViewById(R.id.tv_dialog_message)).setText(message);
        ((TextView) dialogView.findViewById(R.id.tv_dialog_subtext)).setText(title);

        AlertDialog dialog = builder.setView(dialogView).setCancelable(false).create();
        dialog.show();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            dialog.dismiss();
            startActivity(new Intent(LoginActivity.this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        }, DIALOG_DISPLAY_TIME);
    }

    // ------------------ FACEBOOK CALLBACK ---------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
