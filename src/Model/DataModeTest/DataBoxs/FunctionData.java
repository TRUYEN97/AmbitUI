/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataModeTest.DataBoxs;

import Model.DataModeTest.ErrorLog;
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
    private final String itemFunction;
    private final TimeS timeS;
    private final List<ItemTestData> itemTests;
    private ErrorFunctionTest errorFunc;
    private boolean testing;
    private boolean isPass;
    private boolean isMultistacking;
    private String resultTest;
    private Double testTime;

    public FunctionData(String subUIName, String itemFunction) {
        this.loger = new MyLoger();
        this.timeS = new TimeS();
        this.itemTests = new ArrayList<>();
        this.itemFunction = itemFunction;
        this.testing = false;
        this.isPass = false;
        String localFunctionsFile = Setting.getInstance().getFunctionsLocalLog();
        String fileLogName = String.format("%s\\%s\\%s.txt",
                localFunctionsFile, subUIName, itemFunction);
        if (!this.loger.begin(new File(fileLogName), true, true)) {
            String mess = "can't delete local function log file of " + itemFunction;
            ErrorLog.addError(mess);
            JOptionPane.showMessageDialog(null, mess);
        }
    }

    public String getItemFunction() {
        return itemFunction;
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
            this.resultTest = new String();
            return;
        }
        this.resultTest = resultTest;
    }

    public void setMultistacking() {
        this.isMultistacking = true;
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

    public void start() {
        this.timeS.start(0);
        this.testing = true;
    }

    public double getRunTime() {
        if (testTime != null) {
            return testTime;
        }
        return this.timeS.getTime();
    }

    public void end() {
        this.loger.close();
        this.testing = false;
        this.testTime = getRunTime();
        if (this.itemTests.isEmpty()) {
            ItemTestData itemTest = new ItemTestData(itemFunction);
            itemTest.setValue(getResultTest());
            itemTest.setIsPass(isPass);
            addItemtest(itemTest);
        }
    }

    public boolean isMultiStacking() {
        return isMultistacking;
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

}
