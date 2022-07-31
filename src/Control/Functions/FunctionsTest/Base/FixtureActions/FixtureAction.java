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
    private int baud;
    private String com;

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

    public void setBaud(String port, int baud) {
        this.com = port;
        this.baud = baud;
    }

    @Override
    public boolean test() {
        if (com == null) {
            com = this.allConfig.getString("ComPort");
            baud = this.allConfig.getInteger("BaudRate", 9600);
        }
        ComPort comPort = this.base.getComport(com, baud);
        if (comPort == null) {
            return false;
        }
        List<String> commands = this.allConfig.getListJsonArray("command");
        String keyWord = this.allConfig.getString("keyWord");
        int time = this.allConfig.getInteger("Time", 1);
        return sendCommand(comPort, commands, keyWord, time);
    }

    public boolean sendCommand(ComPort comPort, List<String> commands, String keyWord, int time) {
        addLog("Config", "keyWord: " + keyWord);
        addLog("Config", "Time: " + time);
        if (commands.isEmpty() || keyWord == null) {
            addLog("ERROR", "Config failed");
            return false;
        }
        String key = getValueOfKey(keyWord);
        addLog(String.format("KeyWord is %s - %s", keyWord, key));
        try {
            for (String command : commands) {
                if (!this.base.sendCommand(comPort, command)) {
                    return false;
                }
                if (!this.analysisBase.isResponseContainKey(comPort, key, key, new TimeS(time))) {
                    return false;
                }
            }
            return true;
        } finally {
            comPort.disConnect();
        }
    }

    private String getValueOfKey(String keyWord) {
        return this.productData.getString(keyWord) == null ? keyWord
                : this.productData.getString(keyWord);
    }

}
