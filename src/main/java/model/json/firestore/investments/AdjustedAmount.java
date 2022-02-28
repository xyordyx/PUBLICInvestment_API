package model.json.firestore.investments;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "doubleValue"
})
public class AdjustedAmount {

    @JsonProperty("doubleValue")
    private Double doubleValue;

    @JsonProperty("doubleValue")
    public Double getDoubleValue() {
        return doubleValue;
    }

    @JsonProperty("doubleValue")
    public void setDoubleValue(Double doubleValue) {
        this.doubleValue = doubleValue;
    }

}