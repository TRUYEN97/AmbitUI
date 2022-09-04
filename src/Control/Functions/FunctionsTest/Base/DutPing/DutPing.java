/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.DutPing;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Model.DataTest.FunctionParameters;

/**
 *
 * @author Administrator
 */
public class DutPing extends AbsFunction {
    
    private final FunctionBase baseFunc;
    private final AnalysisBase analysisBase;

    public DutPing(FunctionParameters parameters) {
        this(parameters, null);
    }
    
    public DutPing(FunctionParameters parameters, String item) {
        super(parameters, item);
        this.baseFunc = new FunctionBase(parameters, item);
        this.analysisBase = new AnalysisBase(parameters, item);
    }
    
    
    @Override
    public boolean test() {
        String ip = this.analysisBase.getIp();
        addLog("IP: " + ip);
        if (ip == null) {
            return false;
        }
        return this.baseFunc.pingTo(ip, 50);
    }
    
}
