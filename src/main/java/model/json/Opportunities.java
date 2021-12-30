package model.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Opportunities {
    private double tem;
    private String currency;
    private int v1IdGroup;
    private String _id;
    private List<PhysicalInvoices> physicalInvoices;
    private double availableBalanceAmount;
    private double availableBalancePercentage;
    private String debtorCompanyName;
    private Debtor debtor;

    @Override
    public String toString() {
        return "Opportunities{" +
                "tem=" + tem +
                ", currency='" + currency + '\'' +
                ", v1IdGroup=" + v1IdGroup +
                ", _id='" + _id + '\'' +
                ", physicalInvoices=" + physicalInvoices +
                ", availableBalanceAmount=" + availableBalanceAmount +
                ", debtorCompanyName='" + debtorCompanyName + '\'' +
                ", debtor=" + debtor +
                '}';
    }

    @JsonCreator
    public Opportunities(@JsonProperty("tem") double tem, @JsonProperty("currency") String currency,
                         @JsonProperty("v1IdGroup") int v1IdGroup, @JsonProperty("_id") String _id,
                         @JsonProperty("physicalInvoices")List<PhysicalInvoices> physicalInvoices,
                         @JsonProperty("availableBalanceAmount") double availableBalanceAmount,
                         @JsonProperty("debtorCompanyName") String debtorCompanyName, @JsonProperty("debtor") Debtor debtor,
                         @JsonProperty("availableBalancePercentage")Double availableBalancePercentage) {
        this.tem = tem;
        this.currency = currency;
        this.v1IdGroup = v1IdGroup;
        this._id = _id;
        this.physicalInvoices = physicalInvoices;
        this.availableBalanceAmount = availableBalanceAmount;
        this.debtorCompanyName = debtorCompanyName;
        this.debtor = debtor;
        this.availableBalancePercentage = availableBalancePercentage;
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

    @JsonGetter("debtorCompanyName")
    public String getDebtorCompanyName() {
        return debtorCompanyName;
    }

    public void setDebtorCompanyName(String debtorCompanyName) {
        this.debtorCompanyName = debtorCompanyName;
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
