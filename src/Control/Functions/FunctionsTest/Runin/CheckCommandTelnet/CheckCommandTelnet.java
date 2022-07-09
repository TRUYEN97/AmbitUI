/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Runin.CheckCommandTelnet;

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
public class CheckCommandTelnet extends AbsFunction {

    private final BaseFunction baseFunc;

    public CheckCommandTelnet(String itemName) {
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
        Telnet telnet;
        if (ip == null || (telnet = this.baseFunc.getTelnet(ip, 23)) == null
                || !this.baseFunc.sendCommand(telnet, allConfig.getString("command"))) {
            return false;
        }
        String value = this.baseFunc.getValue(telnet);
        addLog("Telnet", "Value is: " + value);
        if (value == null) {
            return false;
        }
        setResult(value);
        return true;
    }

}
