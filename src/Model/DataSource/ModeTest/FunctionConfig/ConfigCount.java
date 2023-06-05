/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataSource.ModeTest.FunctionConfig;

import Model.AllKeyWord;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class ConfigCount {

    Map<String, Integer> itemCount;

    public ConfigCount() {
        this.itemCount = new HashMap<>();
    }

    public String getItemCountName(String itemName, Integer point) {
        if (itemName.matches(".+_[0-9]+$")) {
            if (!this.itemCount.containsKey(itemName)) {
                setBaseCount(itemName);
            }
            itemName = itemName.substring(0, itemName.lastIndexOf("_"));
        }
        Integer count = this.getItemCount(itemName, point);
        addOneCount(itemName);
        return count == null ? itemName : String.format("%s_%s", itemName, count);
    }

    private Integer getItemCount(String itemName, Integer point) {
        Integer count = this.itemCount.get(itemName);
        if (count == null) {
            count = point;
        } else {
            count = point != null && point > count ? point : count;
        }
        this.itemCount.put(itemName, count);
        return count;
    }

    private void setBaseCount(String itemName) throws NumberFormatException {
        String baseItem = itemName.substring(0, itemName.lastIndexOf("_"));
        String num = itemName.substring(itemName.lastIndexOf("_") + 1);
        this.itemCount.put(baseItem, Integer.valueOf(num));
    }

    private void addOneCount(String baseItem) {
        Integer count = this.itemCount.get(baseItem);
        if (count == null) {
            this.itemCount.put(baseItem, 0);
        } else {
            this.itemCount.put(baseItem, ++count);
        }
    }

}
