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
import java.util.List;

/**
 *
 * @author 21AK22
 */
public class UiData {

    private final List<DataBox> dataBoxs;
    private final UiStatus uiStatus;
    private final UISignal signal;
    private String message;
    private boolean isPass;

    public UiData(UiStatus uiStatus) {
        this.dataBoxs = new ArrayList<>();
        this.uiStatus = uiStatus;
        this.signal = new UISignal();
    }

    public List<DataBox> getDataBoxs() {
        return dataBoxs;
    }

    public void clear() {
        dataBoxs.clear();
    }

    public DataBox createDataBox(String itemName) {
        DataBox dataBox = new DataBox(uiStatus, itemName);
        this.dataBoxs.add(dataBox);
        return dataBox;
    }

    public DataBox getDataBox(String itemName) {
        for (DataBox dataBox : dataBoxs) {
            if (dataBox.getItemFunction().equals(itemName)) {
                return dataBox;
            }
        }
        return null;
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
        return isPass;
    }

    public void setPass(boolean isPass) {
        this.isPass = isPass;
    }
    
    public String getMassage() {
        if (this.message != null) {
            return message;
        }
        for (DataBox dataBox : dataBoxs) {
            if (!dataBox.isPass()) {
                return String.format("FAILED\r\n%s", dataBox.getItemFunction());
            }
        }
        return "PASS";
    }

}
