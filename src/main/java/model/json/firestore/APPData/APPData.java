package model.json.firestore.APPData;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "passwordCipher",
        "emailCipher"
})
public class APPData {
    @JsonProperty("passwordCipher")
    private String passwordCipher;
    @JsonProperty("emailCipher")
    private String emailCipher;

    public APPData(String passwordCipher) {
        this.passwordCipher = passwordCipher;
    }

    @JsonProperty("passwordCipher")
    public String getPasswordCipher() {
        return passwordCipher;
    }

    @JsonProperty("passwordCipher")
    public void setPasswordCipher(String passwordCipher) {
        this.passwordCipher = passwordCipher;
    }

    @JsonProperty("emailCipher")
    public String getEmailCipher() {
        return emailCipher;
    }

    @JsonProperty("emailCipher")
    public void setEmailCipher(String emailCipher) {
        this.emailCipher = emailCipher;
    }
}
