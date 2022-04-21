package model.json.firestore.investments;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "doubleValue"
})
public class OnSaleSlot {

    @JsonProperty("doubleValue")
    private double doubleValue;

    @JsonProperty("doubleValue")
    public double getDoubleValue() {
        return doubleValue;
    }

    @JsonProperty("doubleValue")
    public void setDoubleValue(double doubleValue) {
        this.doubleValue = doubleValue;
    }

}