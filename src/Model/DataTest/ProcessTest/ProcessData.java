/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataTest.ProcessTest;

import Model.AllKeyWord;
import Model.DataSource.DataWareHouse;
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
public class ProcessData {

    private final List<FunctionData> functionData;
    private final Map<String, ItemTestData> mapfunctionData;
    private final UiStatus uiStatus;
    private final DataWareHouse data;
    private final TimeBase timeBase;
    private String message;

    public ProcessData(UiStatus uiStatus) {
        this.functionData = new ArrayList<>();
        this.mapfunctionData = new HashMap<>();
        this.uiStatus = uiStatus;
        this.data = new DataWareHouse();
        this.timeBase = new TimeBase();
    }

    public List<FunctionData> getDataBoxs() {
        return functionData;
    }

    public JSONObject getBaseData() {
        return this.data.toJson();
    }

    public JSONObject getItemData(String itemName, List<String> keys) {
        if (getItemTestData(itemName) == null) {
            return null;
        }
        return getItemTestData(itemName).getData(keys);
    }

    public ItemTestData getItemTestData(String itemName) {
        if (this.mapfunctionData.containsKey(itemName)) {
            return this.mapfunctionData.get(itemName);
        }
        return null;
    }

    public FunctionData getDataBox(int index) {
        if (index >= this.functionData.size()) {
            return null;
        }
        return this.functionData.get(index);
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
        for (FunctionData dataBox : functionData) {
            if (!dataBox.isPass() && !dataBox.isTesting()) {
                return dataBox;
            }
        }
        return null;
    }

    public String getMassage() {
        if (this.message != null) {
            return message;
        }
        if (isPass()) {
            return "PASS";
        }
        return String.format("Failed: %s", getFirstFail().getItemFunctionName());
    }

    public void addFunctionData(FunctionData functionData) {
        if (functionData == null || this.functionData.contains(functionData)) {
            return;
        }
        functionData.setFinalMapItems(mapfunctionData);
        this.functionData.add(functionData);
    }

    public void setFinishTime() {
        this.data.put(AllKeyWord.FINISH_TIME, timeBase.getSimpleDateTime());
    }

    public void setStartTime() {
        reset();
        this.data.put(AllKeyWord.START_TIME, timeBase.getSimpleDateTime());
    }

    private void reset() {
        this.message = null;
        this.functionData.clear();
    }
}
