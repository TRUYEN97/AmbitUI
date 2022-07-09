/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Runin.MMC_BadBlock;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction;
import Model.DataTest.FunctionData.FunctionData;
import Model.ManagerUI.UIStatus.UiStatus;
import Time.WaitTime.Class.TimeMs;
import commandprompt.Communicate.Telnet.Telnet;

/**
 *
 * @author Administrator
 */
public class MMC_BadBlock extends AbsFunction {

    private final BaseFunction baseFunc;
    private String command;

    public MMC_BadBlock(String itemName) {
        super(itemName);
        this.baseFunc = new BaseFunction(itemName);
    }

    @Override
    public void setResources(UiStatus uiStatus, FunctionData functionData) {
        super.setResources(uiStatus, functionData); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        this.baseFunc.setResources(uiStatus, functionData);
    }

    @Override
    protected boolean test() {
        String ip = this.baseFunc.getIp();
        addLog("IP: " + ip);
        if (ip == null) {
            return false;
        }
        return check(ip);
    }

    public void setConfig(String command, String spec) {
        this.command = command;
    }

    private boolean check(String ip) {
        Telnet telnet = this.baseFunc.getTelnet(ip, 23);
        if (command == null) {
            command = this.allConfig.getString("command");
        }
        if (!telnet.sendCommand(command)) {
            return false;
        }
        String result = this.baseFunc.getValue(telnet);
        if (this.baseFunc.isMun(result)) {
            setResult(result);
            return true;
        }
        return false;
    }

}
