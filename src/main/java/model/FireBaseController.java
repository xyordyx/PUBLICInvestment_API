package model;

import model.GoogleCloud.CIGAppEngine;
import model.GoogleCloud.CIGFireStore;
import model.GoogleCloud.GoogleCloudAuthenticator;
import model.json.InvestmentData;
import model.json.firestore.APPData.APPData;
import model.json.firestore.APPData.UserEmail;
import model.json.firestore.instances.InstanceData;
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
        if (Util.isAutoManaged(investment.getTime())) {
            investment.setCurrentState("DB");
            googleToken = GoogleCloudAuthenticator.getGoogleCloudToken();
            if(googleToken != null) return new ResponseEntity<>(cig.createFireInvestment(googleToken, investment), HttpStatus.OK);
        } else {
            //POST INVESTMENT TO THE INSTANCE URL
            return new ResponseEntity<>(new CIGFinsmart().scheduleInvestment(investment), HttpStatus.OK);
        }
        return new ResponseEntity<>("", HttpStatus.CONFLICT);
    }

    @GetMapping(value = "/getuserdata")
    public ResponseEntity<Boolean> getUserData(@RequestHeader Map<String, String> headers){
        String userEmail = headers.get("useremail");
        CIGFireStore cig = new CIGFireStore();
        String token = GoogleCloudAuthenticator.getGoogleCloudToken();
        if(token != null){
            if(cig.getUserDataById(token,userEmail) != null){
                return ResponseEntity.ok(true);
            }
        }
        return ResponseEntity.ok(false);
    }

    @GetMapping("/setappdata")
    public ResponseEntity<Boolean> setAPPData(@RequestHeader Map<String, String> headers) {
        String fnPassword = headers.get("password");
        String fnEmail = headers.get("email");

        CIGFireStore cig = new CIGFireStore();
        String token = GoogleCloudAuthenticator.getGoogleCloudToken();
        if(token != null) {
            return ResponseEntity.ok(cig.createAPPData(token, fnPassword,fnEmail));
        }
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
                    if(data.getFields().getCurrentState().getStringValue().equals("DB")){
                        if(cig.deleteFireInvestment(token,id)){
                            return new ResponseEntity<>((Util.getListInvestmentFromList
                                    (cig.getInvestmentsByCompleted(token,false))), HttpStatus.OK);
                        }
                    }
                    //DELETE METHOD WHEN INVESTMENT IS ALREADY IN A INSTANCE
                    else if(data.getFields().getCurrentState().getStringValue().equals("Scheduled")){
                        if(CIGAppEngine.deleteAppEngineInstance(token,data.getFields().getInstanceId().getStringValue())
                                && cig.deleteFireInvestment(token,id)){
                            return new ResponseEntity<>((Util.getListInvestmentFromList
                                    (cig.getInvestmentsByCompleted(token,false))), HttpStatus.OK);
                        }
                    }
                    //REMOVE FROM INVESTED RECENTLY
                }else{
                    if(cig.deleteFireInvestment(token,id)) {
                        return new ResponseEntity<>((Util.getListInvestmentFromList
                                (cig.getInvestmentsByCompleted(token, true))), HttpStatus.OK);
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
}
