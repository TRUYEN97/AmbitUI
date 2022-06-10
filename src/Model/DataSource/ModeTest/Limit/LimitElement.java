/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataSource.ModeTest.Limit;

import Model.DataSource.AbsElementInfo;
import Model.DataSource.DataWareHouse;
import com.alibaba.fastjson.JSONObject;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Administrator
 */
public class LimitElement extends AbsElementInfo {

    private final String itemName;

    LimitElement(String itemName, JSONObject base, JSONObject element) {
        super(KeyWord.KEYS, base, base);
        this.itemName = itemName;
    }

    public String getModeName() {
        return itemName;
    }

    public Set<String> getListItem() {
        return warehouse.toJson().keySet();
    }

    public String getString(String key, String defaultValue) {
        String value = warehouse.getString(key);
        return value == null ? defaultValue : value;
    }

    public String getString(String key) {
        return warehouse.getString(key);
    }
}
