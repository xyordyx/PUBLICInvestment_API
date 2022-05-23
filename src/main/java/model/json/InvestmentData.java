package model.json;

import model.json.firestore.investments.Document;

import java.util.Objects;

//VARIABLES REMOVED ON THIS FILE
public class InvestmentData {

    private String userId;

    public InvestmentData(){

    }

    public InvestmentData(Document document){

        this.userId = document.getFields().getUserId().getStringValue();

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}
