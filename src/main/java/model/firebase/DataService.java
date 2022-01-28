package model.firebase;

import model.json.InvestmentData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataService {
    Boolean saveInvestment(InvestmentData dataModel, String state) throws Exception;
    List<InvestmentData> getAllInvestments();
    InstanceData getInstanceById(String instanceId);
    Boolean deleteInvestmentById(String id);
    InvestmentData getInvestmentById(String invoiceId);
    Boolean saveInstance(String dataModel, int version);
    Boolean updateInstance(String dataModel, int version);
    Boolean updateInvestment(InvestmentData dataModel, String state);
    List<InvestmentData> getInvestmentsByCompleted(Boolean isCompleted);
}
