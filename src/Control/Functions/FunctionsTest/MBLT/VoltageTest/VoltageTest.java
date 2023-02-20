/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.MBLT.VoltageTest;

import Communicate.Impl.Comport.ComPort;
import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
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
public class VoltageTest extends AbsFunction {

    private final FixtureAction fixtureAction;
    private final AnalysisBase analysisBase;

    public VoltageTest(FunctionParameters parameters) {
        this(parameters, null);
    }

    public VoltageTest(FunctionParameters parameters, String item) {
        super(parameters, item);
        this.fixtureAction = new FixtureAction(parameters, item);
        this.analysisBase = new AnalysisBase(parameters, item);
    }

    @Override
    protected boolean test() {
        String command = this.config.getString("Command");
        addLog("Config", "Command: " + command);
        try ( ComPort fixture = this.fixtureAction.getComport()) {
            if (fixture == null || !fixture.sendCommand(command)) {
                return false;
            }
            JSONObject voltageItems = getVoltageItems();
            List<String> skipTP = this.config.getJsonList("SkipTP");
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
        }
    }

    private boolean checkItem(String item, String value) {
        if (item == null) {
            return false;
        }
        checkVolt checkVolt;
        checkVolt = new checkVolt(functionParameters, item);
        checkVolt.setValue(value);
        checkVolt.runTest();
        return checkVolt.isPass();
    }

    private JSONObject getVoltageItems() {
        String path = this.config.getString("FileVoltageItems");
        addLog("Config", path);
        String volList = new FileService().readFile(new File(path));
        JSONObject voltageItems = JSONObject.parseObject(volList);
        return voltageItems;
    }

}
