package model.GoogleCloud;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.json.appengine.Instances;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class CIGAppEngine {

    private static final String appEngineURL =
            "https://appengine.googleapis.com/v1beta/apps/hmrestapi-333720/services/s1/versions/dev/instances";

    public Instances getCurrentInstances(String token){
        HttpClient httpClient = HttpClientBuilder.create().build();
        String stringResponse;
        HttpGet getRequest = new HttpGet(appEngineURL);

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
            return objectMapper.readValue(stringResponse, Instances.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean deleteAppEngineInstance(String token, String instanceId){
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            HttpDelete httpDelete = new HttpDelete(appEngineURL+"/"+instanceId);
            httpDelete.setHeader("Accept", "application/json");
            httpDelete.setHeader("Content-type", "application/json");
            httpDelete.setHeader("Authorization", "Bearer "+token);

            CloseableHttpResponse response;
            response = client.execute(httpDelete);
            if(response.getStatusLine().getStatusCode() == 200) {
                return true;
            }
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
