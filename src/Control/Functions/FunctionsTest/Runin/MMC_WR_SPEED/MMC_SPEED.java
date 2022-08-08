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
    public void setResources(FunctionElement funcConfig, UiStatus uiStatus, FunctionData functionData) {
        super.setResources(funcConfig, uiStatus, functionData); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        this.baseFunc.setResources(funcConfig, uiStatus, functionData);
        this.analysisBase.setResources(funcConfig, uiStatus, functionData);
    }

    @Override
    protected boolean test() {
        if (data == null) {
            String ip = this.analysisBase.getIp();
            addLog("IP: " + ip);
            if (ip == null) {
                return false;
            }
            try {
                telnet = this.baseFunc.getTelnet(ip, 23);
                if (!this.baseFunc.sendCommand(telnet, this.allConfig.getString("command"))) {
                    return false;
                }
                int time = this.allConfig.getInteger("Time", 5);
                String until = this.allConfig.getString("ReadUntil");
                data = this.analysisBase.getUntil(telnet, until, time);
            } catch (Exception e) {
                e.printStackTrace();
                ErrorLog.addError(this, e.getMessage());
                return false;
            } finally {
                if (telnet != null) {
                    telnet.disConnect();
                }
            }
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
        String[] blockData = data.split("\r\n");
        int model = 0;
        for (String line : blockData) {
            if (line.contains("B of " + block + " in ")) {
                model = 1;
            }
            if (line.contains(key) && model == 1) {
                model = 2;
            }
            int start = line.lastIndexOf(" seconds, ") + 10;
            int end = line.lastIndexOf("MB/s");
            if (model == 2 && start < end && end > -1) {
                String value = line.substring(start, end);
                if (this.analysisBase.isNumber(value)) {
                    setResult(value);
                    return true;
                }
            }
        }
        return false;
    }

}
