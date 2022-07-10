/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Runin.MMC_BadBlock;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction;
import Model.DataTest.FunctionData.FunctionData;
import Model.ManagerUI.UIStatus.UiStatus;
import commandprompt.Communicate.Telnet.Telnet;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class MMC_BadBlock extends AbsFunction {

    private final BaseFunction baseFunc;
    private Telnet telnet;

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

    private boolean check(String ip) {
        telnet = this.baseFunc.getTelnet(ip, 23);
        String startkey = allConfig.getString("Startkey");
        String endkey = allConfig.getString("Endkey");
        List<String> commands = this.allConfig.getListSlip("command", "\\|");
        if (commands == null || commands.isEmpty()) {
            addLog("ERROR", "command in config is null or empty!");
            return false;
        }
        return runCommand(commands, startkey, endkey);
    }

    private boolean runCommand(List<String> commands, String startkey, String endkey) {
        Integer sunBadblock = 0;
        for (String subCommand : commands) {
            if (!this.baseFunc.sendCommand(telnet, subCommand)) {
                return false;
            }
            String result = this.baseFunc.getValue(telnet, startkey, endkey);
            if (!this.baseFunc.isNumber(result)) {
                return false;
            }
            sunBadblock += this.baseFunc.string2Integer(result);
            addLog("PC", "Sum of bad blocks: " + sunBadblock);
        }
        setResult(sunBadblock.toString());
        return true;
    }

}
