package model;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.json.*;
import model.json.firestore.APPData.APPData;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import static model.Util.getTime;

public class CIGPlatformV1 {
    //VARIABLES MODIFIED TO PROTECT PLATFORMS
    private static final String platformAPIv1 = "";
    private static final String APITransactions="/transactions";
    private static final String APIInvoices ="/invoices";
    private static final String auth ="/";
    private static final String delta ="/";

    private static final String appEnginePath="";

    public Boolean scheduleInvestment(InvestmentData investment) {
        CloseableHttpClient client = HttpClients.createDefault();
        String stringResponse;
        try {
            HttpPost httpPost = new HttpPost("https://");
            final String json = "LOGIC REMOVED";
            StringEntity entity = new StringEntity(json, "UTF-8");
            httpPost.setEntity(entity);
            httpPost.setHeader("Content-type", "application/json");

            CloseableHttpResponse response;
            response = client.execute(httpPost);
            if(response.getStatusLine().getStatusCode() == 200) {
                stringResponse = EntityUtils.toString(response.getEntity());
                client.close();
                return new ObjectMapper()
                        .readerFor(Boolean.class)
                        .readValue(stringResponse);
            }
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ResponseJSON executeInvestment1(String urlParameters, String token) {
        URL url;
        ResponseJSON responseJSON = null;
        String json;
        try {
            url = new URL(platformAPIv1+APITransactions);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            System.out.println(Thread.currentThread().getName()+"CIGReq:"+getTime()+" Investment Initialization");
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept", "application/json, text/plain, */*");
            con.setRequestProperty("Content-type", "application/json");
            con.setRequestProperty("Authorization", "Bearer "+token);

            con.setDoOutput(true);
            //LOGIC REMOVED
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            responseJSON = objectMapper.readValue("json",ResponseJSON.class);
        } catch (SocketTimeoutException e) {
            System.out.println(Thread.currentThread().getName()+"CIGReq:"+getTime()+e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseJSON;
    }

    public FinancialTransactions getFinancialTransactions(String token){
        HttpClient httpClient = HttpClientBuilder.create().build();
        String stringResponse;
        FinancialTransactions financialTransactions = null;
        HttpGet getRequest = new HttpGet(platformAPIv1+APITransactions);

        //Set the API media type in http accept header
        getRequest.addHeader("Accept", "application/json");
        getRequest.addHeader("Content-Type", "application/json");
        getRequest.addHeader("Authorization", "Bearer "+token);

        //Send the request

            //LOGIC REMOVED
            financialTransactions = null;

        return financialTransactions;
    }

    public static List<InvoiceTransactions> getInvoices(String token){
        HttpClient httpClient = HttpClientBuilder.create().build();
        String stringResponse;
        List<InvoiceTransactions> invoiceTransactions = new ArrayList<>();
        HttpGet getRequest = new HttpGet(platformAPIv1+ APIInvoices);

        //Set the API media type in http accept header
        getRequest.addHeader("Accept", "application/json");
        getRequest.addHeader("Content-Type", "application/json");
        getRequest.addHeader("Authorization", "Bearer "+token);

        //Send the request
        //LOGIC REMOVED
        return invoiceTransactions;
    }


    public static LoginJSON getAuthentications(APPData userData) {
        CloseableHttpClient client = HttpClients.createDefault();
        String stringResponse = null;
        try {
            HttpPost httpPost = new HttpPost(platformAPIv1+ auth);
            final String json = "{\"email\":\""+userData.getFields().getUserEmail().getStringValue()
                    +"\",\"actualPassword\":\""+userData.getFields().getPasswordCipher().getStringValue()+"\"}";
            StringEntity entity = new StringEntity(json);
            httpPost.setEntity(entity);
            httpPost.setHeader("Content-type", "application/json");

        //LOGIC REMOVED
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Opportunities> getOpportunitiesJSON(String token) {
        //LOGIC REMOVED
        return null;
    }
}
