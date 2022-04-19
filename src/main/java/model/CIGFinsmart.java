package model;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import model.finsmartData.FinsmartUtil;
import model.json.*;
import model.json.firestore.APPData.APPData;
import model.json.firestore.instances.InstanceData;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.sound.midi.SysexMessage;
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
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static model.Util.getTime;

public class CIGFinsmart {
    private static final String smartURLv1 = "https://api.finsmart.pe/api/v1";
    private static final String financialTransactionsPath="/financial-transactions";
    private static final String invoices="/invoices";
    private static final String authenticationPath="/authentications";
    private static final String opportunitiesPath="/opportunities";

    private static final String appEnginePath="hmrestapi-333720.uk.r.appspot.com";

    public Boolean scheduleInvestment(InvestmentData investment) {
        CloseableHttpClient client = HttpClients.createDefault();
        String stringResponse;
        int instance = new Random().nextInt(19);
        String scheduleURL = investment.getInvoiceId() + "-dot-"+ instance + "-dot-s1-dot-";

        try {
            HttpPost httpPost = new HttpPost("https://"+scheduleURL+appEnginePath+"/scheduleinvestment");
            //HttpPost httpPost = new HttpPost("http://localhost:8080/scheduleinvestment");
            final String json = "{" +
                    "\"invoiceId\":\""+investment.getInvoiceId()+"\"," +
                    "\"time\":\""+investment.getTime()+"\"," +
                    "\"amount\":\""+investment.getAmount()+"\"," +
                    "\"currency\":\""+investment.getCurrency()+"\"," +
                    "\"smartToken\":\""+investment.getSmartToken()+"\"," +
                    "\"message\":\""+investment.getMessage()+"\"," +
                    "\"status\":\""+investment.isStatus()+"\"," +
                    "\"currentState\":\""+investment.getCurrentState()+"\"," +
                    "\"instanceVersion\":\""+instance+"\"," +
                    "\"adjustedAmount\":\""+investment.getAdjustedAmount()+"\"," +
                    "\"autoAdjusted\":\""+investment.isAutoAdjusted()+"\"," +
                    "\"completed\":\""+investment.isCompleted()+"\"," +
                    "\"userId\":\""+investment.getUserId()+"\"," +
                    "\"debtorName\":\""+investment.getDebtorName()+
                    "\"}";
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
        HttpClient httpClient = HttpClientBuilder.create().build();
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

    public static List<InvoiceTransactions> getInvoices(String token){
        HttpClient httpClient = HttpClientBuilder.create().build();
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
            ObjectMapper objectMapper = Util.initiatePrettyObjectMapper();
            invoiceTransactions =  Util.customReadJavaType(response,objectMapper);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return invoiceTransactions;
    }

    public static List<InvoiceTransactions> getInvoices0(String token){
        HttpClient httpClient = HttpClientBuilder.create().build();
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
        System.out.println("INVOICES COUNT: "+invoiceTransactions.size());
        return invoiceTransactions;
    }

    public static LoginJSON getAuthentications(APPData userData) {
        CloseableHttpClient client = HttpClients.createDefault();
        String stringResponse = null;
        try {
            HttpPost httpPost = new HttpPost(smartURLv1+authenticationPath);
            final String json = "{\"email\":\""+userData.getFields().getUserEmail().getStringValue()
                    +"\",\"actualPassword\":\""+userData.getFields().getPasswordCipher().getStringValue()+"\"}";
            StringEntity entity = new StringEntity(json);
            httpPost.setEntity(entity);
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

    public static ArrayList<Opportunities> getOpportunitiesJSON(String token) {
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
