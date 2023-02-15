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
import Time.WaitTime.Class.TimeMs;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class TempCPU extends AbsFunction {

    private final FunctionBase functionBase;
    private final AnalysisBase analysisBase;

    public TempCPU(FunctionParameters functionParameters) {
        this(functionParameters, null);
    }

    public TempCPU(FunctionParameters parameters, String item) {
        super(parameters, item);
        this.functionBase = new FunctionBase(parameters, item);
        this.analysisBase = new AnalysisBase(parameters, item);
    }

    @Override
    protected boolean test() {
        List<String> commands = this.config.getListJsonArray("command");
        if (commands.isEmpty()) {
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

    private ArrayList<Integer> getTempCPU(List<String> commands, final Telnet telnet) throws NumberFormatException {
        ArrayList<Integer> temps = new ArrayList<>();
        String startkey = config.getString("Startkey");
        String endkey = config.getString("Endkey");
        String regex = config.getString("Regex");
        int time = config.getInteger("Time", 1);
        for (String command : commands) {
            if (!this.functionBase.sendCommand(telnet, command)) {
                return null;
            }
            String value = this.analysisBase.getValue(telnet, startkey, endkey, regex, new TimeMs(time));
            if (!this.analysisBase.isNumber(value)) {
                addLog("ERROR", String.format("value is not number! value: %s", value));
                return null;
            }
            temps.add(Integer.valueOf(value));
        }
        return temps;
    }

    private Integer getMaxValue(ArrayList<Integer> temps) {
        Integer max = Integer.MIN_VALUE;
        for (Integer temp : temps) {
            max = Integer.max(max, temp);
        }
        return max;
    }

}
