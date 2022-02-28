package model.thread;

import model.finsmartData.FinsmartUtil;
import model.firebase.CIGGoogleServices;
import model.json.InvestmentData;
import model.json.ResponseJSON;
import model.json.firestore.investments.Document;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static model.Util.getTime;
import static model.Util.timesDiff;

public class InstanceScheduler implements Runnable{
    private final AtomicBoolean running = new AtomicBoolean(true);
    private ExecutorService poolSubmit = Executors.newFixedThreadPool(1);
    private InvestmentData investmentData;
    private boolean flag;

    private static final String amountBigger = "INVESTMENTS.INVESTMENT_AMOUNT_IS_BIGGER_THAN_TARGET_INVOICE_AVAILABLE_BALANCE";
    private static final String notPublished = "INVESTMENTS.TARGET_INVOICE_NOT_PUBLISHED";

    public InstanceScheduler(InvestmentData investmentData) {
        this.investmentData = investmentData;
        this.flag = true;
    }

    public void interrupt(){
        running.set(false);
        poolSubmit.shutdown();
        poolSubmit.shutdownNow();
    }

    @Override
    public void run() {
        Future<InvestmentData> future = poolSubmit.submit(callable);
        try {
            if(future.get() != null){
                CIGGoogleServices cig = new CIGGoogleServices();
                Document document = cig.getInvestmentsById(investmentData.getFireToken(),investmentData.getInvoiceId());
                if(document != null){
                    InvestmentData tempData = new InvestmentData(document);
                    tempData.setCurrentState("Processed");
                    cig.updateInvestment(investmentData.getFireToken(),tempData);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            poolSubmit.shutdown();
            poolSubmit.shutdownNow();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    Callable<InvestmentData> callable = new Callable<>() {
        @Override
        public InvestmentData call()  {
            ResponseJSON responseJSON = new ResponseJSON(true,"");
            double actualAmount;
            System.out.println(Thread.currentThread().getName() + ":"+investmentData.getDebtorName() +" - scheduled - " + getTime());
            try {
                //TimeUnit.MILLISECONDS.sleep(timesDiff(investmentData.getTime())-600);
                TimeUnit.MILLISECONDS.sleep(100000);
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + ":"+investmentData.getDebtorName()+" - interrupted - " + getTime());
                flag = false;
                //Thread.currentThread().interrupt();
            }
            if(flag){
                //responseJSON = FinsmartUtil.postToFinSmartInstance(investmentData.getAmount(),investmentData);
                actualAmount = investmentData.getAmount();
                //INVOICE NOT PUBLISHED YET
                while (responseJSON.getMessage().replace('"', ' ').equals(notPublished) &&
                        !Thread.currentThread().isInterrupted()) {
                    //responseJSON = FinsmartUtil.postToFinSmartInstance(investmentData.getAmount(),investmentData);
                }
                //INVOICE AMOUNT IS LESS THAN DESIRED AMOUNT
                if (responseJSON.getMessage().replace('"', ' ').equals(amountBigger)
                        && !Thread.currentThread().isInterrupted()) {
                    actualAmount = FinsmartUtil.updateOpportunity(investmentData.getToken(), investmentData.getInvoiceId());
                    //responseJSON = FinsmartUtil.postToFinSmartInstance(actualAmount, investmentData);
                    FinsmartUtil.updateInvestment(investmentData, responseJSON, 4, actualAmount);
                }
                //INVESTMENT COMPLETED
                else {
                    FinsmartUtil.updateInvestment(investmentData, responseJSON, 1, null);
                }
                System.out.println(": "+Thread.currentThread().getName() +
                        investmentData.getDebtorName() + " - " + "STATUS: " + investmentData.isStatus()
                        + " MSG:" + investmentData.getMessage() + " Amount:" + actualAmount+" - " + getTime());
                investmentData.setCompleted(true);
            }
            return investmentData;
        }
    };
}
