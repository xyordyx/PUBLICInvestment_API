package model.GoogleCloud;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;

import java.io.FileInputStream;
import java.io.IOException;

public class GoogleCloudAuthenticator {
    private static final String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
    private static final String DATASTORE_SCOPE = "https://www.googleapis.com/auth/datastore";
    private static final String CPLATFORM_SCOPE = "https://www.googleapis.com/auth/cloud-platform";
    private static final String ADMIN_SCOPE = "https://www.googleapis.com/auth/appengine.admin";
    private static final String CLOUD_SCOPE = "https://www.googleapis.com/auth/cloud-platform";
    private static final String READ_SCOPE = "https://www.googleapis.com/auth/cloud-platform.read-only";
    private static final String[] ADMIN_SCOPES = { ADMIN_SCOPE,CLOUD_SCOPE,READ_SCOPE,CPLATFORM_SCOPE,DATASTORE_SCOPE,MESSAGING_SCOPE };

    public static String getGoogleCloudToken() {
        GoogleCredentials credentials;
        try {
            //GoogleCredentials credentials = AppEngineCredentials.getApplicationDefault();
            FileInputStream fileInputStream = new FileInputStream("src/main/resources/static/hmrestapi-333720-7dad7f1da042.json");
            credentials = ServiceAccountCredentials.fromStream(fileInputStream).createScoped(ADMIN_SCOPES);
            return credentials.refreshAccessToken().getTokenValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
