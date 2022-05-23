package model;

import model.platformV1Data.APIDebtorData;
import model.platformV1Data.PlatformData;
import model.json.InvoiceTransactions;

import java.util.ArrayList;


public class CoreProcessor {

    //CLASS TO PROCESS PLATFORM DATA AND REUSE THROUGH API
    public PlatformData getExtraData(PlatformData smartData, String token) {
        //LOGIC REMOVED
        return smartData;
    }

    public PlatformData getBalance(PlatformData smartData) {
        //LOGIC REMOVED
        return smartData;
    }

    public APIDebtorData getDebtorHistory(PlatformData smartData, String debtor, String token) {

        ArrayList<InvoiceTransactions> temp = new ArrayList<>();
        //LOGIC REMOVED
        return new APIDebtorData(temp,0);
    }

    public APIDebtorData getCurrentInvestments(PlatformData smartData, String token) {
        ArrayList<InvoiceTransactions> temp = new ArrayList<>();
        int delayedInvoices = 0;
        //LOGIC REMOVED
        return new APIDebtorData(temp, delayedInvoices);
    }
}
