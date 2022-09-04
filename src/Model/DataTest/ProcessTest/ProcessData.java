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
public class ProcessData implements IgetTime{

    private final List<FunctionData> listFunctionData;
    private final Map<FunctionName, FunctionData> mapFunctionData;
    private final Map<String, ItemTestData> mapItemTestData;
    private final Map<String, ItemTestData> faidItems;
    private final UiInformartion informartion;
    private final DataWareHouse data;
    private final TimeBase timeBase;
    private final ProcessTestSignal signal;
    private final ProductData productData;
    private final UiStatus uiStatus;
    private String message;
    private final IgetTime testTimer;

    public ProcessData( UiStatus uiStatus, IgetTime testTimer) {
        this.listFunctionData = new ArrayList<>();
        this.mapItemTestData = new HashMap<>();
        this.faidItems = new HashMap<>();
        this.mapFunctionData = new HashMap<>();
        this.data = new DataWareHouse();
        this.signal = new ProcessTestSignal();
        this.productData = new ProductData();
        this.timeBase = new TimeBase();
        this.testTimer = testTimer;
        this.uiStatus = uiStatus;
        this.informartion = uiStatus.getInfo();
        this.message = "Ready";
    }

    public List<FunctionData> getDataBoxs() {
        return listFunctionData;
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

    public FunctionData getFirstFail() {
        for (FunctionData dataBox : listFunctionData) {
            if (!dataBox.isPass() && !dataBox.isTesting()) {
                return dataBox;
            }
        }
        return null;
    }

    public String getMassage() {
        if (this.uiStatus.isTesting() || message != null) {
            return message;
        }
        if (isPass()) {
            return message = "Done!";
        }
        return message = String.format("Failed: %s", getFirstFail().getFunctionName());
    }

    public void setFinishTime() {
        this.data.put(AllKeyWord.FINISH_TIME, timeBase.getSimpleDateTime());
        this.data.put(AllKeyWord.FINISH_DAY, timeBase.getDate());
        if (testTimer != null) {
            this.data.put(AllKeyWord.CYCLE_TIME, String.format("%.3f", getRuntime() / 1000));
        }
        FunctionData testData = getFirstFail();
        if (testData == null) {
            this.data.put(AllKeyWord.STATUS, ItemTestData.PASS);
        } else {
            this.data.put(AllKeyWord.STATUS, ItemTestData.FAIL);
            this.data.put(AllKeyWord.ERROR_CODE, testData.getLocalErrorCode());
            this.data.put(AllKeyWord.ERROR_DES, testData.getErrorDes());
        }
    }

    public void setStartTime() {
        reset();
        this.data.put(AllKeyWord.START_TIME, timeBase.getSimpleDateTime());
        this.data.put(AllKeyWord.START_DAY, timeBase.getDate());
    }

    private void reset() {
        this.message = null;
        this.listFunctionData.clear();
        this.mapItemTestData.clear();
        this.faidItems.clear();
        this.mapFunctionData.clear();
    }

    public ProcessTestSignal getSignal() {
        return signal;
    }

    public ProductData getProductData() {
        return productData;
    }

    public String getString(String key) {
        if (this.informartion.getString(key) != null) {
            return this.informartion.getString(key);
        }
        if (this.data.getString(key) != null) {
            return this.data.getString(key);
        }
        if (this.productData.getString(key) != null) {
            return this.productData.getString(key);
        }
        if (this.signal.getString(key) != null) {
            return this.signal.getString(key);
        }
        return null;
    }

    public void clearSignal() {
        this.data.clear();
        this.signal.clear();
        this.signal.disConnectAll();
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
        if (functionData == null || this.listFunctionData.contains(functionData)) {
            return;
        }
        functionData.setFinalMapItems(mapItemTestData);
        this.listFunctionData.add(functionData);
        this.mapFunctionData.put(functionData.getFunctionName(), functionData);
    }
}
