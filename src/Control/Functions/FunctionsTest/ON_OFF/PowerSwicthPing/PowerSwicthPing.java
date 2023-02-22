/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.ON_OFF.PowerSwicthPing;

import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Control.Functions.FunctionsTest.Runin.PowerSwitch.PowerSwitchFunc;
import Model.DataTest.FunctionParameters;

/**
 *
 * @author Administrator
 */
public class PowerSwicthPing extends PowerSwitchFunc{

    private final FunctionBase baseFunc;
    private final AnalysisBase analysisBase;

    public PowerSwicthPing(FunctionParameters parameters) {
        this(parameters, null);
    }
    
    public PowerSwicthPing(FunctionParameters parameters, String item) {
        super(parameters, item);
        this.baseFunc = new FunctionBase(parameters, item);
        this.analysisBase = new AnalysisBase(parameters, item);
    }
    
    @Override
    public boolean doSomethings() {
        String ip = this.baseFunc.getIp();
        addLog("IP: " + ip);
        if (ip == null) {
            return false;
        }
        return this.baseFunc.pingTo(ip, 120, this.modeTest.isUseDHCP());
    }
    
}
