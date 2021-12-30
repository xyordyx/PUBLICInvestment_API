package model.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseJSON {
    private boolean status;
    private String message;

    @JsonCreator
    public ResponseJSON(@JsonProperty("status") boolean status, @JsonProperty("message") String message) {
        this.status = status;
        this.message = message;
    }

    @JsonGetter("status")
    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @JsonGetter("message")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
