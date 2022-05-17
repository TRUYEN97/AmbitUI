/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.ManagerUI.UIStatus.Elemants;

import Model.DataModeTest.DataBoxs.DataBox;
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

    public UiData(UiStatus uiStatus) {
        this.dataBoxs = new ArrayList<>();
        this.uiStatus = uiStatus;
    }

    public List<DataBox> getDataBoxs() {
        return dataBoxs;
    }

    public void clear() {
        for (var dataBox : dataBoxs) {
            dataBox.remove();
        }
        dataBoxs.clear();
    }

    public DataBox createDataBox(String itemName) {
        DataBox dataBox = new DataBox(uiStatus, itemName);
        this.dataBoxs.add(dataBox);
        return dataBox;
    }

    public DataBox getDataBox(String itemName) {
        for (DataBox dataBox : dataBoxs) {
            if (dataBox.getItemName().equals(itemName)) {
                return dataBox;
            }
        }
        return null;
    }

}
