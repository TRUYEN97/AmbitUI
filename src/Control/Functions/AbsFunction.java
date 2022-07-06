/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions;

import Model.DataTest.ProcessTest.ProcessData;
import Model.ManagerUI.UIStatus.UiStatus;
import View.subUI.SubUI.AbsSubUi;
import Model.Interface.IFunction;
import Model.DataTest.FunctionData.FunctionData;
import Model.DataTest.FunctionData.ItemTestData;
import Model.DataSource.ModeTest.FunctionConfig.FuncAllConfig;
import Model.DataTest.ProcessTest.ProcessTestSignal;
import Model.DataTest.ProcessTest.ProductData;
import Model.DataTest.ProcessTest.UiInformartion;

/**
 *
 * @author 21AK22
 */
public abstract class AbsFunction implements IFunction {

    protected final FuncAllConfig allConfig;
    protected ProcessData processData;
    protected AbsSubUi subUi;
    protected ProcessTestSignal testSignal;
    protected ProductData productData;
    protected UiInformartion uIInfo;
    private ItemTestData itemTestData;
    private AnalysisResult analysisResult;
    private FunctionData functionData;

    protected AbsFunction(String itemName) {
        this.allConfig = new FuncAllConfig(itemName);
    }

    public void setResources(UiStatus uiStatus, FunctionData functionData) {
        this.functionData = functionData;
        this.uIInfo = uiStatus.getInfo();
        this.processData = uiStatus.getProcessData();
        this.testSignal = this.processData.getSignal();
        this.productData = this.processData.getProductData();
        this.subUi = uiStatus.getSubUi();
        this.allConfig.setResources(uiStatus);
        this.itemTestData = new ItemTestData(allConfig);
        this.functionData.addItemtest(itemTestData);
        this.processData.addFunctionData(functionData);
        this.analysisResult = new AnalysisResult(itemTestData, allConfig);
    }

    void start() {
        this.itemTestData.start();
    }

    @Override
    public void run() {
        this.analysisResult.checkResult(test(), getResult());
        this.itemTestData.endThisTurn();
    }

    void end() {
        this.itemTestData.end();
    }

    String getItemName() {
        return this.allConfig.getItemName();
    }

    String getFunctionName() {
        return this.allConfig.getFunctionName();
    }

    protected abstract boolean test();

    protected void addLog(Object str) {
        this.functionData.addLog(str);
    }

    protected void addLog(String key, Object str) {
        this.functionData.addLog(key, str);
    }

    @Override
    public boolean isPass() {
        return itemTestData.isPass();
    }

    protected void setResult(String result) {
        this.itemTestData.setResult(result);
    }

    protected String getResult() {
        return this.functionData.getResultTest();
    }

}
