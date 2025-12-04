package course.examples.cinepople.data.remote.response;

import java.util.List;

import course.examples.cinepople.domain.Booking;

public class BookingResponse {
    public boolean success;
    public List<Booking> data;
    public String message;

    public boolean isSuccess() {
        return success;
    }

    public List<Booking> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
