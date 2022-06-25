/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataTest.DataBoxs;

import Model.AllKeyWord;
import Model.DataTest.ErrorLog;
import Model.DataTest.InputData;
import Model.DataSource.DataWareHouse;
import Model.DataSource.ModeTest.FunctionConfig.FunctionName;
import Model.ManagerUI.UIStatus.UiStatus;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author 21AK22
 */
public class UiData {

    private final List<FunctionData> dataBoxs;
    private final UiStatus uiStatus;
    private final ProcessTestSignal signal;
    private final DataWareHouse productData;
    private InputData inputData;
    private String message;

    public UiData(UiStatus uiStatus) {
        this.dataBoxs = new ArrayList<>();
        this.signal = new ProcessTestSignal();
        this.productData = new DataWareHouse();
        this.uiStatus = uiStatus;
    }

    public List<FunctionData> getDataBoxs() {
        return dataBoxs;
    }

    public void clear() {
        this.message = null;
        this.signal.clear();
        mergeData();
        this.dataBoxs.clear();
    }

    private void mergeData() {
        this.productData.clear();
        if (inputData == null) {
            String mess = "Input = null, can not to test!";
            JOptionPane.showMessageDialog(null, mess);
            ErrorLog.addError(this, mess);
            System.exit(1);
        }
        for (String key : inputData.getkeySet()) {
            this.productData.put(key, inputData.getValue(key).toUpperCase());
        }
        this.productData.put(AllKeyWord.MODE, this.uiStatus.getModeTest().getModeType());
    }

    public FunctionData getFunctionData(String itemName) {
        for (FunctionData dataBox : dataBoxs) {
            if (dataBox.getItemFunction().equals(itemName)) {
                return dataBox;
            }
        }
        return null;
    }

    public FunctionData getDataBox(int index) {
        if (index >= this.dataBoxs.size()) {
            return null;
        }
        return this.dataBoxs.get(index);
    }

    public Object getSignal(String key) {
        return this.signal.get(key);
    }

    public void putToSignal(String key, Object data) {
        this.signal.put(key, data);
    }

    public List<FunctionName> getFunctionSelected() {
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

    public FunctionData getFirstFail() {
        for (FunctionData dataBox : dataBoxs) {
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
        return String.format("Failed: %s", getFirstFail().getItemFunction());
    }

    public void putProductInfo(String key, String data) {
        if (key == null || key.isBlank()) {
            return;
        }
        this.productData.put(key, data);
    }

    public String getProductInfo(String key) {
        if (key.equals(AllKeyWord.STATUS)) {
            return isPass() ? "PASS" : "FAIL";
        }
        return this.productData.getString(key);
    }

    public void setInput(InputData inputData) {
        this.inputData = inputData;
    }

    public void addFunctionData(FunctionData dataBox) {
        if (dataBox == null || this.dataBoxs.contains(dataBox)) {
            return;
        }
        this.dataBoxs.add(dataBox);
    }

}
