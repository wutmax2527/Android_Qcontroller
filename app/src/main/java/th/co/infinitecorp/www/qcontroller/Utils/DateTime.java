package th.co.infinitecorp.www.qcontroller.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import th.co.infinitecorp.www.qcontroller.DataInfo.DateTimeInfo;

public class DateTime {
    public static String GetDateTimeNow() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDateTime = sdf.format(new Date());

        return strDateTime;
    }
    public static String GetCurrentDateTime(String pattern) {
        Date currentTime = Calendar.getInstance().getTime();
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String strDT=format.format(currentTime);
        return strDT;
    }
    public static DateTimeInfo ConvertStringeDateTimeToModel(String eDateTime){
        String[] eDateTimeArr = eDateTime.split(" ");
        String[] eDate = eDateTimeArr[0].split("-");
        int year = Integer.parseInt(eDate[0]);
        byte month = Byte.valueOf(eDate[1]);
        byte day = Byte.valueOf(eDate[2]);

        String[] eTime = eDateTimeArr[1].split(":");
        byte hour = Byte.valueOf(eTime[0]);
        byte min = Byte.valueOf(eTime[1]);
        byte sec = Byte.valueOf(eTime[2]);

        DateTimeInfo dateTimeInfo = new DateTimeInfo(year,month,day,hour,min,sec);

        return dateTimeInfo;
    }
}
