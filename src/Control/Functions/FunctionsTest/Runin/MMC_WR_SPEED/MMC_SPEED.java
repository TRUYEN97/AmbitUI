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

/**
 *
 * @author Administrator
 */
public class MMC_SPEED extends AbsFunction {

    private final FunctionBase baseFunc;
    private final AnalysisBase analysisBase;
    private Telnet telnet;
    private String data;
    private String block;
    private String key;

    public MMC_SPEED(String itemName) {
        super(itemName);
        this.baseFunc = new FunctionBase(itemName);
        this.analysisBase = new AnalysisBase(itemName);
    }

    @Override
    public void setResources(String trueItemName, UiStatus uiStatus, FunctionData functionData) {
        super.setResources(trueItemName, uiStatus, functionData); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        this.baseFunc.setResources(trueItemName, uiStatus, functionData);
        this.analysisBase.setResources(trueItemName, uiStatus, functionData);
    }

    @Override
    protected boolean test() {
        if (data == null) {
            String ip = this.analysisBase.getIp();
            addLog("IP: " + ip);
            if (ip == null) {
                return false;
            }
            telnet = this.baseFunc.getTelnet(ip, 23);
            if (!this.baseFunc.sendCommand(telnet, this.allConfig.getString("command"))) {
                return false;
            }
            int time = this.allConfig.getInteger("Time", 5);
            String until = this.allConfig.getString("ReadUntil");
            data = this.analysisBase.getUntil(telnet, until, time);
            addLog("Telnet", data);
        }
        return checkResponse();
    }

    public void setData(String data, String block, String key) {
        this.data = data;
        this.block = block;
        this.key = key;
    }

    private boolean checkResponse() {
        if (!checkConfig()) {
            return false;
        }
        return getSpeed();
    }

    private boolean checkConfig() {
        if (block == null) {
            block = allConfig.getString("Block");
        }
        addLog("Config", "Block: " + block);
        if (key == null) {
            key = this.allConfig.getString("KeyWord");
        }
        addLog("Config", "KeyWord: " + key);
        return key != null && block != null;
    }

    private boolean getSpeed() {
        int index;
        while ((index = data.lastIndexOf("Writing ")) > -1) {
            String blockData = data.substring(index);
            data = data.substring(0, index);
            if (blockData.contains("B of " + block + " in ")) {
                int start, end;
                if ((start = blockData.indexOf(key)) > -1 && (end = blockData.indexOf("\r\n\r\n")) > -1) {
                    String subData = blockData.substring(start, end);
                    String value = subData.substring(subData.lastIndexOf(" seconds, ") + 10, subData.lastIndexOf("MB/s"));
                    if (this.analysisBase.isNumber(value)) {
                        setResult(value);
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
