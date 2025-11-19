package course.examples.cinepople.data.remote; // (Thay bằng package của bạn)

import android.content.Context;
import android.content.SharedPreferences;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import course.examples.cinepople.activity.auth.LoginActivity;
import course.examples.cinepople.app.MyApplication;

public class AuthInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
//        // Lấy SharedPreferences từ Application Context
//        SharedPreferences sharedPref = MyApplication.getAppContext()
//                .getSharedPreferences(LoginActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
//
//        String token = sharedPref.getString(LoginActivity.KEY_AUTH_TOKEN, null);

        // Lấy request gốc
        Request originalRequest = chain.request();
        Request.Builder requestBuilder = originalRequest.newBuilder();

//        // Nếu có token, thêm vào header
//        if (token != null && !token.isEmpty()) {
//            requestBuilder.addHeader("Authorization", "Bearer " + token);
//        }

        Request newRequest = requestBuilder.build();
        return chain.proceed(newRequest);
    }
}