package model.json;

import com.fasterxml.jackson.annotation.JsonGetter;
import model.json.firestore.investments.Evaluation;

import java.text.SimpleDateFormat;
import java.util.List;


//VARIABLES REMOVED ON THIS FILES
public class Opportunities {

    private String _id;


    public Opportunities(String _id) {

        this._id = _id;
    }


    @JsonGetter("_id")
    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

}
