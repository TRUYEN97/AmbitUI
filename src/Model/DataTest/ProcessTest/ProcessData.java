/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataTest.ProcessTest;

import Model.DataTest.UiInformartion;
import Model.AllKeyWord;
import Model.DataSource.DataWareHouse;
import Model.DataSource.ModeTest.FunctionConfig.FunctionName;
import Model.DataSource.Tool.IgetTime;
import Model.DataTest.FunctionData.FunctionData;
import Model.DataTest.FunctionData.ItemTestData;
import Model.ManagerUI.UIStatus.UiStatus;
import Time.TimeBase;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author 21AK22
 */
public class ProcessData implements IgetTime {

    private final List<FunctionData> listFunctionData;
    private final Map<FunctionName, FunctionData> mapFunctionData;
    private final Map<String, ItemTestData> mapItemTestData;
    private final List<ItemTestData> listItemTestData;
    private final List<ItemTestData> faidItems;
    private final UiInformartion informartion;
    private final DataWareHouse data;
    private final TimeBase timeBase;
    private final ProcessTestSignal signal;
    private final ProductData productData;
    private String message;
    private final IgetTime testTimer;

    public ProcessData(UiStatus uiStatus, IgetTime testTimer) {
        this.listFunctionData = new ArrayList<>();
        this.listItemTestData = new ArrayList<>();
        this.mapFunctionData = new HashMap<>();
        this.mapItemTestData = new HashMap<>();
        this.faidItems = new ArrayList<>();
        this.data = new DataWareHouse();
        this.signal = new ProcessTestSignal();
        this.productData = new ProductData();
        this.timeBase = new TimeBase(TimeBase.UTC);
        this.testTimer = testTimer;
        this.informartion = uiStatus.getInfo();
        this.message = "Ready";
    }

    public List<FunctionData> getDataBoxs() {
        return listFunctionData;
    }

    public List<ItemTestData> getListItemTestData() {
        return listItemTestData;
    }

    public JSONObject getItemData(String itemName, List<String> keys) {
        if (getItemTestData(itemName) == null) {
            return null;
        }
        return getItemTestData(itemName).getData(keys);
    }

    public ItemTestData getItemTestData(String itemName) {
        if (this.mapItemTestData.containsKey(itemName)) {
            return this.mapItemTestData.get(itemName);
        }
        return null;
    }

    public FunctionData getDataBox(int index) {
        if (index >= this.listFunctionData.size()) {
            return null;
        }
        return this.listFunctionData.get(index);
    }

    public FunctionData getDataBox(FunctionName item) {
        if (item == null) {
            return null;
        }
        return mapFunctionData.get(item);
    }

    public void setMessage(String message) {
        if (message == null || message.isBlank()) {
            return;
        }
        this.message = message;
    }

    public boolean isPass() {
        return getFirstFail() == null;
    }

    public ItemTestData getFirstFail() {
        if (this.faidItems.isEmpty()) {
            return null;
        }
        return this.faidItems.get(0);
    }

    public String getMassage() {
        return message;
    }

    public void setFinishTime() {
        this.data.put(AllKeyWord.FINISH_TIME, timeBase.getSimpleDateTime());
        this.data.put(AllKeyWord.FINISH_DAY, timeBase.getDate());
        if (testTimer != null) {
            this.data.put(AllKeyWord.CYCLE_TIME, String.format("%.3f", getRuntime()));
        }
        setEndTestStatus();
    }

    public void setFinishTimeFinal() {
        if (testTimer != null) {
            this.data.put(AllKeyWord.CYCLE_TIME_FINAL, String.format("%.3f", getRuntime()));
        }
        setEndTestStatus();
    }

    private void setEndTestStatus() {
        ItemTestData itemFailed = getFirstFail();
        if (itemFailed == null) {
            this.data.put(AllKeyWord.SFIS.STATUS, ItemTestData.PASS);
        } else {
            this.data.put(AllKeyWord.SFIS.STATUS, ItemTestData.FAIL);
            this.data.put(AllKeyWord.CONFIG.ERROR_CODE, itemFailed.getLimitsErrorCode());
            this.data.put(AllKeyWord.CONFIG.ERROR_DES, itemFailed.getErrorDes());
            this.data.put(AllKeyWord.SFIS.ERRORCODE, itemFailed.getLocalErrorCode());
            this.data.put(AllKeyWord.SFIS.ERRORDES, itemFailed.getLocalErrorDes());
        }
    }

    public void setStartTime() {
        resetFunctionsData();
        resetItemsData();
        this.message = null;
        this.data.put(AllKeyWord.START_TIME, timeBase.getSimpleDateTime());
        this.data.put(AllKeyWord.START_DAY, timeBase.getDate());
    }

    private void resetItemsData() {
        this.mapItemTestData.clear();
        this.listItemTestData.clear();
        this.faidItems.clear();
    }

    private void resetFunctionsData() {
        this.listFunctionData.clear();
        this.mapFunctionData.clear();
    }

    public ProcessTestSignal getSignal() {
        return signal;
    }

    public ProductData getProductData() {
        return productData;
    }

    public String getString(String key) {
        if (this.productData.getString(key) != null) {
            return this.productData.getString(key);
        }
        if (this.informartion.getString(key) != null) {
            return this.informartion.getString(key);
        }
        if (this.data.getString(key) != null) {
            return this.data.getString(key);
        }
        if (this.signal.getString(key) != null) {
            return this.signal.getString(key);
        }
        return null;
    }

    public void clearSignal() {
        this.data.clear();
        this.signal.clear();
        this.productData.clear();
    }

    public void setMode(String mode) {
        if (this.data == null) {
            return;
        }
        this.data.put(AllKeyWord.MODE, mode);
    }

    @Override
    public double getRuntime() {
        if (this.testTimer == null) {
            return 0;
        }
        return testTimer.getRuntime();
    }

    public void addFunctionData(FunctionData functionData) {
        if (functionData == null) {
            throw new NullPointerException("functionData == null");
        }
        FunctionName functionName = functionData.getFunctionName();
        if (functionName == null || this.mapFunctionData.containsKey(functionName)) {
            throw new NullPointerException("functionName == null");
        }
        if (this.mapFunctionData.containsKey(functionName)) {
            return;
        }
        this.listFunctionData.add(functionData);
        this.mapFunctionData.put(functionName, functionData);
    }

    public synchronized void putAllItem(Map<String, ItemTestData> itemTests, List<ItemTestData> listFunctionData) {
        this.mapItemTestData.putAll(itemTests);
        this.listItemTestData.addAll(listFunctionData);
    }

    public synchronized void addFailItem(ItemTestData itemTestData) {
        if (this.faidItems.contains(itemTestData)) {
            return;
        }
        this.faidItems.add(itemTestData);
    }

    public List<ItemTestData> getFaidItems() {
        return faidItems;
    }

    public void endTest() {
        if (isPass()) {
            setMessage(message == null ? "Finished!" : message);
            return;
        }
        StringBuilder faildItemName = new StringBuilder("Items\r\n");
        int i = 0;
        for (ItemTestData failItemData : getFaidItems()) {
            if (i++ == 5) {
                faildItemName.append("...").append("\r\n");
                break;
            }
            faildItemName.append("- ").append(failItemData.getItemName()).append(": ")
                    .append(failItemData.getLocalErrorCode()).append("\r\n");
        }
        setMessage(String.format("%s\"%s\"",
                faildItemName,
                message == null ? "..." : message));
    }

    public String getString(String key, String defaultValue) {
        String value = getString(key);
        return value == null ? defaultValue : value;
    }

}
