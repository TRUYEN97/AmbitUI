/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.ManagerUI.UIStatus.Elemants;

import Control.Functions.AbsFunction;
import Model.DataModeTest.DataBoxs.DataBox;
import Model.DataModeTest.DataBoxs.UISignal;
import Model.DataModeTest.InputData;
import Model.ManagerUI.UIStatus.UiStatus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author 21AK22
 */
public class UiData {

    private final List<DataBox> dataBoxs;
    private final UiStatus uiStatus;
    private final UISignal signal;
    private final HashMap<String, Object> productInfo;
    private InputData inputData;
    private String message;

    public UiData(UiStatus uiStatus) {
        this.dataBoxs = new ArrayList<>();
        this.productInfo = new HashMap<>();
        this.uiStatus = uiStatus;
        this.signal = new UISignal();
    }

    public List<DataBox> getDataBoxs() {
        return dataBoxs;
    }

    public void clear() {
        this.message = null;
        this.productInfo.clear();
        this.dataBoxs.clear();
    }

    public DataBox getDataBox(String itemName) {
        for (DataBox dataBox : dataBoxs) {
            if (dataBox.getItemFunction().equals(itemName)) {
                return dataBox;
            }
        }
        return createDataBox(itemName);
    }

    private DataBox createDataBox(String itemName) {
        DataBox dataBox = new DataBox(uiStatus.getName(), itemName);
        this.dataBoxs.add(dataBox);
        return dataBox;
    }

    public DataBox getDataBox(int index) {
        if (index >= this.dataBoxs.size()) {
            return null;
        }
        return this.dataBoxs.get(index);
    }

    public List<AbsFunction> getFunctionSelected() {
        return this.signal.getFunctionSelected();
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

    public DataBox getFirstFail() {
        for (DataBox dataBox : dataBoxs) {
            if (!dataBox.isPass()) {
                return dataBox;
            }
        }
        return null;
    }

    public String getMassage() {
        if (this.message != null) {
            return message;
        }
        DataBox firstFail = getFirstFail();
        if (firstFail == null) {
            return "PASS";
        }
        return String.format("Failed: %s", firstFail.getItemFunction());
    }

    public void putProductInfo(String key, Object data) {
        if (key == null || key.isBlank()) {
            return;
        }
        this.productInfo.put(key, data);
    }

    public Object getProductInfo(String key) {
        return this.productInfo.get(key);
    }

    public void setInput(InputData inputData) {
        this.inputData = inputData;
    }

    public InputData getInputData() {
        return inputData;
    }

}
