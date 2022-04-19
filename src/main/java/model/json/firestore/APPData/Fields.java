package model.json.firestore.APPData;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "userEmail",
        "passwordCipher"
})
@Generated("jsonschema2pojo")
public class Fields {

    @JsonProperty("userEmail")
    private UserEmail userEmail;
    @JsonProperty("passwordCipher")
    private PasswordCipher passwordCipher;

    @JsonProperty("userEmail")
    public UserEmail getUserEmail() {
        return userEmail;
    }

    @JsonProperty("userEmail")
    public void setUserEmail(UserEmail userEmail) {
        this.userEmail = userEmail;
    }

    @JsonProperty("passwordCipher")
    public PasswordCipher getPasswordCipher() {
        return passwordCipher;
    }

    @JsonProperty("passwordCipher")
    public void setPasswordCipher(PasswordCipher passwordCipher) {
        this.passwordCipher = passwordCipher;
    }

}