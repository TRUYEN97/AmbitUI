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
            TimeZone tz = TimeZone.getTimeZone(this.config.getString("timezone", "UTC"));
            String webUrl = this.config.getString("wedUrl", "http://time.windows.com");
            addLog(LOG_KEYS.CONFIG, "time zone: %s", tz.getDisplayName());
            addLog(LOG_KEYS.CONFIG, "wedURl: %s", webUrl);
            String PatternDate = TimeBase.DATE_TIME_MS;
            SimpleDateFormat sdf = new SimpleDateFormat(PatternDate);
            TimeZone.setDefault(tz);
            sdf.setTimeZone(tz);
            String dateTime = sdf.format(this.timeBase.getWebsiteDatetime(webUrl));
            try ( Cmd cmd = new Cmd()) {
                this.functionBase.sendCommand(cmd, 
                        String.format("date %s", dateTime.split(" ")[0]));
                cmd.readLine();
                this.functionBase.sendCommand(cmd, 
                        String.format("time %s", dateTime.split(" ")[1]));
                cmd.readLine();
            } catch (Exception e) {
            }

            String timePc = getDateByFormatUTC(PatternDate);
            String newTime = dateTime.substring(0, dateTime.lastIndexOf(":"));
            addLog(LOG_KEYS.PC, "This time: %s", timePc);
            addLog(LOG_KEYS.PC, "spec time: %s", newTime);
            return timePc.contains(newTime);
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
