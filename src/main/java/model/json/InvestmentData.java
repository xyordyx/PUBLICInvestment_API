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
    private int onSaleSlot;
    private boolean onSale;

    private String message;
    private boolean status;
    private boolean completed;
    private String currentState;
    private int instanceVersion;
    private String instanceId;
    private String userId;

    @Override
    public int hashCode() {
        return Objects.hash(getInvoiceId());
    }

    public InvestmentData(){

    }

    public InvestmentData(Document document){
        if(document.getFields().getAmount().getDoubleValue() != null){
            this.amount = document.getFields().getAmount().getDoubleValue();
        }else this.amount = 0.00;
        if(document.getFields().getAdjustedAmount().getDoubleValue() != null){
            this.adjustedAmount = document.getFields().getAdjustedAmount().getDoubleValue();
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
        this.userId = document.getFields().getUserId().getStringValue();
        this.onSale = document.getFields().getOnSale().getBooleanValue();
        this.onSaleSlot = (int)document.getFields().getOnSaleSlot().getDoubleValue();
    }

    public int getOnSaleSlot() {
        return onSaleSlot;
    }

    public void setOnSaleSlot(int onSaleSlot) {
        this.onSaleSlot = onSaleSlot;
    }

    public boolean isOnSale() {
        return onSale;
    }

    public void setOnSale(boolean onSale) {
        this.onSale = onSale;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getInstanceVersion() {
        return instanceVersion;
    }

    public void setInstanceVersion(int instanceVersion) {
        this.instanceVersion = instanceVersion;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
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
