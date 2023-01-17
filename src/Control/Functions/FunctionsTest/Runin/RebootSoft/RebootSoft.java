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
        String ip = this.analysisBase.getIp();
        addLog("IP: " + ip);
        if (ip == null) {
            return false;
        }
        return cycleReboot(ip);
    }

    private boolean cycleReboot(String ip) {
        int time = this.config.getInteger("time", 5);
        addLog("CONFIG", "Time: " + time);
        try {
            if (!this.functionBase.rebootSoft(ip, time) || !this.functionBase.pingTo(ip, 200)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getLocalizedMessage());
            return false;
        }
    }

}
