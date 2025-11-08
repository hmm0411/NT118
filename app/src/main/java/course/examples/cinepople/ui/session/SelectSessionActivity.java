package course.examples.cinepople.ui.session;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

//import course.examples.cinepople.databinding.ActivitySelectSessionBinding;
//import course.examples.cinepople.fragment.ProvinceSelectFragment;
//import course.examples.cinepople.fragment.SessionSelectFragment;

public class SelectSessionActivity extends AppCompatActivity{
//        implements ProvinceSelectFragment.OnProvinceSelectedListener,
//        SessionSelectFragment.OnLocationFilterClickListener {
//
//    private ActivitySelectSessionBinding binding;
//    private String movieId; // ID phim được truyền từ MovieDetailsActivity
//    private String currentProvinceName = "Ho Chi Minh City"; // Tỉnh/TP mặc định
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivitySelectSessionBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        // Lấy movieId từ Intent (từ MovieDetailsActivity)
//        // (Giả sử bạn đã truyền nó qua)
//        // movieId = getIntent().getStringExtra(MovieDetailsActivity.MOVIE_ID_KEY);
//
//        // Cài đặt Toolbar
//        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
//
//        // Tải Fragment mặc định (SessionSelectFragment) khi Activity mới được tạo
//        if (savedInstanceState == null) {
//            loadSessionFragment(currentProvinceName);
//        }
//    }
//
//    /**
//     * Tải Fragment chọn rạp/suất chiếu
//     */
//    private void loadSessionFragment(String provinceName) {
//        this.currentProvinceName = provinceName; // Cập nhật tỉnh/TP hiện tại
//
//        SessionSelectFragment sessionFragment = SessionSelectFragment.newInstance(movieId, provinceName);
//
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.fragment_container, sessionFragment)
//                .commit();
//    }
//
//    /**
//     * Tải Fragment chọn tỉnh/TP
//     */
//    private void loadProvinceFragment() {
//        // Truyền tỉnh/TP hiện tại cho Fragment để nó có thể hiển thị
//        ProvinceSelectFragment provinceFragment = ProvinceSelectFragment.newInstance(currentProvinceName);
//
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.fragment_container, provinceFragment)
//                .addToBackStack(null) // Thêm vào back stack để người dùng có thể quay lại
//                .commit();
//    }
//
//    // --- Interface Implementations ---
//
//    /**
//     * Được gọi từ ProvinceSelectFragment khi người dùng chọn một tỉnh/TP mới
//     */
//    @Override
//    public void onProvinceSelected(String provinceName) {
//        // Quay lại màn hình trước đó (thoát khỏi ProvinceSelectFragment)
//        getSupportFragmentManager().popBackStack();
//        // Tải lại SessionSelectFragment với dữ liệu của tỉnh/TP mới
//        loadSessionFragment(provinceName);
//    }
//
//    /**
//     * Được gọi từ SessionSelectFragment khi người dùng nhấn nút lọc "Ho Chi Minh City"
//     */
//    @Override
//    public void onLocationFilterClicked() {
//        // Mở màn hình chọn tỉnh/TP
//        loadProvinceFragment();
//    }
}
