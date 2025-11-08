package course.examples.cinepople.ui.main;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import course.examples.cinepople.R;
import course.examples.cinepople.ui.main.home.HomeFragment;
import course.examples.cinepople.ui.main.search.SearchFragment;
import course.examples.cinepople.ui.main.tickets.TicketsFragment;
import course.examples.cinepople.ui.main.profile.ProfileFragment;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigation;

        bottomNavigation = findViewById(R.id.bottom_navigation_bar);
        bottomNavigation.setOnItemSelectedListener(item -> {

            // SỬA 1: Khởi tạo là null
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.nav_search) {
                selectedFragment = new SearchFragment();
            } else if (itemId == R.id.nav_tickets) {
                selectedFragment = new TicketsFragment();
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }

            // Code của bạn đã đúng ở đây
            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }
            return false;
        });

        // SỬA 2: Xóa 'loadFragment' thừa
        if (savedInstanceState == null) {
            // Dòng này sẽ tự động kích hoạt listener ở trên,
            // và listener sẽ gọi loadFragment(new HomeFragment())
            bottomNavigation.setSelectedItemId(R.id.nav_home);
            // loadFragment(new HomeFragment()); // <-- DÒNG NÀY THỪA (ĐÃ XÓA)
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}