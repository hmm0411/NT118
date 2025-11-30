package course.examples.cinepople.activity.booking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import course.examples.cinepople.R;
import course.examples.cinepople.databinding.ActivityReviewSummaryBinding;
import course.examples.cinepople.domain.Movie;
import course.examples.cinepople.viewmodel.ReviewSummaryViewModel;

public class ReviewSummaryActivity extends AppCompatActivity {

    private static final String TAG = "ReviewSummaryActivity";
    private ActivityReviewSummaryBinding binding;
    private ReviewSummaryViewModel viewModel;

    // Các biến dữ liệu
    private String sessionId;
    private String movieId;
    private String sessionTime;
    private String cinemaName;
    private ArrayList<String> selectedSeats;
    private double packagePrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReviewSummaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Log.d(TAG, "onCreate: Activity started.");

        viewModel = new ViewModelProvider(this).get(ReviewSummaryViewModel.class);

        // 🟢 CODE NÀY KHÔNG SET TIÊU ĐỀ, CHỈ SET CHỨC NĂNG ACTION BAR (ĐÚNG YÊU CẦU)
        setSupportActionBar(binding.appBarLayout);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
            binding.appBarLayout.setNavigationOnClickListener(v -> {
                Log.d(TAG, "User clicked Back navigation.");
                onBackPressed();
            });
        }

        getDataFromIntent();

        setupBookingDetails();
        setupPriceDetails();

        if (movieId != null) {
            Log.d(TAG, "Calling viewModel.loadMovieInfo with MovieID: " + movieId);
            viewModel.loadMovieInfo(movieId);
        } else {
            Log.w(TAG, "MovieID is null. Cannot load movie header details.");
        }
        observeViewModel();

        // 🟢 2. Xử lý nút Continue
        // Trong ReviewSummaryActivity.java

// SỬA ĐOẠN NÀY:
        binding.btnContinue.setOnClickListener(v -> {
            // Lấy tổng tiền đã tính toán
            TextView tvActualPay = binding.tvActualPay;
            double actualPay = 0.0;

            // 🟢 Lấy giá trị tiền đã được tính toán trong hàm setupPriceDetails
            // (Ta cần parse lại chuỗi VND thành số double)
            String priceText = tvActualPay.getText().toString().replace(" đ", "").replace(",", "");
            try {
                actualPay = Double.parseDouble(priceText.trim());
            } catch (NumberFormatException e) {
                // Xử lý lỗi nếu chuỗi tiền không phải là số
            }

            if (actualPay == 0.0) {
                Toast.makeText(this, "Lỗi: Tổng tiền không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            // TODO: BẠN CẦN LẤY BOOKING ID THẬT SỰ TỪ VIEWMODEL (SAU KHI GỌI CREATE BOOKING API)
            String finalBookingId = "BOOKING_XYZ_123"; // ⚠️ THAY BẰNG BOOKING ID THẬT SỰ

            Intent intent = new Intent(this, PaymentActivity.class);

            // Truyền tổng tiền
            //intent.putExtra(PaymentActivity.TOTAL_PAYMENT_KEY, actualPay);

            // Truyền ID Booking thật sự (để Backend biết giao dịch nào cần thanh toán)
            //intent.putExtra(PaymentActivity.BOOKING_ID_KEY, finalBookingId);

            startActivity(intent);
        });
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        sessionId = intent.getStringExtra("SESSION_ID");
        sessionTime = intent.getStringExtra("SESSION_TIME");
        selectedSeats = intent.getStringArrayListExtra("SELECTED_SEATS");
        packagePrice = intent.getDoubleExtra("FINAL_PRICE", 0.0);

        cinemaName = intent.getStringExtra("CINEMA_NAME");
        movieId = intent.getStringExtra("MOVIE_ID");

        Log.d(TAG, "Intent Received -> Seats: " + (selectedSeats != null ? selectedSeats.size() : 0) +
                ", Price: " + packagePrice + ", Time: " + sessionTime);
    }

    private void setupBookingDetails() {
        Log.d(TAG, "setupBookingDetails: Binding booking info.");

        setRowData(binding.rowCinema.getRoot(), "Cinema", getSafeString(cinemaName));
        setRowData(binding.rowPackage.getRoot(), "Package", "Standard");

        String seatsStr = (selectedSeats != null && !selectedSeats.isEmpty()) ? String.join(", ", selectedSeats) : "Chưa chọn";
        setRowData(binding.rowSeats.getRoot(), "Seat(s)", seatsStr);

        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.US);
        String currentDate = sdf.format(new Date());
        setRowData(binding.rowDate.getRoot(), "Date", getSafeString(currentDate));

        String endTime = calculateEndTime(sessionTime, 120);
        setRowData(binding.rowHours.getRoot(), "Hours", getSafeString(sessionTime) + " - " + endTime);
    }

    private void setupPriceDetails() {
        int seatCount = selectedSeats != null ? selectedSeats.size() : 0;
        double vat = packagePrice * 0.1;
        double actualPay = packagePrice + vat;

        Log.d(TAG, "Price Calc: Subtotal=" + packagePrice + ", VAT=" + vat + ", Total=" + actualPay);

        binding.labelPackagePrice.setText("Standard (x" + seatCount + ")");
        binding.tvPackagePrice.setText(formatCurrency(packagePrice));
        binding.tvVat.setText(formatCurrency(vat));
        binding.tvActualPay.setText(formatCurrency(actualPay));
    }

    private void observeViewModel() {
        viewModel.getMovieData().observe(this, movie -> {
            if (movie != null) {
                Log.d(TAG, "Observer: Movie data received. Title: " + movie.getTitle());

                updateMovieHeader(movie);

                try {
                    int durationInt = Integer.parseInt(movie.getDuration());
                    String endTime = calculateEndTime(sessionTime, durationInt);

                    TextView tvValueHours = binding.rowHours.getRoot().findViewById(R.id.tv_value);
                    tvValueHours.setText(sessionTime + " - " + endTime);

                    Log.d(TAG, "Hours updated with duration: " + movie.getDuration());
                } catch (NumberFormatException | NullPointerException e) {
                    Log.e(TAG, "Error parsing duration for end time: " + e.getMessage());
                    TextView tvValueHours = binding.rowHours.getRoot().findViewById(R.id.tv_value);
                    tvValueHours.setText(sessionTime + " - " + "--:--");
                }
            } else {
                Log.w(TAG, "Observer: Movie object is NULL.");
            }
        });

        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Log.e(TAG, "Observer Error from ViewModel: " + error);
                Toast.makeText(this, "Lỗi tải phim: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateMovieHeader(Movie movie) {
        Log.d(TAG, "Binding Movie Header data...");

        RequestOptions requestOptions = new RequestOptions().transform(new CenterCrop(), new RoundedCorners(24));
        Glide.with(this)
                .load(movie.getPosterUrl())
                .apply(requestOptions)
                .into(binding.layoutMovieInfo.imgPoster);

        binding.layoutMovieInfo.tvMovieTitle.setText(getSafeString(movie.getTitle()));

        String genres = (movie.getGenres() != null && !movie.getGenres().isEmpty())
                ? String.join(", ", movie.getGenres()) : "N/A";
        binding.layoutMovieInfo.tvGenre.setText("Genre: " + genres);

        binding.layoutMovieInfo.tvDuration.setText("Duration: " + getSafeString(movie.getDuration()) + " minutes");
        binding.layoutMovieInfo.tvAgeRating.setText(getSafeString(movie.getAgeRating()));
    }

    // --- Helper Methods ---

    private String getSafeString(String input) {
        return input != null && !input.isEmpty() ? input : "N/A";
    }

    private void setRowData(View rootView, String label, String value) {
        TextView tvLabel = rootView.findViewById(R.id.tv_label);
        TextView tvValue = rootView.findViewById(R.id.tv_value);
        if (tvLabel != null) tvLabel.setText(label);
        if (tvValue != null) tvValue.setText(value);
    }

    private String formatCurrency(double amount) {
        return String.format(Locale.US, "%,.0f đ", amount);
    }

    private String calculateEndTime(String startTime, int durationMinutes) {
        if (startTime == null) return "--:--";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US);
            Date date = sdf.parse(startTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MINUTE, durationMinutes);
            return sdf.format(calendar.getTime());
        } catch (Exception e) {
            return "--:--";
        }
    }

}