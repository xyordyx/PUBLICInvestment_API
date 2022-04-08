package model.json.firestore.investments;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "stringValue"
})
public class AdjustedAmount {

    @JsonProperty("stringValue")
    private Double stringValue;

    @JsonProperty("stringValue")
    public Double getStringValue() {
        return stringValue;
    }

    @JsonProperty("stringValue")
    public void setDoubleValue(Double doubleValue) {
        this.stringValue = doubleValue;
    }

}