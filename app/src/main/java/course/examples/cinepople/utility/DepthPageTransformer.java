package course.examples.cinepople.utility;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;


public class DepthPageTransformer implements ViewPager2.PageTransformer {

    // Góc nghiêng tối đa (20 độ)
    private static final float ROTATION_ANGLE = 40.0f;

    @Override
    public void transformPage(@NonNull View view, float position) {

        float pageWidth = view.getWidth();

        if (position < -1) { // Trang quá xa bên trái
            view.setAlpha(0f);
        } else if (position <= 0) { // Trang hiện tại hoặc trượt vào (position = 0)
            // Đặt lại các thuộc tính về trạng thái mặc định (thẳng đứng)
            view.setAlpha(1f);
            view.setTranslationX(0f);
            view.setRotationY(0f);
        } else if (position <= 1) { // Trang bên phải đang trượt ra
            // Trang này sẽ bị nghiêng (xoay)

            // Đặt độ xoay (rotationY) nghịch đảo với position để nghiêng vào trong.
            // Ví dụ: position 0.5f -> nghiêng -10 độ
            view.setRotationY(-position * ROTATION_ANGLE);

            // Điều chỉnh vị trí ngang (TranslationX) để hiệu ứng xoay đẹp hơn,
            // tránh việc bị cắt hoặc bị dính vào trang bên cạnh.
            view.setTranslationX(pageWidth * -position * 0.5f);

            view.setAlpha(1f); // Giữ Alpha 1f

        } else { // Trang quá xa bên phải
            view.setAlpha(0f);
        }
    }
}