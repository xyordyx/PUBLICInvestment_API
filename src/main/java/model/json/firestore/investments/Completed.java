
package model.json.firestore.investments;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "booleanValue"
})
public class Completed {

    @JsonProperty("booleanValue")
    private Boolean booleanValue;

    @JsonProperty("booleanValue")
    public Boolean getBooleanValue() {
        return booleanValue;
    }

    @JsonProperty("booleanValue")
    public void setBooleanValue(Boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

}
