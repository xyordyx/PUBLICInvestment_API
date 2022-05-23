package model.thread;

import model.GoogleCloud.CIGFireStore;
import model.json.InvestmentData;
import model.json.ResponseJSON;


import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static model.Util.timesDiff;

public class InstanceScheduler implements Runnable{
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final ExecutorService poolSubmit = Executors.newFixedThreadPool(1);
    private final InvestmentData investmentData;
    private boolean flag;
    private final String googleToken;

    private static final String amountBigger = "//LOGIC REMOVED";
    private static final String notPublished = "//LOGIC REMOVED";

    public InstanceScheduler(InvestmentData investmentData,String googleToken) {
        this.investmentData = investmentData;
        this.flag = true;
        this.googleToken = googleToken;
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
                CIGFireStore cig = new CIGFireStore();
                InvestmentData tempData = future.get();
                //SHUTDOWN GOOGLE INSTANCE
                //LOGIC REMOVED
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
            ResponseJSON responseJSON;
            double actualAmount;
            //LOGIC REMOVED
            //WAIT UNTIL RIGHT TIME
            if(flag){
                //EXECUTE INVESTMENT
                //LOGIC REMOVED

                //INVOICE NOT PUBLISHED YET
                //LOGIC REMOVED

                //INVOICE AMOUNT IS LESS THAN DESIRED AMOUNT ADJUST IT
                //LOGIC REMOVED

                //INVESTMENT COMPLETED SAVE ON DB
                //LOGIC REMOVED
            }
            return investmentData;
        }
    };
}
