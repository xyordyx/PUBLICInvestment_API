package model.json.firestore.instances;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "integerValue"
})

public class Version {

    @JsonProperty("integerValue")
    private int integerValue;

    @JsonProperty("integerValue")
    public int getIntegerValue() {
        return integerValue;
    }

    @JsonProperty("integerValue")
    public void setIntegerValue(int integerValue) {
        this.integerValue = integerValue;
    }

}