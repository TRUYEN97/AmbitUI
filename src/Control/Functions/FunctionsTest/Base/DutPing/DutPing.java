/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.DutPing;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Model.DataTest.FunctionParameters;

/**
 *
 * @author Administrator
 */
public class DutPing extends AbsFunction {
    
    private final FunctionBase baseFunc;

    public DutPing(FunctionParameters parameters) {
        this(parameters, null);
    }
    
    public DutPing(FunctionParameters parameters, String item) {
        super(parameters, item);
        this.baseFunc = new FunctionBase(parameters, item);
    }
    
    
    @Override
    public boolean test() {
        String ip = this.baseFunc.getIp();
        addLog("IP: " + ip);
        if (ip == null) {
            return false;
        }
        int timePing = config.getInteger("time_ping", 120);
        return this.baseFunc.pingTo(ip, timePing, this.modeTest.isUseDHCP());
    }
    
}
