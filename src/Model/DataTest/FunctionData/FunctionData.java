/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataTest.FunctionData;

import Model.AllKeyWord;
import Model.DataSource.ModeTest.ErrorCode.ErrorCodeElement;
import Model.DataSource.ModeTest.FunctionConfig.FunctionName;
import Model.DataSource.Setting.Setting;
import Model.DataSource.Tool.IgetTime;
import Model.ErrorLog;
import Model.ManagerUI.UIStatus.UiStatus;
import MyLoger.MyLoger;
import java.io.File;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class FunctionData {

    private final MyLoger loger;
    private final ItemTestManager itemTestManager;
    private final FunctionName functionName;
    private final UiStatus uiStatus;
    private final IgetTime testTimer;
    private Map<String, ItemTestData> finaltemTests;

    public FunctionData(UiStatus uiStatus, FunctionName functionName) {
        this.loger = new MyLoger();
        this.itemTestManager = new ItemTestManager();
        this.uiStatus = uiStatus;
        this.functionName = functionName;
        this.testTimer = this.uiStatus.getProcessData();
    }

    public FunctionName getFunctionName() {
        return this.functionName;
    }

    public void addItemtest(ItemTestData itemTest) {
        this.itemTestManager.addItemtest(itemTest);
    }

    public void setResult(String resultTest) {
        if (resultTest == null) {
            return;
        }
        this.itemTestManager.getThisItem().setResult(resultTest);
    }

    public void addLog(Object str) {
        this.loger.addLog(str);
    }

    public void addLog(String key, Object str) {
        this.loger.addLog(key, str);
    }

    public String getResultTest() {
        return itemTestManager.getThisItem().getResultTest();
    }

    public String getStatusTest() {
        return itemTestManager.getThisItem().getStatusTest();
    }

    public boolean isTesting() {
        return itemTestManager.getThisItem().isTest();
    }

    public void start() {
        if (!this.loger.begin(new File(createLogPath()), true, true)) {
            String mess = "can't delete local function log file of ".concat(functionName.getItemName());
            ErrorLog.addError(mess);
            JOptionPane.showMessageDialog(null, mess);
        }
        addLog(String.format("ITEM[%s] - FUNCTION[%s]",
                this.functionName, functionName.getFunctionName()));
    }
    
    private String createLogPath() {
        String settingPath = Setting.getInstance().getFunctionsLocalLogPath();
        return String.format("%s/%s/%s_%s.txt",
                settingPath, this.uiStatus.getName(),
                this.functionName.getFunctionName(), this.functionName.getItemName());
    }

    public double getRunTime() {
        return itemTestManager.getThisItem().getRunTime();
    }

    public void end() {
        this.addLog(String.format("TIME[%.3f s] - STATUS[%S]",
                this.getRunTime(), getStatusTest()));
        this.loger.close();
        this.finaltemTests.putAll(itemTestManager.getItemTests());
    }

    public String getLog() {
        return this.loger.getLog();
    }

    public MyLoger getLoger() {
        return this.loger;
    }

    public boolean isPass() {
        return itemTestManager.getFirstFail() == null;
    }

    public void setStatus(boolean stt) {
        if (stt) {
            this.itemTestManager.getThisItem().setPass();
        } else {
            this.itemTestManager.getThisItem().setFail(ErrorCodeElement.SIMPLE);
        }
    }

    public void setFail(String errorType) {
        for (var itemTest : itemTestManager.getListItemTests()) {
            if (itemTest.isTest()) {
                itemTest.setFail(errorType);
            }
        }
    }

    public void setFinalMapItems(Map<String, ItemTestData> mapfunctionData) {
        this.finaltemTests = mapfunctionData;
    }

    public String getLocalErrorCode() {
        if (isPass()) {
            return null;
        }
        return this.itemTestManager.getFirstFail().getString(AllKeyWord.LOCAL_ERROR_CODE);
    }

    public String getErrorDes() {
        if (isPass()) {
            return null;
        }
        return this.itemTestManager.getFirstFail().getString(AllKeyWord.ERROR_DES);
    }
    
    public String getErrorLocalDes() {
        if (isPass()) {
            return null;
        }
        return this.itemTestManager.getFirstFail().getString(AllKeyWord.LOCAL_ERROR_DES);
    }

    public String getCusErrorCode() {
        if (isPass()) {
            return null;
        }
        return this.itemTestManager.getFirstFail().getString(AllKeyWord.ERROR_CODE);
    }
}
