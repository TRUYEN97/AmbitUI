/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Runin.MMC_WR_SPEED;

import Communicate.Impl.Telnet.Telnet;
import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Model.ErrorLog;
import Model.DataTest.FunctionParameters;

/**
 *
 * @author Administrator
 */
public class MMC_SPEED extends AbsFunction {

    private final FunctionBase baseFunc;
    private final AnalysisBase analysisBase;
    private String data;
    private String block;
    private String key;

    public MMC_SPEED(FunctionParameters parameters) {
        this(parameters, null);
    }

    public MMC_SPEED(FunctionParameters parameters, String item) {
        super(parameters, item);
        this.baseFunc = new FunctionBase(parameters, item);
        this.analysisBase = new AnalysisBase(parameters, item);
    }

    @Override
    protected boolean test() {
        if (data == null) {
            String ip = this.baseFunc.getIp();
            addLog("IP: " + ip);
            if (ip == null) {
                return false;
            }
            try ( Telnet telnet = this.baseFunc.getTelnet(ip, 23)) {
                if (telnet == null) {
                    return false;
                }
                if (!this.baseFunc.sendCommand(telnet, this.config.getString("command"))) {
                    return false;
                }
                int time = this.config.getInteger("Time", 5);
                String until = this.config.getString("ReadUntil");
                data = this.analysisBase.readUntilAndShow(telnet, until, time);
            } catch (Exception e) {
                e.printStackTrace();
                ErrorLog.addError(this, e.getMessage());
                return false;
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
            block = config.getString("Block");
        }
        addLog("Config", "Block: " + block);
        if (key == null) {
            key = this.config.getString("KeyWord");
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
