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
    protected boolean test() {
        String com = this.allConfig.getString("ComPort");
        Integer baud = this.allConfig.getInteger("BaudRate");
        ComPort comPort = this.base.getComport(com, baud);
        if (comPort == null) {
            return false;
        }
        return sendCommand(comPort);
    }

    private boolean sendCommand(ComPort comPort) {
        List<String> commands = this.allConfig.getListJsonArray("command");
        String keyWord = this.allConfig.getString("keyWord");
        Integer time = this.allConfig.getInteger("Time");
        if (commands.isEmpty() || time == null || keyWord == null) {
            addLog("ERROR", "Config failed");
            return false;
        }
        String key = getValueOfKey(keyWord);
        addLog(String.format("KeyWord is %s - %s", keyWord, key));
        for (String command : commands) {
            if (!this.base.sendCommand(comPort, command)) {
                return false;
            }
            String result = comPort.readUntil(key, new TimeS(time));
            addLog(comPort.getClass().getSimpleName(), result);
            if (!result.endsWith(key)) {
                return false;
            }
        }
        return true;
    }

    private String getValueOfKey(String keyWord) {
        return this.productData.getString(keyWord) == null ? keyWord
                : this.productData.getString(keyWord);
    }

}
