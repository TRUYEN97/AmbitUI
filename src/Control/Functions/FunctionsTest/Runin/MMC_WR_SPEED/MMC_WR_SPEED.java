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
import java.util.List;

/**
 *
 * @author Administrator
 */
public class MMC_WR_SPEED extends AbsFunction {

    private final FunctionBase baseFunc;
    private final AnalysisBase analysisBase;
    private MMC_SPEED mmc_speed;

    public MMC_WR_SPEED(FunctionParameters parameters) {
        this(parameters, null);
    }

    public MMC_WR_SPEED(FunctionParameters parameters, String item) {
        super(parameters, item);
        this.baseFunc = new FunctionBase(parameters, item);
        this.analysisBase = new AnalysisBase(parameters, item);
    }

    @Override
    protected boolean test() {
        String ip = this.analysisBase.getIp();
        addLog("IP: " + ip);
        if (ip == null) {
            return false;
        }
        String response;
        try (Telnet telnet = this.baseFunc.getTelnet(ip, 23)){
            if (telnet == null) {
                return false;
            }
            String command = this.config.getString("command");
            if (!this.baseFunc.sendCommand(telnet, command)) {
                return false;
            }
            response = getResponse(telnet);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getMessage());
            return false;
        }
        List<String> items = this.config.getListJsonArray("ItemNames");
        List<String> blocks = this.config.getListJsonArray("Block");
        List<String> KeyWords = this.config.getListJsonArray("KeyWord");
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
            mmc_speed = new MMC_SPEED(functionParameters, item);
            mmc_speed.setData(response, blocks.get(i), KeyWords.get(i));
            mmc_speed.runTest();
            if (!mmc_speed.isPass()) {
                return false;
            }
        }
        return true;
    }

    private String getResponse(Telnet telnet) {
        int time = this.config.getInteger("Time", 5);
        String until = this.config.getString("ReadUntil");
        return this.analysisBase.getUntil(telnet, until, time);
    }

}
