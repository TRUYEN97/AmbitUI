/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Runin.TempCPU;

import Communicate.Impl.Telnet.Telnet;
import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Control.Functions.FunctionsTest.Runin.CheckCommandTelnet.CheckCommandTelnet;
import Model.DataTest.FunctionParameters;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class TempCPU extends AbsFunction {

    private final CheckCommandTelnet checkCommandTelnet;
    private final FunctionBase functionBase;
    private final AnalysisBase analysisBase;

    public TempCPU(FunctionParameters functionParameters) {
        this(functionParameters, null);
    }

    public TempCPU(FunctionParameters parameters, String item) {
        super(parameters, item);
        this.checkCommandTelnet = new CheckCommandTelnet(parameters, item);
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
        String ip = this.analysisBase.getIp();
        try ( Telnet telnet = this.functionBase.getTelnet(ip, 23)) {
            ArrayList<Double> temps = getTempCPU(commands, telnet);
            addLog("PC", String.format("Temp cpu: %s", temps));
            setResult(getMaxValue(temps));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private ArrayList<Double> getTempCPU(List<String> commands, final Telnet telnet) throws NumberFormatException {
        ArrayList<Double> temps = new ArrayList<>();
        for (String command : commands) {
            String value = this.checkCommandTelnet.getTempCPU(telnet, command);
            if (!this.analysisBase.isNumber(value)) {
                addLog("ERROR", String.format("value is not number! value: %s", value));
            }
            temps.add(Double.valueOf(value));
        }
        return temps;
    }

    private Double getMaxValue(ArrayList<Double> temps) {
        Double max = Double.MIN_VALUE;
        for (Double temp : temps) {
            max = Double.max(max, temp);
        }
        return max;
    }

}
