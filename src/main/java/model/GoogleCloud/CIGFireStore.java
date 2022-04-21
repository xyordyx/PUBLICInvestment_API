package model.GoogleCloud;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.json.InvestmentData;
import model.json.firestore.APPData.APPData;
import model.json.firestore.instances.Instances;
import model.json.firestore.investments.Document;
import model.json.firestore.investments.Investments;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.IOException;

public class CIGFireStore {
    private static final String authURL = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=";
    private static final String firebaseKey = "AIzaSyAGw55i3fjlUCIVmAKbAiARf4SDba8ggSs";
    private static final String fireDatabasesURL =
            "https://firestore.googleapis.com/v1/projects/hmnorth/databases/(default)/documents";
    private static final String fireIntancesURL =
            "https://appengine.googleapis.com/v1/apps/hmnorth/services/s1/versions/dev/instances";

    //APPDATA METHODS
    public boolean updateAPPData(String fireToken, String passwordCipher, String userEmail){
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            HttpPatch httpPatch = new HttpPatch(fireDatabasesURL+"/APPData/"+userEmail+"?currentDocument.exists=true");
            final String json = "{\n" +
                    "  \"fields\": {\n" +
                    "    \"userEmail\": {\n" +
                    "      \"stringValue\": \""+userEmail+"\"\n" +
                    "    },\n" +
                    "    \"passwordCipher\": {\n" +
                    "      \"stringValue\": \""+passwordCipher+"\"\n" +
                    "    }\n" +
                    "  }\n" +
                    "}";
            StringEntity entity = new StringEntity(json);
            httpPatch.setEntity(entity);
            httpPatch.setHeader("Content-type", "application/json");
            httpPatch.setHeader("Accept", "application/json");
            httpPatch.setHeader("Authorization", "Bearer "+fireToken);

            CloseableHttpResponse response;
            response = client.execute(httpPatch);
            if(response.getStatusLine().getStatusCode() == 200) {
                return true;
            }
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean createAPPData(String fireToken, String passwordCipher, String userEmail){
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(fireDatabasesURL+"/APPData?documentId="+userEmail);
            final String json= "{\n" +
                    "  \"fields\": {\n" +
                    "    \"userEmail\": {\n" +
                    "      \"stringValue\": \""+userEmail+"\"\n" +
                    "    },\n" +
                    "    \"passwordCipher\": {\n" +
                    "      \"stringValue\": \""+passwordCipher+"\"\n" +
                    "    }\n" +
                    "  }\n" +
                    "}";
            if (getStringEntity(fireToken, client, httpPost, json)) return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return updateAPPData(fireToken,passwordCipher, userEmail);
    }

    public APPData getUserDataById(String fireToken, String userEmail){
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet getRequest = new HttpGet(fireDatabasesURL+"/APPData/"+userEmail);
        String stringResponse;
        //Set the API media type in http accept header
        getRequest.addHeader("Accept", "application/json");
        getRequest.addHeader("Content-Type", "application/json");
        getRequest.addHeader("Authorization", "Bearer "+fireToken);

        //Send the request
        try {
            HttpResponse response = httpClient.execute(getRequest);
            if(response.getStatusLine().getStatusCode() == 200){
                stringResponse = EntityUtils.toString(response.getEntity());
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                return objectMapper.readValue(stringResponse, APPData.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //INVESTMENTS METHODS
    public Investments[] getAllInvestments(String fireToken){
        HttpClient httpClient = HttpClientBuilder.create().build();
        String stringResponse;
        HttpGet getRequest = new HttpGet(fireDatabasesURL+"/Investments/?key="+firebaseKey);

        //Set the API media type in http accept header
        getRequest.addHeader("Accept", "application/json");
        getRequest.addHeader("Content-Type", "application/json");
        getRequest.addHeader("Authorization", "Bearer "+fireToken);

        //Send the request
        try {
            HttpResponse response = httpClient.execute(getRequest);
            stringResponse = EntityUtils.toString(response.getEntity());
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.readValue(stringResponse, Investments[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Investments[] getInvestmentsByCompleted(String fireToken, Boolean isCompleted){
        CloseableHttpClient client = HttpClients.createDefault();
        String stringResponse;
        try {
            HttpPost httpPost = new HttpPost(fireDatabasesURL+":runQuery");
            final String json = "{\n" +
                    "    \"structuredQuery\": {\n" +
                    "        \"where\" : {\n" +
                    "            \"fieldFilter\" : { \n" +
                    "                \"field\": {\"fieldPath\": \"completed\"}, \n" +
                    "                \"op\":\"EQUAL\", \n" +
                    "                \"value\": {\"booleanValue\":"+isCompleted+"}\n" +
                    "            }\n" +
                    "        },\n" +
                    "        \"from\": [{\"collectionId\": \"Investments\"}]\n" +
                    "    }\n" +
                    "}";
            StringEntity entity = new StringEntity(json);
            httpPost.setEntity(entity);
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("Authorization", "Bearer "+fireToken);

            CloseableHttpResponse response;

            response = client.execute(httpPost);
            if(response.getStatusLine().getStatusCode() == 200) {
                stringResponse = EntityUtils.toString(response.getEntity());
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                return objectMapper.readValue(stringResponse, Investments[].class);
            }
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Document getInvestmentsById(String fireToken, String invoiceId){
        HttpClient httpClient = HttpClientBuilder.create().build();
        String stringResponse;
        HttpGet getRequest = new HttpGet(fireDatabasesURL+"/Investments/"+invoiceId);

        //Set the API media type in http accept header
        getRequest.addHeader("Accept", "application/json");
        getRequest.addHeader("Content-Type", "application/json");
        getRequest.addHeader("Authorization", "Bearer "+fireToken);

        //Send the request
        try {
            HttpResponse response = httpClient.execute(getRequest);
            stringResponse = EntityUtils.toString(response.getEntity());
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.readValue(stringResponse, Document.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean createFireInvestment(String fireToken, InvestmentData investment){
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(fireDatabasesURL+"/Investments?documentId="+investment.getInvoiceId());
            if (getStringEntity(fireToken, client, httpPost, investmentBlock(investment))) return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return updateFireInvestment(fireToken,investment);
    }

    public boolean updateFireInvestment(String fireToken, InvestmentData investment){
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            HttpPatch httpPatch = new HttpPatch(
                    fireDatabasesURL+"/Investments/"+investment.getInvoiceId()+ "?currentDocument.exists=true");
            StringEntity entity = new StringEntity(investmentBlock(investment));
            httpPatch.setEntity(entity);
            httpPatch.setHeader("Accept", "application/json");
            httpPatch.setHeader("Content-type", "application/json");
            httpPatch.setHeader("Authorization", "Bearer "+fireToken);

            CloseableHttpResponse response;
            response = client.execute(httpPatch);
            if(response.getStatusLine().getStatusCode() == 200) {
                return true;
            }
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteFireInvestment(String fireToken, String invoiceId){
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            HttpDelete httpDelete = new HttpDelete(fireDatabasesURL+"/Investments/"+invoiceId);
            httpDelete.setHeader("Accept", "application/json");
            httpDelete.setHeader("Content-type", "application/json");
            httpDelete.setHeader("Authorization", "Bearer "+fireToken);

            CloseableHttpResponse response;
            response = client.execute(httpDelete);
            if(response.getStatusLine().getStatusCode() == 200) {
                return true;
            }
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Object getCollections(String fireToken, String collectionName){
        HttpClient httpClient = HttpClientBuilder.create().build();
        String stringResponse;
        HttpGet getRequest = new HttpGet(fireDatabasesURL+"/"+collectionName+"/?key="+firebaseKey);

        //Set the API media type in http accept header
        getRequest.addHeader("Accept", "application/json");
        getRequest.addHeader("Content-Type", "application/json");
        getRequest.addHeader("Authorization", "Bearer "+fireToken);

        //Send the request
        try {
            HttpResponse response = httpClient.execute(getRequest);
            stringResponse = EntityUtils.toString(response.getEntity());
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            if(collectionName.equals("Instance")){
                return objectMapper.readValue(stringResponse, Instances.class);
            }else if(collectionName.equals("Investments")){
                return objectMapper.readValue(stringResponse, Investments.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getFireToken(String email, String pass) {
        CloseableHttpClient client = HttpClients.createDefault();
        String stringResponse = null;
        try {
            HttpPost httpPost = new HttpPost(authURL+firebaseKey);
            final String json = "{\"email\":\""+email+"\",\"password\":\""+pass+"\",\"returnSecureToken\":true}";
            StringEntity entity = new StringEntity(json);
            httpPost.setEntity(entity);
            httpPost.setHeader("Content-type", "application/json");

            CloseableHttpResponse response;

            response = client.execute(httpPost);
            if(response.getStatusLine().getStatusCode() == 200) {
                stringResponse = EntityUtils.toString(response.getEntity());
            }
            client.close();
            return new ObjectMapper().readTree(stringResponse).get("idToken").asText();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void getAllCloudInstances(String fireToken){
        HttpClient httpClient = HttpClientBuilder.create().build();
        String stringResponse;
        HttpGet getRequest = new HttpGet(fireIntancesURL+"?key="+firebaseKey);

        //Set the API media type in http accept header
        getRequest.addHeader("Accept", "application/json");
        getRequest.addHeader("Content-Type", "application/json");
        getRequest.addHeader("Authorization", "Bearer "+fireToken);

        //Send the request
        try {
            HttpResponse response = httpClient.execute(getRequest);
            EntityUtils.toString(response.getEntity());
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    boolean getStringEntity(String fireToken, CloseableHttpClient client, HttpPost httpPost, String json) throws IOException {
        StringEntity entity = new StringEntity(json);
        httpPost.setEntity(entity);
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setHeader("Authorization", "Bearer "+fireToken);

        CloseableHttpResponse response;

        response = client.execute(httpPost);
        if(response.getStatusLine().getStatusCode() == 200) {
            return true;
        }
        //System.out.println(EntityUtils.toString(response.getEntity()));
        client.close();
        return false;
    }

    String investmentBlock(InvestmentData investment) {
        return "{\n" +
                "  \"fields\": {\n" +
                "    \"adjustedAmount\": {\n" +
                "      \"doubleValue\": \""+investment.getAdjustedAmount()+"\"\n" +
                "    },\n" +
                "    \"autoAdjusted\": {\n" +
                "      \"booleanValue\": "+investment.isAutoAdjusted()+"\n" +
                "    },\n" +
                "    \"completed\": {\n" +
                "      \"booleanValue\": "+investment.isCompleted()+"\n" +
                "    },\n" +
                "    \"currency\": {\n" +
                "      \"stringValue\": \""+investment.getCurrency()+"\"\n" +
                "    },\n" +
                "    \"currentState\": {\n" +
                "      \"stringValue\": \""+investment.getCurrentState()+"\"\n" +
                "    },\n" +
                "    \"debtorName\": {\n" +
                "      \"stringValue\": \""+investment.getDebtorName()+"\"\n" +
                "    },\n" +
                "    \"invoiceId\": {\n" +
                "      \"stringValue\": \""+investment.getInvoiceId()+"\"\n" +
                "    },\n" +
                "    \"message\": {\n" +
                "      \"stringValue\": \""+investment.getMessage()+"\"\n" +
                "    },\n" +
                "    \"status\": {\n" +
                "      \"booleanValue\": "+investment.isStatus()+"\n" +
                "    },\n" +
                "    \"time\": {\n" +
                "      \"stringValue\": \""+investment.getTime()+"\"\n" +
                "    },\n" +
                "    \"smartToken\": {\n" +
                "      \"stringValue\": \""+investment.getSmartToken()+"\"\n" +
                "    },\n" +
                "    \"instanceId\": {\n" +
                "      \"stringValue\": \""+investment.getInstanceId()+"\"\n" +
                "    },\n" +
                "    \"userId\": {\n" +
                "      \"stringValue\": \""+investment.getUserId()+"\"\n" +
                "    },\n" +
                "    \"instanceVersion\": {\n" +
                "      \"doubleValue\": \""+investment.getInstanceVersion()+"\"\n" +
                "    },\n" +
                "    \"onSale\": {\n" +
                "      \"booleanValue\": \""+investment.isOnSale()+"\"\n" +
                "    },\n" +
                "    \"onSaleSlot\": {\n" +
                "      \"integerValue\": \""+investment.getOnSaleSlot()+"\"\n" +
                "    },\n" +
                "    \"amount\": {\n" +
                "      \"doubleValue\": "+investment.getAmount()+"\n" +
                "    }\n" +
                "  }\n" +
                "}";
    }


}
