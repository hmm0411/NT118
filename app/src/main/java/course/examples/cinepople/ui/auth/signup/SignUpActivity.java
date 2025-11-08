package course.examples.cinepople.ui.auth.signup;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import course.examples.cinepople.R;

public class SignUpActivity extends AppCompatActivity {

    private ImageView btnReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_sign_up);

        btnReturn = findViewById(R.id.btn_return);

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                } else {
                    finish();
                }
            }
        });

        if (savedInstanceState == null) {
            loadFragment(new SignUpCreateAccountFragment(), false);
        }
    }

    public void loadFragment(Fragment fragment, boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (addToBackStack) {
            transaction.setCustomAnimations(
                    R.anim.slide_in_right,  // Fragment mới trượt vào từ phải
                    R.anim.slide_out_left,  // Fragment cũ trượt ra bên trái
                    R.anim.slide_in_left,   // (Khi back) Fragment cũ trượt vào từ trái
                    R.anim.slide_out_right  // (Khi back) Fragment hiện tại trượt ra bên phải
            );
        }

        transaction.replace(R.id.fragment_container, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }
}