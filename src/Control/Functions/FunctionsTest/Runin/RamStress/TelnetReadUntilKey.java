/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Runin.RamStress;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.AnalysisBase;
import Control.Functions.FunctionsTest.Base.FunctionBase;
import Model.DataTest.FunctionData.FunctionData;
import Model.ManagerUI.UIStatus.UiStatus;
import Time.WaitTime.Class.TimeS;
import commandprompt.AbstractStream.SubClass.ReadStreamOverTime;
import commandprompt.Communicate.Telnet.Telnet;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class TelnetReadUntilKey extends AbsFunction {

    private final FunctionBase baseFunc;
    private final AnalysisBase analysisBase;

    public TelnetReadUntilKey(String itemName) {
        super(itemName);
        this.baseFunc = new FunctionBase(itemName);
        this.analysisBase = new AnalysisBase(itemName);
    }

    @Override
    public void setResources(UiStatus uiStatus, FunctionData functionData) {
        super.setResources(uiStatus, functionData); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        this.baseFunc.setResources(uiStatus, functionData);
        this.analysisBase.setResources(uiStatus, functionData);
    }

    @Override
    protected boolean test() {
        String ip = this.analysisBase.getIp();
        Telnet telnet;
        addLog("IP: " + ip);
        if (ip == null || (telnet = this.baseFunc.getTelnet(ip, 23, new ReadStreamOverTime())) == null) {
            return false;
        }
        return runTest(telnet);
    }

    private boolean runTest(Telnet telnet) {
        List<String> commands = this.allConfig.getListJsonArray("command");
        if (commands.isEmpty()) {
            addLog("ERROR", "Commands is empty!");
            return false;
        }
        String key = this.allConfig.getString("keyWord");
        Integer time = this.allConfig.getInteger("Time");
        if (time == null) {
            addLog("Config", "Time == null !!");
            return false;
        }
        for (String command : commands) {
            addLog("Config", "Wait for " + time + " s");
            if (!this.baseFunc.sendCommand(telnet, command)
                    || !this.analysisBase.isResponseContainKey(telnet, key, new TimeS(time))) {
                return false;
            }
        }
        return true;
    }

}
