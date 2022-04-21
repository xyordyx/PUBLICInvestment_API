package model;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.google.gson.Gson;
import model.finsmartData.FinsmartData;
import model.finsmartData.InvoiceIndexes;
import model.json.InvestmentData;
import model.json.InvoiceTransactions;
import model.json.Opportunities;
import model.json.firestore.investments.Document;
import model.json.firestore.investments.Investments;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.json.MappingJacksonInputMessage;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    public static long getDuePastDays(InvoiceTransactions inv){
        long diffInMillis = Math.abs(inv.getActualPaymentDate().getTime() - inv.getPaymentDate().getTime());
        return TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
    }

    public static double calculateROI(double tem, int days, double amount){
        return BigDecimal.valueOf(((tem/30)*amount*days)*0.01)
                .setScale(3, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public static long timesDiff(int saleSlot){
        String time2 = ((saleSlot == 1) ? "12:30" : "17:30");
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

    public static long timesDiff(String time2){
        if(time2 == null || time2.equals("0:00")){
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

    public static FinsmartData resetData(FinsmartData data){
        data.setTotalPENDeposited(0);
        data.setTotalUSDDeposited(0);
        data.setTotalPENRetentions(0);
        data.setTotalUSDRetentions(0);
        data.setTotalUSDProfited(0);
        data.setTotalPENProfited(0);
        data.setTotalUSDCurrentInvested(0);
        data.setTotalPENCurrentInvested(0);
        data.setTotalPENAvailable(0);
        data.setTotalUSDAvailable(0);
        return data;
    }

    public static ObjectMapper initiatePrettyObjectMapper() {
        ObjectMapper customObjectMapper = new ObjectMapper();
        customObjectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        customObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // additional indentation for arrays
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        pp.indentArraysWith(new DefaultIndenter());
        customObjectMapper.setDefaultPrettyPrinter(pp);

        return customObjectMapper;
    }

    static List<InvoiceTransactions> customReadJavaType(HttpResponse inputMessage, ObjectMapper objectMapper) throws IOException {
        try {
            if (inputMessage instanceof MappingJacksonInputMessage) { //MappingJackson2HttpMessageConverter
                Class<?> deserializationView = ((MappingJacksonInputMessage) inputMessage).getDeserializationView();
                if (deserializationView != null) {
                    InvoiceTransactions[] op = objectMapper.readerWithView(deserializationView).forType(InvoiceTransactions[].class).
                            readValue(EntityUtils.toString(inputMessage.getEntity()));
                    return new ArrayList<>(Arrays.asList(op));
                    //return objectMapper.readerWithView(deserializationView).forType(InvoiceTransactions[].class).readValue(EntityUtils.toString(inputMessage.getEntity()));
                }
            }
            InvoiceTransactions[] op = objectMapper.readValue(EntityUtils.toString(inputMessage.getEntity()), InvoiceTransactions[].class);
            return new ArrayList<>(Arrays.asList(op));
            //return objectMapper.readValue(EntityUtils.toString(inputMessage.getEntity()), InvoiceTransactions[].class);
        }
        catch (InvalidDefinitionException ex) {
            throw new HttpMessageConversionException("Type definition error: " + ex.getType(), ex);
            //return "Type definition error";
        }
    }

    static Boolean isAutoManaged(String time){
        //Asking for 1.5 minutes before execution time
        return 90000 <= timesDiff(time);
    }

    public static List<InvestmentData> getListInvestmentData(Investments investments){
        List<InvestmentData> response = new ArrayList<>();
        if(investments.getDocuments() != null) {
            for (Document inv : investments.getDocuments()) {
                response.add(new InvestmentData(inv));
            }
        }
        return response;
    }

    public static List<InvestmentData> getListInvestmentFromList(Investments [] investments){
        List<InvestmentData> response = new ArrayList<>();
        for(Investments inv: investments){
            if(inv.getDocument() != null){
                response.add(new InvestmentData(inv.getDocument()));
            }
        }
        return response;
    }

    public static List<InvestmentData> getListInvestmentFromList(Investments [] investments, String currentState){
        List<InvestmentData> response = new ArrayList<>();
        for(Investments inv: investments){
            if(inv.getDocument() != null){
                if(inv.getDocument().getFields().getCurrentState().getStringValue().equals(currentState)){
                    response.add(new InvestmentData(inv.getDocument()));
                }
            }
        }
        return response;
    }

    public static Opportunities[] loadJSONFromFile(){
        try {
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get("src/main/resources/static/opps.json"));
            Opportunities[] opportunities = gson.fromJson(reader, Opportunities[].class);
            reader.close();
            return opportunities;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
