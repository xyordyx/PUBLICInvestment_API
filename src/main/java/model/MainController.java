package model;

import model.GoogleCloud.CIGAppEngine;
import model.GoogleCloud.GoogleCloudAuthenticator;
import model.finsmartData.*;
import model.GoogleCloud.CIGFireStore;
import model.json.Opportunities;
import model.json.firestore.APPData.APPData;
import model.json.firestore.instances.Document;
import model.json.firestore.instances.InstanceData;
import model.json.FinancialTransactions;
import model.json.InvestmentData;
import model.json.firestore.investments.Investments;
import model.thread.InstanceScheduler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import javax.crypto.spec.IvParameterSpec;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class MainController {

    private final CoreProcessor core = new CoreProcessor();
    private FinsmartData data = new FinsmartData();
    private final ExecutorService threadPool = Executors.newFixedThreadPool(10);
    ArrayList<Opportunities> lastOpportunities = new ArrayList<>();
    public boolean tempFlag = true;

    public ArrayList<Integer> temp = new ArrayList<>();

    @GetMapping("/getresumeopps")
    public ResponseEntity<APIData> getResumeOpportunities(@RequestHeader Map<String, String> headers) {
        String smartToken = headers.get("authorization");
        int scheduledInvestmentsNum = Integer.parseInt(headers.get("scheduledinvestments"));
        CIGFireStore cig = new CIGFireStore();
        StopWatch sw = new org.springframework.util.StopWatch();
        sw.start("Method-1 Finsmart Opportunities Resume");
        String googleToken = GoogleCloudAuthenticator.getGoogleCloudToken();
        List<InvestmentData> scheduledDBInvestments;
        ArrayList<Opportunities> response;
        if (!smartToken.isEmpty() && googleToken != null) {
            lastOpportunities = CIGFinsmart.getOpportunitiesJSON(smartToken);
            //lastOpportunities = Util.cleanOpportunities(CIGFinsmart.getOpportunitiesJSON(smartToken),lastOpportunities);
            scheduledDBInvestments =
                    Util.getListInvestmentFromList(cig.getInvestmentsByCompleted(googleToken,false));
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
            StopWatch sw = new org.springframework.util.StopWatch();
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
            StopWatch sw = new org.springframework.util.StopWatch();
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
            StopWatch sw = new org.springframework.util.StopWatch();
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
            StopWatch sw = new org.springframework.util.StopWatch();
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

    //FIRESTORE REQUESTS
    @GetMapping(value = "/getuserdata")
    public ResponseEntity<Boolean> getUserData(@RequestHeader Map<String, String> headers){
        String userEmail = headers.get("useremail");
        CIGFireStore cig = new CIGFireStore();
        String token = GoogleCloudAuthenticator.getGoogleCloudToken();
        if(token != null){
            return ResponseEntity.ok(cig.getUserDataById(token,userEmail));
        }
        return null;
    }

    @GetMapping("/setappdata")
    public ResponseEntity<Boolean> setAPPData(@RequestHeader Map<String, String> headers) {
        String fnPassword = headers.get("password");
        String fnEmail = headers.get("email");

        String cryptPassword = headers.get("salt");
        String cipherPassword = Util.encrypt(fnPassword, cryptPassword);

        CIGFireStore cig = new CIGFireStore();
        String token = GoogleCloudAuthenticator.getGoogleCloudToken();
        if(token != null) {
            return ResponseEntity.ok(cig.createAPPData(token, new APPData(cipherPassword),fnEmail));
        }
        return ResponseEntity.badRequest().body(false);
    }

    @PostMapping(value = "/createinvestment")
    public ResponseEntity<Object> createInvestment(@RequestBody InvestmentData investment) {
        CIGFireStore cig = new CIGFireStore();
        investment.setCompleted(false);
        String googleToken;
        if (Util.isAutoManaged(investment.getTime())) {
            investment.setCurrentState("DB");
            googleToken = GoogleCloudAuthenticator.getGoogleCloudToken();
            if(googleToken != null) return new ResponseEntity<>(cig.createFireInvestment(googleToken, investment), HttpStatus.OK);
        } else {
            googleToken = GoogleCloudAuthenticator.getGoogleCloudToken();
            if(googleToken != null){
                investment.setCurrentState("Manual_DB");
                Document instanceData = cig.getInstanceById(googleToken, investment.getInvoiceId());
                int version = 1;
                if (instanceData != null) {
                    version = instanceData.getFields().getVersion().getIntegerValue() + 1;
                    instanceData.getFields().getVersion().setIntegerValue(version);
                    cig.updateFireInstance(googleToken, instanceData);
                } else {
                    cig.createFireInstance(googleToken, new InstanceData(investment.getInvoiceId(), version));
                }
                String scheduleURL = investment.getInvoiceId() + "-dot-" + version + "-dot-s1-dot-";
                Boolean responseJSON = new CIGFinsmart().scheduleInvestment(investment, scheduleURL);
                if (responseJSON) {
                    if (cig.createFireInvestment(googleToken, investment)) {
                        return new ResponseEntity<>(cig.createFireInvestment(googleToken, investment), HttpStatus.OK);
                    }
                }
            }
        }
        return new ResponseEntity<>("", HttpStatus.CONFLICT);
    }

    @DeleteMapping(value = "/stopinvestment")
    @ResponseBody
    public ResponseEntity<List<InvestmentData>> stopInvestment(@RequestParam(name = "filter")String id,
                                                               @RequestParam(name = "caller")Boolean isCompleted){
        CIGFireStore cig = new CIGFireStore();
        String token = GoogleCloudAuthenticator.getGoogleCloudToken();
        if(token != null){
            model.json.firestore.investments.Document data = cig.getInvestmentsById(token,id);
            if(data != null){
                if(!isCompleted){
                    if(data.getFields().getCurrentState().getStringValue().equals("DB") ||
                            data.getFields().getCurrentState().getStringValue().equals("Manual_DB")){
                        if(cig.deleteFireInvestment(token,id)){
                            return new ResponseEntity<>((Util.getListInvestmentFromList
                                    (cig.getInvestmentsByCompleted(token,false))), HttpStatus.OK);
                        }
                    }
                    else if(data.getFields().getCurrentState().getStringValue().equals("Scheduled")){
                        if(CIGAppEngine.deleteAppEngineInstance(token,id) && cig.deleteFireInvestment(token,id)){
                            return new ResponseEntity<>((Util.getListInvestmentFromList
                                    (cig.getInvestmentsByCompleted(token,false))), HttpStatus.OK);
                        }
                    }
                }else{
                    InvestmentData tempData = new InvestmentData(data);
                    tempData.setCurrentState("Removed");
                    if(cig.updateFireInvestment(token,tempData)){
                        return new ResponseEntity<>((Util.getListInvestmentFromList
                                (cig.getInvestmentsByCompleted(token,true))), HttpStatus.OK);
                    }

                }
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/currentschedule")
    public ResponseEntity<ArrayList<InvestmentData>> currentSchedule() {
        CIGFireStore cig = new CIGFireStore();
        String token = GoogleCloudAuthenticator.getGoogleCloudToken();
        if(token != null){
            return ResponseEntity.ok(new ArrayList<>(Util.getListInvestmentData
                    ((Investments) cig.getCollections(token,"Investments"))));
        }
        return null;
    }

    @GetMapping(value = "/getbycompleted")
    public ResponseEntity<ArrayList<InvestmentData>> getByCompleted(@RequestParam(name = "filter")Boolean isCompleted){
        CIGFireStore cig = new CIGFireStore();
        String token = GoogleCloudAuthenticator.getGoogleCloudToken();
        if(token != null){
            return ResponseEntity.ok(new ArrayList<>
                    (Util.getListInvestmentFromList(cig.getInvestmentsByCompleted(token,isCompleted))));
        }
        return null;
    }

    //HEALTH REQUESTS
    @GetMapping("/")
    public String hello() {
        return "<center><h1>Live and ready to rock</h1></center>";
    }

    @RequestMapping(value = "/hello", method = RequestMethod.GET, produces = {"application/json"})
    public @ResponseBody ResponseEntity<ArrayList<Integer>> initial() {
        if(tempFlag){
            temp.add(ThreadLocalRandom.current().nextInt(0,20));
            temp.add(ThreadLocalRandom.current().nextInt(0,20));
            temp.add(ThreadLocalRandom.current().nextInt(0,20));
            temp.add(ThreadLocalRandom.current().nextInt(0,20));
            temp.add(ThreadLocalRandom.current().nextInt(0,20));
            tempFlag = false;
        }
        return ResponseEntity.ok(temp);
    }

    //NOT CALLED FROM APP
    @PostMapping(value = "/scheduleinvestment")
    public ResponseEntity<Boolean> scheduleInvestment(@RequestBody InvestmentData investment) {
        CIGFireStore cig = new CIGFireStore();
        String googleToken = GoogleCloudAuthenticator.getGoogleCloudToken();
        if(googleToken != null){
            InstanceScheduler inv =  new InstanceScheduler(investment,googleToken);
            threadPool.submit(inv);
            String temp;
            if(investment.getCurrentState().equals("DB")){
                temp = "Instance_Schedule";
            }else temp = "Manual_DB";
            model.json.firestore.investments.Document document =
                    cig.getInvestmentsById(googleToken, investment.getInvoiceId());
            if (document != null) {
                InvestmentData tempData = new InvestmentData(document);
                tempData.setCurrentState(temp);
                if (cig.updateFireInvestment(googleToken, tempData)) {
                    return ResponseEntity.status(200).body(true);
                }
            }
        }
        return ResponseEntity.badRequest().body(false);
    }

}
