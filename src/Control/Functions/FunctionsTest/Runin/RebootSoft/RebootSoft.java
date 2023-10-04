/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Runin.RebootSoft;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Model.DataTest.FunctionParameters;
import Model.ErrorLog;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class RebootSoft extends AbsFunction {

    private final FunctionBase functionBase;

    public RebootSoft(FunctionParameters parameters) {
        this(parameters, null);
    }

    public RebootSoft(FunctionParameters parameters, String item) {
        super(parameters, item);
        this.functionBase = new FunctionBase(parameters, item);
    }

    @Override
    protected boolean test() {
        try {
            String ip = this.functionBase.getIp();
            addLog("IP: " + ip);
            if (ip == null) {
                return false;
            }
            int times = this.config.getInteger("times", 1);
            addLog("Config", "Run test %s times", times);
            for (int i = 0; i < times; i++) {
                addLog(LOG_KEYS.PC, "Times: %s", i + 1);
                if (!cycleReboot(ip)) {
                    return false;
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            addLog(LOG_KEYS.ERROR, ex.getLocalizedMessage());
            return false;
        }
    }

    private boolean cycleReboot(String ip) {
        String cmd = this.config.getString("command", "reboot");
        addLog(LOG_KEYS.CONFIG, "command: %s", cmd);
        int waitShutdownTime = this.config.getInteger("waitTime", 10);
        addLog(LOG_KEYS.CONFIG, "Wait for DUT to shut down: %s S", waitShutdownTime);
        int pingTime = this.config.getInteger("pingTimes", 120);
        addLog(LOG_KEYS.CONFIG, "Ping time: %s", pingTime);
        try {
            return this.functionBase.rebootSoft(ip, cmd, waitShutdownTime, pingTime);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getLocalizedMessage());
            addLog(LOG_KEYS.ERROR, e.getLocalizedMessage());
            return false;
        }
    }

}
