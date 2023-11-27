/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.UpdateTime;

import Communicate.Impl.Cmd.Cmd;
import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Model.DataTest.FunctionParameters;
import Time.TimeBase;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author Administrator
 */
public class CheckTimePC extends AbsFunction {

    private final TimeBase timeBase;
    private final FunctionBase functionBase;

    public CheckTimePC(FunctionParameters parameter) {
        this(parameter, null);
    }

    public CheckTimePC(FunctionParameters functionParameters, String itemName) {
        super(functionParameters, itemName);
        this.timeBase = new TimeBase();
        this.functionBase = new FunctionBase(functionParameters, itemName);
    }

    @Override
    protected boolean test() {
        try {
            int spec = this.config.getInteger("delta_time", 900);
            TimeZone tz = TimeZone.getTimeZone(this.config.getString("timezone", "UTC"));
            String webUrl = this.config.getString("wedUrl", "http://time.windows.com");
            addLog(LOG_KEYS.CONFIG, "time zone: %s", tz.getDisplayName());
            addLog(LOG_KEYS.CONFIG, "wedURl: %s", webUrl);
            String PatternDate = TimeBase.DATE_TIME_MS;
            SimpleDateFormat sdf = new SimpleDateFormat(PatternDate);
            TimeZone.setDefault(tz);
            sdf.setTimeZone(tz);
            Date wedDate = this.timeBase.getWebsiteDatetime(webUrl);
            String dateTime = sdf.format(wedDate);
            try ( Cmd cmd = new Cmd()) {
                this.functionBase.sendCommand(cmd,
                        String.format("date %s", dateTime.split(" ")[0]));
                cmd.readLine();
                this.functionBase.sendCommand(cmd,
                        String.format("time %s", dateTime.split(" ")[1]));
                cmd.readLine();
            } catch (Exception e) {
            }
            long wedms = wedDate.getTime();
            long pctime = System.currentTimeMillis();
            spec = spec * 1000;
            String timePc = getDateByFormatUTC(PatternDate);
            addLog(LOG_KEYS.PC, "This time: %s - %s ms", timePc, pctime);
            addLog(LOG_KEYS.PC, "spec time: %s - %s ms", dateTime, wedms);
            addLog(LOG_KEYS.PC, "delta time: %s ms", spec);
            return Math.abs(wedms - pctime) < spec;
        } catch (Exception ex) {
            ex.printStackTrace();
            addLog(LOG_KEYS.ERROR, ex.getLocalizedMessage());
            return false;
        }
    }

    public String getDateByFormatUTC(String format) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Calendar cal_Two = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        String dateStr = "";
        try {
            SimpleDateFormat simFormat = new SimpleDateFormat(format);
            dateStr = simFormat.format(cal_Two.getTime());
        } catch (Exception e) {
        }
        return dateStr;
    }

}
