package model.json;

import java.util.Date;
import java.util.Objects;

public class InvoiceTransactions {

    private String status;
    private int minimumDuration;
    private double tem;
    private double tea;
    private Date paymentDate;
    private String _id;
    private String currency;
    private String toBeCollectedIn;
    private Date actualPaymentDate;
    private Debtor debtor;
    private int moraDays;
    private String updatedAt;
    private Date createdAt;

    //ADD-ONS
    private long pastDueDays;
    private Double amountInvested;
    private Double profitedAmount;
    private Double expectedProfit;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InvoiceTransactions)) return false;
        InvoiceTransactions that = (InvoiceTransactions) o;
        return get_id().equals(that.get_id());
    }

    @Override
    public int hashCode() {
        return Objects.hash(get_id());
    }

    @Override
    public String toString() {
        return "InvoiceTransactions{" +
                "status='" + status + '\'' +
                ", minimumDuration=" + minimumDuration +
                ", tem=" + tem +
                ", tea=" + tea +
                ", paymentDate=" + paymentDate +
                ", _id='" + _id + '\'' +
                ", currency='" + currency + '\'' +
                ", toBeCollectedIn='" + toBeCollectedIn + '\'' +
                ", actualPaymentDate=" + actualPaymentDate +
                ", debtor=" + debtor +
                ", moraDays=" + moraDays +
                ", updatedAt='" + updatedAt + '\'' +
                ", pastDueDays=" + pastDueDays +
                '}';
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Double getExpectedProfit() {
        return expectedProfit;
    }

    public void setExpectedProfit(Double expectedProfit) {
        this.expectedProfit = expectedProfit;
    }

    public Double getAmountInvested() {
        return amountInvested;
    }

    public void setAmountInvested(Double amountInvested) {
        this.amountInvested = amountInvested;
    }

    public Double getProfitedAmount() {
        return profitedAmount;
    }

    public void setProfitedAmount(Double profitedAmount) {
        this.profitedAmount = profitedAmount;
    }

    public long getPastDueDays() {
        return pastDueDays;
    }

    public void setPastDueDays(long pastDueDays) {
        this.pastDueDays = pastDueDays;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getMinimumDuration() {
        return minimumDuration;
    }

    public void setMinimumDuration(int minimumDuration) {
        this.minimumDuration = minimumDuration;
    }

    public double getTem() {
        return tem;
    }

    public void setTem(double tem) {
        this.tem = tem;
    }

    public double getTea() {
        return tea;
    }

    public void setTea(double tea) {
        this.tea = tea;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getToBeCollectedIn() {
        return toBeCollectedIn;
    }

    public void setToBeCollectedIn(String toBeCollectedIn) {
        this.toBeCollectedIn = toBeCollectedIn;
    }

    public Date getActualPaymentDate() {
        return actualPaymentDate;
    }

    public void setActualPaymentDate(Date actualPaymentDate) {
        this.actualPaymentDate = actualPaymentDate;
    }

    public Debtor getDebtor() {
        return debtor;
    }

    public void setDebtor(Debtor debtor) {
        this.debtor = debtor;
    }

    public int getMoraDays() {
        return moraDays;
    }

    public void setMoraDays(int moraDays) {
        this.moraDays = moraDays;
    }
}
