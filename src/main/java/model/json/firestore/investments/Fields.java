
package model.json.firestore.investments;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "debtorName",
    "completed",
    "currentState",
    "message",
    "amount",
    "time",
    "smartToken",
    "adjustedAmount",
    "currency",
    "status",
    "invoiceId",
    "autoAdjusted",
        "instanceId",
        "userId"
})
public class Fields {

    @JsonProperty("debtorName")
    private DebtorName debtorName;
    @JsonProperty("completed")
    private Completed completed;
    @JsonProperty("currentState")
    private CurrentState currentState;
    @JsonProperty("message")
    private Message message;
    @JsonProperty("amount")
    private Amount amount;
    @JsonProperty("time")
    private Time time;
    @JsonProperty("smartToken")
    private smartToken smartToken;
    @JsonProperty("adjustedAmount")
    private AdjustedAmount adjustedAmount;
    @JsonProperty("currency")
    private Currency currency;
    @JsonProperty("status")
    private Status status;
    @JsonProperty("invoiceId")
    private InvoiceId invoiceId;
    @JsonProperty("autoAdjusted")
    private AutoAdjusted autoAdjusted;
    @JsonProperty("instanceId")
    private InstanceId instanceId;
    @JsonProperty("userId")
    private UserId userId;

    @JsonProperty("userId")
    public UserId getUserId() {
        return userId;
    }

    @JsonProperty("userId")
    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    @JsonProperty("instanceId")
    public InstanceId getInstanceId() {
        return instanceId;
    }

    @JsonProperty("instanceId")
    public void setInstanceId(InstanceId instanceId) {
        this.instanceId = instanceId;
    }

    @JsonProperty("debtorName")
    public DebtorName getDebtorName() {
        return debtorName;
    }

    @JsonProperty("debtorName")
    public void setDebtorName(DebtorName debtorName) {
        this.debtorName = debtorName;
    }

    @JsonProperty("completed")
    public Completed getCompleted() {
        return completed;
    }

    @JsonProperty("completed")
    public void setCompleted(Completed completed) {
        this.completed = completed;
    }

    @JsonProperty("currentState")
    public CurrentState getCurrentState() {
        return currentState;
    }

    @JsonProperty("currentState")
    public void setCurrentState(CurrentState currentState) {
        this.currentState = currentState;
    }

    @JsonProperty("message")
    public Message getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(Message message) {
        this.message = message;
    }

    @JsonProperty("amount")
    public Amount getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    @JsonProperty("time")
    public Time getTime() {
        return time;
    }

    @JsonProperty("time")
    public void setTime(Time time) {
        this.time = time;
    }

    @JsonProperty("smartToken")
    public smartToken getToken() {
        return smartToken;
    }

    @JsonProperty("smartToken")
    public void setToken(smartToken smartToken) {
        this.smartToken = smartToken;
    }

    @JsonProperty("adjustedAmount")
    public AdjustedAmount getAdjustedAmount() {
        return adjustedAmount;
    }

    @JsonProperty("adjustedAmount")
    public void setAdjustedAmount(AdjustedAmount adjustedAmount) {
        this.adjustedAmount = adjustedAmount;
    }

    @JsonProperty("currency")
    public Currency getCurrency() {
        return currency;
    }

    @JsonProperty("currency")
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @JsonProperty("status")
    public Status getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(Status status) {
        this.status = status;
    }

    @JsonProperty("invoiceId")
    public InvoiceId getInvoiceId() {
        return invoiceId;
    }

    @JsonProperty("invoiceId")
    public void setInvoiceId(InvoiceId invoiceId) {
        this.invoiceId = invoiceId;
    }

    @JsonProperty("autoAdjusted")
    public AutoAdjusted getAutoAdjusted() {
        return autoAdjusted;
    }

    @JsonProperty("autoAdjusted")
    public void setAutoAdjusted(AutoAdjusted autoAdjusted) {
        this.autoAdjusted = autoAdjusted;
    }

}
