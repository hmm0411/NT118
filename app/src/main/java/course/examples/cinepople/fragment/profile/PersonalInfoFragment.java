package course.examples.cinepople.fragment.profile;

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
import com.google.firebase.firestore.FirebaseFirestore;

import course.examples.cinepople.databinding.FragmentProfilePersonalInfoBinding;
import course.examples.cinepople.domain.User;

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
            loadUserProfileData();
        } else {
            Toast.makeText(getContext(), "Lỗi: Người dùng chưa đăng nhập", Toast.LENGTH_SHORT).show();
        }
    }


    private void loadUserProfileData() {
        if (currentUserId == null || getContext() == null) return;

        db.collection("users").document(currentUserId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (!isAdded()) return;

                    if (documentSnapshot.exists()) {
                        User userProfile = documentSnapshot.toObject(User.class);

                        if (userProfile != null) {
                            // SỬA: Dùng getName() (tên đã sửa) và getDob()
                            binding.txtFullName.setText(userProfile.getName());
                            binding.txtEmail.setText(userProfile.getEmail());
                            binding.txtPhone.setText(userProfile.getPhone());
                            binding.txtBirthday.setText(userProfile.getDob());
                            binding.txtGender.setText(userProfile.getGender());
                        }
                    } else {
                        Toast.makeText(getContext(), "Vui lòng cập nhật thông tin của bạn", Toast.LENGTH_SHORT).show();

                        if (mAuth.getCurrentUser() != null) {
                            binding.txtEmail.setText(mAuth.getCurrentUser().getEmail());
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    if (!isAdded()) return;
                    Log.e(TAG, "Lỗi khi tải dữ liệu người dùng", e);
                    Toast.makeText(getContext(), "Lỗi: Không thể tải dữ liệu hồ sơ.", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}