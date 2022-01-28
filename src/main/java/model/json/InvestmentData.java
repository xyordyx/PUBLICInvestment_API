package model.json;

public class InvestmentData {
    private Double amount;
    private String currency;
    private String invoiceId;
    private String time;
    private double adjustedAmount;
    private boolean autoAdjusted;
    private String debtorName;
    private String token;

    private String message;
    private boolean status;
    private boolean completed;
    private String currentState;

    public InvestmentData() {
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getDebtorName() {
        return debtorName;
    }

    public void setDebtorName(String debtorName) {
        this.debtorName = debtorName;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getMessage() {
        return message;
    }

    public double getAdjustedAmount() {
        return adjustedAmount;
    }

    public void setAdjustedAmount(double adjustedAmount) {
        this.adjustedAmount = adjustedAmount;
    }

    public boolean isAutoAdjusted() {
        return autoAdjusted;
    }

    public void setAutoAdjusted(boolean autoAdjusted) {
        this.autoAdjusted = autoAdjusted;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }
}
