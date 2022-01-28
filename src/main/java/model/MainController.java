package model;

import model.finsmartData.APIData;
import model.finsmartData.APIDebtorData;
import model.finsmartData.FinsmartData;
import model.firebase.DataService;
import model.firebase.InstanceData;
import model.json.Debtor;
import model.json.FinancialTransactions;
import model.json.InvestmentData;
import model.json.ResponseJSON;
import model.thread.InstanceScheduler;
import model.thread.InvestorScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class MainController {

    @Autowired
    private DataService dataService;

    private CoreProcessor core = new CoreProcessor();
    private FinsmartData data = new FinsmartData();
    private ExecutorService threadPool = Executors.newFixedThreadPool(10);
    private ConcurrentHashMap<String, InvestorScheduler> invQueue = new ConcurrentHashMap<>();
    private ArrayList<InvestmentData> activeInvestments = new ArrayList<>();

    public boolean tempFlag = true;
    public ArrayList<Integer> temp = new ArrayList<>();

    @GetMapping("/getbalance")
    public ResponseEntity<APIData> getBalance(@RequestHeader Map<String, String> headers) {
        String token = headers.get("authorization");
        if(!token.isEmpty()){
            StopWatch sw = new org.springframework.util.StopWatch();
            sw.start("Method-1 Finsmart Financial Transactions");
            FinancialTransactions transactions = new CIG().getFinancialTransactions(token);
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

    @RequestMapping(value = "/chao", method = RequestMethod.GET, produces = {"application/json"})
    public @ResponseBody ResponseEntity<ArrayList<Integer>> chao() {
        if(!temp.isEmpty()){
            temp.remove(0);
        }
        return ResponseEntity.ok(temp);
    }

    @PostMapping(value = "/createinvestment")
    public ResponseEntity<Object> createInvestment(@RequestBody InvestmentData investment) throws Exception {
        investment.setCompleted(false);
        if(Util.isAutoManaged(investment.getTime())){
            investment.setCurrentState("DB");
            return new ResponseEntity<>(dataService.saveInvestment(investment,"DB"), HttpStatus.OK);
        } else {
            investment.setCurrentState("Manual_DB");
            if(dataService.saveInvestment(investment,"Manual_DB")){
                int version = 1;
                InstanceData instanceData = dataService.getInstanceById(investment.getInvoiceId());
                if(dataService.saveInstance(investment.getInvoiceId(),version)){
                    String url = investment.getInvoiceId()+"."+version+".";
                    Boolean responseJSON = new CIG().scheduleInvestment(investment,url);
                    if(responseJSON){
                        return new ResponseEntity<>(true, HttpStatus.OK);
                    }
                }else{
                    version = instanceData.getVersion()+1;
                    if(dataService.updateInstance(investment.getInvoiceId(),version)){
                        String url = investment.getInvoiceId()+"."+version+".";
                        Boolean responseJSON = new CIG().scheduleInvestment(investment,url);
                        if(responseJSON){
                            return new ResponseEntity<>(true, HttpStatus.OK);
                        }
                    }
                }
            }
        }
        return new ResponseEntity<>("", HttpStatus.CONFLICT);
    }

    @PostMapping(value = "/scheduleinvestment")
    public ResponseEntity<Boolean> scheduleInvestment(@RequestBody InvestmentData investment) {
        InstanceScheduler inv =  new InstanceScheduler(investment,dataService);
        threadPool.submit(inv);
        String temp;
        if(investment.getCurrentState().equals("DB")){
            temp = "Instance_Schedule";
        }else temp = "Manual_DB";
        if(dataService.updateInvestment(investment,temp)){
            return ResponseEntity.status(200).body(true);
        }
        return ResponseEntity.badRequest().body(false);
    }

    @DeleteMapping(value = "/stopinvestment")
    @ResponseBody
    public ResponseEntity<List<InvestmentData>> stopInvestment(@RequestParam(name = "filter")String id,
                                                               @RequestParam(name = "caller")Boolean isCompleted){
        InvestmentData data = dataService.getInvestmentById(id);
        if(data != null){
            if(!isCompleted){
                if(data.getCurrentState().equals("DB") || data.getCurrentState().equals("Manual_DB")){
                    if(dataService.deleteInvestmentById(id)){
                        return new ResponseEntity<>(dataService.getInvestmentsByCompleted(false), HttpStatus.OK);
                    }
                }
                else if(data.getCurrentState().equals("Scheduled")){
                    //TODO: ELIMINAR INSTANCIA
                }
            }else{
                if(data.getCurrentState().equals("Processed")){
                    if(dataService.updateInvestment(data,"Removed")){
                        return new ResponseEntity<>(dataService.getInvestmentsByCompleted(true),HttpStatus.OK);
                    }
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/currentschedule")
    public ResponseEntity<ArrayList<InvestmentData>> currentSchedule() {
        return ResponseEntity.ok(new ArrayList<>(dataService.getAllInvestments()));
    }

    @GetMapping(value = "/getbycompleted")
    public ResponseEntity<ArrayList<InvestmentData>> getByCompleted(@RequestParam(name = "filter")Boolean isCompleted){
        List<InvestmentData> a = dataService.getInvestmentsByCompleted(true);
        List<InvestmentData> b = dataService.getInvestmentsByCompleted(false);
        return ResponseEntity.ok(new ArrayList<>(dataService.getInvestmentsByCompleted(isCompleted)));
    }

}
