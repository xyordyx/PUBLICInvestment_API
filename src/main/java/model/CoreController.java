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
            //PROD START
            //SCHEDULE INVESTMENTS ON DIFFERENT INSTANCES
            //LOGIC REMOVED
            return ResponseEntity.status(200).body(true);
            //PROD END
        }
        return ResponseEntity.status(200).body(false);
    }

    @GetMapping("/cloudscheduler")
    public ResponseEntity<Boolean> cronScheduler(@RequestHeader Map<String, String> headers){
        CIGFireStore cig = new CIGFireStore();
        //LOGIC REMOVED
        //HANDLE EACH REQUEST ON EACH INSTANCE TROUGH THIS CALL
        return ResponseEntity.ok(false);
    }

}
