/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions;

import Model.DataTest.ProcessTest.ProcessData;
import Model.Interface.IFunction;
import Model.DataTest.FunctionData.FunctionData;
import Model.DataTest.FunctionData.ItemTestData;
import Model.DataSource.ModeTest.FunctionConfig.FuncAllConfig;
import Model.DataTest.FunctionParameters;
import Model.DataTest.ProcessTest.ProcessTestSignal;
import Model.DataTest.ProcessTest.ProductData;
import Model.DataTest.UiInformartion;
import Model.ErrorLog;
import View.subUI.SubUI.AbsSubUi;

/**
 *
 * @author 21AK22
 */
public abstract class AbsFunction implements IFunction {

    protected final FuncAllConfig config;
    protected final ProcessData processData;
    protected final ProcessTestSignal testSignal;
    protected final ProductData productData;
    protected final UiInformartion uIInfo;
    protected final AbsSubUi subUI;
    private final ItemTestData itemTestData;
    private final AnalysisResult analysisResult;
    protected final FunctionParameters functionParameters;
    private final FunctionData functionData;
    private boolean status;
    protected int turn;

    protected AbsFunction(FunctionParameters functionParameters, String itemName) {
        this.functionParameters = functionParameters;
        this.functionData = functionParameters.getFunctionData();
        if (itemName == null) {
            itemName = this.functionData.getFunctionName().getItemName();
        }
        this.functionData.addItemtest(itemName);
        this.itemTestData = this.functionData.getItemData(itemName);
        this.config = this.itemTestData.getAllConfig();
        this.analysisResult = new AnalysisResult(itemTestData);
        this.subUI = functionParameters.getUiStatus().getSubUi();
        this.uIInfo = functionParameters.getUiStatus().getInfo();
        this.processData = functionParameters.getUiStatus().getProcessData();
        this.testSignal = processData.getSignal();
        this.productData = processData.getProductData();
        this.turn = 1;
    }

    @Override
    public void run() {
        status = false;
        this.addLog(String.format("Turn: %s", turn++));
        try {
            status = test();
        } catch (Exception ex) {
            ex.printStackTrace();
            ErrorLog.addError(this, ex.getLocalizedMessage());

        }
    }

    public void runTest() {
        try {
            start();
            run();
            endTurn();
        } finally {
            end();
        }
    }

    @Override
    public boolean isPass() {
        return itemTestData.isPass();
    }

    @Override
    public boolean isTesting() {
        return itemTestData.isTest();
    }

    void start() {
        this.itemTestData.start();
    }

    void endTurn() {
        this.analysisResult.checkResult(this.status, getResult());
        this.itemTestData.endTurn();
    }

    void end() {
        this.itemTestData.end();
    }

    protected void setResult(Object result) {
        this.itemTestData.setResult(String.format("%s", result));
    }

    public String getResult() {
        return this.itemTestData.getResultTest();
    }

    protected abstract boolean test();

    protected void addLog(Object log) {
        this.functionData.addLog(log);
    }

    protected void addLog(String key, Object log) {
        this.functionData.addLog(key, log);
    }

}
