package model;

import model.GoogleCloud.GoogleCloudAuthenticator;
import model.GoogleCloud.CIGFireStore;
import model.json.InvestmentData;
import model.thread.InstanceScheduler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class CoreController {

    private final ExecutorService threadPool = Executors.newFixedThreadPool(10);
    public boolean tempFlag = true;

    public ArrayList<Integer> temp = new ArrayList<>();

    //HEALTH REQUESTS
    @GetMapping("/")
    public String hello() {
        return " <center><h1>Live and ready to rock</h1></center>";
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
            //TEST START
            /*InstanceScheduler inv =  new InstanceScheduler(investment,googleToken);
            threadPool.submit(inv);
            investment.setCurrentState("Scheduled");
            investment.setInstanceId("NEPERIANO");
            cig.createFireInvestment(googleToken,investment);
            return ResponseEntity.status(200).body(true);*/
            //TEST END

            //PROD START
            InstanceScheduler inv =  new InstanceScheduler(investment,googleToken);
            threadPool.submit(inv);
            investment.setCurrentState("Scheduled");
            investment.setInstanceId(System.getenv("GAE_INSTANCE"));
            cig.createFireInvestment(googleToken,investment);
            return ResponseEntity.status(200).body(true);
            //PROD END
        }
        return ResponseEntity.status(200).body(false);
    }

    @GetMapping("/cloudscheduler")
    public ResponseEntity<Boolean> cronScheduler(@RequestHeader Map<String, String> headers){
        CIGFireStore cig = new CIGFireStore();
        if(headers.get("token").equals("NEPERIANO")){
            String token = GoogleCloudAuthenticator.getGoogleCloudToken();
            if(token != null){
                List<InvestmentData> dbInvestments = Util.getListInvestmentFromList
                        (cig.getInvestmentsByCompleted(token,false),"DB");
                for(InvestmentData inv: dbInvestments){
                    inv.setSmartToken(Objects.requireNonNull(
                            CIGFinsmart.getAuthentications(cig.getUserDataById(token, inv.getUserId())))
                            .getAccessToken());
                    new CIGFinsmart().scheduleInvestment(inv);
                }
                return ResponseEntity.ok(true);
            }
        }
        return ResponseEntity.ok(false);
    }

}
