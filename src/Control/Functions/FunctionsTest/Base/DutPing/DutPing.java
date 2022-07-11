/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.DutPing;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.AnalysisBase;
import Control.Functions.FunctionsTest.Base.FunctionBase;
import Model.DataTest.FunctionData.FunctionData;
import Model.ManagerUI.UIStatus.UiStatus;

/**
 *
 * @author Administrator
 */
public class DutPing extends AbsFunction {
    
    private final FunctionBase baseFunc;
    private final AnalysisBase analysisBase;

    public DutPing(String itemName) {
        super(itemName);
        this.baseFunc = new FunctionBase(itemName);
        this.analysisBase = new AnalysisBase(itemName);
    }

    @Override
    public void setResources(UiStatus uiStatus, FunctionData functionData) {
        super.setResources(uiStatus, functionData); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        this.baseFunc.setResources(uiStatus, functionData);
        this.analysisBase.setResources(uiStatus, functionData);
    }
    
    
    @Override
    protected boolean test() {
        String ip = this.analysisBase.getIp();
        addLog("IP: " + ip);
        if (ip == null) {
            return false;
        }
        return this.baseFunc.pingTo(ip, 200);
    }
    
}
