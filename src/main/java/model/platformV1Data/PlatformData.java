package model.platformV1Data;

import model.json.FinancialTransactions;
import model.json.Transactions;

import java.util.HashMap;

public class PlatformData {

    private double totalPENAvailable;
    private double totalUSDAvailable;

    private double totalPENScheduled;
    private double totalUSDScheduled;

    private double totalPENDeposited;
    private double totalUSDDeposited;

    private double totalPENRetentions;
    private double totalUSDRetentions;

    private double totalPENProfited;
    private double totalUSDProfited;

    private double totalPENCurrentInvested;
    private double totalUSDCurrentInvested;

    private double solesOnRisk;
    private double dollarOnRisk;

    private double solesProfitExpected;
    private double dollarProfitExpected;

    private int scheduledInvestmentsNum;

    private int invoicesIndex;
    private int financialIndex;
    private FinancialTransactions financialTransactions;

    //Investments in progress Indexed
    private InvoiceIndexes invoiceIndex;

    private HashMap<String,Transactions> transactionsHashMap;

    public PlatformData() {
        this.transactionsHashMap = new HashMap<>();
    }

    public double getTotalPENScheduled() {
        return totalPENScheduled;
    }

    public void setTotalPENScheduled(double totalPENScheduled) {
        this.totalPENScheduled = totalPENScheduled;
    }

    public int getScheduledInvestmentsNum() {
        return scheduledInvestmentsNum;
    }

    public void setScheduledInvestmentsNum(int scheduledInvestmentsNum) {
        this.scheduledInvestmentsNum = scheduledInvestmentsNum;
    }

    public double getTotalUSDScheduled() {
        return totalUSDScheduled;
    }

    public void setTotalUSDScheduled(double totalUSDScheduled) {
        this.totalUSDScheduled = totalUSDScheduled;
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

    public double getTotalPENCurrentInvested() {
        return totalPENCurrentInvested;
    }

    public void setTotalPENCurrentInvested(double totalPENCurrentInvested) {
        this.totalPENCurrentInvested = totalPENCurrentInvested;
    }

    public double getTotalUSDCurrentInvested() {
        return totalUSDCurrentInvested;
    }

    public void setTotalUSDCurrentInvested(double totalUSDCurrentInvested) {
        this.totalUSDCurrentInvested = totalUSDCurrentInvested;
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
