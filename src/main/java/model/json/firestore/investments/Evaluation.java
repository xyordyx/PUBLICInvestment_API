package model.json.firestore.investments;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Evaluation {
    @JsonProperty("_id")
    private String _id;
    @JsonProperty("rating")
    private String rating;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
