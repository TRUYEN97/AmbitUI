/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Runin.MMC_WR_SPEED_NEW;

import Communicate.AbsCommunicate;
import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Model.ErrorLog;
import Model.DataTest.FunctionParameters;
import Time.WaitTime.Class.TimeS;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class MMC_WR_SPEED_NEW extends AbsFunction {

    private final FunctionBase baseFunc;
    private final AnalysisBase analysisBase;

    public MMC_WR_SPEED_NEW(FunctionParameters parameters) {
        this(parameters, null);
    }

    public MMC_WR_SPEED_NEW(FunctionParameters parameters, String item) {
        super(parameters, item);
        this.baseFunc = new FunctionBase(parameters, item);
        this.analysisBase = new AnalysisBase(parameters, item);
    }

    @Override
    protected boolean test() {
        String response;
        try ( AbsCommunicate communicate = this.baseFunc.getTelnetOrComportConnector()) {
            if (communicate == null) {
                return false;
            }
            String command = this.config.getString("command");
            if (!this.baseFunc.sendCommand(communicate, command)) {
                return false;
            }
            response = getResponse(communicate);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getMessage());
            return false;
        }
        List<String> items = this.config.getJsonList("ItemNames");
        List<String> KeyWords = this.config.getJsonList("KeyWord");
        addLog("Config", "Items: %s", items);
        addLog("Config", "KeyWord: %s", KeyWords);
        return runSubItems(items, response, KeyWords);
    }

    private boolean runSubItems(List<String> items, String response, List<String> KeyWords) {
        int itemsSize = items.size();
        boolean rs = true;
        for (int i = 0; i < itemsSize; i++) {
            String item = createChildItemName(items.get(i));
            addLog("PC", item);
            MMC_SPEED_NEW mmc_speed = new MMC_SPEED_NEW(functionParameters, item);
            mmc_speed.setData(response, KeyWords.get(i));
            mmc_speed.runTest();
            if (!mmc_speed.isPass()) {
                rs = false;
            }
        }
        return rs;
    }

    private String getResponse(AbsCommunicate telnet) {
        int time = this.config.getInteger("Time", 5);
        String until = this.config.getString("ReadUntil");
        return this.analysisBase.readShowUntil(telnet, until, new TimeS(time));
    }

}
