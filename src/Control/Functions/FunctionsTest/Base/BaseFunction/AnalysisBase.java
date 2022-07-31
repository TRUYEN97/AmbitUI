/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.BaseFunction;

import Control.Functions.AbsFunction;
import Model.AllKeyWord;
import Model.DataSource.Setting.Setting;
import Time.WaitTime.AbsTime;
import Time.WaitTime.Class.TimeS;
import commandprompt.Communicate.Comport.ComPort;
import commandprompt.DHCP.DhcpData;
import commandprompt.Communicate.IReadable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Administrator
 */
public class AnalysisBase extends AbsFunction {

    public AnalysisBase(String itemName) {
        super(itemName);
    }

    @Override
    protected boolean test() {
        addLog("Messager", "This is not a function test!");
        return false;
    }

    public String getValue(IReadable readable, String regex) {
        return getValue(readable, null, null, regex, null);
    }

    public String getValue(IReadable readable, String regex, AbsTime time) {
        return getValue(readable, null, null, regex, time);
    }

    public String getValue(IReadable readable, String startkey, String endkey, AbsTime time) {
        return getValue(readable, startkey, endkey, null, time);
    }

    public String getValue(IReadable readable, String startkey, String endkey) {
        return getValue(readable, startkey, endkey, null, null);
    }

    public String getValue(IReadable readable, String startkey, String endkey, String regex, AbsTime time) {
        String line;
        String name = readable.getClass().getSimpleName();
        String value = null;
        try {
            while ((line = time == null ? readable.readLine() : readable.readLine(time)) != null) {
                addLog(name, line);
                if (regex != null && !regex.isBlank()) {
                    value = findGroup(line, regex);
                } else {
                    value = subString(line, startkey, endkey);
                }
                if (value != null) {
                    break;
                }
            }
            return value;
        } finally {
            addLog("CONFIG", String.format("Start key: \"%s\"", startkey));
            addLog("CONFIG", String.format("End key: \"%s\"", endkey));
            addLog("CONFIG", String.format("Regex: \"%s\"", regex));
            addLog("PC", String.format("Value: \"%s\"", value));
        }

    }

    public boolean isResponseContainKey(IReadable readable, List<String> keyWords, TimeS timeS) {
        addLog("Config", String.format("keyWords: %s", keyWords));
        String line;
        while (timeS.onTime()) {
            line = readable.readLine(timeS);
            addLog(readable.getClass().getSimpleName(), line);
            if (line != null && keyWords.contains(line.trim())) {
                return true;
            }
        }
        return false;
    }

    public boolean isResponseContainKey(IReadable readable, String spec, String readUntil) {
        return AnalysisBase.this.isResponseContainKey(readable, spec, readUntil, null);
    }

    public boolean isResponseContainKey(IReadable readable, String spec, String readUntil, AbsTime time) {
        try {

            if (readUntil == null) {
                addLog("Config", "Read until == null !!");
                return false;
            }
            if (spec == null) {
                addLog("Config", "spec == null !!");
                return false;
            }
            String name = readable.getClass().getSimpleName();
            String result = readable.readUntil(readUntil, time);
            addLog(name, result);
            return result != null && result.contains(spec);
        } finally {
            addLog("CONFIG", String.format("Spec: \"%s\"", spec));
        }
    }

    public String getIp() {
        if (Setting.getInstance().isOnDHCP()) {
            String mac = this.processData.getString(AllKeyWord.MAC);
            if (mac == null) {
                addLog("It's DHCP mode but MAC is null!");
                return null;
            }
            addLog(String.format("Get IP from the DHCP with MAC is \"%s\"", mac));
            return DhcpData.getInstance().getIP(mac);
        }
        addLog("Get IP from the function config with key is \"IP\".");
        return allConfig.getString("IP");
    }

    public Integer string2Integer(String value) {
        if (value == null) {
            addLog("ERROR", "Can't convert null to integer!");
            return null;
        }
        try {
            int result = Integer.valueOf(value);
            addLog("PC", "Convert sucessed! value: " + result);
            return result;
        } catch (NumberFormatException e) {
            addLog("ERROR", e.getLocalizedMessage());
            return null;
        }
    }

    public Double string2Double(String value) {
        if (value == null) {
            addLog("ERROR", "Can't convert null to Double!");
            return null;
        }
        try {
            double result = Double.valueOf(value);
            addLog("PC", "Convert sucessed! value: " + result);
            return result;
        } catch (NumberFormatException e) {
            addLog("ERROR", e.getLocalizedMessage());
            return null;
        }
    }

    public boolean isNumber(String value) {
        if (value == null) {
            addLog("PC", value + " is not a number");
            return false;
        }
        try {
            Double.valueOf(value);
            addLog("PC", value + " is a number");
            return true;
        } catch (NumberFormatException e) {
            addLog("PC", value + " is not a number");
            return false;
        }
    }

    public String getUntil(IReadable readable, String until, int time) {
        addLog("Config", "Time: " + time);
        addLog("Config", "ReadUntil: " + until);
        String response = readable.readUntil(until, new TimeS(time));
        addLog("Telnet", response);
        return response;
    }

    public String findGroup(String line, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    public String subString(String line, String startkey, String endkey) {
        if (!stringAvailable(startkey) && !stringAvailable(endkey)) {
            return line.trim();
        } else if (stringAvailable(startkey) && line.contains(startkey)
                && !stringAvailable(endkey)) {
            int index = line.indexOf(startkey) + startkey.length();
            return line.substring(index).trim();
        } else if (stringAvailable(endkey) && line.contains(endkey)
                && !stringAvailable(startkey)) {
            int index = line.indexOf(endkey);
            return line.substring(0, index).trim();
        } else if (stringAvailable(startkey) && line.contains(startkey)
                && stringAvailable(endkey) && line.contains(endkey)) {
            int start = line.indexOf(startkey) + startkey.length();
            int end = line.lastIndexOf(endkey);
            return line.substring(start, end).trim();
        }
        return null;
    }

    public boolean stringAvailable(String str) {
        return str != null && !str.isBlank();
    }
}
