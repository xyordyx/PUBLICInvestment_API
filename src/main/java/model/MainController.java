package model;

import model.finsmartData.APIData;
import model.finsmartData.APIDebtorData;
import model.finsmartData.FinsmartData;
import model.firebase.CIGGoogleServices;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class MainController {

    private CoreProcessor core = new CoreProcessor();
    private FinsmartData data = new FinsmartData();
    private ExecutorService threadPool = Executors.newFixedThreadPool(10);

    @GetMapping("/getbalance")
    public ResponseEntity<APIData> getBalance(@RequestHeader Map<String, String> headers) {
        String token = headers.get("authorization");
        if(!token.isEmpty()){
            StopWatch sw = new org.springframework.util.StopWatch();
            sw.start("Method-1 Finsmart Financial Transactions");
            FinancialTransactions transactions = new CIGFinsmart().getFinancialTransactions(token);
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

            return ResponseEntity.ok(new APIData(data));
        } else return null;
    }

    @GetMapping("/getextradata")
    public ResponseEntity<APIData> getExtraData(@RequestHeader Map<String, String> headers) {
        String token = headers.get("authorization");
        if(!token.isEmpty()){
            StopWatch sw = new org.springframework.util.StopWatch();
            sw.start("Method-3 ExtraData");
            data = core.getExtraData(data,token);
            sw.stop();

            StopWatch.TaskInfo[] listofTasks = sw.getTaskInfo();
            for (StopWatch.TaskInfo task : listofTasks) {
                System.out.format("[%s]:[%d]\n",
                        task.getTaskName(), task.getTimeMillis());
            }
            if(data != null){
                return ResponseEntity.ok(new APIData(data));
            }else return null;
        }else return null;
    }

    @GetMapping("/getcurrentinvestments")
    public ResponseEntity<APIDebtorData> getCurrentInvestments(@RequestHeader Map<String, String> headers) {
        String token = headers.get("authorization");
        if(!token.isEmpty()){
            StopWatch sw = new org.springframework.util.StopWatch();
            sw.start("Method-4 Current Investments");
            APIDebtorData investments = core.getCurrentInvestments(data,token);
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
        String token = headers.get("authorization");
        if(!token.isEmpty()) {
            StopWatch sw = new org.springframework.util.StopWatch();
            if (!debtor.isEmpty()) {
                sw.start("Method-4 Debtor History");
                APIDebtorData debtorHistory = core.getDebtorHistory(data, debtor, token);
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

    @PostMapping(value = "/createinvestment")
    public ResponseEntity<Object> createInvestment(@RequestBody InvestmentData investment) {
        CIGGoogleServices cig = new CIGGoogleServices();
        investment.setCompleted(false);
        if (Util.isAutoManaged(investment.getTime())) {
            investment.setCurrentState("DB");
            return new ResponseEntity<>(cig.createInvestment(investment.getFireToken(), investment), HttpStatus.OK);
        } else {
            investment.setCurrentState("Manual_DB");
            Document instanceData = cig.getInstanceById(investment.getFireToken(), investment.getInvoiceId());
            int version = 1;
            if (instanceData != null) {
                version = instanceData.getFields().getVersion().getIntegerValue() + 1;
                instanceData.getFields().getVersion().setIntegerValue(version);
                cig.updateInstance(investment.getFireToken(), instanceData);
            } else {
                cig.createInstance(investment.getFireToken(), new InstanceData(investment.getInvoiceId(), version));
            }
            String scheduleURL = investment.getInvoiceId() + "-dot-" + version + "-dot-s1-dot-";
            Boolean responseJSON = new CIGFinsmart().scheduleInvestment(investment, scheduleURL);
            if (responseJSON) {
                if (cig.createInvestment(investment.getFireToken(), investment)) {
                    return new ResponseEntity<>(cig.createInvestment(investment.getFireToken(), investment), HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>("", HttpStatus.CONFLICT);
    }

    //NOT CALLED FROM APP
    @PostMapping(value = "/scheduleinvestment")
    public ResponseEntity<Boolean> scheduleInvestment(@RequestBody InvestmentData investment) {
        InstanceScheduler inv =  new InstanceScheduler(investment);
        threadPool.submit(inv);
        String temp;
        if(investment.getCurrentState().equals("DB")){
            temp = "Instance_Schedule";
        }else temp = "Manual_DB";

        CIGGoogleServices cig = new CIGGoogleServices();
        model.json.firestore.investments.Document document =
                cig.getInvestmentsById(investment.getFireToken(),investment.getInvoiceId());
        if(document != null){
            InvestmentData tempData = new InvestmentData(document);
            tempData.setCurrentState(temp);
            if(cig.updateInvestment(investment.getFireToken(),tempData)){
                return ResponseEntity.status(200).body(true);
            }
        }
        return ResponseEntity.badRequest().body(false);
    }

    @DeleteMapping(value = "/stopinvestment")
    @ResponseBody
    public ResponseEntity<List<InvestmentData>> stopInvestment(@RequestHeader Map<String, String> headers,
                                                               @RequestParam(name = "filter")String id,
                                                               @RequestParam(name = "caller")Boolean isCompleted){
        String token = headers.get("firestoretoken");
        CIGGoogleServices cig = new CIGGoogleServices();
        model.json.firestore.investments.Document data = cig.getInvestmentsById(token,id);
        if(data != null){
            if(!isCompleted){
                if(data.getFields().getCurrentState().getStringValue().equals("DB") ||
                        data.getFields().getCurrentState().getStringValue().equals("Manual_DB")){
                    if(cig.deleteInvestment(token,id)){
                        return new ResponseEntity<>((Util.getListInvestmentFromList
                                (cig.getInvestmentsByCompleted(token,false))), HttpStatus.OK);
                    }
                }
                else if(data.getFields().getCurrentState().getStringValue().equals("Scheduled")){
                    //TODO: ELIMINAR INSTANCIA
                }
            }else{
                InvestmentData tempData = new InvestmentData(data);
                tempData.setCurrentState("Removed");
                if(cig.updateInvestment(token,tempData)){
                    return new ResponseEntity<>((Util.getListInvestmentFromList
                            (cig.getInvestmentsByCompleted(token,true))), HttpStatus.OK);
                }

            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/currentschedule")
    public ResponseEntity<ArrayList<InvestmentData>> currentSchedule(@RequestHeader Map<String, String> headers) {
        CIGGoogleServices cig = new CIGGoogleServices();
        String token = headers.get("firestoretoken");
        return ResponseEntity.ok(new ArrayList<>(Util.getListInvestmentData
                ((Investments) cig.getCollections(token,"Investments"))));
    }

    @GetMapping(value = "/getbycompleted")
    public ResponseEntity<ArrayList<InvestmentData>> getByCompleted(@RequestHeader Map<String, String> headers,
                                                                    @RequestParam(name = "filter")Boolean isCompleted){
        CIGGoogleServices cig = new CIGGoogleServices();
        String token = headers.get("firestoretoken");
        List<InvestmentData> a = Util.getListInvestmentFromList(cig.getInvestmentsByCompleted(token,isCompleted));
        return ResponseEntity.ok(new ArrayList<>
                (Util.getListInvestmentFromList(cig.getInvestmentsByCompleted(token,isCompleted))));
    }

    @GetMapping("/")
    public String hello() {
        return "<center><h1>Live and ready to rock</h1></center>";
    }

    @RequestMapping(value = "/fire", method = RequestMethod.GET, produces = {"application/json"})
    public @ResponseBody void fire() {
        CIGGoogleServices cig = new CIGGoogleServices();
        String a = cig.getFireToken("u@ser.com","root12");
        /*Object b = cig.getInvestmentsByCompleted(a,false);
        Investments[] x = (Investments []) b;
        Object b = cig.getCollections("Investments",a);
        Investments x = (Investments)b;
        x.getDocuments().get(0).getFields().getCompleted().getBooleanValue();

        InstanceData z = new InstanceData();
        z.setId("asdfsf324324123");
        z.setVersion(123);
        cig.createInstance(a,z);

        InstanceData z = new InstanceData();
        z.setId("123456");
        z.setVersion(1985);
        cig.updateInstance(a,z);

        InvestmentData z = new InvestmentData();
        cig.createInvestment(a,z);

        Document z = cig.getInvestmentsById(a,"sdfdsfds66666");
        z.getFields().getDebtorName().setStringValue("UPDATED DEBTORv2");
        InvestmentData investmentData = new InvestmentData(z);
        cig.updateInvestment(a,investmentData);

        cig.deleteInvestment(a,"pennywisesdfdsfds66666");

        cig.deleteInstance(a,"CHACHA");

        Document x = cig.getInstanceById(a,"CHACHO");
        */
        System.out.println();
    }

}
