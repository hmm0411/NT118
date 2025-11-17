package course.examples.cinepople.activity.booking;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import course.examples.cinepople.viewmodel.SelectSeatsViewModel;
import course.examples.cinepople.adapter.SeatsAdapter;
import course.examples.cinepople.databinding.ActivitySelectionSeatsBinding;

public class SelectSeatsActivity extends AppCompatActivity {

    private ActivitySelectionSeatsBinding binding;
    private SelectSeatsViewModel viewModel;
    private SeatsAdapter seatsAdapter;

    public static final String SESSION_ID_KEY = "SESSION_ID";
    public static final String FINAL_PRICE_KEY = "FINAL_PRICE";
    public static final String SELECTED_SEATS_KEY = "SELECTED_SEATS";
    public static final String SESSION_TIME_KEY = "SESSION_TIME";

    private String sessionId;
    private String sessionTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectionSeatsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(SelectSeatsViewModel.class);

        // 1. Nhận dữ liệu từ Intent
        sessionId = getIntent().getStringExtra(SESSION_ID_KEY);
        sessionTime = getIntent().getStringExtra(SESSION_TIME_KEY);
        String cinemaName = getIntent().getStringExtra("CINEMA_NAME");
        String movieTitle = getIntent().getStringExtra("MOVIE_TITLE");

        // 2. Cài đặt Toolbar
        binding.toolbarTitle.setText(cinemaName);
        binding.toolbarSubtitle.setText(movieTitle);
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // 3. Setup RecyclerView
        setupRecyclerView();

        if (sessionId != null) {
            viewModel.loadSessionData(sessionId);
        } else {
            Toast.makeText(this, "Lỗi: Không tìm thấy ID suất chiếu.", Toast.LENGTH_LONG).show();
            finish();
        }

        observeViewModel();

        binding.btnContinue.setOnClickListener(v -> onContinueClicked());
    }

    private void setupRecyclerView() {
        seatsAdapter = new SeatsAdapter(viewModel);
        binding.rvSeats.setLayoutManager(new GridLayoutManager(this, 10)); // 10 cột
        binding.rvSeats.setAdapter(seatsAdapter);
    }

    private void observeViewModel() {
        viewModel.getSeatMap().observe(this, seatsAdapter::submitList);

        viewModel.getTotalPrice().observe(this, price -> {
            binding.tvTotalPrice.setText(String.format(Locale.US, "$%.2f", price));
        });

        viewModel.getSelectedSeatsIds().observe(this, selectedIds -> {
            if (selectedIds == null || selectedIds.isEmpty()) {
                binding.tvSelectedSeats.setText("None");
                binding.btnContinue.setEnabled(false);
            } else {
                String seatsText = selectedIds.stream().collect(Collectors.joining(" "));
                binding.tvSelectedSeats.setText(seatsText);
                binding.btnContinue.setEnabled(true);
            }
        });
    }

    private void onContinueClicked() {
        List<String> selectedSeats = viewModel.getSelectedSeatsIds().getValue();
        Double totalPrice = viewModel.getTotalPrice().getValue();

        if (selectedSeats == null || selectedSeats.isEmpty() || totalPrice == null || totalPrice == 0) {
            Toast.makeText(this, "Vui lòng chọn ít nhất một ghế.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, ReviewSummaryActivity.class);

        intent.putExtra(SESSION_ID_KEY, sessionId);
        intent.putExtra(SESSION_TIME_KEY, sessionTime);
        intent.putStringArrayListExtra(SELECTED_SEATS_KEY, new ArrayList<>(selectedSeats));
        intent.putExtra(FINAL_PRICE_KEY, totalPrice);

        intent.putExtra("CINEMA_NAME", binding.toolbarTitle.getText().toString());
        intent.putExtra("MOVIE_TITLE", binding.toolbarSubtitle.getText().toString());

        startActivity(intent);
    }
}