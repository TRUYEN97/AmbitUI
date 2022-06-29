/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataTest.ProcessTest;

import Model.DataSource.DataWareHouse;
import Model.DataTest.FunctionData.FunctionData;
import Model.DataTest.InputData;
import Model.ManagerUI.UIStatus.UiStatus;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 21AK22
 */
public class ProcessData {

    private final List<FunctionData> dataBoxs;
    private final UiStatus uiStatus;
    private final DataWareHouse data;
    private String message;

    public ProcessData(UiStatus uiStatus) {
        this.dataBoxs = new ArrayList<>();
        this.uiStatus = uiStatus;
        this.data = new DataWareHouse();
    }

    public List<FunctionData> getDataBoxs() {
        return dataBoxs;
    }

    public void clear() {
        this.message = null;
        this.dataBoxs.clear();
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


    public void addFunctionData(FunctionData dataBox) {
        if (dataBox == null || this.dataBoxs.contains(dataBox)) {
            return;
        }
        this.dataBoxs.add(dataBox);
    }

    public void setFinishTime(String simpleDateTime) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setStartTime(String simpleDateTime) {
        this.
    }

}
