package course.examples.cinepople.auth;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import course.examples.cinepople.R;

public class AuthenticationActivity extends AppCompatActivity {

    private ImageView btnReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

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
            loadFragment(new LoginFragment(), false);
        }
    }

    public void loadFragment(Fragment fragment, boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (addToBackStack) {
            transaction.setCustomAnimations(
                    R.anim.slide_in_right,  // 1. Fragment MỚI (SignUp) đi vào
                    R.anim.slide_out_left,  // 2. Fragment CŨ (Login) đi ra
                    R.anim.slide_in_left,   // 3. (Khi nhấn Back) Fragment CŨ (Login) quay lại
                    R.anim.slide_out_right  // 4. (Khi nhấn Back) Fragment MỚI (SignUp) đi ra
            );
        }

        transaction.replace(R.id.fragment_container, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }
}