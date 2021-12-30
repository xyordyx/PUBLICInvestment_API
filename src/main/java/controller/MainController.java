package controller;

import model.CIG;
import model.CoreProcessor;
import model.Util;
import model.finsmartData.APIData;
import model.finsmartData.APIDebtorData;
import model.finsmartData.FinsmartData;
import model.json.FinancialTransactions;
import model.json.InvestmentData;
import model.json.InvoiceTransactions;
import model.json.LoginJSON;
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
    private LoginJSON fnToken = new LoginJSON();
    private FinsmartData data = new FinsmartData();
    private ExecutorService threadPool = Executors.newFixedThreadPool(10);
    private ConcurrentHashMap<String, InvestorScheduler> invQueue = new ConcurrentHashMap<>();
    private ArrayList<InvestmentData> activeInvestments = new ArrayList<>();

    @GetMapping("/getbalance")
    public ResponseEntity<APIData> getBalance(@RequestHeader Map<String, String> headers) {
        fnToken.setAccessToken(headers.get("authorization"));
        if(fnToken.getAccessToken() != null){
            StopWatch sw = new org.springframework.util.StopWatch();
            sw.start("Method-1 Finsmart Financial Transactions");
            FinancialTransactions transactions = new CIG().getFinancialTransactions(fnToken.getAccessToken());
            sw.stop();

            sw.start("Method-2 Get Balance");
            if(transactions.getFinancialTransactions().size() != data.getFinancialIndex()){
                data.setFinancialTransactions(transactions);
                data = core.getBalance(data);
            }
            sw.stop();

            StopWatch.TaskInfo[] listofTasks = sw.getTaskInfo();
            for (StopWatch.TaskInfo task : listofTasks) {
                System.out.format("[%s]:[%d]\n",
                        task.getTaskName(), task.getTimeMillis());
            }

            return ResponseEntity.ok(new APIData(data));
        }else return null;
    }

    @GetMapping("/getdebtorhistory")
    @ResponseBody
    public ResponseEntity<APIDebtorData> getDebtorHistory(@RequestParam(name = "filter")String debtor) {

        StopWatch sw = new org.springframework.util.StopWatch();
        sw.start("Method-4 Debtor History");

        ArrayList<InvoiceTransactions> debtorHistory = core.getDebtorHistory(data, debtor,fnToken.getAccessToken());


        sw.stop();

        StopWatch.TaskInfo[] listofTasks = sw.getTaskInfo();
        for (StopWatch.TaskInfo task : listofTasks) {
            System.out.format("[%s]:[%d]\n",
                    task.getTaskName(), task.getTimeMillis());
        }
        return ResponseEntity.ok(new APIDebtorData(debtorHistory));
    }

    @GetMapping("/getextradata")
    public ResponseEntity<APIData> getExtraData() {
        if(fnToken.getAccessToken() != null){
            StopWatch sw = new org.springframework.util.StopWatch();
            sw.start("Method-3 ExtraData");
            data = core.getExtraData(data,fnToken.getAccessToken());
            sw.stop();

            StopWatch.TaskInfo[] listofTasks = sw.getTaskInfo();
            for (StopWatch.TaskInfo task : listofTasks) {
                System.out.format("[%s]:[%d]\n",
                        task.getTaskName(), task.getTimeMillis());
            }

            return ResponseEntity.ok(new APIData(data));
        }else return null;
    }

    @PostMapping(value = "/scheduleinvestment")
    public ResponseEntity<String> scheduleInvestment(@RequestBody InvestmentData investment) {
        if(Util.isValidData(investment)){
            if(Util.isCurrentScheduled(activeInvestments,investment.getInvoiceId()) == 66){
                InvestorScheduler inv = new InvestorScheduler(investment, fnToken, invQueue);
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
    public ResponseEntity<ArrayList<InvestmentData>> currentSchedule() {
        return ResponseEntity.ok(new ArrayList<>(this.activeInvestments));
    }

    @GetMapping("/getcurrentinvestments")
    @ResponseBody
    public ResponseEntity<APIDebtorData> getCurrentInvestments() {

        StopWatch sw = new org.springframework.util.StopWatch();
        sw.start("Method-4 Current Investments");
        APIDebtorData investments = core.getCurrentInvestments(data,fnToken.getAccessToken());
        sw.stop();

        StopWatch.TaskInfo[] listofTasks = sw.getTaskInfo();
        for (StopWatch.TaskInfo task : listofTasks) {
            System.out.format("[%s]:[%d]\n",
                    task.getTaskName(), task.getTimeMillis());
        }
        return ResponseEntity.ok(investments);
    }

    @GetMapping("/")
    public String hello() {
        return "<center><h1>Live and ready to rock</h1></center>";
    }
}
