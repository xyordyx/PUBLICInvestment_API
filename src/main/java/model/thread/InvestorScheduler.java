package model.thread;

import model.finsmartData.FinsmartUtil;
import model.json.InvestmentData;
import model.json.ResponseJSON;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static model.Util.getTime;
import static model.Util.timesDiff;

public class InvestorScheduler implements Runnable{
    private final AtomicBoolean running = new AtomicBoolean(true);
    private ExecutorService poolSubmit = Executors.newFixedThreadPool(1);
    private InvestmentData investmentData;
    private String token;
    private boolean flag;
    private Map<String, InvestorScheduler> invQueue;

    private static final String amountBigger = "INVESTMENTS.INVESTMENT_AMOUNT_IS_BIGGER_THAN_TARGET_INVOICE_AVAILABLE_BALANCE";
    private static final String notPublished = "INVESTMENTS.TARGET_INVOICE_NOT_PUBLISHED";

    public InvestorScheduler(InvestmentData investmentData, String token, Map<String, InvestorScheduler> invQueue) {
        this.investmentData = investmentData;
        this.flag = true;
        this.token = token;
        this.invQueue = invQueue;
    }

    public void interrupt(){
        running.set(false);
        //Thread.currentThread().interrupt();
        poolSubmit.shutdown();
        poolSubmit.shutdownNow();
    }

    @Override
    public void run() {
        Future<InvestmentData> future = poolSubmit.submit(callable);
        try {
            if(future.get() != null){
                invQueue.put(investmentData.getInvoiceId(),this);
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
            String scheduleTime;
            double actualAmount;
            if(investmentData.getTime() == 0){
                scheduleTime = "12:30";
            }else scheduleTime = "17:30";
            System.out.println(Thread.currentThread().getName() + ":"+investmentData.getDebtorName() +" - scheduled - " + getTime());
            try {
                TimeUnit.MILLISECONDS.sleep(timesDiff(scheduleTime)-700);
                //TimeUnit.MILLISECONDS.sleep(10000);
                investmentData.setScheduled(false);
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + ":"+investmentData.getDebtorName()+" - interrupted - " + getTime());
                flag = false;
                //Thread.currentThread().interrupt();
            }
            if(flag){
                responseJSON = FinsmartUtil.postToFinSmart(investmentData.getAmount(),investmentData,token);
                actualAmount = investmentData.getAmount();
                //INVOICE NOT PUBLISHED YET
                while (responseJSON.getMessage().replace('"', ' ').equals(notPublished) &&
                        !Thread.currentThread().isInterrupted()) {
                    /*try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        System.out.println(Thread.currentThread().getName() + "Submit:" + getTime() +
                                investmentData.getInvoiceId() + " - interrupted");
                        Thread.currentThread().interrupt();
                    }*/
                    responseJSON = FinsmartUtil.postToFinSmart(investmentData.getAmount(), investmentData, token);
                }
                //INVOICE AMOUNT IS LESS THAN DESIRED AMOUNT
                if (responseJSON.getMessage().replace('"', ' ').equals(amountBigger)
                        && !Thread.currentThread().isInterrupted()) {
                    actualAmount = FinsmartUtil.updateOpportunity(token, investmentData.getInvoiceId());
                    responseJSON = FinsmartUtil.postToFinSmart(actualAmount, investmentData, token);
                    FinsmartUtil.updateInvestment(investmentData, responseJSON, 4, actualAmount);
                }
                //INVESTMENT COMPLETED
                else {
                    FinsmartUtil.updateInvestment(investmentData, responseJSON, 1, null);
                }
                System.out.println(Thread.currentThread().getName() +
                        investmentData.getDebtorName() + " " + " STATUS: " + investmentData.isStatus()
                        + " MSG:" + investmentData.getMessage() + " Amount:" + actualAmount+" - " + getTime());
                investmentData.setCompleted(true);
            }
            return investmentData;
        }
    };
}
