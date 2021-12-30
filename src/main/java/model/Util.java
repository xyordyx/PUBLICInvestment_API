package model;

import model.finsmartData.InvoiceIndexes;
import model.json.InvestmentData;
import model.json.InvoiceTransactions;
import model.json.Transactions;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Util {

    public static InvoiceIndexes indexInvoices(List<InvoiceTransactions> invoices){
        InvoiceIndexes invoicesIndexes = new InvoiceIndexes();
        for(InvoiceTransactions inv : invoices){
            if(inv.getActualPaymentDate() != null && inv.getPaymentDate() != null){
                inv.setPastDueDays(getDuePastDays(inv));
            }
            invoicesIndexes.getInvoicesIndex().put(inv.get_id(),inv);

            ArrayList<InvoiceTransactions> invoiceArray = new ArrayList<>();
            if (invoicesIndexes.getDebtorInvoiceIndex().containsKey(inv.getDebtor().get_id())) {
                invoiceArray = invoicesIndexes.getDebtorInvoiceIndex().get(inv.getDebtor().get_id());
            }
            invoiceArray.add(inv);
            invoicesIndexes.getDebtorInvoiceIndex().put(inv.getDebtor().get_id(),invoiceArray);
        }
        return invoicesIndexes;
    }

    public static HashMap<String, Set<InvoiceTransactions>> indexInvoicesDebtor(List<InvoiceTransactions> invoices){
        HashMap<String, Set<InvoiceTransactions>> invoicesIndex = new HashMap<>();
        for(InvoiceTransactions inv : invoices){
            if(inv.getActualPaymentDate() != null && inv.getPaymentDate() != null){
                inv.setPastDueDays(getDuePastDays(inv));
            }
            if(invoicesIndex.containsKey(inv.getDebtor().get_id())){
                invoicesIndex.get(inv.getDebtor().get_id()).add(inv);
            }else {
                Set<InvoiceTransactions> setOfInvoices = new HashSet<>();
                setOfInvoices.add(inv);
                invoicesIndex.put(inv.getDebtor().get_id(), setOfInvoices);
            }
        }
        return invoicesIndex;
    }

    public static HashMap<String, Set<Transactions>> indexFinancialDebtor(List<Transactions> invoices){
        HashMap<String, Set<Transactions>> financialIndex = new HashMap<>();
        for(Transactions inv : invoices){
            if(inv.getType().equals("investment return") || inv.getType().equals("investment")){
                if(financialIndex.containsKey(inv.getInvoice().getDebtor().get_id())){
                    financialIndex.get(inv.getInvoice().getDebtor().get_id()).add(inv);
                }else {
                    Set<Transactions> setOfInvoices = new HashSet<>();
                    setOfInvoices.add(inv);
                    financialIndex.put(inv.getInvoice().getDebtor().get_id(), setOfInvoices);
                }
            }
        }
        return financialIndex;
    }

    public static long getDuePastDays(InvoiceTransactions inv){
        long diffInMillis = Math.abs(inv.getActualPaymentDate().getTime() - inv.getPaymentDate().getTime());
        return TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
    }

    public static double calculateROI(double tem, int days, double amount){
        return BigDecimal.valueOf(((tem/30)*amount*days)*0.01)
                .setScale(3, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public static long timesDiff(String time2){
        if(time2 == null){
            return 0;
        }
        SimpleDateFormat format1 = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat format2 = new SimpleDateFormat("HH:mm");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = format1.parse(getNowTime());
            date2 = format2.parse(time2);
            if(date2.before(date1)){
                Calendar c = Calendar.getInstance();
                c.setTime(date2);
                c.add(Calendar.DATE, 1);
                date2 = c.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assert date2 != null;
        return date2.getTime() - date1.getTime();
    }

    public static String getNowTime(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        ZoneId zone = ZoneId.of("America/Lima");
        LocalDateTime now = LocalDateTime.now(zone);
        return dtf.format(now);
    }

    public static String getTime(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss SSS ");
        ZoneId zone = ZoneId.of("America/Lima");
        LocalDateTime now = LocalDateTime.now(zone).plus(329, ChronoUnit.MILLIS);
        return dtf.format(now);
    }

    public static Boolean isValidData(InvestmentData data){
        if(data != null){
            return data.getInvoiceId() != null && data.getCurrency() != null && data.getAmount() != null &&
                    data.getDebtorName() != null;
        }
        return false;
    }

    public static int isCurrentScheduled(ArrayList<InvestmentData> data, String invoiceId){
        int index = 0;
        for(InvestmentData temp : data){
            if(temp.getInvoiceId().equals(invoiceId)){
                return index;
            }
            index++;
        }
        return 66;
    }
}
