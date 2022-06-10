/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions;

import Model.DataSource.ModeTest.FunctionConfig.FunctionElement;
import Model.ManagerUI.UIStatus.Elemants.UiData;
import Model.ManagerUI.UIStatus.UiStatus;
import View.subUI.SubUI.AbsSubUi;
import Model.Interface.IFunction;
import Model.DataTest.DataBoxs.FunctionData;
import Model.DataTest.FuncAllConfig;

/**
 *
 * @author 21AK22
 */
public abstract class AbsFunction implements IFunction {
    
    protected final FuncAllConfig allConfig;
    private FunctionData functionData;
    protected UiData uiData;
    protected AbsSubUi subUi;
    
    protected AbsFunction(String functionName) {
        this.allConfig = new FuncAllConfig(functionName);
    }
    
    public void setUIStatus(UiStatus uiStatus, FunctionData functionData, FunctionElement funcConfig) {
        this.allConfig.setUIStatus(uiStatus, funcConfig);
        this.functionData = functionData;
        this.uiData = uiStatus.getUiData();
        this.subUi = uiStatus.getSubUi();
    }
    
    @Override
    public void run() {
        boolean status = test();
        if (getResult() == null || getResult().isBlank()) {
            this.functionData.setStatus(status);
            setResult(this.functionData.createDefaultResult());
        }else{
            checkResultWithLimits(getResult());
        }
    }
    
    protected abstract boolean test();
    
    @Override
    public boolean isPass() {
        return functionData.isPass();
    }
    
    protected void setResult(String result) {
        this.functionData.setResult(result);
    }
    
    public String getFuntionName() {
        return this.allConfig.getFunctionName();
    }
    
    protected void addLog(Object str) {
        this.functionData.addLog(str);
    }
    
    protected String getResult() {
        return this.functionData.getResultTest();
    }

    private void checkResultWithLimits(String result) {
        
    }
    
}
