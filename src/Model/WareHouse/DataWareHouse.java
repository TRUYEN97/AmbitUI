/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.WareHouse;

import Model.DataSource.Setting.KeyWord;
import Control.Message;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class DataWareHouse {

    private JSONObject coreData = null;

    public DataWareHouse() {
        this.coreData = new JSONObject();
    }

    protected boolean setData(JSONObject data) {
        if (data != null && !data.isEmpty()) {
            this.coreData = data;
            return true;
        }
        return false;
    }

    public boolean setData(Map data) {
        if (data != null && !data.isEmpty()) {
            this.coreData = new JSONObject(data);
            return true;
        }
        return false;
    }

    public List<String> cvtArrays2List(JSONArray array) {
        List<String> result = new ArrayList<>();
        for (Object object : array) {
            if (object != null) {
                result.add(object.toString());
            }
        }
        return result;
    }

    public String getString(String key) {
        return getString(coreData, key);
    }

    public String getString(JSONObject json, String key) {
        if (json.containsKey(key)) {
            return json.getString(key);
        }
        return null;
    }

    public String[] getArrays(String key) {
        return getArrays(coreData, key);
    }

    public String[] getArrays(JSONObject json, String key) {
        if (json.containsKey(key)) {
            String[] arr = json.getString(key).split(",");
            String[] newArr = new String[arr.length];
            for (int index = 0; index < arr.length; index++) {
                newArr[index] = arr[index].trim();
            }
            return newArr;
        }
        return null;
    }

    public Integer getIntege(JSONObject json, String key, int radix) {
        try {
            return Integer.parseInt(getString(json, key), radix);
        } catch (Exception e) {
            Message.WriteMessger.Console("ParseInt fail!\r\nvalue: %s\r\n%s",
                    getString(json, key), getAllClassName(this), null);
            e.printStackTrace();
            return null;
        }
    }

    public Integer getIntege(String key, int radix) {
        return getIntege(coreData, key, radix);
    }

    public Integer getIntege(JSONObject json, String key) {
        return getIntege(json, key, 10);
    }

    public Integer getInteger(String key) {
        return getIntege(coreData, key, 10);
    }

    public Double getDouble(JSONObject json, String key) {
        try {
            return Double.parseDouble(getString(json, key));
        } catch (Exception e) {
            Message.WriteMessger.Console("ParseDouble fail!\r\nvalue: %s\r\n%s",
                    getString(json, key), getAllClassName(this), null);
            e.printStackTrace();
            return null;
        }
    }

    public Double getDouble(String key) {
        return getDouble(coreData, key);
    }

    public JSONObject getJson(JSONObject jsono, String key) {
        Object value = jsono.get(key);
        if (value instanceof JSONObject) {
            return (JSONObject) value;
        }
        return null;
    }

    public JSONObject getJson(String key) {
        Object value = getJson(coreData, key);
        if (value instanceof JSONObject) {
            return (JSONObject) value;
        }
        Message.WriteMessger.ShowAll("%s json not exists!!\r\n%s",
                key, getAllClassName(this), null);
        return null;
    }

    public JSONObject toJson() {
        return (JSONObject) coreData.clone();
    }

    public String getClassName(Object object) {
        return object.getClass().getSimpleName();
    }

    public String getAllClassName(Object object) {
        return object.getClass().getName();
    }

    public JSONArray getJSONArray(JSONObject jsono, String key) {
        JSONArray listModeSetting = jsono.getJSONArray(key);
        return listModeSetting;
    }

    public JSONArray getJSONArray(String key) {
        JSONArray listModeSetting = this.coreData.getJSONArray(key);
        return listModeSetting;
    }

    public void put(String key, Object get) {
        this.coreData.put(key, get);
    }
}
