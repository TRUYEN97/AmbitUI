/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.ManagerUI.UIStatus.Elemants;

import Model.DataModeTest.DataBoxs.DataBox;
import Model.ManagerUI.UIStatus.UiStatus;
import java.util.HashMap;

/**
 *
 * @author 21AK22
 */
public class UIData {

    private final HashMap<String, DataBox> dataBoxs;
    private final UiStatus uiStatus;

    public UIData(UiStatus uiStatus) {
        this.dataBoxs = new HashMap<>();
        this.uiStatus = uiStatus;
    }

    public DataBox getDataBox(String itemName) {
        DataBox dataBox = this.dataBoxs.get(itemName);
        if (dataBox == null) {
            dataBox = new DataBox(this.uiStatus, itemName);
            this.dataBoxs.put(itemName, dataBox);
        }
        return dataBox;
    }

}
