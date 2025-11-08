package course.examples.cinepople.ui.seats;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

//import course.examples.cinepople.databinding.ActivitySelectSeatsBinding;

public class SelectSeatsActivity extends AppCompatActivity {

//    private ActivitySelectSeatsBinding binding; // Dùng ViewBinding
//    private SelectSeatsViewModel viewModel;
//    private SeatsAdapter seatsAdapter;
//
//    private static final int GRID_SPAN_COUNT = 10; // 10 ghế mỗi hàng
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        // Khởi tạo ViewBinding
//        binding = ActivitySelectSeatsBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        // Khởi tạo ViewModel
//        viewModel = new ViewModelProvider(this).get(SelectSeatsViewModel.class);
//
//        // Cài đặt RecyclerView
//        setupRecyclerView();
//
//        // Lắng nghe (Observe) các thay đổi từ ViewModel
//        observeViewModel();
//
//        // Xử lý sự kiện click
//        setupClickListeners();
//
//        // Tải dữ liệu ghế (giả lập)
//        viewModel.loadSeats("session_123");
//    }
//
//    private void setupRecyclerView() {
//        // Tạo listener và truyền nó vào Adapter
//        seatsAdapter = new SeatsAdapter(seat -> {
//            // Khi một ghế được click, gọi ViewModel
//            viewModel.onSeatClicked(seat);
//        });
//
//        binding.rvSeats.setLayoutManager(new GridLayoutManager(this, GRID_SPAN_COUNT));
//        binding.rvSeats.setAdapter(seatsAdapter);
//        binding.rvSeats.setHasFixedSize(true);
//    }
//
//    private void observeViewModel() {
//        // 1. Lắng nghe thay đổi của bản đồ ghế
//        viewModel.getSeatMap().observe(this, seats -> {
//            // Cập nhật danh sách ghế lên Adapter
//            seatsAdapter.submitList(seats);
//        });
//
//        // 2. Lắng nghe thay đổi của tổng tiền
//        viewModel.getTotalPrice().observe(this, totalPrice -> {
//            // Cập nhật TextView tổng tiền
//            binding.tvTotalPrice.setText(String.format(Locale.US, "$%.2f", totalPrice));
//        });
//
//        // 3. Lắng nghe thay đổi của danh sách ghế đã chọn
//        viewModel.getSelectedSeats().observe(this, selectedSeats -> {
//            // Cập nhật TextView danh sách ghế
//            if (selectedSeats == null || selectedSeats.isEmpty()) {
//                binding.tvSelectedSeats.setText("None");
//            } else {
//                // Biến danh sách [Seat(id="A1"), Seat(id="B2")] thành chuỗi "A1, B2"
//                String seatsText = selectedSeats.stream()
//                        .map(Seat::getId)
//                        .collect(Collectors.joining(", "));
//                binding.tvSelectedSeats.setText(seatsText);
//            }
//        });
//    }
//
//    private void setupClickListeners() {
//        // Nút quay lại
//        binding.toolbar.setNavigationOnClickListener(v -> finish());
//
//        // Nút Continue
//        binding.btnContinue.setOnClickListener(v -> {
//            List<Seat> selected = viewModel.getSelectedSeats().getValue();
//            if (selected == null || selected.isEmpty()) {
//                Toast.makeText(this, "Please select at least one seat.", Toast.LENGTH_SHORT).show();
//            } else {
//                // TODO: Chuyển sang Activity Thanh toán (CheckoutActivity)
//                Toast.makeText(this, "Proceeding with " + selected.size() + " seats.", Toast.LENGTH_SHORT).show();
//                // Intent intent = new Intent(this, CheckoutActivity.class);
//                // intent.putExtra("totalPrice", viewModel.getTotalPrice().getValue());
//                // ... (truyền danh sách ghế)
//                // startActivity(intent);
//            }
//        });
//    }
}