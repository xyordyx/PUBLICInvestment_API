package model.json;

import com.fasterxml.jackson.annotation.JsonGetter;
import model.json.firestore.investments.Evaluation;

import java.text.SimpleDateFormat;
import java.util.List;

public class Opportunities {
    private double tem;
    private String currency;
    private int v1IdGroup;
    private String _id;
    private List<PhysicalInvoices> physicalInvoices;
    private Evaluation evaluation;
    private double availableBalanceAmount;
    private double availableBalancePercentage;
    private Debtor debtor;
    private Boolean isConfirming;
    private double advanceAmount;
    private int toBeCollectedIn;
    private String paymentDate;
    private double tea;
    private Boolean onSale;
    private String createdAt;
    private int onSaleSlot;

    public Opportunities(double tem, String currency, int v1IdGroup, String _id, List<PhysicalInvoices> physicalInvoices,
                         Evaluation evaluation, double availableBalanceAmount, double availableBalancePercentage,
                         Debtor debtor, Boolean isConfirming, double advanceAmount, int toBeCollectedIn, String paymentDate,
                         double tea, Boolean onSale, String createdAt, int onSaleSlot) {
        this.tem = tem;
        this.currency = currency;
        this.v1IdGroup = v1IdGroup;
        this._id = _id;
        this.physicalInvoices = physicalInvoices;
        this.evaluation = evaluation;
        this.availableBalanceAmount = availableBalanceAmount;
        this.availableBalancePercentage = availableBalancePercentage;
        this.debtor = debtor;
        this.isConfirming = isConfirming;
        this.advanceAmount = advanceAmount;
        this.toBeCollectedIn = toBeCollectedIn;
        this.paymentDate = paymentDate;
        this.tea = tea;
        this.onSale = onSale;
        this.createdAt = createdAt;
        this.onSaleSlot = onSaleSlot;
    }

    @JsonGetter("onSale")
    public Boolean getOnSale() {
        return onSale;
    }
    @JsonGetter("onSale")
    public void setOnSale(Boolean onSale) {
        this.onSale = onSale;
    }

    @JsonGetter("onSaleSlot")
    public int getOnSaleSlot() {
        return onSaleSlot;
    }

    @JsonGetter("onSaleSlot")
    public void setOnSaleSlot(int onSaleSlot) {
        this.onSaleSlot = onSaleSlot;
    }

    @JsonGetter("createdAt")
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        this.createdAt = formatter.format(createdAt);
    }

    @JsonGetter("evaluation")
    public Evaluation getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Evaluation evaluation) {
        this.evaluation = evaluation;
    }

    @JsonGetter("isConfirming")
    public Boolean getConfirming() {
        return isConfirming;
    }

    public void setConfirming(Boolean confirming) {
        isConfirming = confirming;
    }

    @JsonGetter("advanceAmount")
    public double getAdvanceAmount() {
        return advanceAmount;
    }

    public void setAdvanceAmount(double advanceAmount) {
        this.advanceAmount = advanceAmount;
    }

    @JsonGetter("toBeCollectedIn")
    public int getToBeCollectedIn() {
        return toBeCollectedIn;
    }

    public void setToBeCollectedIn(int toBeCollectedIn) {
        this.toBeCollectedIn = toBeCollectedIn;
    }

    @JsonGetter("paymentDate")
    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    @JsonGetter("tea")
    public double getTea() {
        return tea;
    }

    public void setTea(double tea) {
        this.tea = tea;
    }

    @JsonGetter("availableBalancePercentage")
    public double getAvailableBalancePercentage() {
        return availableBalancePercentage;
    }

    public void setAvailableBalancePercentage(double availableBalancePercentage) {
        this.availableBalancePercentage = availableBalancePercentage;
    }

    @JsonGetter("debtor")
    public Debtor getDebtor() {
        return debtor;
    }

    public void setDebtor(Debtor debtor) {
        this.debtor = debtor;
    }

    @JsonGetter("tem")
    public double getTem() {
        return tem;
    }

    public void setTem(double tem) {
        this.tem = tem;
    }

    @JsonGetter("currency")
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @JsonGetter("v1IdGroup")
    public int getV1IdGroup() {
        return v1IdGroup;
    }

    public void setV1IdGroup(int v1IdGroup) {
        this.v1IdGroup = v1IdGroup;
    }

    @JsonGetter("_id")
    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    @JsonGetter("physicalInvoices")
    public List<PhysicalInvoices> getPhysicalInvoices() {
        return physicalInvoices;
    }

    public void setPhysicalInvoices(List<PhysicalInvoices> physicalInvoices) {
        this.physicalInvoices = physicalInvoices;
    }

    @JsonGetter("availableBalanceAmount")
    public double getAvailableBalanceAmount() {
        return availableBalanceAmount;
    }

    public void setAvailableBalanceAmount(double availableBalanceAmount) {
        this.availableBalanceAmount = availableBalanceAmount;
    }
}
