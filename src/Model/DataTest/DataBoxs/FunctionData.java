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
    private final List<ItemTestData> itemTests;
    private final String itemName;
    private final String funcName;

    public FunctionData(String itemName, String funcName) {
        this.loger = new MyLoger();
        this.itemTests = new ArrayList<>();
        this.itemName = itemName;
        this.funcName = funcName;
    }

    public String getItemFunction() {
        return this.itemName;
    }

    public boolean addItemtest(ItemTestData itemTest) {
        itemTest.setLoger(this.loger);
        return this.itemTests.add(itemTest);
    }

    public ItemTestData getItemTest(String itemName) {
        for (ItemTestData itemTest : itemTests) {
            if (itemTest.getItemTestName().equals(itemName)) {
                return itemTest;
            }
        }
        return null;
    }

    public List<ItemTestData> getListItemTest() {
        return new ArrayList<>(itemTests);
    }

    public void setResult(String resultTest) {
        if (resultTest == null) {
            return;
        }
        getItemFirst().setResult(resultTest);
    }

    private ItemTestData getItemFirst() {
        return this.itemTests.get(0);
    }

    public void addLog(Object str) {
        this.loger.addLog(str);
    }

    public String getResultTest() {
        return getItemFirst().getResultTest();
    }

    public boolean isTesting() {
        for (ItemTestData itemTest : itemTests) {
            if (itemTest.isTest()) {
                return true;
            }
        }
        return false;
    }

    public void start(String LogPath) {
        String fileLogName = String.format("%s%s%s_%s.txt",
                LogPath, File.separator, getItemFunction(), getFunctionName());
        if (!this.loger.begin(new File(fileLogName), true, true)) {
            String mess = "can't delete local function log file of " + getItemFunction();
            ErrorLog.addError(mess);
            JOptionPane.showMessageDialog(null, mess);
        }
        addLog(startFunction());
    }

    public String getFunctionName() {
        return this.funcName;
    }

    public double getRunTime() {
        return getItemFirst().getRunTime();
    }

    public void end() {
        this.addLog(endFunction());
        this.loger.close();
    }

    public String getLog() {
        return this.loger.getLog();
    }

    public MyLoger getLoger() {
        return this.loger;
    }

    public boolean isPass() {
        return getItemFirst().isPass();
    }

    public void setStatus(boolean stt) {
        this.getItemFirst().setPass(stt);
    }

    public String createDefaultResult() {
        return isPass() ? "PASS" : "FAILED";
    }

    private String startFunction() {
        return String.format("Item[%s] - Function[%s]",
                this.getItemFunction(), this.getFunctionName());
    }

    private String endFunction() {
        return String.format("Time[%.3f s] - Status[%s]",
                this.getRunTime(), createDefaultResult());
    }

    public String getStaus() {
        if (isTesting()) {
            return "Testing";
        }
        return createDefaultResult();
    }
}
