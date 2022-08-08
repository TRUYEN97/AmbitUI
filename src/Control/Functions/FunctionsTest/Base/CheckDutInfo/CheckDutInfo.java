/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.CheckDutInfo;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Model.DataSource.ModeTest.FunctionConfig.FunctionElement;
import Model.DataTest.FunctionData.FunctionData;
import Model.ManagerUI.UIStatus.UiStatus;
import Time.WaitTime.Class.TimeMs;
import Communicate.Telnet.Telnet;

/**
 *
 * @author Administrator
 */
public class CheckDutInfo extends AbsFunction {

    private final FunctionBase baseFunc;
    private final AnalysisBase analysisBase;

    public CheckDutInfo(String itemName) {
        super(itemName);
        this.baseFunc = new FunctionBase(itemName);
        this.analysisBase = new AnalysisBase(itemName);
    }

    @Override
    public void setResources(FunctionElement funcConfig, UiStatus uiStatus, FunctionData functionData) {
        super.setResources(funcConfig, uiStatus, functionData); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        this.baseFunc.setResources(funcConfig, uiStatus, functionData);
        this.analysisBase.setResources(funcConfig, uiStatus, functionData);
    }

    @Override
    protected boolean test() {
        String ip = this.analysisBase.getIp();
        addLog("IP: " + ip);
        if (ip == null) {
            return false;
        }
        return check(ip);
    }

    private boolean check(String ip) {
        Telnet telnet = this.baseFunc.getTelnet(ip, 23);
        if (telnet == null || !this.baseFunc.sendCommand(telnet, allConfig.getString("command"))) {
            return false;
        }
        String startkey = allConfig.getString("Startkey");
        String endkey = allConfig.getString("Endkey");
        String regex = allConfig.getString("Regex");
        return checkValue(this.analysisBase.getValue(telnet, startkey, endkey, regex, new TimeMs(1000)));
    }

    private boolean checkValue(String value) {
        addLog("Value is: " + value);
        String spec = productData.getString("compareWith");
        addLog("Spec is: " + spec);
        setResult(value);
        return spec != null && value != null && spec.equals(value);
    }

}
