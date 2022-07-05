/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataTest.FunctionData;

import Model.AllKeyWord;
import Model.ErrorLog;
import MyLoger.MyLoger;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class FunctionData {

    private final MyLoger loger;
    private final Map<String, ItemTestData> itemTests;
    private ItemTestData thisItem;
    private Map<String, ItemTestData> finalItemTests;

    public FunctionData() {
        this.loger = new MyLoger();
        this.itemTests = new HashMap<>();
    }

    public Map<String, ItemTestData> getitemTests() {
        return itemTests;
    }

    public String getItemFunctionName() {
        return this.thisItem.getItemTestName();
    }

    public void addItemtest(ItemTestData itemTest) {
        itemTest.setLoger(this.loger);
        if (this.itemTests.isEmpty()) {
            this.thisItem = itemTest;
        }
        this.itemTests.put(itemTest.getItemTestName(), itemTest);
    }

    public ItemTestData getItemTest(String itemName) {
        if (this.itemTests.containsKey(itemName)) {
            return this.itemTests.get(itemName);
        }
        return null;
    }

    public void setResult(String resultTest) {
        if (resultTest == null) {
            return;
        }
        thisItem.setResult(resultTest);
    }

    public void addLog(Object str) {
        this.loger.addLog(str);
    }

    public String getResultTest() {
        return thisItem.getResultTest();
    }
    
    public String getStausTest() {
        return thisItem.getStatusTest();
    }

    public boolean isTesting() {
        return thisItem.isTest();
    }

    public void start(String logPath, String funcName) {
        if (!this.loger.begin(new File(logPath), true, true)) {
            String mess = "can't delete local function log file of ".concat(getItemFunctionName());
            ErrorLog.addError(mess);
            JOptionPane.showMessageDialog(null, mess);
        }
        addLog(String.format("ITEM[%s] - FUNCTION[%s]",
                this.getItemFunctionName(), funcName));
    }

    public double getRunTime() {
        return thisItem.getRunTime();
    }

    public void end() {
        this.addLog(String.format("TIME[%.3f s] - STATUS[%S]",
                this.getRunTime(), getStausTest()));
        this.loger.close();
        this.finalItemTests.putAll(itemTests);
    }

    public String getLog() {
        return this.loger.getLog();
    }

    public MyLoger getLoger() {
        return this.loger;
    }

    public boolean isPass() {
        return thisItem.isPass();
    }

    public void setStatus(boolean stt) {
        this.thisItem.setPass(stt);
    }
    
    public void setFail(String errorType) {
       this.thisItem.setFail(errorType);
    }

    public String getErrorCode() {
        if (thisItem == null) {
            return null;
        }
        return thisItem.getString(AllKeyWord.ERROR_CODE);
    }

    public String getCusErrorCode() {
        if (isTesting() || isPass()) {
            return null;
        }
        return thisItem.getString(AllKeyWord.LOCAL_ERROR_CODE);
    }

    public void setFinalMapItems(Map<String, ItemTestData> mapfunctionData) {
        this.finalItemTests = mapfunctionData;
    }
}
