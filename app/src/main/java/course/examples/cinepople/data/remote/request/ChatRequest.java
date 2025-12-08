package course.examples.cinepople.data.remote.request;

import com.google.gson.annotations.SerializedName;

public class ChatRequest {

    @SerializedName("message")
    private String message;

    public ChatRequest(String message) {
        this.message = message;
    }
}
