package model;

import model.GoogleCloud.CIGAppEngine;
import model.GoogleCloud.GoogleCloudAuthenticator;
import model.GoogleCloud.CIGFireStore;
import model.json.InvestmentData;
import model.json.LoginJSON;
import model.json.appengine.Instance;
import model.json.appengine.Instances;
import model.json.firestore.APPData.APPData;
import model.thread.InstanceScheduler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

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
            Instances instances = CIGAppEngine.getCurrentInstances(GoogleCloudAuthenticator.getGoogleCloudToken());
            if(instances != null){
                if(instances.getInstances().size() != 1){
                    InstanceScheduler inv =  new InstanceScheduler(investment,googleToken);
                    threadPool.submit(inv);
                    investment.setCurrentState("Scheduled");
                    investment.setInstanceId(instances.getInstances().stream()
                            .sorted(Comparator.comparing(Instance::getStartTime))
                            .collect(Collectors.toList()).get(instances.getInstances().size()-1).getId());
                    cig.createFireInvestment(googleToken,investment);
                    return ResponseEntity.status(200).body(true);
                }
            }
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
                    inv.setSmartToken(CIGFinsmart.getAuthentications(cig.getUserDataById(token,inv.getUserId()))
                            .getAccessToken());
                    new CIGFinsmart().scheduleInvestment(inv);
                }
                return ResponseEntity.ok(true);
            }
        }
        return ResponseEntity.ok(false);
    }

}
