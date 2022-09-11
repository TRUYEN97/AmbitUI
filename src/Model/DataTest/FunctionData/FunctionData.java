/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataTest.FunctionData;

import Model.DataSource.ModeTest.ErrorCode.ErrorCodeElement;
import Model.DataSource.ModeTest.FunctionConfig.FuncAllConfig;
import Model.DataSource.ModeTest.FunctionConfig.FunctionConfig;
import Model.DataSource.ModeTest.FunctionConfig.FunctionName;
import Model.DataSource.Setting.Setting;
import Model.DataSource.Tool.IgetTime;
import Model.DataTest.ProcessTest.ProcessData;
import Model.ErrorLog;
import Model.ManagerUI.UIStatus.UiStatus;
import MyLoger.MyLoger;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class FunctionData {

    private final MyLoger loger;
    private final FunctionName functionName;
    private final ProcessData processData;
    private final UiStatus uiStatus;
    private final Map<String, ItemTestData> itemTests;
    private final List<ItemTestData> listItemTests;
    private final ItemTestData thisItem;
    private final FunctionConfig functionConfig;
    private final IgetTime timer;

    public FunctionData(UiStatus uiStatus, FunctionConfig functionConfig) {
        this.loger = new MyLoger();
        this.itemTests = new HashMap<>();
        this.listItemTests = new ArrayList<>();
        this.uiStatus = uiStatus;
        this.processData = uiStatus.getProcessData();
        this.functionConfig = functionConfig;
        this.timer = uiStatus.getProcessData();
        this.functionName = functionConfig.getfFunctionName();
        this.thisItem = new ItemTestData(getAllConfig(functionName.getItemName()), loger, timer);
        addItemData(functionName.getItemName(), thisItem);
    }

    private void addItemData(String itemName, ItemTestData itemData) {
        this.itemTests.put(itemName, itemData);
        this.listItemTests.add(itemData);
    }

    private FuncAllConfig getAllConfig(String item) {
        return new FuncAllConfig(uiStatus, functionConfig, item);
    }

    public FunctionName getFunctionName() {
        return this.functionName;
    }

    public final void addItemtest(String itemTest) {
        if (itemTest == null || this.itemTests.containsKey(itemTest)) {
            return;
        }
        ItemTestData testData = new ItemTestData(getAllConfig(itemTest), loger, timer);
        addItemData(itemTest, testData);
    }

    public void setResult(String resultTest) {
        if (resultTest == null) {
            return;
        }
        this.thisItem.setResult(resultTest);
    }

    public void addLog(Object str) {
        try {
            this.loger.addLog(str);
        } catch (IOException ex) {
            ErrorLog.addError(this, ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }

    public void addLog(String key, Object str) {
        try {
            this.loger.addLog(key, str);
        } catch (IOException ex) {
            ErrorLog.addError(this, ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }

    public String getResultTest() {
        return thisItem.getResultTest();
    }

    public String getStatusTest() {
        return thisItem.getStatusTest();
    }

    public boolean isTesting() {
        return thisItem.isTest();
    }

    public boolean start() {
        try {
            this.loger.begin(new File(createLogPath()), true, true);
        } catch (IOException ex) {
            String mess = String.format("can't delete local function log file of %s - %s\r\n%s",
                    functionName.getItemName(), uiStatus.getInfo().getName(), ex.getLocalizedMessage());
            ErrorLog.addError(this, mess);
            JOptionPane.showMessageDialog(null, mess);
            return false;
        }
        addLog(String.format("ITEM[%s] - FUNCTION[%s]",
                this.functionName, functionName.getFunctionName()));
        return true;
    }

    private String createLogPath() {
        String settingPath = Setting.getInstance().getLocalLogPath();
        return String.format("%s/functions/%s/%s_%s.txt",
                settingPath, this.uiStatus.getName(),
                this.functionName.getFunctionName(), this.functionName.getItemName());
    }

    public double getRunTime() {
        return thisItem.getRunTime();
    }

    public void end() {
        this.addLog(String.format("TIME[%.3f s] - STATUS[%S]",
                this.getRunTime(), getStatusTest()));
        giveItemToProcessData();
    }

    public void closeLoger() {
        try {
            this.loger.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            ErrorLog.addError(this, ex.getLocalizedMessage());
        }
    }

    private void giveItemToProcessData() {
        this.processData.putAllItem(itemTests);
        for (ItemTestData itemTest : listItemTests) {
            if (!itemTest.isPass()) {
                this.processData.addFailItem(itemTest);
            }
        }
    }

    public String getLog() {
        return this.loger.getLog();
    }

    public MyLoger getLoger() {
        return this.loger;
    }

    public boolean isPass() {
        return getFisrtFailed() == null;
    }

    public void setStatus(boolean stt) {
        if (stt) {
            this.thisItem.setPass();
        } else {
            this.thisItem.setFail(ErrorCodeElement.SIMPLE);
        }
    }

    public void setFail(String errorType) {
        for (var itemTest : listItemTests) {
            if (itemTest.isTest()) {
                itemTest.setFail(errorType);
            }
        }
    }

    public String getLocalErrorCode() {
        if (isPass()) {
            return null;
        }
        return this.getFisrtFailed().getLocalErrorCode();
    }

    public String getErrorDes() {
        if (isPass()) {
            return null;
        }
        return this.getFisrtFailed().getErrorDes();
    }

    public String getLimitsErrorCode() {
        if (isPass()) {
            return null;
        }
        return this.getFisrtFailed().getLimitsErrorCode();
    }

    public ItemTestData getFisrtFailed() {
        for (ItemTestData itemTest : listItemTests) {
            if (itemTest != null && !itemTest.isPass()) {
                return itemTest;
            }
        }
        return null;
    }

    public ItemTestData getItemData(String itemName) {
        return this.itemTests.get(itemName);
    }
}
