package course.examples.cinepople.ui.main.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.firebase.auth.FirebaseAuth;

import course.examples.cinepople.R;
import course.examples.cinepople.ui.auth.login.LoginActivity;
import course.examples.cinepople.databinding.FragmentMainProfileBinding;


public class ProfileFragment extends Fragment {

    private FragmentMainProfileBinding binding;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // 3. "Nạp" layout XML bằng ViewBinding
        binding = FragmentMainProfileBinding.inflate(inflater, container, false);

        // 4. Trả về view gốc của layout
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        binding.btnEditProfile.setOnClickListener(v -> {

            // 1. Tạo một đối tượng (instance) của Fragment bạn muốn đến
            Fragment personalInfoFragment = new PersonalInfoFragment();

            // 2. Lấy FragmentManager của Activity (chính là MainActivity)
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

            // 3. Bắt đầu một "giao dịch" (transaction)
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            // (Tùy chọn) Thêm hiệu ứng trượt (slide)
            transaction.setCustomAnimations(
                    R.anim.slide_in_right,  // Fragment mới đi vào
                    R.anim.slide_out_left,  // Fragment cũ (Profile) đi ra
                    R.anim.slide_in_left,   // (Khi nhấn Back) Fragment cũ (Profile) quay lại
                    R.anim.slide_out_right  // (Khi nhấn Back) Fragment mới đi ra
            );

            // 4. Thay thế nội dung của FrameLayout trong MainActivity
            // (R.id.fragment_container là ID của FrameLayout trong activity_main.xml)
            transaction.replace(R.id.fragment_container, personalInfoFragment);

            // 5. (CỰC KỲ QUAN TRỌNG) Thêm vào Back Stack
            // Bước này "ghi nhớ" rằng bạn đang ở ProfileFragment
            // Nó cho phép nút Back của điện thoại hoạt động
            transaction.addToBackStack(null);

            // 6. Thực thi giao dịch
            transaction.commit();
        });

         binding.btnLogout.setOnClickListener(v -> {
             mAuth.signOut();
             Intent intent = new Intent(getContext(), LoginActivity.class);
             intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
             startActivity(intent);
         });
    }

    // 5. (Rất quan trọng) Dọn dẹp binding để tránh rò rỉ bộ nhớ
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}