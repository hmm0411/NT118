package course.examples.cinepople.activity.main;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.splashscreen.SplashScreen;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import course.examples.cinepople.R;
import course.examples.cinepople.fragment.home.HomeFragment;
import course.examples.cinepople.fragment.search.SearchFragment;
import course.examples.cinepople.fragment.tickets.TicketsFragment;
import course.examples.cinepople.fragment.profile.ProfileFragment;


public class MainActivity extends AppCompatActivity {
    final Fragment fragmentHome = new HomeFragment();
    final Fragment fragmentSearch = new SearchFragment();
    final Fragment fragmentTickets = new TicketsFragment();
    final Fragment fragmentProfile = new ProfileFragment();

    final FragmentManager fm = getSupportFragmentManager();
    Fragment activeFragment = fragmentHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigation;

        bottomNavigation = findViewById(R.id.bottom_navigation_bar);
        bottomNavigation.setOnItemSelectedListener(item -> {

            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) { selectedFragment = fragmentHome; }
            else if (itemId == R.id.nav_search) { selectedFragment = fragmentSearch; }
            else if (itemId == R.id.nav_tickets) { selectedFragment = fragmentTickets; }
            else if (itemId == R.id.nav_profile) { selectedFragment = fragmentProfile; }

            if (selectedFragment != null) {
                switchFragment(selectedFragment);
                return true;
            }
            return false;
        });

        if (savedInstanceState == null) {
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragmentProfile, "4")
                    .hide(fragmentProfile).commit();

            fm.beginTransaction()
                    .add(R.id.fragment_container, fragmentTickets, "3")
                    .hide(fragmentTickets).commit();

            fm.beginTransaction()
                    .add(R.id.fragment_container, fragmentSearch, "2")
                    .hide(fragmentSearch).commit();

            fm.beginTransaction()
                    .add(R.id.fragment_container, fragmentHome, "1")
                    .commit();

            activeFragment = fragmentHome;
        }
    }
    private void switchFragment(Fragment fragment) {
        fm.beginTransaction()
                .hide(activeFragment)
                .show(fragment)
                .commit();
        activeFragment = fragment;
    }
}