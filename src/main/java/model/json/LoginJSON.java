package model.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

//VARIABLES REMOVED ON THIS FILE
public class LoginJSON {
    private String id;

    @JsonCreator
    public LoginJSON(@JsonProperty("id")String id) {
        this.id = id;
    }


    @JsonGetter("id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
