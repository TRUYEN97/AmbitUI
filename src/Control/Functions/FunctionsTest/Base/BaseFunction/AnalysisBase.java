/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.BaseFunction;

import Control.Functions.AbsFunction;
import Time.WaitTime.AbsTime;
import Time.WaitTime.Class.TimeS;
import Communicate.IReadable;
import Model.DataTest.FunctionParameters;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Administrator
 */
public class AnalysisBase extends AbsFunction {

    public AnalysisBase(FunctionParameters itemName, String item) {
        super(itemName, item);
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
            if (time != null) {
                time.update();
            }
            while ((line = getLine(time, readable)) != null) {
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
            if (line == null) {
                if (time != null) {
                    addLog("PC", " null - %.3f S", time.getTime());
                } else {
                    addLog("PC", " null");
                }
            }
            return value;
        } finally {
            if (startkey != null) {
                addLog("CONFIG", "Start key: \"%s\"", startkey);
            }
            if (endkey != null) {
                addLog("CONFIG", "End key: \"%s\"", endkey);
            }
            if (regex != null) {
                addLog("CONFIG", "Regex: \"%s\"", regex);
            }
            addLog("PC", "Value: \"%s\"", value);
            if (time != null) {
                addLog("PC", "Time: \"%.3f/%.3f (S)\"", time.getTime(), time.getSpec());
            }
        }

    }

    private static String getLine(AbsTime time, IReadable readable) {
        return time == null ? readable.readLine() : readable.readLine(time);
    }

    public boolean isResponseContainKey(IReadable readable, List<String> keyWords, TimeS timeS) {
        addLog("Config", String.format("keyWords: %s", keyWords));
        String line;
        String name = readable.getClass().getSimpleName();
        timeS.update();
        while (timeS.onTime()) {
            line = readable.readLine(timeS);
            addLog(name, line);
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
            String result = readable.readUntil(time, readUntil);
            addLog(name, result);
            return result != null && result.contains(spec);
        } finally {
            addLog("CONFIG", String.format("Spec: \"%s\"", spec));
        }
    }

    public boolean isResponseContainKeyAndShow(IReadable readable, String spec, String readUntil, AbsTime time) {
        try {
            if (readUntil == null) {
                addLog("Config", "Read until == null !!");
                return false;
            }
            if (spec == null) {
                addLog("Config", "spec == null !!");
                return false;
            }
            String response = readShowUntil(readable, readUntil, time);
            if (response != null && response.contains(spec)) {
                return true;
            } else {
                addLog("PC", "Response no content spec");
                return false;
            }
        } finally {
            addLog("CONFIG", String.format("Spec: \"%s\"", spec));
        }
    }

    public String readUntilAndShow(IReadable readable, String until, AbsTime time) {
        if (readable == null) {
            addLog("Config", "readable == null !!");
            return null;
        }
        if (until == null) {
            addLog("Config", "spec == null !!");
            return null;
        }
        addLog("Config", "Time: " + time.getSpec());
        addLog("Config", "ReadUntil: " + until);
        String response = readable.readUntil(time, until);
        addLog("Telnet", response);
        return response;
    }

    public boolean contains(String response, List<String> specs) {
        if (response == null) {
            addLog(LOG_KEYS.PC, "String data == null");
            return false;
        }
        addLog(LOG_KEYS.CONFIG, "Check contains: %s", specs);
        for (String spec : specs) {
            if (!response.contains(spec)) {
                addLog(LOG_KEYS.PC, "Not contain \"%s\"", spec);
                return false;
            }
        }
        return true;
    }

    public String readShowUntil(IReadable readable, String readUntil, AbsTime time) {
        if (readable == null) {
            addLog("Config", "readable == null !!");
            return null;
        }
        if (readUntil == null) {
            addLog("Config", "spec == null !!");
            return null;
        }
        addLog("Config", "Time: %s", time.getSpec());
        addLog("Config", "ReadUntil: %s", readUntil);
        String readableName = readable.getClass().getSimpleName();
        StringBuilder respose = new StringBuilder();
        time.update();
        while (time.onTime()) {
            String line = readable.readUntil(time, "\n", readUntil);
            addLog(readableName, line == null ? "" : line);
            respose.append(line).append("\r\n");
            if (line != null && line.contains(readUntil)) {
                break;
            }
        }
        return respose.toString();
    }

    public Integer string2Integer(String value) {
        if (value == null) {
            addLog("ERROR", "Can't convert null to integer!");
            return null;
        }
        try {
            int result = Integer.parseInt(value);
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
            double result = Double.parseDouble(value);
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
