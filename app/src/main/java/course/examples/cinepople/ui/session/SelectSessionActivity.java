package course.examples.cinepople.ui.session;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import course.examples.cinepople.R;
import course.examples.cinepople.databinding.ActivitySelectionSessionBinding;
import course.examples.cinepople.ui.movies.MovieDetailsActivity;

// SỬA: Đảm bảo import đúng RegionSelectFragment
import course.examples.cinepople.ui.session.RegionSelectFragment;

import course.examples.cinepople.ui.session.SessionSelectFragment;

public class SelectSessionActivity extends AppCompatActivity
        implements RegionSelectFragment.OnProvinceSelectedListener,
        SessionSelectFragment.OnLocationFilterClickListener {

    private ActivitySelectionSessionBinding binding;
    private String movieId;
    private String movieTitle;
    // SỬA: Đã xóa biến currentProvinceName

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

    private void loadSessionFragment(String provinceName, boolean addToBackStack) {
        // SỬA: Đã xóa dòng "this.currentProvinceName = provinceName;"

        SessionSelectFragment sessionFragment = SessionSelectFragment.newInstance(movieId, provinceName);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, sessionFragment);

        if (addToBackStack) {
            transaction.addToBackStack(null); // Thêm vào back stack để có thể quay lại
        }

        transaction.commit();
    }

    private void loadProvinceFragment() {
        // SỬA: Gọi newInstance() không có tham số
        RegionSelectFragment provinceFragment = RegionSelectFragment.newInstance();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, provinceFragment)
                .commit();
    }


    @Override
    public void onProvinceSelected(String provinceName) {
        loadSessionFragment(provinceName, true);
    }

//    @Override
//    public void onLocationFilterClicked() {
//        getSupportFragmentManager().popBackStack();
//    }
}