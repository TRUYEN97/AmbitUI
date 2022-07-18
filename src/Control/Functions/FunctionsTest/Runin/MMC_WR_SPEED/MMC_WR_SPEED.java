/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Runin.MMC_WR_SPEED;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Model.DataTest.FunctionData.FunctionData;
import Model.ManagerUI.UIStatus.UiStatus;
import commandprompt.Communicate.Telnet.Telnet;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class MMC_WR_SPEED extends AbsFunction {

    private final FunctionBase baseFunc;
    private final AnalysisBase analysisBase;
    private final MMC_SPEED mmc_speed;;
    private Telnet telnet;

    public MMC_WR_SPEED(String itemName) {
        super(itemName);
        this.mmc_speed = new MMC_SPEED(itemName);
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
        addLog("IP: " + ip);
        if (ip == null) {
            return false;
        }
        if (!sendCommand(ip)) {
            return false;
        }
        String response;
        int time = this.allConfig.getInteger("Time", 5);
        String until = this.allConfig.getString("ReadUntil");
        List<String> items = this.allConfig.getListJsonArray("ItemNames");
        List<String> blocks = this.allConfig.getListJsonArray("Block");
        List<String> KeyWords = this.allConfig.getListJsonArray("KeyWord");
        response = this.analysisBase.getUntil(telnet, until, time);
        addLog("Config", "Items: " + items);
        addLog("Config", "Block: " + blocks);
        addLog("Config", "KeyWord: " + blocks);
        addLog("Telnet", response);
        for (int i = 0; i < items.size(); i++) {
            mmc_speed.setResources(items.get(i), uiStatus, functionData);
            mmc_speed.setData(response, blocks.get(i), KeyWords.get(i));
            mmc_speed.start();
            mmc_speed.run();
            mmc_speed.end();
            if (!mmc_speed.isPass()) {
                return false;
            }
        }
        return false;
    }

    private boolean sendCommand(String ip) {
        telnet = this.baseFunc.getTelnet(ip, 23);
        String command = this.allConfig.getString("command");
        return this.baseFunc.sendCommand(telnet, command);
    }

}
