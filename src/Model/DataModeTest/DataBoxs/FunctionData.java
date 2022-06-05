/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataModeTest.DataBoxs;

import Model.DataModeTest.ErrorLog;
import Model.DataSource.FunctionConfig.FunctionConfig;
import Model.DataSource.FunctionConfig.FunctionElement;
import Model.DataSource.Setting.Setting;
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
    private FunctionElement funcConfig;
    private boolean testing;
    private boolean isPass;
    private boolean isMultistacking;
    private String resultTest;
    private Double testTime;

    public FunctionData() {
        this.loger = new MyLoger();
        this.timeS = new TimeS();
        this.itemTests = new ArrayList<>();
        this.testing = false;
        this.isPass = false;
    }

    public void setFuncconfig(FunctionElement funcConfig) {
        this.funcConfig = funcConfig;
    }

    public String getItemFunction() {
        return this.funcConfig.getItemName();
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
        if (testing) {
            return funcConfig.isMutiTasking() ? "multitasking" : "Testing";
        }
        if (resultTest == null) {
            return createDefaultResult();
        }
        return resultTest;
    }

    public boolean isTesting() {
        return testing;
    }

    public void start(String LogPath) {
        String fileLogName = String.format("%s%s%s_%s.txt",
                LogPath,File.separator, getItemFunction(), getFunctionName());
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
        return this.funcConfig.getFunctionName();
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

    public void setPass() {
        this.isPass = true;
    }

    public void setFail() {
        this.isPass = false;
    }

    private String startFunction() {
        return String.format("Item[%s]-Function[%s]",
                this.getItemFunction(), this.getFunctionName());
    }

    private String createDefaultResult() {
        return isPass ? "PASS" : "FAILED";
    }

    private String endFunction() {
        return String.format("Time[%.3f s]---Result[%s]",
                this.testTime, createDefaultResult());
    }
}
