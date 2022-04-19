package model;

import model.GoogleCloud.CIGFireStore;
import model.GoogleCloud.GoogleCloudAuthenticator;
import model.finsmartData.*;
import model.json.FinancialTransactions;
import model.json.InvestmentData;
import model.json.Opportunities;
import model.json.firestore.investments.Investments;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
public class ProviderController {

    private final CoreProcessor core = new CoreProcessor();
    private FinsmartData data = new FinsmartData();
    ArrayList<Opportunities> lastOpportunities = new ArrayList<>();
    public boolean tempFlag = true;

    @GetMapping("/getresumeopps")
    public ResponseEntity<APIData> getResumeOpportunities(@RequestHeader Map<String, String> headers) {
        String smartToken = headers.get("authorization");
        int scheduledInvestmentsNum = Integer.parseInt(headers.get("scheduledinvestments"));
        CIGFireStore cig = new CIGFireStore();
        StopWatch sw = new StopWatch();
        sw.start("Method-1 Finsmart Opportunities Resume");
        String googleToken = GoogleCloudAuthenticator.getGoogleCloudToken();
        List<InvestmentData> scheduledDBInvestments;
        ArrayList<Opportunities> response;
        if (!smartToken.isEmpty() && googleToken != null) {
            lastOpportunities = CIGFinsmart.getOpportunitiesJSON(smartToken);

            //FOR TESTING
               //lastOpportunities = new ArrayList<>(Arrays.asList(Util.loadJSONFromFile()));
            //FOR TESTING

            scheduledDBInvestments =
                    Util.getListInvestmentData(((Investments) cig.getCollections(googleToken,"Investments")));
            data.setScheduledInvestmentsNum(scheduledDBInvestments.size());
            response = FinsmartUtil.opportunitiesNoScheduled(lastOpportunities,scheduledDBInvestments);
            sw.stop();

            if(scheduledInvestmentsNum != scheduledDBInvestments.size() || tempFlag){
                sw.start("Method-2 Finsmart Financial Transactions");
                FinancialTransactions transactions = new CIGFinsmart().getFinancialTransactions(smartToken);
                sw.stop();

                sw.start("Method-3 Get Balance");
                data.setFinancialTransactions(transactions);
                data = core.getBalance(Util.resetData(data));
                FinsmartUtil.processToBeAmount(scheduledDBInvestments, data);
                sw.stop();
            }

            StopWatch.TaskInfo[] listOfTasks = sw.getTaskInfo();
            for (StopWatch.TaskInfo task : listOfTasks) {
                System.out.format("[%s]:[%d]\n",
                        task.getTaskName(), task.getTimeMillis());
            }
            tempFlag = false;
            return ResponseEntity.ok(new APIData(new FinancialAPIData(data),response));
        }
        return null;
    }

    //FINSMART REQUESTS
    @GetMapping("/getbalance")
    public ResponseEntity<FinancialAPIData> getBalance(@RequestHeader Map<String, String> headers) {
        String smartToken = headers.get("authorization");
        if(!smartToken.isEmpty()){
            StopWatch sw = new StopWatch();
            sw.start("Method-1 Finsmart Financial Transactions");
            FinancialTransactions transactions = new CIGFinsmart().getFinancialTransactions(smartToken);
            sw.stop();

            sw.start("Method-2 Get Balance");
            if(transactions.getFinancialTransactions().size() != data.getFinancialIndex()){
                data.setFinancialTransactions(transactions);
                data = core.getBalance(Util.resetData(data));
            }
            sw.stop();

            StopWatch.TaskInfo[] listofTasks = sw.getTaskInfo();
            for (StopWatch.TaskInfo task : listofTasks) {
                System.out.format("[%s]:[%d]\n",
                        task.getTaskName(), task.getTimeMillis());
            }

            return ResponseEntity.ok(new FinancialAPIData(data));
        } else return null;
    }

    @GetMapping("/getextradata")
    public ResponseEntity<FinancialAPIData> getExtraData(@RequestHeader Map<String, String> headers) {
        String smartToken = headers.get("authorization");
        if(!smartToken.isEmpty()){
            StopWatch sw = new StopWatch();
            sw.start("Method-3 ExtraData");
            data = core.getExtraData(data,smartToken);
            sw.stop();

            StopWatch.TaskInfo[] listofTasks = sw.getTaskInfo();
            for (StopWatch.TaskInfo task : listofTasks) {
                System.out.format("[%s]:[%d]\n",
                        task.getTaskName(), task.getTimeMillis());
            }
            if(data != null){
                return ResponseEntity.ok(new FinancialAPIData(data));
            }else return null;
        }else return null;
    }

    @GetMapping("/getcurrentinvestments")
    public ResponseEntity<APIDebtorData> getCurrentInvestments(@RequestHeader Map<String, String> headers) {
        String smartToken = headers.get("authorization");
        if(!smartToken.isEmpty()){
            StopWatch sw = new StopWatch();
            sw.start("Method-4 Current Investments");
            APIDebtorData investments = core.getCurrentInvestments(data,smartToken);
            sw.stop();

            StopWatch.TaskInfo[] listofTasks = sw.getTaskInfo();
            for (StopWatch.TaskInfo task : listofTasks) {
                System.out.format("[%s]:[%d]\n",
                        task.getTaskName(), task.getTimeMillis());
            }
            if(investments != null) {
                return ResponseEntity.ok(investments);
            }
        }
        return null;
    }

    @GetMapping("/getdebtorhistory")
    @ResponseBody
    public ResponseEntity<APIDebtorData> getDebtorHistory(@RequestHeader Map<String, String> headers,
                                                          @RequestParam(name = "filter")String debtor) {
        String smartToken = headers.get("authorization");
        if(!smartToken.isEmpty()) {
            StopWatch sw = new StopWatch();
            if (!debtor.isEmpty()) {
                sw.start("Method-4 Debtor History");
                APIDebtorData debtorHistory = core.getDebtorHistory(data, debtor, smartToken);
                sw.stop();
                StopWatch.TaskInfo[] listofTasks = sw.getTaskInfo();
                for (StopWatch.TaskInfo task : listofTasks) {
                    System.out.format("[%s]:[%d]\n",
                            task.getTaskName(), task.getTimeMillis());
                }
                if (data != null) {
                    return ResponseEntity.ok(debtorHistory);
                } else {
                    return null;
                }
            }
        }return null;
    }
}
