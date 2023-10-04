/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Runin.MMC_BadBlock;

import Communicate.AbsCommunicate;
import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Model.ErrorLog;
import Model.DataTest.FunctionParameters;
import Time.WaitTime.Class.TimeS;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class MMC_BadBlock extends AbsFunction {

    private final FunctionBase baseFunc;
    private final AnalysisBase analysisBase;

    public MMC_BadBlock(FunctionParameters parameters) {
        this(parameters, null);
    }

    public MMC_BadBlock(FunctionParameters parameters, String item) {
        super(parameters, item);
        this.baseFunc = new FunctionBase(parameters, item);
        this.analysisBase = new AnalysisBase(parameters, item);
    }

    @Override
    protected boolean test() {
        try {
            String ip = this.baseFunc.getIp();
            addLog("IP: " + ip);
            if (ip == null) {
                return false;
            }
            return check(ip);
        } catch (Exception ex) {
            ex.printStackTrace();
            addLog(LOG_KEYS.ERROR, ex.getLocalizedMessage());
            return false;
        }
    }

    private boolean check(String ip) {
        try ( AbsCommunicate communicate = this.baseFunc.getTelnetOrComportConnector()) {
            if (communicate == null) {
                return false;
            }
            return runCommand(communicate);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getMessage());
            return false;
        }
    }

    private boolean runCommand(AbsCommunicate telnet) {
        int sunBadblock = 0;
        String readUntil = this.config.getString("ReadUntil");
        int time = this.config.getInteger("Time", 10);
        List<String> commands = this.config.getJsonList("command");
        if (commands == null || commands.isEmpty()) {
            addLog("ERROR", "command in config is null or empty!");
            return false;
        }
        for (String subCommand : commands) {
            if (!this.baseFunc.sendCommand(telnet, subCommand)) {
                return false;
            }
            String result = this.analysisBase.readShowUntil(telnet, readUntil, new TimeS(time));
            String value = getValue(result);
            if (!this.analysisBase.isNumber(value)) {
                return false;
            }
            sunBadblock += this.analysisBase.string2Integer(value);
            addLog("PC", "Sum of bad blocks: " + sunBadblock);
        }
        setResult(sunBadblock);
        return true;
    }

    private String getValue(String result) {
        if (result == null) {
            return null;
        }
        String startkey = config.getString("Startkey");
        String endkey = config.getString("Endkey");
        return this.analysisBase.subString(result, startkey, endkey);
    }

}
