package course.examples.cinepople.fragment.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import course.examples.cinepople.R;
import course.examples.cinepople.activity.auth.LoginActivity;
import course.examples.cinepople.databinding.FragmentMainProfileBinding;


public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    private FragmentMainProfileBinding binding;
    private FirebaseAuth mAuth;

    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        loadUserProfile();

        binding.optionPersonalInfo.setOnClickListener(v -> {

            Fragment personalInfoFragment = new PersonalInfoFragment();
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            transaction.setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
            );
            transaction.replace(R.id.fragment_container, personalInfoFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        binding.btnLogout.setOnClickListener(v -> {
            logout();
        });
    }


    private void loadUserProfile() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            logout();
            return;
        }

        String email = currentUser.getEmail();
        if (email != null && !email.isEmpty()) {
            binding.tvUserEmail.setText(email);
        } else {
            binding.tvUserEmail.setText("No email provided");
        }

        // 2. Lấy Tên (Name) từ Firestore
        String uid = currentUser.getUid();
        db.collection("users").document(uid).get()
                .addOnCompleteListener(task -> {
                    // Kiểm tra Fragment còn tồn tại
                    if (!isAdded() || binding == null) {
                        return;
                    }

                    String nameToShow = "User"; // Tên mặc định

                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            String firestoreName = document.getString("name");
                            if (firestoreName != null && !firestoreName.isEmpty()) {
                                nameToShow = firestoreName;
                            }
                        }
                    } else {
                        Log.w(TAG, "Failed to load user profile from Firestore.", task.getException());
                    }

                    // 3. Nếu không có tên trong Firestore, thử lấy DisplayName từ Auth
                    if (nameToShow.equals("User") && currentUser.getDisplayName() != null && !currentUser.getDisplayName().isEmpty()) {
                        nameToShow = currentUser.getDisplayName();
                    }

                    // 4. Hiển thị tên
                    binding.tvUserName.setText(nameToShow);

                    // (Tùy chọn) Tải ảnh đại diện (avatar)
                    // if (currentUser.getPhotoUrl() != null) {
                    //    Glide.with(this).load(currentUser.getPhotoUrl()).into(binding.ivAvatar);
                    // }
                });
    }

    /**
     * SỬA: Hàm mới để xử lý đăng xuất
     */
    private void logout() {
        mAuth.signOut();
        // Đảm bảo getContext() không null
        if (getContext() != null) {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}