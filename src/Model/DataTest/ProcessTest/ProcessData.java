/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataTest.ProcessTest;

import Model.AllKeyWord;
import Model.DataSource.DataWareHouse;
import Model.DataTest.FunctionData.FunctionData;
import Model.DataTest.FunctionData.ItemTestData;
import Model.DataTest.ProcessTestSignal;
import Model.DataTest.ProductData;
import MyLoger.MyLoger;
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

    private final List<FunctionData> listFunctionData;
    private final Map<String, ItemTestData> mapFunctionData;
    private final DataWareHouse data;
    private final TimeBase timeBase;
    private final ProcessTestSignal signal;
    private final ProductData productData;
    private String message;

    public ProcessData() {
        this.listFunctionData = new ArrayList<>();
        this.mapFunctionData = new HashMap<>();
        this.data = new DataWareHouse();
        this.signal = new ProcessTestSignal();
        this.productData = new ProductData();
        this.timeBase = new TimeBase();
    }

    public List<FunctionData> getDataBoxs() {
        return listFunctionData;
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
        if (this.mapFunctionData.containsKey(itemName)) {
            return this.mapFunctionData.get(itemName);
        }
        return null;
    }

    public FunctionData getDataBox(int index) {
        if (index >= this.listFunctionData.size()) {
            return null;
        }
        return this.listFunctionData.get(index);
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
        if (this.message != null) {
            return message;
        }
        if (isPass()) {
            return "PASS";
        }
        return String.format("Failed: %s", getFirstFail().getItemFunctionName());
    }

    public void addFunctionData(FunctionData functionData) {
        if (functionData == null || this.listFunctionData.contains(functionData)) {
            return;
        }
        functionData.setFinalMapItems(mapFunctionData);
        this.listFunctionData.add(functionData);
    }

    public void setFinishTime() {
        this.data.put(AllKeyWord.FINISH_TIME, timeBase.getSimpleDateTime());
    }

    public void setStartTime() {
        reset();
        this.data.put(AllKeyWord.START_TIME, timeBase.getSimpleDateTime());
    }

    public void reset() {
        this.message = null;
        this.listFunctionData.clear();
        this.mapFunctionData.clear();
        this.data.clear();
    }

    public ProcessTestSignal getSignal() {
        return signal;
    }

    public ProductData getProductData() {
        return productData;
    }
}
