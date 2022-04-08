package model.json.appengine;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "instances"
})
public class Instances {

    @JsonProperty("instances")
    private List<Instance> instances = null;

    @JsonProperty("instances")
    public List<Instance> getInstances() {
        return instances;
    }

    @JsonProperty("instances")
    public void setInstances(List<Instance> instances) {
        this.instances = instances;
    }

}