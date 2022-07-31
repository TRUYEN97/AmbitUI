/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.FixtureActions;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Model.DataSource.ModeTest.FunctionConfig.FunctionElement;
import Model.DataTest.FunctionData.FunctionData;
import Model.ErrorLog;
import Model.ManagerUI.UIStatus.UiStatus;
import Time.WaitTime.Class.TimeS;
import commandprompt.Communicate.Comport.ComPort;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class FixtureAction extends AbsFunction {

    private final FunctionBase base;
    private final AnalysisBase analysisBase;

    public FixtureAction(String itemName) {
        super(itemName);
        this.base = new FunctionBase(itemName);
        this.analysisBase = new AnalysisBase(itemName);
    }

    @Override
    public void setResources(FunctionElement funcConfig, UiStatus uiStatus, FunctionData functionData) {
        super.setResources(funcConfig, uiStatus, functionData); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        this.base.setResources(funcConfig, uiStatus, functionData);
        this.analysisBase.setResources(funcConfig, uiStatus, functionData);
    }

    @Override
    public boolean test() {
        ComPort comPort = getComport();
        if (comPort == null) {
            return false;
        }
        List<String> commands = this.allConfig.getListJsonArray("FixtureCommands");
        List<String> keyWords = this.allConfig.getListJsonArray("FixtureKeywords");
        int time = this.allConfig.getInteger("FixtureWait", 1);
        return sendCommand(comPort, commands, keyWords, time);
    }

    public boolean sendCommand(ComPort comPort, List<String> commands, List<String> keyWords, int time) {
        addLog("Config", "keyWord: " + keyWords);
        addLog("Config", "Time: " + time);
        if (commands == null || commands.isEmpty() || keyWords == null || keyWords.isEmpty()) {
            addLog("ERROR", "Config failed");
            return false;
        }
        int keySide = keyWords.size();
        for (int index = 0; index < keySide; index++) {
            keyWords.set(index, getValueOfKey(keyWords.get(index)));
        }
        addLog("PC", String.format("True keys: %s", keyWords));
        try {
            for (String command : commands) {
                if (!this.base.sendCommand(comPort, command)) {
                    return false;
                }
                if (!this.analysisBase.isResponseContainKey(comPort, keyWords, new TimeS(time))) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getMessage());
            return false;
        } finally {
            comPort.disConnect();
        }
    }

    private String getValueOfKey(String keyWord) {
        return this.productData.getString(keyWord) == null ? keyWord
                : this.productData.getString(keyWord);
    }

    public ComPort getComport() {
        String com = this.allConfig.getString("FixtureCom");
        int baud = this.allConfig.getInteger("FixtureBaudRate", 9600);
        return this.base.getComport(com, baud);
    }

}
