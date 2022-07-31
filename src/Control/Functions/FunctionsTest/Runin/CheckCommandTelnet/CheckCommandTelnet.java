/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Runin.CheckCommandTelnet;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Model.DataSource.ModeTest.FunctionConfig.FunctionElement;
import Model.DataTest.FunctionData.FunctionData;
import Model.ErrorLog;
import Model.ManagerUI.UIStatus.UiStatus;
import Time.WaitTime.Class.TimeMs;
import commandprompt.Communicate.Telnet.Telnet;

/**
 *
 * @author Administrator
 */
public class CheckCommandTelnet extends AbsFunction {

    private final FunctionBase functionBase;
    private final AnalysisBase analysisBase;

    public CheckCommandTelnet(String itemName) {
        super(itemName);
        this.functionBase = new FunctionBase(itemName);
        this.analysisBase = new AnalysisBase(itemName);
    }

    @Override
    public void setResources(FunctionElement funcConfig, UiStatus uiStatus, FunctionData functionData) {
        super.setResources(funcConfig, uiStatus, functionData); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        this.functionBase.setResources(funcConfig, uiStatus, functionData);
        this.analysisBase.setResources(funcConfig, uiStatus, functionData);
    }

    @Override
    protected boolean test() {
        String ip = this.analysisBase.getIp();
        Telnet telnet = null;
        try {
            if (ip == null || (telnet = this.functionBase.getTelnet(ip, 23)) == null
                    || !this.functionBase.sendCommand(telnet, allConfig.getString("command"))) {
                return false;
            }
            String startkey = allConfig.getString("Startkey");
            String endkey = allConfig.getString("Endkey");
            String regex = allConfig.getString("Regex");
            String value = this.analysisBase.getValue(telnet, startkey, endkey, regex, new TimeMs(1000));
            if (value == null) {
                return false;
            }
            setResult(value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getMessage());
            return false;
        } finally {
            if (telnet != null) {
                telnet.disConnect();
            }
        }
    }

}
