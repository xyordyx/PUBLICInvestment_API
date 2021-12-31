package controller;

import model.CIG;
import model.CoreProcessor;
import model.Util;
import model.finsmartData.APIData;
import model.finsmartData.APIDebtorData;
import model.finsmartData.FinsmartData;
import model.json.FinancialTransactions;
import model.json.InvestmentData;
import model.thread.InvestorScheduler;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class MainController {
    private CoreProcessor core = new CoreProcessor();
    private FinsmartData data = new FinsmartData();
    private ExecutorService threadPool = Executors.newFixedThreadPool(10);
    private ConcurrentHashMap<String, InvestorScheduler> invQueue = new ConcurrentHashMap<>();
    private ArrayList<InvestmentData> activeInvestments = new ArrayList<>();

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

    @PostMapping(value = "/scheduleinvestment")
    public ResponseEntity<String> scheduleInvestment(@RequestBody InvestmentData investment) {
        if(Util.isValidData(investment)){
            if(Util.isCurrentScheduled(activeInvestments,investment.getInvoiceId()) == 66){
                InvestorScheduler inv = new InvestorScheduler(investment, investment.getToken(), invQueue);
                invQueue.put(investment.getInvoiceId(), inv);
                threadPool.submit(inv);
                investment.setCompleted(false);
                investment.setScheduled(true);
                activeInvestments.add(investment);
                return ResponseEntity.status(200).body("Scheduled");
            }else return ResponseEntity.status(406).body("Duplicated");
        }else return ResponseEntity.badRequest().body("Bad Request");
    }

    @GetMapping("/stopinvestment")
    @ResponseBody
    public ResponseEntity<ArrayList<InvestmentData>> stopInvestment(@RequestParam(name = "filter")String invoiceId) {
        InvestorScheduler inv = invQueue.get(invoiceId);
        if(inv != null){
            inv.interrupt();
            invQueue.remove(invoiceId);
            activeInvestments.remove(Util.isCurrentScheduled(activeInvestments,invoiceId));
            return ResponseEntity.status(200).body(new ArrayList<>(this.activeInvestments));
        }else return ResponseEntity.status(204).body(new ArrayList<>(this.activeInvestments));
    }

    @GetMapping("/currentschedule")
    public ResponseEntity<ArrayList<InvestmentData>> currentSchedule(@RequestHeader Map<String, String> headers) {
        String token = headers.get("authorization");
        if(!token.isEmpty()){
            return ResponseEntity.ok(new ArrayList<>(this.activeInvestments));
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
}
