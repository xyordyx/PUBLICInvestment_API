package model.json;

import java.util.ArrayList;

public class FinancialTransactions {
    private ArrayList<Transactions> financialTransactions;

    public ArrayList<Transactions> getFinancialTransactions() {
        return financialTransactions;
    }

    public void setFinancialTransactions(ArrayList<Transactions> financialTransactions) {
        this.financialTransactions = financialTransactions;
    }
}
