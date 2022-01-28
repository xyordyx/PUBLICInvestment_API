package model.firebase;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import model.json.InvestmentData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static model.Util.getTime;

@Service
public class DataServiceImpl implements DataService {
    @Autowired
    private FirebaseInitialize firebase;

    @Override
    public List<InvestmentData> getAllInvestments() {
        List<InvestmentData> response = new ArrayList<>();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getInvestments().get();
        return getInvestmentData(response, querySnapshotApiFuture);
    }

    @Override
    public List<InvestmentData> getInvestmentsByCompleted(Boolean isCompleted) {
        List<InvestmentData> response = new ArrayList<>();
        Query query = getInvestments().whereEqualTo("completed",isCompleted);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        return getInvestmentData(response, querySnapshot);
    }

    private List<InvestmentData> getInvestmentData(List<InvestmentData> response, ApiFuture<QuerySnapshot> future) {
        InvestmentData temp;
        try {
            for(DocumentSnapshot doc : future.get().getDocuments()){
                temp = doc.toObject(InvestmentData.class);
                assert temp != null;
                temp.setInvoiceId(doc.getId());
                response.add(temp);
            }
            return response;
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }

    @Override
    public Boolean saveInvestment(InvestmentData dataModel, String state) {
        try {
            dataModel.setCurrentState(state);
            ApiFuture<WriteResult> investmentCollections = getInvestments().document(dataModel.getInvoiceId())
                    .create(dataModel);
            if (null != investmentCollections.get()) {
                //System.out.println(Thread.currentThread().getName() + ":"+dataModel.getDebtorName() +" - DB recorded- " + getTime());
                return Boolean.TRUE;
            }
        } catch (Exception e) {
            System.out.println("Error: "+e.getCause());
            return Boolean.FALSE;
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean updateInvestment(InvestmentData dataModel, String state) {
        Map<String, Object> doc = new HashMap<>();
        doc.put("adjustedAmount",dataModel.getAdjustedAmount());
        doc.put("autoAdjusted",dataModel.isAutoAdjusted());
        doc.put("completed",dataModel.isCompleted());
        doc.put("currentState",state);
        doc.put("message",dataModel.getMessage());
        doc.put("status",dataModel.isStatus());
        doc.put("token", dataModel.getToken());
        try {
            ApiFuture<WriteResult> investmentCollections = getInvestments().document(dataModel.getInvoiceId())
                    .update(doc);
            if (null != investmentCollections.get()) {
                //System.out.println(Thread.currentThread().getName() + ":"+dataModel.getDebtorName()
                //        +" - Investment updated- " + getTime());
                return Boolean.TRUE;
            }
        } catch (Exception e) {
            System.out.println("Error: "+e.getCause());
            return Boolean.FALSE;
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean saveInstance(String instanceId, int version) {
        InstanceData temp = new InstanceData();
        temp.setVersion(version);
        temp.setId(instanceId);
        try {
            ApiFuture<WriteResult> investmentCollections = getInstances().document(instanceId)
                    .create(temp);
            if (null != investmentCollections.get()) {
                //System.out.println(Thread.currentThread().getName() + ":"+instanceId +" - Instance recorded- " + getTime());
                return Boolean.TRUE;
            }
        } catch (Exception e) {
            System.out.println("Error: "+e.getCause());
            return Boolean.FALSE;
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean updateInstance(String instanceId, int version) {
        Map<String, Object> doc = new HashMap<>();
        doc.put("version",version);
        try {
            ApiFuture<WriteResult> investmentCollections = getInstances().document(instanceId)
                    .update(doc);
            if (null != investmentCollections.get()) {
                //System.out.println(Thread.currentThread().getName() + ":"+instanceId +" - Instance updated- " + getTime());
                return Boolean.TRUE;
            }
        } catch (Exception e) {
            System.out.println("Error: "+e.getCause());
            return Boolean.FALSE;
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean deleteInvestmentById(String id) {
        ApiFuture<WriteResult> collectionsApiFuture = getInvestments().document(id).delete();
        try {
            if(null != collectionsApiFuture.get()){
                System.out.println(Thread.currentThread().getName() + ":"+id+" - DB deleted- " + getTime());
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } catch (Exception e) {
            System.out.println("Error: "+e.getCause());
            return Boolean.FALSE;
        }
    }

    private CollectionReference getInvestments() {
        return firebase.getFirestore().collection("Investments");
    }

    private CollectionReference getInstances() {
        return firebase.getFirestore().collection("Instances");
    }

    public InvestmentData getInvestmentById(String invoiceId) {
        DocumentReference docRef = getInvestments().document(invoiceId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document;
        try {
            document = future.get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (document.exists()) {
            return document.toObject(InvestmentData.class);
        }
        return null;
    }

    public InstanceData getInstanceById(String instanceId) {
        DocumentReference docRef = getInstances().document(instanceId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document;
        try {
            document = future.get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (document.exists()) {
            return document.toObject(InstanceData.class);
        }
        return null;
    }
}
