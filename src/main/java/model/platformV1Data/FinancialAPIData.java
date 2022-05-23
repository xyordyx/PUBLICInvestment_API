package model.platformV1Data;

public class FinancialAPIData {
    private double totalPENAvailable;
    private double totalUSDAvailable;

    private double totalPENCurrentInvested;
    private double totalUSDCurrentInvested;

    private double totalPENDeposited;
    private double totalUSDDeposited;

    private double totalPENRetentions;
    private double totalUSDRetentions;

    private double totalPENProfited;
    private double totalUSDProfited;

    private double totalPENScheduled;
    private double totalUSDScheduled;

    private double solesOnRisk;
    private double dollarOnRisk;

    private double solesProfitExpected;
    private double dollarProfitExpected;

    private int scheduledInvestmentsNum;

    public FinancialAPIData(PlatformData data) {
        this.totalPENCurrentInvested = data.getTotalPENCurrentInvested();
        this.totalUSDCurrentInvested = data.getTotalUSDCurrentInvested();
        this.totalPENDeposited = data.getTotalPENDeposited();
        this.totalUSDDeposited = data.getTotalUSDDeposited();
        this.totalPENRetentions = data.getTotalPENRetentions();
        this.totalUSDRetentions = data.getTotalUSDRetentions();
        this.totalPENProfited = data.getTotalPENProfited();
        this.totalUSDProfited = data.getTotalUSDProfited();
        this.totalPENAvailable = data.getTotalPENAvailable();
        this.totalUSDAvailable = data.getTotalUSDAvailable();
        this.totalPENScheduled = data.getTotalPENScheduled();
        this.totalUSDScheduled = data.getTotalUSDScheduled();
        this.solesOnRisk = data.getSolesOnRisk();
        this.dollarOnRisk = data.getDollarOnRisk();
        this.solesProfitExpected = data.getSolesProfitExpected();
        this.dollarProfitExpected = data.getDollarProfitExpected();
        this.scheduledInvestmentsNum = data.getScheduledInvestmentsNum();
    }

    public int getScheduledInvestmentsNum() {
        return scheduledInvestmentsNum;
    }

    public void setScheduledInvestmentsNum(int scheduledInvestmentsNum) {
        this.scheduledInvestmentsNum = scheduledInvestmentsNum;
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

    public double getTotalPENScheduled() {
        return totalPENScheduled;
    }

    public void setTotalPENScheduled(double totalPENScheduled) {
        this.totalPENScheduled = totalPENScheduled;
    }

    public double getTotalUSDScheduled() {
        return totalUSDScheduled;
    }

    public void setTotalUSDScheduled(double totalUSDScheduled) {
        this.totalUSDScheduled = totalUSDScheduled;
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
}
