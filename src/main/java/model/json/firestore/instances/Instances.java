package model.json.firestore.instances;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "documents",
        "document",
        "readTime"
})

public class Instances {

    @JsonProperty("documents")
    private List<Document> documents = null;

    @JsonProperty("documents")
    public List<Document> getDocuments() {
        return documents;
    }

    @JsonProperty("documents")
    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

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