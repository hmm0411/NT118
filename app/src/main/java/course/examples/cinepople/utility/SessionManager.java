package course.examples.cinepople.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    public static final String APP_PREFERENCES = "AppSession";

    private static final String KEY_LOGGED_IN = "LOGGED_IN";
    private static final String KEY_AUTH_TOKEN = "AUTH_TOKEN";
    private static final String KEY_USER_EMAIL = "USER_EMAIL";
    private static final String KEY_DARK_MODE = "IS_DARK_MODE";

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    // --- LƯU EMAIL LOGIN FIREBASE ---
    public static void saveUserSession(Context context, String email) {
        getPrefs(context).edit()
                .putBoolean(KEY_LOGGED_IN, true)
                .putString(KEY_USER_EMAIL, email)
                .apply();
    }

    // --- DARK MODE ---
    public static void setDarkMode(Context context, boolean isDark) {
        getPrefs(context).edit().putBoolean(KEY_DARK_MODE, isDark).apply();
    }

    public static boolean isDarkMode(Context context) {
        return getPrefs(context).getBoolean(KEY_DARK_MODE, false);
    }

    // --- BACKEND TOKEN ---
    public static void saveAuthToken(Context context, String token) {
        getPrefs(context).edit()
                .putString(KEY_AUTH_TOKEN, token)
                .putBoolean(KEY_LOGGED_IN, true)
                .apply();
    }

    public static String getAuthToken(Context context) {
        return getPrefs(context).getString(KEY_AUTH_TOKEN, null);
    }

    // --- KIỂM TRA LOGIN ---
    public static boolean isLoggedIn(Context context) {
        return getPrefs(context).getBoolean(KEY_LOGGED_IN, false);
    }

    // Clear session (logout)
    public static void clearSession(Context context) {
        getPrefs(context).edit().clear().apply();
    }

    // --- LOGOUT ---
    public static void logout(Context context) {
        getPrefs(context).edit().clear().apply();
    }
}
