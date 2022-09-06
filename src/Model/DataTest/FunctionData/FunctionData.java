/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataTest.FunctionData;

import Model.AllKeyWord;
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

    private void addItemData( String itemName, ItemTestData itemData) {
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
        this.loger.addLog(str);
    }

    public void addLog(String key, Object str) {
        this.loger.addLog(key, str);
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

    public void start() {
        if (!this.loger.begin(new File(createLogPath()), true, true)) {
            String mess = String.format("can't delete local function log file of %s - %s",
                    functionName.getItemName(), uiStatus.getInfo().getName());
            ErrorLog.addError(this, mess);
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
        return thisItem.getRunTime();
    }

    public void end() {
        this.addLog(String.format("TIME[%.3f s] - STATUS[%S]",
                this.getRunTime(), getStatusTest()));
        this.loger.close();
        this.processData.putAllItem(itemTests);
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
        return this.getFisrtFailed().getString(AllKeyWord.LOCAL_ERROR_CODE);
    }

    public String getErrorDes() {
        if (isPass()) {
            return null;
        }
        return this.getFisrtFailed().getString(AllKeyWord.ERROR_DES);
    }

    public String getErrorLocalDes() {
        if (isPass()) {
            return null;
        }
        return this.getFisrtFailed().getString(AllKeyWord.LOCAL_ERROR_DES);
    }

    public String getCusErrorCode() {
        if (isPass()) {
            return null;
        }
        return this.getFisrtFailed().getString(AllKeyWord.ERROR_CODE);
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
