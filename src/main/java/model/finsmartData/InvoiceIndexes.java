package model.finsmartData;

import model.json.InvoiceTransactions;

import java.util.ArrayList;
import java.util.HashMap;

public class InvoiceIndexes {

    private HashMap<String, InvoiceTransactions> invoicesIndex;
    private HashMap<String, ArrayList<InvoiceTransactions>> debtorInvoiceIndex;

    public InvoiceIndexes() {
        this.invoicesIndex = new HashMap<>();
        this.debtorInvoiceIndex = new HashMap<>();
    }

    public HashMap<String, InvoiceTransactions> getInvoicesIndex() {
        return invoicesIndex;
    }

    public void setInvoicesIndex(HashMap<String, InvoiceTransactions> invoicesIndex) {
        this.invoicesIndex = invoicesIndex;
    }

    public HashMap<String, ArrayList<InvoiceTransactions>> getDebtorInvoiceIndex() {
        return debtorInvoiceIndex;
    }

    public void setDebtorInvoiceIndex(HashMap<String, ArrayList<InvoiceTransactions>> debtorInvoiceIndex) {
        this.debtorInvoiceIndex = debtorInvoiceIndex;
    }
}
