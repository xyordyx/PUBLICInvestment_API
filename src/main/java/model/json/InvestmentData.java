package model.json;

import model.json.firestore.investments.Document;

import java.util.Objects;

public class InvestmentData {
    private Double amount;
    private String currency;
    private String invoiceId;
    private String time;
    private Double adjustedAmount;
    private boolean autoAdjusted;
    private String debtorName;
    private String smartToken;
    private String saltPass;

    private String message;
    private boolean status;
    private boolean completed;
    private String currentState;

    public InvestmentData() {
        this.amount = 0.00;
        this.adjustedAmount = 0.00;
        this.currency = "";
        this.invoiceId = "";
        this.time = "";
        this.autoAdjusted = false;
        this.debtorName = "";
        this.smartToken = "";
        this.message = "";
        this.status = false;
        this.completed = false;
        this.currentState = "";
        this.saltPass = "";
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInvoiceId());
    }

    public InvestmentData(Document document){
        if(document.getFields().getAmount().getDoubleValue() != null){
            this.amount = document.getFields().getAmount().getDoubleValue();
        }else this.amount = 0.00;
        if(document.getFields().getAdjustedAmount().getStringValue() != null){
            this.adjustedAmount = document.getFields().getAdjustedAmount().getStringValue();
        }else this.adjustedAmount = 0.00;
        this.currency = document.getFields().getCurrency().getStringValue();
        this.invoiceId = document.getFields().getInvoiceId().getStringValue();
        this.time = document.getFields().getTime().getStringValue();
        this.autoAdjusted = document.getFields().getAutoAdjusted().getBooleanValue();
        this.debtorName = document.getFields().getDebtorName().getStringValue();
        this.smartToken = document.getFields().getToken().getStringValue();
        this.message = document.getFields().getMessage().getStringValue();
        this.status = document.getFields().getStatus().getBooleanValue();
        this.completed = document.getFields().getCompleted().getBooleanValue();
        this.currentState = document.getFields().getCurrentState().getStringValue();
        this.saltPass = document.getFields().getSaltPass().getStringValue();
    }

    public String getSaltPass() {
        return saltPass;
    }

    public void setSaltPass(String saltPass) {
        this.saltPass = saltPass;
    }

    public Double getAdjustedAmount() {
        return adjustedAmount;
    }

    public void setAdjustedAmount(Double adjustedAmount) {
        this.adjustedAmount = adjustedAmount;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public String getSmartToken() {
        return smartToken;
    }

    public void setSmartToken(String smartToken) {
        this.smartToken = smartToken;
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
