package model.finsmartData;

import model.json.FinancialTransactions;
import model.json.Transactions;

import java.util.ArrayList;
import java.util.HashMap;

public class FinsmartData {
    private double solesAmountAvailable;
    private double dollarAmountAvailable;
    private double solesCurrentInvested;
    private double dollarCurrentInvested;
    private double solesTotalDeposited;
    private double dollarTotalDeposited;
    private double solesTotalProfit;
    private double dollarTotalProfit;
    private double solesProfitExpected;
    private double dollarProfitExpected;
    private double solesRetentions;
    private double dollarRetentions;
    private double solesOnRisk;
    private double dollarOnRisk;

    private int invoicesIndex;
    private int financialIndex;
    private ArrayList<Transactions> currentDebtorFilter;
    private FinancialTransactions financialTransactions;

    //Investments in progress Indexed
    private InvoiceIndexes invoiceIndex;

    private HashMap<String,Transactions> transactionsHashMap;

    public FinsmartData() {
        this.currentDebtorFilter = new ArrayList<>();
        this.transactionsHashMap = new HashMap<>();
    }

    public InvoiceIndexes getInvoiceIndex() {
        return invoiceIndex;
    }

    public void setInvoiceIndex(InvoiceIndexes invoiceIndex) {
        this.invoiceIndex = invoiceIndex;
    }

    public HashMap<String, Transactions> getTransactionsHashMap() {
        return transactionsHashMap;
    }

    public void setTransactionsHashMap(HashMap<String, Transactions> transactionsHashMap) {
        this.transactionsHashMap = transactionsHashMap;
    }

    public ArrayList<Transactions> getCurrentDebtorFilter() {
        return currentDebtorFilter;
    }

    public void setCurrentDebtorFilter(ArrayList<Transactions> currentDebtorFilter) {
        this.currentDebtorFilter = currentDebtorFilter;
    }

    public FinancialTransactions getFinancialTransactions() {
        return financialTransactions;
    }

    public void setFinancialTransactions(FinancialTransactions financialTransactions) {
        this.financialTransactions = financialTransactions;
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

    public double getSolesAmountAvailable() {
        return solesAmountAvailable;
    }

    public void setSolesAmountAvailable(double solesAmountAvailable) {
        this.solesAmountAvailable = solesAmountAvailable;
    }

    public double getDollarAmountAvailable() {
        return dollarAmountAvailable;
    }

    public void setDollarAmountAvailable(double dollarAmountAvailable) {
        this.dollarAmountAvailable = dollarAmountAvailable;
    }

    public double getSolesCurrentInvested() {
        return solesCurrentInvested;
    }

    public void setSolesCurrentInvested(double solesCurrentInvested) {
        this.solesCurrentInvested = solesCurrentInvested;
    }

    public double getDollarCurrentInvested() {
        return dollarCurrentInvested;
    }

    public void setDollarCurrentInvested(double dollarCurrentInvested) {
        this.dollarCurrentInvested = dollarCurrentInvested;
    }

    public double getSolesTotalDeposited() {
        return solesTotalDeposited;
    }

    public void setSolesTotalDeposited(double solesTotalDeposited) {
        this.solesTotalDeposited = solesTotalDeposited;
    }

    public double getDollarTotalDeposited() {
        return dollarTotalDeposited;
    }

    public void setDollarTotalDeposited(double dollarTotalDeposited) {
        this.dollarTotalDeposited = dollarTotalDeposited;
    }

    public double getSolesTotalProfit() {
        return solesTotalProfit;
    }

    public void setSolesTotalProfit(double solesTotalProfit) {
        this.solesTotalProfit = solesTotalProfit;
    }

    public double getDollarTotalProfit() {
        return dollarTotalProfit;
    }

    public void setDollarTotalProfit(double dollarTotalProfit) {
        this.dollarTotalProfit = dollarTotalProfit;
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

    public double getSolesRetentions() {
        return solesRetentions;
    }

    public void setSolesRetentions(double solesRetentions) {
        this.solesRetentions = solesRetentions;
    }

    public double getDollarRetentions() {
        return dollarRetentions;
    }

    public void setDollarRetentions(double dollarRetentions) {
        this.dollarRetentions = dollarRetentions;
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
}
