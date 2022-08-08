/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataSource;

import com.alibaba.fastjson.JSONObject;
import java.util.List;

/**
 *
 * @author 21AK22
 */
public abstract class AbsElementInfo {

    protected final DataWareHouse warehouse;

    protected AbsElementInfo(List<String> keys, JSONObject base, JSONObject config) {
        this.warehouse = new DataWareHouse();
        setSetting(keys, base);
        this.warehouse.putAll(config);
    }

    private void setSetting(List<String> keys, JSONObject data) {
        if (data == null) {
            return;
        }
        if (keys == null) {
            this.warehouse.putAll(data);
            return;
        }
        for (String key : data.keySet()) {
            if (keys.contains(key)) {
                this.warehouse.put(key, data.get(key));
            }
        }
    }

}
