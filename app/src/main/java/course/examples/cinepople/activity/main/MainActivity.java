package course.examples.cinepople.activity.main;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import course.examples.cinepople.R;
import course.examples.cinepople.fragment.home.HomeFragment;
import course.examples.cinepople.fragment.profile.ProfileFragment;
import course.examples.cinepople.fragment.search.SearchFragment;
import course.examples.cinepople.fragment.tickets.TicketsFragment;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    // Khai báo các Fragment (Không khởi tạo new ở đây nữa)
    private Fragment fragmentHome;
    private Fragment fragmentSearch;
    private Fragment fragmentTickets;
    private Fragment fragmentProfile;
    private Fragment activeFragment;

    final FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Xử lý sự kiện click vào nút Chat Bubble
        com.google.android.material.floatingactionbutton.FloatingActionButton fabChat = findViewById(R.id.fab_chat_bubble);
        fabChat.setOnClickListener(v -> {
            // TODO: Mở giao diện ChatFragment hoặc ChatActivity tại đây
            Toast.makeText(this, "Mở Chatbox", Toast.LENGTH_SHORT).show();
        });

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation_bar);

        if (savedInstanceState == null) {
            // Lần đầu chạy app: Tạo mới và Add các fragment
            setupInitialFragments();
        } else {
            // App được tạo lại (xoay màn hình, kill process): Khôi phục fragment cũ
            restoreFragments(savedInstanceState);
        }

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                switchFragment(fragmentHome);
                return true;
            } else if (itemId == R.id.nav_search) {
                switchFragment(fragmentSearch);
                return true;
            } else if (itemId == R.id.nav_tickets) {
                switchFragment(fragmentTickets);
                return true;
            } else if (itemId == R.id.nav_profile) {
                switchFragment(fragmentProfile);
                return true;
            }
            return false;
        });
    }

    private void setupInitialFragments() {
        fragmentHome = new HomeFragment();
        fragmentSearch = new SearchFragment();
        fragmentTickets = new TicketsFragment();
        fragmentProfile = new ProfileFragment();

        fm.beginTransaction()
                .add(R.id.fragment_container, fragmentProfile, "TAG_PROFILE").hide(fragmentProfile)
                .add(R.id.fragment_container, fragmentTickets, "TAG_TICKETS").hide(fragmentTickets)
                .add(R.id.fragment_container, fragmentSearch, "TAG_SEARCH").hide(fragmentSearch)
                .add(R.id.fragment_container, fragmentHome, "TAG_HOME") // Home hiển thị đầu tiên
                .commit();

        activeFragment = fragmentHome;
    }

    private void openChatFragment() {
        // ⚠️ LƯU Ý: Bạn cần đảm bảo đã tạo class ChatFragment.java
        Fragment chatFragment = new course.examples.cinepople.fragment.chat.ChatFragment();

        // Sử dụng transaction ADD để đặt ChatFragment lên trên các fragment hiện tại
        fm.beginTransaction()
                .setCustomAnimations(
                        android.R.anim.slide_in_left, // Tùy chọn: Thêm hiệu ứng trượt
                        android.R.anim.slide_out_right,
                        android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right
                )
                .add(R.id.fragment_container, chatFragment, "TAG_CHAT") // Thêm Fragment Chat
                .addToBackStack("CHAT_OVERLAY") // Thêm vào BackStack để bấm nút Back sẽ đóng ChatFragment
                .commit();
    }

    private void restoreFragments(Bundle savedInstanceState) {
        // 🟢 QUAN TRỌNG: Tìm lại các fragment đã có trong bộ nhớ
        fragmentHome = fm.findFragmentByTag("TAG_HOME");
        fragmentSearch = fm.findFragmentByTag("TAG_SEARCH");
        fragmentTickets = fm.findFragmentByTag("TAG_TICKETS");
        fragmentProfile = fm.findFragmentByTag("TAG_PROFILE");

        // Nếu vì lý do nào đó mà tìm không thấy (rất hiếm), tạo mới lại để tránh crash
        if (fragmentHome == null) fragmentHome = new HomeFragment();
        if (fragmentSearch == null) fragmentSearch = new SearchFragment();
        if (fragmentTickets == null) fragmentTickets = new TicketsFragment();
        if (fragmentProfile == null) fragmentProfile = new ProfileFragment();

        // Khôi phục activeFragment dựa trên Tag đã lưu (nếu bạn có lưu)
        // Hoặc đơn giản là tìm fragment nào đang visible
        if (fragmentHome.isVisible()) activeFragment = fragmentHome;
        else if (fragmentSearch.isVisible()) activeFragment = fragmentSearch;
        else if (fragmentTickets.isVisible()) activeFragment = fragmentTickets;
        else if (fragmentProfile.isVisible()) activeFragment = fragmentProfile;
        else activeFragment = fragmentHome; // Mặc định
    }

    private void switchFragment(Fragment targetFragment) {
        if (targetFragment == activeFragment) return;

        fm.beginTransaction()
                .hide(activeFragment)
                .show(targetFragment)
                .commit();
        activeFragment = targetFragment;
    }

    // 🟢 SỬA LỖI: Xóa hàm onSaveInstanceState nếu bạn đang dùng putFragment bị lỗi
    // Hoặc sửa lại như sau nếu muốn lưu trạng thái active
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Chỉ lưu tag của active fragment, không lưu instance fragment trực tiếp nếu không cần thiết
        if (activeFragment != null && activeFragment.isAdded()) {
            // fm.putFragment(outState, "activeFragment", activeFragment); // Dòng này dễ gây lỗi nếu fragment chưa added
        }
    }
}