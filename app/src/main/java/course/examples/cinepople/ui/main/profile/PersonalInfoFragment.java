package course.examples.cinepople.ui.main.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import course.examples.cinepople.databinding.FragmentProfilePersonalInfoBinding;
import course.examples.cinepople.data.User;

public class PersonalInfoFragment extends Fragment {

    private FragmentProfilePersonalInfoBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String currentUserId;

    private static final String TAG = "PersonalInfoFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfilePersonalInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUserId = currentUser.getUid();
            // Chỉ gọi hàm tải dữ liệu
            loadUserProfileData();
        } else {
            Toast.makeText(getContext(), "Lỗi: Người dùng chưa đăng nhập", Toast.LENGTH_SHORT).show();
        }

        // <<< ĐÃ XÓA >>>
        // Đã xóa sự kiện click cho btnSave vì không còn chức năng lưu
    }

    /**
     * Hàm này TẢI dữ liệu từ Firestore và HIỂN THỊ lên các TextView
     */
    private void loadUserProfileData() {

        db.collection("users").document(currentUserId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User userProfile = documentSnapshot.toObject(User.class);

                        if (userProfile != null) {
                            binding.txtFullName.setText(userProfile.getFullName());
                            binding.txtEmail.setText(userProfile.getEmail());
                            binding.txtPhone.setText(userProfile.getPhone());
                            binding.txtBirthday.setText(userProfile.getBirthday());
                            binding.txtGender.setText(userProfile.getGender());
                        }
                    } else {
                        Toast.makeText(getContext(), "Vui lòng cập nhật thông tin của bạn", Toast.LENGTH_SHORT).show();
                        binding.txtEmail.setText(mAuth.getCurrentUser().getEmail());
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Lỗi khi tải dữ liệu người dùng", e);
                    Toast.makeText(getContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}