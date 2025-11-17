package course.examples.cinepople.activity.booking;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import course.examples.cinepople.R;
import course.examples.cinepople.activity.movie.MovieDetailsActivity;
import course.examples.cinepople.fragment.session.RegionSelectFragment;
import course.examples.cinepople.fragment.session.SessionSelectFragment;
import course.examples.cinepople.databinding.ActivitySelectionSessionBinding;

public class SelectSessionActivity extends AppCompatActivity
        implements RegionSelectFragment.OnProvinceSelectedListener,
        SessionSelectFragment.OnLocationFilterClickListener {

    private ActivitySelectionSessionBinding binding;
    private String movieId;
    private String movieTitle;
    private String currentRegionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectionSessionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        movieId = getIntent().getStringExtra(MovieDetailsActivity.MOVIE_ID_KEY);
        movieTitle = getIntent().getStringExtra(MovieDetailsActivity.MOVIE_TITLE_KEY);

        binding.toolbar.setTitle(movieTitle);
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        if (savedInstanceState == null) {
            loadProvinceFragment();
        }
    }

    private void loadSessionFragment(String regionId, String regionName, boolean addToBackStack) {
        SessionSelectFragment sessionFragment = SessionSelectFragment.newInstance(movieId, regionId);

        Bundle args = sessionFragment.getArguments();
        if (args != null) {
            args.putString("REGION_NAME", regionName);
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, sessionFragment);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }

    private void loadProvinceFragment() {
        RegionSelectFragment provinceFragment = RegionSelectFragment.newInstance();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, provinceFragment)
                .commit();
    }

    @Override
    public void onProvinceSelected(String regionId, String regionName) {
        // Lưu tên lại để dùng
        this.currentRegionName = regionName;
        // Tải Fragment bằng cả ID và Tên
        loadSessionFragment(regionId, regionName, true);
    }

    /**
     * Được gọi từ SessionSelectFragment
     */
    @Override
    public void onLocationFilterClicked() {
        // Quay lại màn hình trước (RegionSelectFragment)
        getSupportFragmentManager().popBackStack();
    }
}