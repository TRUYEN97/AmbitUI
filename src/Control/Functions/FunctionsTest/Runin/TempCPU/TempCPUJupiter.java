/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Runin.TempCPU;

import Communicate.Impl.Telnet.Telnet;
import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Model.DataTest.FunctionParameters;
import Time.WaitTime.Class.TimeS;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class TempCPUJupiter extends AbsFunction {

    private final FunctionBase functionBase;
    private final AnalysisBase analysisBase;

    public TempCPUJupiter(FunctionParameters functionParameters) {
        this(functionParameters, null);
    }

    public TempCPUJupiter(FunctionParameters parameters, String item) {
        super(parameters, item);
        this.functionBase = new FunctionBase(parameters, item);
        this.analysisBase = new AnalysisBase(parameters, item);
    }

    @Override
    protected boolean test() {
        String commands = this.config.getString("command");
        if (commands == null) {
            addLog("CONFIG", "command is empty!");
            return false;
        }
        addLog("CONFIG", commands);
        String ip = this.functionBase.getIp();
        try ( Telnet telnet = this.functionBase.getTelnet(ip, 23)) {
            if (telnet == null) {
                return false;
            }
            ArrayList<Integer> temps = getTempCPU(commands, telnet);
            if (temps == null) {
                return false;
            }
            addLog("PC", String.format("Temp cpu: %s", temps));
            setResult(getMaxValue(temps));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private ArrayList<Integer> getTempCPU(String commands, final Telnet telnet) throws NumberFormatException {
        ArrayList<Integer> temps = new ArrayList<>();
        List<String> startKeys = this.config.getJsonList("Startkeys");
        List<String> endKeys = this.config.getJsonList("Endkeys");
        List<String> Regexs = this.config.getJsonList("Regexs");
        int time = config.getInteger("Time", 10);
        int length = Integer.max(startKeys.size(), endKeys.size());
        if (!this.functionBase.sendCommand(telnet, commands)) {
            return null;
        }
        for (int i = 0; i < length; i++) {
            String startkey = getKey(i, startKeys);
            String endkey = getKey(i, endKeys);
            String regex = getKey(i, Regexs);
            String value = this.analysisBase.getValue(telnet, startkey, endkey, regex, new TimeS(time));
            if (!this.analysisBase.isNumber(value)) {
                addLog("ERROR", String.format("value is not number! value: %s", value));
                return null;
            }
            temps.add(Integer.valueOf(value));
        }
        return temps;
    }

    private String getKey(int i, List<String> keys) {
        if (i >=0 && i < keys.size()) {
            return keys.get(i);
        }
        return null;
    }

    private Integer getMaxValue(ArrayList<Integer> temps) {
        Integer max = Integer.MIN_VALUE;
        for (Integer temp : temps) {
            max = Integer.max(max, temp);
        }
        return max;
    }

}
