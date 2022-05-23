package model.platformV1Data;

import model.json.InvoiceTransactions;

import java.util.ArrayList;

public class APIDebtorData {

    private ArrayList<InvoiceTransactions> debtorInvoices;
    private int delayedInvoices;

    public APIDebtorData(ArrayList<InvoiceTransactions> debtorInvoices){
        this.debtorInvoices = debtorInvoices;
    }

    public APIDebtorData(ArrayList<InvoiceTransactions> debtorInvoices, int delayedInvoices){
        this.debtorInvoices = debtorInvoices;
        this.delayedInvoices = delayedInvoices;
    }

    public int getDelayedInvoices() {
        return delayedInvoices;
    }

    public void setDelayedInvoices(int delayedInvoices) {
        this.delayedInvoices = delayedInvoices;
    }

    public ArrayList<InvoiceTransactions> getDebtorInvoices() {
        return debtorInvoices;
    }

    public void setDebtorInvoices(ArrayList<InvoiceTransactions> debtorInvoices) {
        this.debtorInvoices = debtorInvoices;
    }
}
