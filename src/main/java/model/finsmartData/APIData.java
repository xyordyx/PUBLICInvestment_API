package model.finsmartData;

import model.json.InvoiceTransactions;

import java.util.ArrayList;

public class APIData {
    private double totalPENDeposited;
    private double totalUSDDeposited;

    private double totalPENRetentions;
    private double totalUSDRetentions;

    private double totalPENProfited;
    private double totalUSDProfited;

    private double totalPENAvailable;
    private double totalUSDAvailable;

    private double totalPENInProgress;
    private double totalUSDInProgress;

    private double solesOnRisk;
    private double dollarOnRisk;

    private double solesProfitExpected;
    private double dollarProfitExpected;

    private int invoicesIndex;
    private int financialIndex;

    private ArrayList<InvoiceTransactions> debtorInvoices;

    public APIData(ArrayList<InvoiceTransactions> debtorInvoices){
        this.debtorInvoices = debtorInvoices;
    }

    public APIData() {}

    public APIData(FinsmartData data) {
        this.totalPENDeposited = data.getSolesTotalDeposited();
        this.totalUSDDeposited = data.getDollarTotalDeposited();
        this.totalPENRetentions = data.getSolesRetentions();
        this.totalUSDRetentions = data.getDollarRetentions();
        this.totalPENProfited = data.getSolesTotalProfit();
        this.totalUSDProfited = data.getDollarTotalProfit();
        this.totalPENAvailable = data.getSolesAmountAvailable();
        this.totalUSDAvailable = data.getDollarAmountAvailable();
        this.totalPENInProgress = data.getSolesCurrentInvested();
        this.totalUSDInProgress = data.getDollarCurrentInvested();
        this.solesOnRisk = data.getSolesOnRisk();
        this.dollarOnRisk = data.getDollarOnRisk();
        this.solesProfitExpected = data.getSolesProfitExpected();
        this.dollarProfitExpected = data.getDollarProfitExpected();
        this.invoicesIndex = data.getInvoicesIndex();
        this.financialIndex = data.getFinancialIndex();
    }

    public ArrayList<InvoiceTransactions> getDebtorInvoices() {
        return debtorInvoices;
    }

    public void setDebtorInvoices(ArrayList<InvoiceTransactions> debtorInvoices) {
        this.debtorInvoices = debtorInvoices;
    }

    public double getSolesOnRisk() {
        return solesOnRisk;
    }

    public void setSolesOnRisk(double solesOnRisk) {
        this.solesOnRisk = solesOnRisk;
    }

    public double getDollarOnRisk() {
        return dollarOnRisk;
    }

    public void setDollarOnRisk(double dollarOnRisk) {
        this.dollarOnRisk = dollarOnRisk;
    }

    public double getSolesProfitExpected() {
        return solesProfitExpected;
    }

    public void setSolesProfitExpected(double solesProfitExpected) {
        this.solesProfitExpected = solesProfitExpected;
    }

    public double getDollarProfitExpected() {
        return dollarProfitExpected;
    }

    public void setDollarProfitExpected(double dollarProfitExpected) {
        this.dollarProfitExpected = dollarProfitExpected;
    }

    public int getInvoicesIndex() {
        return invoicesIndex;
    }

    public void setInvoicesIndex(int invoicesIndex) {
        this.invoicesIndex = invoicesIndex;
    }

    public int getFinancialIndex() {
        return financialIndex;
    }

    public void setFinancialIndex(int financialIndex) {
        this.financialIndex = financialIndex;
    }

    public double getTotalPENDeposited() {
        return totalPENDeposited;
    }

    public void setTotalPENDeposited(double totalPENDeposited) {
        this.totalPENDeposited = totalPENDeposited;
    }

    public double getTotalUSDDeposited() {
        return totalUSDDeposited;
    }

    public void setTotalUSDDeposited(double totalUSDDeposited) {
        this.totalUSDDeposited = totalUSDDeposited;
    }

    public double getTotalPENRetentions() {
        return totalPENRetentions;
    }

    public void setTotalPENRetentions(double totalPENRetentions) {
        this.totalPENRetentions = totalPENRetentions;
    }

    public double getTotalUSDRetentions() {
        return totalUSDRetentions;
    }

    public void setTotalUSDRetentions(double totalUSDRetentions) {
        this.totalUSDRetentions = totalUSDRetentions;
    }

    public double getTotalPENProfited() {
        return totalPENProfited;
    }

    public void setTotalPENProfited(double totalPENProfited) {
        this.totalPENProfited = totalPENProfited;
    }

    public double getTotalUSDProfited() {
        return totalUSDProfited;
    }

    public void setTotalUSDProfited(double totalUSDProfited) {
        this.totalUSDProfited = totalUSDProfited;
    }

    public double getTotalPENAvailable() {
        return totalPENAvailable;
    }

    public void setTotalPENAvailable(double totalPENAvailable) {
        this.totalPENAvailable = totalPENAvailable;
    }

    public double getTotalUSDAvailable() {
        return totalUSDAvailable;
    }

    public void setTotalUSDAvailable(double totalUSDAvailable) {
        this.totalUSDAvailable = totalUSDAvailable;
    }

    public double getTotalPENInProgress() {
        return totalPENInProgress;
    }

    public void setTotalPENInProgress(double totalPENInProgress) {
        this.totalPENInProgress = totalPENInProgress;
    }

    public double getTotalUSDInProgress() {
        return totalUSDInProgress;
    }

    public void setTotalUSDInProgress(double totalUSDInProgress) {
        this.totalUSDInProgress = totalUSDInProgress;
    }
}
