package model.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Service
public class FirebaseInitialize {
    @PostConstruct
    private  void  iniFirestore() throws IOException {
        InputStream serviceAccount = new FileInputStream("src/main/resources/static/hmrestapi-333720-firebase-adminsdk-dcut9-cc941e0f6c.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://hmrestapi-333720.firebaseapp.com")
                .build();

        if(FirebaseApp.getApps().isEmpty()){
            FirebaseApp.initializeApp(options);
        }
    }

    public Firestore getFirestore(){
        return FirestoreClient.getFirestore();
    }
}
