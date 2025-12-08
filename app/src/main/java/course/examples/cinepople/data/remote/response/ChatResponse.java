package course.examples.cinepople.data.remote.response;

import com.google.gson.annotations.SerializedName;

public class ChatResponse {
    @SerializedName("response")
    private String botMessage;

    public String getResponse() {
        return botMessage;
    }

    public void setResponse(String response) {
        this.botMessage = response;
    }
}
