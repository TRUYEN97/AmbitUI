/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Runin.RebootSoft;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Model.DataTest.FunctionParameters;
import Model.ErrorLog;

/**
 *
 * @author Administrator
 */
public class RebootSoft extends AbsFunction {

    private final FunctionBase functionBase;
    private final AnalysisBase analysisBase;

    public RebootSoft(FunctionParameters parameters) {
        this(parameters, null);
    }

    public RebootSoft(FunctionParameters parameters, String item) {
        super(parameters, item);
        this.functionBase = new FunctionBase(parameters, item);
        this.analysisBase = new AnalysisBase(parameters, item);
    }

    @Override
    protected boolean test() {
        String ip = this.functionBase.getIp();
        addLog("IP: " + ip);
        if (ip == null) {
            return false;
        }
        int times = this.config.getInteger("times", 1);
        addLog("Config", "Run test %s times",times);
        for (int i = 0; i < times; i++) {
            if (!cycleReboot(ip)) {
                return false;
            }
        }
        return true;
    }

    private boolean cycleReboot(String ip) {
        int waitShutdownTime = this.config.getInteger("waitTime", 10);
        addLog("CONFIG", "Wait for DUT to shut down: %s S", waitShutdownTime);
        int pingTime = this.config.getInteger("pingTimes", 120);
        addLog("CONFIG", "Ping times: %s", pingTime);
        try {
            return this.functionBase.rebootSoft(ip, waitShutdownTime, pingTime);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getLocalizedMessage());
            return false;
        }
    }

}
