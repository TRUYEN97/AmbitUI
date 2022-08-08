/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.MBLT.ThermalShutdown;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Control.Functions.FunctionsTest.Base.FixtureActions.FixtureAction;
import Control.Functions.FunctionsTest.MBLT.VoltageTest.checkVolt;
import FileTool.FileService;
import Model.DataSource.ModeTest.FunctionConfig.FunctionElement;
import Model.DataTest.FunctionData.FunctionData;
import Model.ErrorLog;
import Model.ManagerUI.UIStatus.UiStatus;
import Time.WaitTime.Class.TimeS;
import com.alibaba.fastjson.JSONObject;
import Communicate.Comport.ComPort;
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

    public ThermalShutdown(String itemName) {
        super(itemName);
        this.fixtureAction = new FixtureAction(itemName);
        this.analysisBase = new AnalysisBase(itemName);
        this.functionBase = new FunctionBase(itemName);
    }

    @Override
    public void setResources(FunctionElement functionElement, UiStatus uiStatus, FunctionData functionData) {
        super.setResources(functionElement, uiStatus, functionData); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        this.fixtureAction.setResources(functionElement, uiStatus, functionData);
        this.analysisBase.setResources(functionElement, uiStatus, functionData);
        this.functionBase.setResources(functionElement, uiStatus, functionData);
    }

    @Override
    protected boolean test() {
        String outLow = this.allConfig.getString("PowerLowCMD");
        addLog("Config", "PowerLowCMD: " + outLow);
        String outHigh = this.allConfig.getString("PowerHighCMD");
        addLog("Config", "PowerHighCMD: " + outHigh);
        String command = this.allConfig.getString("Command");
        addLog("Config", "Command: " + command);
        ComPort fixture = this.fixtureAction.getComport();
        try {
            if (fixture == null || !this.functionBase.sendCommand(fixture, outLow)) {
                return false;
            }
            delay();
            if (!this.functionBase.sendCommand(fixture, command)) {
                return false;
            }
            JSONObject voltageItems = getVoltageItems();
            String inline;
            List<String> skipTP = this.allConfig.getListJsonArray("SkipTP");
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
        } catch (Exception e) {
            e.printStackTrace();
            addLog("ERROR", e.getMessage());
            ErrorLog.addError(this, e.getMessage());
            return false;
        } finally {
            try {
                this.functionBase.sendCommand(fixture, outHigh);
                delay();
            } catch (Exception e) {
                e.printStackTrace();
                addLog("ERROR", e.getMessage());
                ErrorLog.addError(this, e.getMessage());
            }
            if (fixture != null) {
                fixture.disConnect();
            }
        }
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
        String path = this.allConfig.getString("FileVoltageItems");
        addLog("Config", path);
        String volList = new FileService().readFile(new File(path));
        JSONObject voltageItems = JSONObject.parseObject(volList);
        return voltageItems;
    }

}
