/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataTest.DataBoxs;

import Model.AllKeyWord;
import Model.DataTest.ErrorLog;
import MyLoger.MyLoger;
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

    public FunctionData() {
        this.loger = new MyLoger();
        this.itemTests = new ArrayList<>();
    }

    public String getItemFunction() {
        return this.getFirstItem().getItemTestName();
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
        getFirstItem().setResult(resultTest);
    }

    private ItemTestData getFirstItem() {
        return this.itemTests.get(0);
    }

    public void addLog(Object str) {
        this.loger.addLog(str);
    }

    public String getResultTest() {
        return getFirstItem().getResultTest();
    }

    public boolean isTesting() {
        for (ItemTestData itemTest : itemTests) {
            if (itemTest.isTest()) {
                return true;
            }
        }
        return false;
    }

    public void start(String logPath, String funcName) {
        if (!this.loger.begin(new File(logPath), true, true)) {
            String mess = "can't delete local function log file of " + getItemFunction();
            ErrorLog.addError(mess);
            JOptionPane.showMessageDialog(null, mess);
        }
        addLog(startFunction(funcName));
    }

    public double getRunTime() {
        return getFirstItem().getRunTime();
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
        return getFirstItem().isPass();
    }

    public void setStatus(boolean stt) {
        this.getFirstItem().setPass(stt);
    }

    public String createDefaultResult() {
        return isPass() ? "PASS" : "FAILED";
    }

    private String startFunction(String funcName) {
        return String.format("Item[%s] - Function[%s]",
                this.getItemFunction(), funcName);
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

    public String getErrorCode() {
        if (getFirstItem() == null) {
            return null;
        }
        return getFirstItem().getString(AllKeyWord.ERROR_CODE);
    }

    public String getCusErrorCode() {
        if (getFirstItem() == null && (getFirstItem().isTest() || getFirstItem().isPass())) {
            return null;
        }
        return getFirstItem().getString(AllKeyWord.LOCAL_ERROR_CODE);
    }
}
