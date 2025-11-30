package course.examples.cinepople.activity.booking;

import android.os.Bundle;
import android.util.Log; // 🟢 Import Log
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import course.examples.cinepople.R;
import course.examples.cinepople.activity.movie.MovieDetailsActivity;
import course.examples.cinepople.databinding.ActivitySelectionSessionBinding;
import course.examples.cinepople.domain.Region;
import course.examples.cinepople.fragment.session.RegionSelectFragment;
import course.examples.cinepople.fragment.session.CinemaSelectFragment;
import course.examples.cinepople.viewmodel.RegionViewModel;

public class SelectSessionActivity extends AppCompatActivity
        implements RegionSelectFragment.OnProvinceSelectedListener,
        CinemaSelectFragment.OnLocationFilterClickListener {

    private static final String TAG = "SelectSessionActivity"; // 🟢 Tag để lọc log
    private ActivitySelectionSessionBinding binding;
    private RegionViewModel regionViewModel;
    private String movieId;
    private String movieTitle;

    // Giữ tham chiếu đến Fragment để tái sử dụng
    private RegionSelectFragment regionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectionSessionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        movieId = getIntent().getStringExtra(MovieDetailsActivity.MOVIE_ID_KEY);
        movieTitle = getIntent().getStringExtra(MovieDetailsActivity.MOVIE_TITLE_KEY);

        Log.d(TAG, "onCreate: Activity Started. MovieID=" + movieId + ", Title=" + movieTitle); // 🟢 Log khởi tạo

        binding.textToolbarTitle.setText(movieTitle);

        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        regionViewModel = new ViewModelProvider(this).get(RegionViewModel.class);

        observeRegionViewModel();

        if (savedInstanceState == null) {
            Log.d(TAG, "onCreate: savedInstanceState is null -> Calling API fetchRegions"); // 🟢 Log gọi API
            regionViewModel.fetchRegions();
        }
    }

    private void observeRegionViewModel() {
        regionViewModel.getRegions().observe(this, regions -> {
            if (regions != null && !regions.isEmpty()) {
                Log.d(TAG, "observeRegionViewModel: Nhận được " + regions.size() + " tỉnh thành."); // 🟢 Log dữ liệu về
                // Luôn hiển thị màn hình chọn Tỉnh trước tiên
                loadInitialRegionFragment(regions);
            } else {
                Log.w(TAG, "observeRegionViewModel: Danh sách tỉnh thành RỖNG hoặc NULL"); // 🟢 Log cảnh báo
                Toast.makeText(this, "Không tìm thấy danh sách tỉnh thành", Toast.LENGTH_SHORT).show();
            }
        });

        regionViewModel.getErrorMessage().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Log.e(TAG, "observeRegionViewModel: Lỗi API -> " + error); // 🟢 Log lỗi
                Toast.makeText(this, "Lỗi tải dữ liệu: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 1. Hiển thị màn hình chọn Tỉnh (Region) lần đầu
     */
    private void loadInitialRegionFragment(List<Region> regions) {
        Log.d(TAG, "loadInitialRegionFragment: Kiểm tra RegionFragment..."); // 🟢 Log bắt đầu load

        if (regionFragment == null) {
            Log.d(TAG, "loadInitialRegionFragment: Tạo mới RegionSelectFragment và ADD vào container."); // 🟢 Log tạo mới
            regionFragment = RegionSelectFragment.newInstance(regions);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, regionFragment, "TAG_REGION")
                    .commit();
        } else {
            Log.d(TAG, "loadInitialRegionFragment: RegionSelectFragment đã tồn tại. Không làm gì cả."); // 🟢 Log đã tồn tại
        }
    }

    /**
     * 2. Chuyển sang màn hình chọn Rạp (Cinema)
     */
    private void navigateToCinemaFragment(String regionId, String regionName) {
        Log.d(TAG, "navigateToCinemaFragment: Chuyển sang Cinema với RegionID=" + regionId); // 🟢 Log điều hướng

        CinemaSelectFragment cinemaFragment = CinemaSelectFragment.newInstance(movieId, regionId);

        Bundle args = cinemaFragment.getArguments();
        if (args != null) {
            args.putString("REGION_NAME", regionName);
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.setCustomAnimations(
                android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                android.R.anim.slide_in_left, android.R.anim.slide_out_right
        );

        // 1. Ẩn Region Fragment (giữ trạng thái)
        if (regionFragment != null && regionFragment.isAdded()) {
            Log.d(TAG, "navigateToCinemaFragment: HIDE RegionFragment"); // 🟢 Log ẩn Fragment cũ
            transaction.hide(regionFragment);
        } else {
            Log.w(TAG, "navigateToCinemaFragment: RegionFragment chưa được add hoặc null!"); // 🟢 Log cảnh báo
        }

        // 2. Thêm Cinema Fragment mới
        Log.d(TAG, "navigateToCinemaFragment: ADD CinemaFragment và thêm vào BackStack"); // 🟢 Log thêm Fragment mới
        transaction.add(R.id.fragment_container, cinemaFragment, "TAG_CINEMA");

        // 3. Thêm vào BackStack
        transaction.addToBackStack("TO_CINEMA");

        transaction.commit();
    }

    // --- XỬ LÝ SỰ KIỆN TỪ FRAGMENTS ---

    @Override
    public void onProvinceSelected(String regionId, String regionName) {
        Log.d(TAG, "onProvinceSelected: User chọn tỉnh " + regionName + " (" + regionId + ")"); // 🟢 Log user action
        navigateToCinemaFragment(regionId, regionName);
    }

    @Override
    public void onLocationFilterClicked() {
        Log.d(TAG, "onLocationFilterClicked: User bấm nút đổi tỉnh -> PopBackStack"); // 🟢 Log user action
        // Quay lại màn hình trước (ẩn Cinema, hiện Region)
        getOnBackPressedDispatcher().onBackPressed();
    }

    // --- Lifecycle Logs ---
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Activity bị hủy");
    }
}