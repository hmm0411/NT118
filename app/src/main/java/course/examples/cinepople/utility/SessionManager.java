package course.examples.cinepople.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    public static final String APP_PREFERENCES = "AppSession";
    private static final String KEY_LOGGED_IN = "LOGGED_IN";
    private static final String KEY_USER_EMAIL = "USER_EMAIL";

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    // Lưu phiên sau khi đăng nhập thành công
    public static void saveUserSession(Context context, String email) {
        getPrefs(context).edit()
                .putBoolean(KEY_LOGGED_IN, true)
                .putString(KEY_USER_EMAIL, email)
                .apply();
    }

    // Kiểm tra trạng thái đăng nhập
    public static boolean isLoggedIn(Context context) {
        return getPrefs(context).getBoolean(KEY_LOGGED_IN, false);
    }

    // Xóa phiên khi đăng xuất
    public static void clearSession(Context context) {
        getPrefs(context).edit().clear().apply();
    }

    private static final String KEY_DARK_MODE = "IS_DARK_MODE";

    // Lưu trạng thái Dark Mode
    public static void setDarkMode(Context context, boolean isDark) {
        getPrefs(context).edit().putBoolean(KEY_DARK_MODE, isDark).apply();
    }

    // Lấy trạng thái Dark Mode (Mặc định là false - Light mode)
    public static boolean isDarkMode(Context context) {
        return getPrefs(context).getBoolean(KEY_DARK_MODE, false);
    }
}