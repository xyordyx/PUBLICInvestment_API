package model;

import model.finsmartData.APIDebtorData;
import model.finsmartData.FinsmartData;
import model.json.InvoiceTransactions;
import model.json.Transactions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CoreProcessor {

    public FinsmartData getExtraData(FinsmartData smartData, String token) {
        if(smartData.getInvoiceIndex() == null){
            List<InvoiceTransactions> temp = CIGFinsmart.getInvoices(token);
            if(temp != null){
                smartData.setInvoiceIndex(Util.indexInvoices(temp));
            }else return null;
        }

        smartData.setSolesProfitExpected(0);
        smartData.setSolesOnRisk(0);
        smartData.setDollarProfitExpected(0);
        smartData.setDollarOnRisk(0);
        for(Transactions transaction : smartData.getFinancialTransactions().getFinancialTransactions()){
            double amountExpected;
            double amountOnRisk;
            double tempOnRisk = 0;

            //TOTAL CURRENT IN PROGRESS
            if (transaction.getType().equals("investment") &&
                    transaction.getStatus().equals("in progress")) {
                if(smartData.getInvoiceIndex().getInvoicesIndex().containsKey(transaction.getInvoice().get_id())) {
                    InvoiceTransactions currentTransact =
                            smartData.getInvoiceIndex().getInvoicesIndex().get(transaction.getInvoice().get_id());
                    double tempProfit = Util.calculateROI(currentTransact.getTem(), currentTransact.getMinimumDuration(),
                            transaction.getAmount());
                    //21 DAYS TO INCLUDE INTO RISK
                    if(currentTransact.getToBeCollectedIn().equals("En mora") && currentTransact.getMoraDays() > 21) {
                        tempOnRisk = transaction.getAmount();
                    }
                    if (transaction.getCurrency().equals("pen")) {
                        amountExpected = smartData.getSolesProfitExpected() + tempProfit;
                        amountOnRisk = smartData.getSolesOnRisk() + tempOnRisk;
                        smartData.setSolesProfitExpected(amountExpected);
                        smartData.setSolesOnRisk(amountOnRisk);
                    } else {
                        amountExpected = smartData.getDollarProfitExpected() + tempProfit;
                        amountOnRisk = smartData.getDollarOnRisk() + tempOnRisk;
                        smartData.setDollarProfitExpected(amountExpected);
                        smartData.setDollarOnRisk(amountOnRisk);
                    }
                }
            }
        }
        return smartData;
    }

    public FinsmartData getBalance(FinsmartData smartData) {
        //int indexFinancial = smartData.getFinancialIndex();
        double tempPENSum = 0.00;
        double tempUSDSum = 0.00;
        for(Transactions transactions : smartData.getFinancialTransactions().getFinancialTransactions()){
            double amountInvested;
            double sum;
            //TOTAL DEPOSITED
            if (transactions.getType().equals("deposit") &&
                    transactions.getStatus().equals("approved")) {
                if (transactions.getCurrency().equals("pen")) {
                    smartData.setTotalPENDeposited(smartData.getTotalPENDeposited() +
                            transactions.getAmount());
                } else {
                    smartData.setTotalUSDDeposited(smartData.getTotalUSDDeposited() +
                            transactions.getAmount());
                }
            }
            //TOTAL RETENTIONS
            else if (transactions.getType().equals("retention") &&
                    transactions.getStatus().equals("approved")) {
                if (transactions.getCurrency().equals("pen")) {
                    smartData.setTotalPENRetentions(smartData.getTotalPENRetentions() +
                            transactions.getAmount());
                } else {
                    smartData.setTotalUSDRetentions(smartData.getTotalUSDRetentions() +
                            transactions.getAmount());
                }
            }
            //TOTAL PROFITED
            else if (transactions.getType().equals("investment return") &&
                    transactions.getStatus().equals("approved")) {
                if (transactions.getCurrency().equals("pen")) {
                    sum = smartData.getTotalPENProfited() + transactions.getAmount();
                    smartData.setTotalPENProfited(sum);
                    if(transactions.getNetAmount() != null){
                        tempPENSum = tempPENSum + transactions.getNetAmount();
                    }
                } else {
                    sum = smartData.getTotalUSDProfited() + transactions.getAmount();
                    smartData.setTotalUSDProfited(sum);
                    if(transactions.getNetAmount() != null){
                        tempUSDSum = tempUSDSum + transactions.getNetAmount();
                    }
                }
            }
            else if (transactions.getType().equals("investment") &&
                    transactions.getStatus().equals("capital refunded")) {
                smartData.getTransactionsHashMap().put(transactions.getInvoice().get_id(),transactions);
            }
            //INVESTMENTS IN COLLECTION
            else if(transactions.getStatus().equals("investment start")){
                if (transactions.getCurrency().equals("pen")) {
                    smartData.setTotalPENCurrentInvested(smartData.getTotalPENCurrentInvested() + transactions.getAmount());
                } else {
                    smartData.setTotalUSDCurrentInvested(smartData.getTotalUSDCurrentInvested() + transactions.getAmount());
                }
            }
            //TOTAL CURRENT IN PROGRESS
            else if (transactions.getType().equals("investment") &&
                    transactions.getStatus().equals("in progress")) {
                    if (transactions.getCurrency().equals("pen")) {
                        amountInvested = smartData.getTotalPENCurrentInvested() + transactions.getAmount();
                        smartData.setTotalPENCurrentInvested(amountInvested);
                    } else {
                        amountInvested = smartData.getTotalUSDCurrentInvested() + transactions.getAmount();
                        smartData.setTotalUSDCurrentInvested(amountInvested);
                    }
                    smartData.getTransactionsHashMap().put(transactions.getInvoice().get_id(),transactions);
            }
            /*indexFinancial++;
            if(indexFinancial == smartData.getFinancialTransactions().getFinancialTransactions().size()){
                break;
            }*/
        }
        //TOTAL PROFIT LESS RETENTIONS
        smartData.setTotalPENProfited(smartData.getTotalPENProfited() - smartData.getTotalPENRetentions());
        smartData.setTotalUSDProfited(smartData.getTotalUSDProfited() - smartData.getTotalUSDRetentions());
        //TOTAL AVAILABLE
        smartData.setTotalPENAvailable((smartData.getTotalPENDeposited() + smartData.getTotalPENProfited()) -
                smartData.getTotalPENCurrentInvested() + smartData.getTotalPENAvailable());
        smartData.setTotalUSDAvailable((smartData.getTotalUSDDeposited() + smartData.getTotalUSDProfited()) -
                smartData.getTotalUSDCurrentInvested() + smartData.getTotalUSDAvailable());
        smartData.setFinancialIndex(smartData.getFinancialTransactions().getFinancialTransactions().size());
        return smartData;
    }

    public APIDebtorData getDebtorHistory(FinsmartData smartData, String debtor, String token) {
        if(smartData.getInvoiceIndex() == null){
            List<InvoiceTransactions> temp = CIGFinsmart.getInvoices(token);
            if(temp != null){
                smartData.setInvoiceIndex(Util.indexInvoices(temp));
            }else return null;
        }

        ArrayList<InvoiceTransactions> invoices = smartData.getInvoiceIndex().getDebtorInvoiceIndex().get(debtor);
        ArrayList<InvoiceTransactions> temp = new ArrayList<>();
        if(invoices != null){
            for(InvoiceTransactions inv: invoices){
                Transactions transactionTemp = smartData.getTransactionsHashMap().get(inv.get_id());
                if(transactionTemp != null){
                    inv.setAmountInvested(transactionTemp.getAmount());
                    inv.setProfitedAmount(transactionTemp.getProfit());
                    inv.setExpectedProfit(Util.calculateROI(inv.getTem(),inv.getMinimumDuration(),transactionTemp.getAmount()));
                }
                temp.add(inv);
            }
        }
        return new APIDebtorData(temp,0);
    }

    public APIDebtorData getCurrentInvestments(FinsmartData smartData, String token) {
        if(smartData.getInvoiceIndex() == null){
            List<InvoiceTransactions> temp = CIGFinsmart.getInvoices(token);
            if(temp != null){
                smartData.setInvoiceIndex(Util.indexInvoices(temp));
            }else return null;
        }

        HashMap<String,InvoiceTransactions> invoiceMap = smartData.getInvoiceIndex().getInvoicesIndex();
        ArrayList<InvoiceTransactions> temp = new ArrayList<>();
        int delayedInvoices = 0;
        for (var entry : invoiceMap.entrySet()) {
            if(entry.getValue().getStatus().equals("awaiting collection") ||
                    entry.getValue().getStatus().equals("in progress")){
                Transactions transactionTemp = smartData.getTransactionsHashMap().get(entry.getKey());
                if(transactionTemp != null){
                    entry.getValue().setAmountInvested(transactionTemp.getAmount());
                    entry.getValue().setProfitedAmount(transactionTemp.getProfit());
                    entry.getValue().setExpectedProfit(Util.calculateROI(entry.getValue().getTem(),
                            entry.getValue().getMinimumDuration(),transactionTemp.getAmount()));
                    entry.getValue().setDebtor(transactionTemp.getInvoice().getDebtor());

                    temp.add(entry.getValue());
                    if(entry.getValue().getToBeCollectedIn().equals("En mora")){
                        delayedInvoices++;
                    }
                }
            }
        }
        return new APIDebtorData(temp, delayedInvoices);
    }
}
