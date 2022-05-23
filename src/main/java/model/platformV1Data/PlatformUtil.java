package model.PlatformV1Data;

import model.CIGPlatformV1;
import model.platformV1Data.PlatformData;
import model.json.InvestmentData;
import model.json.Opportunities;
import model.json.ResponseJSON;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class PlatformUtil {

    public static ResponseJSON postToPlatformV1(double amount, InvestmentData investment, String token){
        String parameters;
        ResponseJSON responseJSON = null;
        parameters = generateJSONInvest(amount, investment.getCurrency(), investment.getInvoiceId());
        while(responseJSON == null){
            responseJSON = CIGPlatformV1.executeInvestment1(parameters,token);
        }
        return responseJSON;
    }

    public static ResponseJSON postToPlatformV1Instance(double amount, InvestmentData investment){
        String parameters;
        ResponseJSON responseJSON = null;
        parameters = generateJSONInvest(amount, investment.getCurrency(), investment.getInvoiceId());
        while(responseJSON == null){
            responseJSON = CIGPlatformV1.executeInvestment1(parameters,investment.getSmartToken());
        }
        return responseJSON;
    }

    public static String generateJSONInvest(double amount, String currency, String invoice_id){
        return "{}";
    }

    public static InvestmentData updateInvestment(InvestmentData investment, ResponseJSON responseJSON, int status,
                                                  Double adjustedAmount){
        //LOGIC REMOVED
        return investment;
    }

    public static double updateOpportunity(String token, String invoiceId){
        //LOGIC REMOVED
        return 0.0;
    }

    public static double updateAmountAvailable(List<Opportunities> opportunities, String invoiceId){

        return 0;
    }

    public static StringBuilder readResponse(HttpURLConnection con) throws IOException {
        //LOGIC REMOVED
        return "response";
    }

    public static PlatformData processToBeAmount(List<InvestmentData> investmentData, PlatformData data){

        return data;
    }

    public static ArrayList<Opportunities> opportunitiesNoScheduled(ArrayList<Opportunities> opportunities,
            List<InvestmentData> investmentData){
        //LOGIC REMOVED
        ArrayList<Opportunities> response = new ArrayList<>();
        return response;
    }
}
