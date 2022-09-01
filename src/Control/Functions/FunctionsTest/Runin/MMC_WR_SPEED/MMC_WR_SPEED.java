/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Runin.MMC_WR_SPEED;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Model.DataSource.ModeTest.FunctionConfig.FunctionElement;
import Model.DataTest.FunctionData.FunctionData;
import Model.ErrorLog;
import Model.ManagerUI.UIStatus.UiStatus;
import Communicate.Telnet.Telnet;
import Model.DataSource.ModeTest.FunctionConfig.FunctionName;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class MMC_WR_SPEED extends AbsFunction {

    private final FunctionBase baseFunc;
    private final AnalysisBase analysisBase;
    private MMC_SPEED mmc_speed;

    public MMC_WR_SPEED(FunctionName itemName) {
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
        Telnet telnet = this.baseFunc.getTelnet(ip, 23);
        if (telnet == null) {
            return false;
        }
        String response;
        try {
            String command = this.allConfig.getString("command");
            if (!this.baseFunc.sendCommand(telnet, command)) {
                return false;
            }
            response = getResponse(telnet);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getMessage());
            return false;
        } finally {
            this.baseFunc.disConnect(telnet);
        }
        List<String> items = this.allConfig.getListJsonArray("ItemNames");
        List<String> blocks = this.allConfig.getListJsonArray("Block");
        List<String> KeyWords = this.allConfig.getListJsonArray("KeyWord");
        addLog("Config", "Items: " + items);
        addLog("Config", "Block: " + blocks);
        addLog("Config", "KeyWord: " + blocks);
        return RunSubItems(items, response, blocks, KeyWords);
    }

    private boolean RunSubItems(List<String> items, String response, List<String> blocks, List<String> KeyWords) {
        int itemsSize = items.size();
        for (int i = 0; i < itemsSize; i++) {
            String item = items.get(i);
            addLog("PC", item);
            mmc_speed = new MMC_SPEED(new FunctionName(item, ""));
            mmc_speed.setResources(this.functionElement, uiStatus, functionData);
            mmc_speed.setData(response, blocks.get(i), KeyWords.get(i));
            mmc_speed.run();
            if (!mmc_speed.isPass()) {
                return false;
            }
        }
        return true;
    }

    private String getResponse(Telnet telnet) {
        int time = this.allConfig.getInteger("Time", 5);
        String until = this.allConfig.getString("ReadUntil");
        return this.analysisBase.getUntil(telnet, until, time);
    }

}
