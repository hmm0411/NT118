package course.examples.cinepople.data.repository;

import course.examples.cinepople.data.remote.ApiClient;
import course.examples.cinepople.data.remote.ApiService;
import course.examples.cinepople.domain.LoginRequest;
import course.examples.cinepople.domain.LoginResponse;
import retrofit2.Call;
import retrofit2.Callback;

public class UserRepository {

    private ApiService apiService;

    public UserRepository() {
        this.apiService = ApiClient.getClient().create(ApiService.class);
    }

    public void loginUser(String email, String password, Callback<LoginResponse> callback) {
        LoginRequest request = new LoginRequest(email, password);
        apiService.login(request).enqueue(callback);
    }
}