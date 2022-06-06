/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DataSource;

import Model.DataModeTest.ErrorLog;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static java.util.Objects.isNull;

/**
 *
 * @author Administrator
 */
public class DataWareHouse {

    private final JSONObject coreData;

    public DataWareHouse() {
        this.coreData = new JSONObject();
    }

    public DataWareHouse(JSONObject parseObject) {
        this.coreData = parseObject;
    }

    public boolean putAll(Map data) {
        if (data != null && !data.isEmpty()) {
            this.coreData.putAll(data);
            return true;
        }
        return false;
    }

    public List<String> getListJsonArray(String key) {
        return cvtArrays2List(getJSONArray(key));
    }

    public List<String> cvtArrays2List(JSONArray array) {
        List<String> result = new ArrayList<>();
        if (isNull(array)) {
            return result;
        }
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
        } catch (NumberFormatException e) {
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
        } catch (NumberFormatException e) {
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
        String mess = String.format("%s json not exists!!", key);
        ErrorLog.addError(this, mess);
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

    public List<String> getListSlip(String key) {
        return Arrays.asList(getArrays(key));
    }

    public List<String> getListSlip(JSONObject json, String key) {
        return Arrays.asList(getArrays(json, key));
    }

    public List<JSONObject> getListJson(String key) {
        return getListJson(this.coreData, key);
    }

    public List<JSONObject> getListJson(JSONObject json, String key) {
        List<JSONObject> result = new ArrayList<>();
        for (var object : json.getJSONArray(key)) {
            result.add((JSONObject) object);
        }
        return result;
    }

    public void clear() {
        this.coreData.clear();
    }

    public Long getLong(String key) {
        return getLong(coreData, key);
    }

    public Long getLong(JSONObject json, String key) {
        try {
            return Long.parseLong(getString(json, key));
        } catch (NumberFormatException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public boolean getBoolean(String key) {
        return getBoolean(coreData, key);
    }

    public boolean getBoolean(JSONObject json, String key) {
        try {
            return json.getBooleanValue(key);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return false;
        }
    }
}
