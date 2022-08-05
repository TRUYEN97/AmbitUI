/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.MBLT.VoltageTest;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Control.Functions.FunctionsTest.Base.FixtureActions.FixtureAction;
import FileTool.FileService;
import Model.DataSource.ModeTest.FunctionConfig.FunctionElement;
import Model.DataTest.FunctionData.FunctionData;
import Model.ErrorLog;
import Model.ManagerUI.UIStatus.UiStatus;
import Time.WaitTime.Class.TimeS;
import com.alibaba.fastjson.JSONObject;
import commandprompt.Communicate.Comport.ComPort;
import java.io.File;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class VoltageTest extends AbsFunction {

    private final FixtureAction fixtureAction;
    private final AnalysisBase analysisBase;

    public VoltageTest(String itemName) {
        super(itemName);
        this.fixtureAction = new FixtureAction(itemName);
        this.analysisBase = new AnalysisBase(itemName);
    }

    @Override
    public void setResources(FunctionElement functionElement, UiStatus uiStatus, FunctionData functionData) {
        super.setResources(functionElement, uiStatus, functionData); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        this.fixtureAction.setResources(functionElement, uiStatus, functionData);
        this.analysisBase.setResources(functionElement, uiStatus, functionData);
    }

    @Override
    protected boolean test() {
        String command = this.allConfig.getString("Command");
        addLog("Config", "Command: " + command);
        ComPort fixture = this.fixtureAction.getComport();
        if (fixture == null || !fixture.sendCommand(command)) {
            return false;
        }
        try {
            JSONObject voltageItems = getVoltageItems();
            List<String> skipTP = this.allConfig.getListJsonArray("SkipTP");
            addLog("Config", "Skip point: " + skipTP);
            String inline;
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
                String value = this.analysisBase.findGroup(inline, "\\-?[0-9]+\\.[0-9]+");
                String voltPoint = this.analysisBase.findGroup(inline, "^TP[0-9]+");
                if (skipTP.contains(voltPoint)) {
                    continue;
                }
                String API_ITEM = voltageItems.getString(voltPoint);
                addLog("PC", String.format("Item: %s | %s | %s", API_ITEM, voltPoint, value));
                if (API_ITEM != null && !checkItem(API_ITEM, value)) {
                    result = false;
                }
                if (inline.endsWith(":")) {
                    addLog("PC", String.format("Test done! %s", result ? "Pass" : "Fail"));
                    return result;
                }
            }
            addLog("PC", "Test failed!");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getMessage());
            return false;
        } finally {
            fixture.disConnect();
        }
    }

    private boolean checkItem(String API_ITEM, String value) {
        if (API_ITEM == null) {
            return false;
        }
        checkVolt checkVolt;
        checkVolt = new checkVolt(API_ITEM);
        checkVolt.setResources(functionElement, uiStatus, functionData);
        checkVolt.setValue(value);
        checkVolt.run();
        return checkVolt.isPass();
    }

    private JSONObject getVoltageItems() {
        String path = this.allConfig.getString("FileVoltageItems");
        addLog("Config", path);
        String volList = new FileService().readFile(new File(path));
        JSONObject voltageItems = JSONObject.parseObject(volList);
        return voltageItems;
    }

}
