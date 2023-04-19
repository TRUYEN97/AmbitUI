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

/**
 *
 * @author Administrator
 */
public class FunctionData {

    private final MyLoger loger;
    private final ItemTestData.TYPE type;
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
        this.type = functionConfig.getType();
        this.loger.setSaveMemory(uiStatus.getModeTest().isSaveMemory());
        this.itemTests = new HashMap<>();
        this.listItemTests = new ArrayList<>();
        this.uiStatus = uiStatus;
        this.processData = uiStatus.getProcessData();
        this.functionConfig = functionConfig;
        this.timer = uiStatus.getProcessData();
        this.functionName = functionConfig.getfFunctionName();
        this.thisItem = new ItemTestData(getAllConfig(functionName.getItemName()), loger, timer, this.type);
        addItemData(functionName.getItemName(), thisItem);
    }

    public List<ItemTestData> getListItemTests() {
        return listItemTests;
    }

    private void addItemData(String itemName, ItemTestData itemData) {
        if (this.itemTests.containsKey(itemName)) {
            return;
        }
        this.itemTests.put(itemName, itemData);
        this.listItemTests.add(itemData);
    }

    private FuncAllConfig getAllConfig(String item) {
        return new FuncAllConfig(uiStatus, functionConfig, item);
    }

    public FunctionName getFunctionName() {
        return this.functionName;
    }

    public final synchronized void addItemtest(String itemTest) {
        if (itemTest == null || this.itemTests.containsKey(itemTest)) {
            return;
        }
        ItemTestData testData = new ItemTestData(getAllConfig(itemTest), loger, timer, this.type);
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

    public void addLog(String key, String str, Object... params̉) {
        try {
            this.loger.addLog(key, str, params̉);
        } catch (Exception ex) {
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
        this.loger.setFile(new File(createLogPath()));
        this.loger.clear();
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

    private void giveItemToProcessData() {
        this.processData.putAllItem(itemTests, listItemTests);
        for (ItemTestData itemTest : listItemTests) {
            if (!itemTest.isPass()) {
                this.processData.addFailItem(itemTest);
            }
        }
    }

    public String getLog() {
        try {
            return this.loger.getLog();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public MyLoger getLoger() {
        return this.loger;
    }

    public boolean isPass() {
        return getFisrtFailed() == null;
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

    public boolean isWaiting() {
        return thisItem.isWait();
    }

    public boolean isDone() {
        return thisItem.isDone();
    }
}
