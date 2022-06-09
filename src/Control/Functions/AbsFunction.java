/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions;

import Model.DataSource.ModeTest.FunctionConfig.FunctionElement;
import Model.DataSource.ModeTest.Limit.Limit;
import Model.ManagerUI.UIStatus.Elemants.UiData;
import Model.ManagerUI.UIStatus.UiStatus;
import View.subUI.SubUI.AbsSubUi;
import Model.Interface.IFunction;
import Model.DataTest.DataBoxs.FunctionData;

/**
 *
 * @author 21AK22
 */
public abstract class AbsFunction implements IFunction {

    protected Limit limit;
    protected FunctionElement funcConfig;
    private FunctionData functionData;
    protected UiData uiData;
    protected AbsSubUi subUi;
    private final String itemName;

    protected AbsFunction(String itemName) {
        this.itemName = itemName;
    }

    public void setUIStatus(UiStatus uiStatus, FunctionData functionData) {
        this.uiData = uiStatus.getUiData();
        this.subUi = uiStatus.getSubUi();
        this.limit = uiStatus.getModeTest().getModeTestSource().getLimit();
        this.funcConfig = functionData.getFunctionConfig();
        this.functionData = functionData;
    }

    @Override
    public void run() {
       
    }
   
    protected void setResult(String result) {
        this.functionData.setResult(result);
    }

    public abstract boolean test();

    @Override
    public boolean isPass() {
        return functionData.isPass();
    }

    public String getItemName() {
        return itemName;
    }

    public String getFuncName() {
        return this.funcConfig.getFunctionName();
    }

    protected void addLog(Object str) {
        this.functionData.addLog(str);
    }

    protected String getResult() {
        return this.functionData.getResultTest();
    }

}
