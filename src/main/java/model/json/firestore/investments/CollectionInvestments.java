package model.json.firestore.investments;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "document",
        "readTime"
})
public class CollectionInvestments {
    @JsonProperty("document")
    private Document document;
    @JsonProperty("readTime")
    private String readTime;

    @JsonProperty("document")
    public Document getDocument() {
        return document;
    }

    @JsonProperty("document")
    public void setDocument(Document document) {
        this.document = document;
    }

    @JsonProperty("readTime")
    public String getReadTime() {
        return readTime;
    }

    @JsonProperty("readTime")
    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }

}