package model;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.finsmartData.FinsmartUtil;
import model.json.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static model.Util.getTime;

public class CIG {
    private static String smartURLv1 = "https://api.finsmart.pe/api/v1";
    private static String financialTransactionsPath="/financial-transactions";
    private static String invoices="/invoices";
    private static String authenticationPath="/authentications";
    private static String opportunitiesPath="/opportunities";

    public static ResponseJSON executeInvestment1(String urlParameters, String token) {
        URL url;
        ResponseJSON responseJSON = null;
        String json;
        try {
            url = new URL(smartURLv1+financialTransactionsPath);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            System.out.println(Thread.currentThread().getName()+"CIGReq:"+getTime()+" Investment Initialization");
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept", "application/json, text/plain, */*");
            con.setRequestProperty("Content-type", "application/json");
            con.setRequestProperty("Authorization", "Bearer "+token);
            //con.setConnectTimeout(500);

            con.setDoOutput(true);

            try(OutputStream os = con.getOutputStream()){
                byte[] input = urlParameters.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            int code = con.getResponseCode();
            System.out.println(Thread.currentThread().getName()+"CIGReq:"+getTime()+"CODE RESPONSE: "+code);
            BufferedReader br;
            if (100 <= code && code <= 399) {
                json = "{\"status\":true,\"message\":\"\"}";
            }
            else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                String responseLine;
                StringBuilder response = new StringBuilder();
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                json = "{\"status\":false,\"message\":\""+response+"\"}";
                System.out.println(Thread.currentThread().getName()+"CIGReq:"+getTime()+"ERROR RESPONSE: "
                        +response+" Payload:"+urlParameters);
            }
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            responseJSON = objectMapper.readValue(json,ResponseJSON.class);
        } catch (SocketTimeoutException e) {
            System.out.println(Thread.currentThread().getName()+"CIGReq:"+getTime()+e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseJSON;
    }

    public FinancialTransactions getFinancialTransactions(String token){
        DefaultHttpClient httpClient = new DefaultHttpClient();
        String stringResponse;
        FinancialTransactions financialTransactions = null;
        HttpGet getRequest = new HttpGet(smartURLv1+financialTransactionsPath);

        //Set the API media type in http accept header
        getRequest.addHeader("Accept", "application/json");
        getRequest.addHeader("Content-Type", "application/json");
        getRequest.addHeader("Authorization", "Bearer "+token);

        //Send the request
        try {
            HttpResponse response = httpClient.execute(getRequest);
            stringResponse = EntityUtils.toString(response.getEntity());
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            financialTransactions = objectMapper.readValue(stringResponse, FinancialTransactions.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return financialTransactions;
    }

    public List<InvoiceTransactions> getInvoices(String token){
        DefaultHttpClient httpClient = new DefaultHttpClient();
        String stringResponse;
        List<InvoiceTransactions> invoiceTransactions = new ArrayList<>();
        HttpGet getRequest = new HttpGet(smartURLv1+invoices);

        //Set the API media type in http accept header
        getRequest.addHeader("Accept", "application/json");
        getRequest.addHeader("Content-Type", "application/json");
        getRequest.addHeader("Authorization", "Bearer "+token);

        //Send the request
        try {
            HttpResponse response = httpClient.execute(getRequest);
            stringResponse = EntityUtils.toString(response.getEntity());
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            InvoiceTransactions[] op = objectMapper.readValue(stringResponse, InvoiceTransactions[].class);
            invoiceTransactions = new ArrayList<>(Arrays.asList(op));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return invoiceTransactions;
    }

    public LoginJSON getAuthentications(String email, String passd) {
        CloseableHttpClient client = HttpClients.createDefault();
        String stringResponse = null;
        try {
            HttpPost httpPost = new HttpPost(smartURLv1+authenticationPath);
            final String json = "{\"email\":\""+email+"\",\"actualPassword\":\""+passd+"\"}";
            StringEntity entity = new StringEntity(json);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json, text/plain, */*");
            httpPost.setHeader("Content-type", "application/json");

            CloseableHttpResponse response;

            response = client.execute(httpPost);
            if(response.getStatusLine().getStatusCode() == 201) {
                stringResponse = EntityUtils.toString(response.getEntity());
            }
            client.close();

            return new ObjectMapper()
                    .readerFor(LoginJSON.class)
                    .readValue(stringResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Opportunities> getOpportunitiesJSON(String token) {
        URL url;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            System.setProperty("http.keepAlive", "false");
            url = new URL(smartURLv1+opportunitiesPath);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(1000);
            //con.setReadTimeout(250);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Authorization", "Bearer "+token);
            Type founderListType = new TypeToken<ArrayList<Opportunities>>(){}.getType();
            return gson.fromJson(FinsmartUtil.readResponse(con).toString(), founderListType);
        } catch (MalformedURLException | ProtocolException e ) {
            e.printStackTrace();
        }  catch (SocketTimeoutException e) {
            System.out.println("Opportunities finsmart: 250 milliseconds elapsed on request - "+getTime());
        }catch (Throwable e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
