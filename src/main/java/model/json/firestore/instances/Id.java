package model.json.firestore.instances;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "stringValue"
})

public class Id {

    @JsonProperty("stringValue")
    private String stringValue;

    @JsonProperty("stringValue")
    public String getStringValue() {
        return stringValue;
    }

    @JsonProperty("stringValue")
    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

}