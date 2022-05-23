package model.platformV1Data;

import com.fasterxml.jackson.annotation.JsonGetter;
import model.json.Opportunities;

import java.util.ArrayList;

public class APIData {
    private FinancialAPIData FinancialBalance;
    private ArrayList<Opportunities> Opportunities;

    public APIData(FinancialAPIData financialBalance, ArrayList<Opportunities> opportunities) {
        FinancialBalance = financialBalance;
        Opportunities = opportunities;
    }

    @JsonGetter("financialBalance")
    public FinancialAPIData getFinancialBalance() {
        return FinancialBalance;
    }

    public void setFinancialBalance(FinancialAPIData financialBalance) {
        FinancialBalance = financialBalance;
    }

    @JsonGetter("opportunities")
    public ArrayList<model.json.Opportunities> getOpportunities() {
        return Opportunities;
    }

    public void setOpportunities(ArrayList<model.json.Opportunities> opportunities) {
        Opportunities = opportunities;
    }
}
