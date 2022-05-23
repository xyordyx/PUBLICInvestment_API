package model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import model.platformV1Data.PlatformData;
import model.platformV1Data.InvoiceIndexes;
import model.json.InvestmentData;
import model.json.InvoiceTransactions;
import model.json.firestore.investments.Investments;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.json.MappingJacksonInputMessage;
import java.io.IOException;
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
        //LOGIC REMOVED
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

    public static PlatformData resetData(PlatformData data){
        //LOGIC REMOVED
        return data;
    }

    public static ObjectMapper initiatePrettyObjectMapper() {
        ObjectMapper customObjectMapper = new ObjectMapper();
        //LOGIC REMOVED
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
                }
            }
            InvoiceTransactions[] op = objectMapper.readValue(EntityUtils.toString(inputMessage.getEntity()), InvoiceTransactions[].class);
            return new ArrayList<>(Arrays.asList(op));
        }
        catch (InvalidDefinitionException ex) {
            throw new HttpMessageConversionException("Type definition error: " + ex.getType(), ex);
        }
    }

    static Boolean isAutoManaged(String time){
        //Asking for 1.5 minutes before execution time
        //LOGIC REMOVED
        return true;
    }

    public static List<InvestmentData> getListInvestmentData(Investments investments){
        List<InvestmentData> response = new ArrayList<>();
        //LOGIC REMOVED
        return response;
    }

    public static List<InvestmentData> getListInvestmentFromList(Investments [] investments){
        List<InvestmentData> response = new ArrayList<>();
        //LOGIC REMOVED
        return response;
    }

    public static List<InvestmentData> getListInvestmentFromList(Investments [] investments, String currentState){
        List<InvestmentData> response = new ArrayList<>();
        //LOGIC REMOVED
        return response;
    }

}
