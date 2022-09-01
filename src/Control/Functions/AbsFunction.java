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
import Model.DataSource.ModeTest.FunctionConfig.FunctionElement;
import Model.DataSource.ModeTest.FunctionConfig.FunctionName;
import Model.DataTest.ProcessTest.ProcessTestSignal;
import Model.DataTest.ProcessTest.ProductData;
import Model.DataTest.ProcessTest.UiInformartion;
import Model.ErrorLog;

/**
 *
 * @author 21AK22
 */
public abstract class AbsFunction implements IFunction {

    protected final FuncAllConfig allConfig;
    protected UiStatus uiStatus;
    protected ProcessData processData;
    protected AbsSubUi subUi;
    protected ProcessTestSignal testSignal;
    protected ProductData productData;
    protected UiInformartion uIInfo;
    protected FunctionElement functionElement;
    protected FunctionData functionData;
    protected int retry;
    private ItemTestData itemTestData;
    private AnalysisResult analysisResult;

    protected AbsFunction(FunctionName itemName) {
        this.allConfig = new FuncAllConfig(itemName);
        this.retry = 0;
    }

    public void setResources(FunctionElement functionElement, UiStatus uiStatus, FunctionData functionData) {
        this.uiStatus = uiStatus;
        this.functionData = functionData;
        this.uIInfo = uiStatus.getInfo();
        this.processData = uiStatus.getProcessData();
        this.subUi = uiStatus.getSubUi();
        this.functionElement = functionElement;
        this.allConfig.setResources(uiStatus, functionElement);
        this.itemTestData = new ItemTestData(allConfig);
        this.functionData.addItemtest(itemTestData);
        this.processData.addFunctionData(functionData);
        this.testSignal = this.processData.getSignal();
        this.productData = this.processData.getProductData();
        this.analysisResult = new AnalysisResult(itemTestData, allConfig);
    }

    void setRetry(int retry) {
        this.retry = retry;
    }

    void start() {
        this.itemTestData.start();
    }

    @Override
    public void run() {
        try {
            start();
            runTest();
        } finally {
            end();
        }
    }

    public void runTest() {
        boolean status =false;
        try {
            status = test();
        } catch (Exception e) {
            ErrorLog.addError(this, e.getLocalizedMessage());
            addLog("ERROR", e.getLocalizedMessage());
            e.printStackTrace();
        }finally{
            this.analysisResult.checkResult(status, getResult());
            endTurn();
        }
    }

    protected FunctionName getItemName() {
        return this.allConfig.getItemName();
    }

    protected String getFunctionName() {
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

    @Override
    public boolean isTesting() {
        return itemTestData.isTest();
    }

    protected void setResult(Object result) {
        this.itemTestData.setResult(String.format("%s", result));
    }

    protected String getResult() {
        return this.itemTestData.getResultTest();
    }

    private void endTurn() {
        this.itemTestData.endTurn();
    }

    public void end() {
        this.itemTestData.end();
    }

}
