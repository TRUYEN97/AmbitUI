/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataSource.Setting;

import com.alibaba.fastjson.JSONObject;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class ModeInfo extends AbsSetting {

    private final String name;

    public ModeInfo(JSONObject data) {
        super();
        JSONObject result = new JSONObject();
        setBaseSetting(result);
        setModeSetting(result, data);
        setData(result);
        this.name = getName(data);
    }

    private void setBaseSetting(JSONObject result) {
        JSONObject data = Setting.getInstance().toJson();
        for (String key : KeyWord.MODE_KEY) {
            result.put(key, data.get(key));
        }
    }

    private void setModeSetting(JSONObject result, JSONObject data) {
        for (String key : data.keySet()) {
            if (KeyWord.MODE_KEY.contains(key)) {
                result.put(key, data.get(key));
            }
        }
    }

    public String getModeType() {
        return this.warehouse.getString(KeyWord.TYPE_MODE);
    }

    public String getModeName() {
        return name;
    }

    public List<String> getIniFunc() {
        return this.warehouse.cvtArrays2List(this.warehouse.getJSONArray(KeyWord.INIT_FUNC));
    }

    private String getName(JSONObject data) {
        if (data.containsKey(KeyWord.NAME)) {
            return data.getString(KeyWord.NAME);
        }
        JOptionPane.showMessageDialog(null, "Mode setting no have name!");
        return null;
    }
}
