package model.GoogleCloud;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.json.InvestmentData;
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
    private static final String firebaseKey = "8";
    private static final String fireDatabasesURL =
            "https://firestore.googleapis.com/";
    private static final String fireIntancesURL =
            "https://appengine.googleapis.com/";

    //APPDATA METHODS
    public boolean updateAPPData(String fireToken, String passwordCipher, String userEmail){
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            HttpPatch httpPatch = new HttpPatch(fireDatabasesURL);
            final String json = "//LOGIC REMOVED";
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
            HttpPost httpPost = new HttpPost(fireDatabasesURL);
            final String json= "{//LOGIC REMOVED}";
            if (getStringEntity(fireToken, client, httpPost, json)) return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return updateAPPData(fireToken,passwordCipher, userEmail);
    }

    public APPData getUserDataById(String fireToken, String userEmail){
        //LOGIC REMOVED
        return null;
    }

    //INVESTMENTS METHODS
    public Investments[] getAllInvestments(String fireToken){
        //LOGIC REMOVED
        return null;
    }

    public Investments[] getInvestmentsByCompleted(String fireToken, Boolean isCompleted){
        //LOGIC REMOVED
        return null;
    }

    public Document getInvestmentsById(String fireToken, String invoiceId){
        //LOGIC REMOVED
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
        //LOGIC REMOVED
        return false;
    }

    public boolean deleteFireInvestment(String fireToken, String invoiceId){
        //LOGIC REMOVED
        return false;
    }

    public Object getCollections(String fireToken, String collectionName){
        //LOGIC REMOVED
        return null;
    }

    public String getFireToken(String email, String pass) {
        //LOGIC REMOVED
        return null;
    }



}
