package model.finsmartData;

import model.CIG;
import model.json.InvestmentData;
import model.json.Opportunities;
import model.json.ResponseJSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.List;

public class FinsmartUtil {

    public static ResponseJSON postToFinSmart(double amount, InvestmentData investment, String token){
        String parameters;
        ResponseJSON responseJSON = null;
        parameters = generateJSONInvest(amount, investment.getCurrency(), investment.getInvoiceId());
        while(responseJSON == null){
            responseJSON = CIG.executeInvestment1(parameters,token);
        }
        return responseJSON;
    }

    public static ResponseJSON postToFinSmartInstance(double amount, InvestmentData investment){
        String parameters;
        ResponseJSON responseJSON = null;
        parameters = generateJSONInvest(amount, investment.getCurrency(), investment.getInvoiceId());
        while(responseJSON == null){
            responseJSON = CIG.executeInvestment1(parameters,investment.getToken());
        }
        return responseJSON;
    }

    public static String generateJSONInvest(double amount, String currency, String invoice_id){
        return "{\"amount\":\""+amount+"\",\"currency\":\""+currency+"\",\"invoice\":\""+invoice_id+
                "\",\"type\":\"investment\"}";
    }

    public static InvestmentData updateInvestment(InvestmentData investment, ResponseJSON responseJSON, int status,
                                                  Double adjustedAmount){
        if(responseJSON == null || status == 3) {
            investment.setStatus(false);
            investment.setMessage("AMOUNT AVAILABLE IS 0.00");
        }else{
            if (responseJSON.isStatus() && status == 1) {
                investment.setStatus(true);
            } else if (!responseJSON.isStatus() && status == 1) {
                investment.setStatus(false);
                investment.setMessage(responseJSON.getMessage());
            } else if (responseJSON.isStatus() && status == 4) {
                investment.setAutoAdjusted(true);
                investment.setAdjustedAmount(adjustedAmount);
                investment.setStatus(true);
                investment.setMessage("");
            } else if (!responseJSON.isStatus() && status == 4) {
                investment.setStatus(false);
                investment.setMessage(responseJSON.getMessage());
            }
        }
        return investment;
    }

    public static double updateOpportunity(String token, String invoiceId){
        List<Opportunities> opportunities = CIG.getOpportunitiesJSON(token);
        while(opportunities == null){
            opportunities = CIG.getOpportunitiesJSON(token);
        }
        return updateAmountAvailable(opportunities,invoiceId);
    }

    public static double updateAmountAvailable(List<Opportunities> opportunities, String invoiceId){
        for(Opportunities op : opportunities){
            if(op.getId().equals(invoiceId)){
                return op.getAvailableBalanceAmount();
            }
        }
        return 0;
    }

    public static StringBuilder readResponse(HttpURLConnection con) throws IOException {
        BufferedReader br;
        if (100 <= con.getResponseCode() && con.getResponseCode() <= 399) {
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }
        StringBuilder response = new StringBuilder();
        String responseLine;
        while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
        }
        return response;
    }
}
