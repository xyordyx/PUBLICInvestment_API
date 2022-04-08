package model.json.appengine;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "id",
        "appEngineRelease",
        "availability",
        "startTime",
        "requests",
        "qps",
        "averageLatency",
        "memoryUsage"
})
public class Instance {

    @JsonProperty("name")
    private String name;
    @JsonProperty("id")
    private String id;
    @JsonProperty("appEngineRelease")
    private String appEngineRelease;
    @JsonProperty("availability")
    private String availability;
    @JsonProperty("startTime")
    private String startTime;
    @JsonProperty("requests")
    private Integer requests;
    @JsonProperty("qps")
    private Double qps;
    @JsonProperty("averageLatency")
    private Integer averageLatency;
    @JsonProperty("memoryUsage")
    private String memoryUsage;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("appEngineRelease")
    public String getAppEngineRelease() {
        return appEngineRelease;
    }

    @JsonProperty("appEngineRelease")
    public void setAppEngineRelease(String appEngineRelease) {
        this.appEngineRelease = appEngineRelease;
    }

    @JsonProperty("availability")
    public String getAvailability() {
        return availability;
    }

    @JsonProperty("availability")
    public void setAvailability(String availability) {
        this.availability = availability;
    }

    @JsonProperty("startTime")
    public String getStartTime() {
        return startTime;
    }

    @JsonProperty("startTime")
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @JsonProperty("requests")
    public Integer getRequests() {
        return requests;
    }

    @JsonProperty("requests")
    public void setRequests(Integer requests) {
        this.requests = requests;
    }

    @JsonProperty("qps")
    public Double getQps() {
        return qps;
    }

    @JsonProperty("qps")
    public void setQps(Double qps) {
        this.qps = qps;
    }

    @JsonProperty("averageLatency")
    public Integer getAverageLatency() {
        return averageLatency;
    }

    @JsonProperty("averageLatency")
    public void setAverageLatency(Integer averageLatency) {
        this.averageLatency = averageLatency;
    }

    @JsonProperty("memoryUsage")
    public String getMemoryUsage() {
        return memoryUsage;
    }

    @JsonProperty("memoryUsage")
    public void setMemoryUsage(String memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

}