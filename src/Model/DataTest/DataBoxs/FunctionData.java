/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataTest.DataBoxs;

import Model.DataTest.ErrorLog;
import MyLoger.MyLoger;
import Time.WaitTime.Class.TimeS;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class FunctionData {

    private final MyLoger loger;
    private final TimeS timeS;
    private final List<ItemTestData> itemTests;
    private ErrorFunctionTest errorFunc;
    private boolean testing;
    private boolean isPass;
    private String resultTest;
    private Double testTime;
    private final String itemName;
    private final String funcName;

    public FunctionData(String itemName, String funcName) {
        this.loger = new MyLoger();
        this.timeS = new TimeS();
        this.itemTests = new ArrayList<>();
        this.itemName = itemName;
        this.funcName = funcName;
        this.testing = false;
        this.isPass = false;
    }

    public String getItemFunction() {
        return this.itemName;
    }

    public boolean setErrorFunc(ErrorFunctionTest errorFunc) {
        if (errorFunc == null) {
            return false;
        }
        this.errorFunc = errorFunc;
        return true;
    }

    public ErrorFunctionTest getError() {
        return errorFunc;
    }

    public boolean addItemtest(ItemTestData itemTest) {
        return this.itemTests.add(itemTest);
    }

    public List<ItemTestData> getListItemTest() {
        return new ArrayList<>(itemTests);
    }

    public void setResult(String resultTest) {
        if (resultTest == null) {
            return;
        }
        this.resultTest = resultTest;
    }

    public void addLog(Object str) {
        this.loger.addLog(str);
    }

    public String getResultTest() {
        return resultTest;
    }

    public boolean isTesting() {
        return testing;
    }

    public void start(String LogPath) {
        String fileLogName = String.format("%s%s%s_%s.txt",
                LogPath, File.separator, getItemFunction(), getFunctionName());
        if (!this.loger.begin(new File(fileLogName), true, true)) {
            String mess = "can't delete local function log file of " + getItemFunction();
            ErrorLog.addError(mess);
            JOptionPane.showMessageDialog(null, mess);
        }
        this.timeS.start(0);
        this.testing = true;
        addLog(startFunction());
    }

    public String getFunctionName() {
        return this.funcName;
    }

    public double getRunTime() {
        if (testTime != null) {
            return testTime;
        }
        return this.timeS.getTime();
    }

    public void end() {
        this.testing = false;
        this.testTime = getRunTime();
        if (this.itemTests.isEmpty()) {
            createDefaultItem();
        }
        this.addLog("Result: " + getResultTest());
        this.addLog(endFunction());
        this.loger.close();
    }

    private void createDefaultItem() {
        ItemTestData itemTest = new ItemTestData(getFunctionName());
        itemTest.setValue(getResultTest());
        itemTest.setIsPass(isPass);
        addItemtest(itemTest);
    }

    public String getLog() {
        return this.loger.getLog();
    }

    public MyLoger getLoger() {
        return this.loger;
    }

    public boolean isPass() {
        return isPass;
    }

    public void setStatus(boolean stt) {
        this.isPass = stt;
    }

    public String createDefaultResult() {
        return isPass ? "PASS" : "FAILED";
    }

    private String startFunction() {
        return String.format("Item[%s] - Function[%s]",
                this.getItemFunction(), this.getFunctionName());
    }

    private String endFunction() {
        return String.format("Time[%.3f s] - Status[%s]",
                this.testTime, createDefaultResult());
    }

    public String getStaus() {
        if (testing) {
            return "Testing";
        }
        return createDefaultResult();
    }
}
