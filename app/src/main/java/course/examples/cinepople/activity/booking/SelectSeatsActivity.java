package course.examples.cinepople.activity.booking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log; // 🟢 Import Log
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import course.examples.cinepople.adapter.SeatsAdapter;
import course.examples.cinepople.databinding.ActivitySelectionSeatsBinding;
import course.examples.cinepople.viewmodel.SelectSeatsViewModel;

public class SelectSeatsActivity extends AppCompatActivity {

    // 🟢 1. KHAI BÁO TAG
    private static final String TAG = "SelectSeatsActivity";

    private ActivitySelectionSeatsBinding binding;
    private SelectSeatsViewModel viewModel;
    private SeatsAdapter seatsAdapter;

    public static final String SESSION_ID_KEY = "SESSION_ID";
    public static final String FINAL_PRICE_KEY = "FINAL_PRICE";
    public static final String SELECTED_SEATS_KEY = "SELECTED_SEATS";
    public static final String SESSION_TIME_KEY = "SESSION_TIME";
    private static final String MOVIE_ID_KEY = "MOVIE_ID";

    private String sessionId;
    private String sessionTime;
    private String movieId;

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

        movieId = getIntent().getStringExtra("MOVIE_ID");
        String movieTitle = getIntent().getStringExtra("MOVIE_TITLE");

        // 🟢 Log kiểm tra dữ liệu đầu vào
        Log.d(TAG, "onCreate: Activity Started.");
        Log.d(TAG, "Intent Data -> ID: " + sessionId + " | Time: " + sessionTime + " | Cinema: " + cinemaName + " | Movie: " + movieTitle);

        // 2. Cài đặt Toolbar
        binding.toolbarTitle.setText(cinemaName);
        binding.toolbarSubtitle.setText(movieTitle);
        binding.toolbar.setNavigationOnClickListener(v -> {
            Log.d(TAG, "User clicked Back button");
            onBackPressed();
        });

        // 3. Setup RecyclerView
        setupRecyclerView();

        // 4. Load dữ liệu
        if (sessionId != null) {
            Log.d(TAG, "Calling viewModel.loadSessionData with ID: " + sessionId);
            viewModel.loadSessionData(sessionId);
        } else {
            Log.e(TAG, "Error: Session ID is NULL!");
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
        // Cập nhật danh sách ghế
        viewModel.getSeatList().observe(this, seats -> {
            if (seats != null) {
                Log.d(TAG, "Observer: Received " + seats.size() + " seats from API.");
                seatsAdapter.submitList(seats);
            } else {
                Log.w(TAG, "Observer: Seat list is NULL");
            }
        });

        // Cập nhật danh sách ghế đang chọn
        viewModel.getSelectedSeatsIds().observe(this, selectedIds -> {
            Log.d(TAG, "Observer: Selected Seats changed -> " + selectedIds);

            seatsAdapter.updateSelectedIds(selectedIds);

            if (selectedIds == null || selectedIds.isEmpty()) {
                binding.tvSelectedSeats.setText("Chưa chọn ghế");
                binding.btnContinue.setEnabled(false);
                binding.btnContinue.setAlpha(0.5f);
            } else {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    String seatsText = selectedIds.stream().collect(Collectors.joining(" "));
                    binding.tvSelectedSeats.setText(seatsText);
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (String seat : selectedIds) {
                        sb.append(seat).append(" ");
                    }
                    binding.tvSelectedSeats.setText(sb.toString().trim());
                }
                binding.btnContinue.setEnabled(true);
                binding.btnContinue.setAlpha(1.0f);
            }
        });

        // Cập nhật tổng tiền
        viewModel.getTotalPrice().observe(this, price -> {
            Log.d(TAG, "Observer: Total Price updated -> " + price);
            binding.tvTotalPrice.setText(String.format(Locale.US, "%,.0f đ", price));
        });

        // Lắng nghe lỗi
        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Log.e(TAG, "Observer Error: " + error);
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onContinueClicked() {
        List<String> selectedSeats = viewModel.getSelectedSeatsIds().getValue();
        Double totalPrice = viewModel.getTotalPrice().getValue();

        Log.d(TAG, "User clicked Continue. Seats: " + selectedSeats + ", Price: " + totalPrice);

        if (selectedSeats == null || selectedSeats.isEmpty() || totalPrice == null || totalPrice == 0) {
            Toast.makeText(this, "Vui lòng chọn ít nhất một ghế.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, ReviewSummaryActivity.class);

        intent.putExtra(SESSION_ID_KEY, sessionId);
        intent.putExtra(SESSION_TIME_KEY, sessionTime);
        intent.putStringArrayListExtra(SELECTED_SEATS_KEY, new ArrayList<>(selectedSeats));
        intent.putExtra(FINAL_PRICE_KEY, totalPrice);
        intent.putExtra(MOVIE_ID_KEY, movieId);

        intent.putExtra("CINEMA_NAME", binding.toolbarTitle.getText().toString());
        intent.putExtra("MOVIE_TITLE", binding.toolbarSubtitle.getText().toString());;

        Log.d(TAG, "Navigating to ReviewSummaryActivity...");
        startActivity(intent);
    }

    // Lifecycle Logs
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Activity destroyed.");
    }
}