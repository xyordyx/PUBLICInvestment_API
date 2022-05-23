package model;

import model.GoogleCloud.CIGAppEngine;
import model.GoogleCloud.CIGFireStore;
import model.GoogleCloud.GoogleCloudAuthenticator;
import model.json.InvestmentData;
import model.json.firestore.investments.Investments;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class FireBaseController {

    //FIRESTORE REQUESTS

    @PostMapping(value = "/createinvestment")
    public ResponseEntity<Object> createInvestment(@RequestBody InvestmentData investment) {
        CIGFireStore cig = new CIGFireStore();
        investment.setCompleted(false);
        String googleToken;
        //LOGIC REMOVED
            //POST INVESTMENT TO THE INSTANCE URL
            return new ResponseEntity<>(new CIGPlatformV1().scheduleInvestment(investment), HttpStatus.OK);
    }

    @GetMapping(value = "/getuserdata")
    public ResponseEntity<Boolean> getUserData(@RequestHeader Map<String, String> headers){
        String userEmail = headers.get("useremail");
        CIGFireStore cig = new CIGFireStore();
        String token = GoogleCloudAuthenticator.getGoogleCloudToken();
        //LOGIC REMOVED
        return ResponseEntity.ok(false);
    }

    @GetMapping("/setappdata")
    public ResponseEntity<Boolean> setAPPData(@RequestHeader Map<String, String> headers) {
        String fnPassword = headers.get("password");
        String fnEmail = headers.get("email");

        //LOGIC REMOVED
        return ResponseEntity.badRequest().body(false);
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
                    //LOGIC REMOVED
                    //DELETE METHOD WHEN INVESTMENT IS ALREADY IN A INSTANCE
                    if(data.getFields().getCurrentState().getStringValue().equals("Scheduled")){
                        //LOGIC REMOVED
                    }
                    //REMOVE FROM INVESTED RECENTLY
                }else{
                    if(cig.deleteFireInvestment(token,id)) {
                    //LOGIC REMOVED
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
            Investments[] a = cig.getInvestmentsByCompleted(token,isCompleted);
            return ResponseEntity.ok(new ArrayList<>
                    (Util.getListInvestmentFromList(cig.getInvestmentsByCompleted(token,isCompleted))));
        }
        return null;
    }
}
