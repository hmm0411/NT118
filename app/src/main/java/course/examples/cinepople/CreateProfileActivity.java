package course.examples.cinepople;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
// (Import cho Firestore sẽ được thêm sau)

import course.examples.cinepople.databinding.ActivityCreateProfileBinding;
import course.examples.cinepople.home.HomeFragment;

public class CreateProfileActivity extends AppCompatActivity {

    // 1. Khai báo View Binding
    private ActivityCreateProfileBinding binding;

    // (Khai báo Firebase Auth và Firestore)
    private FirebaseAuth mAuth;
    // private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 2. "Thổi phồng" layout bằng View Binding
        binding = ActivityCreateProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo Firebase
        mAuth = FirebaseAuth.getInstance();
        // db = FirebaseFirestore.getInstance();

        // 3. Xử lý nút "Return" (Quay lại)
        binding.btnReturnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chỉ cần đóng Activity này
                // (Lưu ý: AuthenticationActivity đã bị đóng,
                // nên nhấn back ở đây có thể sẽ thoát app)
                finish();
            }
        });

        // 4. Xử lý nút "NEXT"
        binding.btnNextProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gọi hàm lưu thông tin
                saveUserProfile();
            }
        });

        // (Bạn có thể thêm onClickListener cho et_birthday để mở DatePickerDialog)
    }

    /**
     * Hàm lưu thông tin hồ sơ người dùng
     */
    private void saveUserProfile() {
        String fullName = binding.etFullName.getText().toString().trim();
        String phone = binding.etPhoneNumber.getText().toString().trim();
        String birthday = binding.etBirthday.getText().toString().trim();

        // (Kiểm tra dữ liệu đầu vào: không được trống, v.v.)
        if (fullName.isEmpty()) {
            binding.etFullName.setError("Vui lòng nhập họ tên");
            return;
        }

        // (Logic để lưu thông tin này vào Cloud Firestore sẽ ở đây)
        // ...

        Toast.makeText(this, "Hồ sơ đã được lưu!", Toast.LENGTH_SHORT).show();

        // 5. Chuyển sang HomeActivity
        Intent intent = new Intent(CreateProfileActivity.this, HomeFragment.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish(); // Đóng ProfileActivity
    }
}