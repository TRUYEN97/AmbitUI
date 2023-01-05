/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.MBLT.ThermalShutdown;

import Communicate.Impl.Comport.ComPort;
import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Control.Functions.FunctionsTest.Base.FixtureActions.FixtureAction;
import FileTool.FileService;
import Model.ErrorLog;
import Time.WaitTime.Class.TimeS;
import com.alibaba.fastjson.JSONObject;
import Model.DataTest.FunctionParameters;
import java.io.File;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class ThermalShutdown extends AbsFunction {

    private final FixtureAction fixtureAction;
    private final AnalysisBase analysisBase;
    private final FunctionBase functionBase;

    public ThermalShutdown(FunctionParameters parameters) {
        this(parameters, null);
    }

    public ThermalShutdown(FunctionParameters parameters, String item) {
        super(parameters, item);
        this.fixtureAction = new FixtureAction(parameters, item);
        this.analysisBase = new AnalysisBase(parameters, item);
        this.functionBase = new FunctionBase(parameters, item);
    }

    @Override
    protected boolean test() {
        String outLow = this.config.getString("PowerLowCMD");
        addLog("Config", "PowerLowCMD: " + outLow);
        String outHigh = this.config.getString("PowerHighCMD");
        addLog("Config", "PowerHighCMD: " + outHigh);
        String command = this.config.getString("Command");
        addLog("Config", "Command: " + command);
        try ( ComPort fixture = this.fixtureAction.getComport()) {
            if (fixture == null || !this.functionBase.sendCommand(fixture, outLow)) {
                return false;
            }
            try {
                delay();
                if (!this.functionBase.sendCommand(fixture, command)) {
                    return false;
                }
                return getValue(fixture);
            } finally {
                delay();
                this.functionBase.sendCommand(fixture, outHigh);
                delay();
            }
        } catch (Exception e) {
            e.printStackTrace();
            addLog("ERROR", e.getMessage());
            ErrorLog.addError(this, e.getMessage());
            return false;
        }
    }

    private boolean getValue(final ComPort fixture) throws NumberFormatException {
        JSONObject voltageItems = getVoltageItems();
        String inline;
        List<String> skipTP = this.config.getListJsonArray("SkipTP");
        addLog("Config", "Skip point: " + skipTP);
        boolean result = true;
        while ((inline = fixture.readLine(new TimeS(1))) != null) {
            addLog("PC", "-------------------------------------");
            addLog("Comport", inline);
            if (!inline.startsWith("TP") || !inline.contains("=")) {
                continue;
            }
            if (inline.contains(",")) {
                inline = inline.replaceAll(",", ".");
            }
            String voltPoint = this.analysisBase.findGroup(inline, "^TP[0-9]+");
            if (skipTP.contains(voltPoint)) {
                continue;
            }
            Double value = Double.valueOf(this.analysisBase.findGroup(inline, "\\-?[0-9]+\\.[0-9]+"));
            addLog("PC", String.format("Item: %s | %s", voltPoint, value));
            result = checkVoltPoint(voltageItems, voltPoint, value);
            if (inline.endsWith(":")) {
                break;
            }
        }
        return result;
    }

    private void delay() {
        try {
            addLog("PC", "Delay 200 ms");
            Thread.sleep(200);
        } catch (InterruptedException ex) {
        }
    }

    private boolean checkVoltPoint(JSONObject voltageItems, String voltPoint, Double value) {
        Double spec = voltageItems.getDouble(voltPoint);
        addLog("Config", String.format("Value: %s | Spec: %s", value, spec));
        return spec == null || value <= spec;
    }

    private JSONObject getVoltageItems() {
        String path = this.config.getString("FileVoltageItems");
        addLog("Config", path);
        String volList = new FileService().readFile(new File(path));
        JSONObject voltageItems = JSONObject.parseObject(volList);
        return voltageItems;
    }

}
