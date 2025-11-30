package course.examples.cinepople.viewmodel;

import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {
//
//    private UserRepository userRepository;
//    private MutableLiveData<LoginResponse> loginSuccess = new MutableLiveData<>();
//    private MutableLiveData<String> loginError = new MutableLiveData<>();
//
//    public LoginViewModel() {
//        this.userRepository = new UserRepository();
//    }
//
//    public LiveData<LoginResponse> getLoginSuccess() {
//        return loginSuccess;
//    }
//    public LiveData<String> getLoginError() {
//        return loginError;
//    }
//
//    public void login(String email, String password) {
//        userRepository.loginUser(email, password, new Callback<LoginResponse>() {
//            @Override
//            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    loginSuccess.postValue(response.body());
//                } else {
//                    // Xử lý lỗi từ server (VD: 401 Sai mật khẩu)
//                    loginError.postValue("Login failed: " + response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<LoginResponse> call, Throwable t) {
//                // Xử lý lỗi mạng (VD: Mất kết nối)
//                loginError.postValue("Network error: " + t.getMessage());
//            }
//        });
//    }
}