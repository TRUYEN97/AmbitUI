/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DataSource;

import Model.Interface.IInit;
import Control.FileType.ITypeRead;
import Control.Message;
import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class ReadFileSource implements IInit {
    protected static final String NEW_LINE = "\r\n";
    private String path;
    private final ITypeRead type ;
    private final JSONObject coreData;

    public ReadFileSource(ITypeRead typeRead) {
        this.type = typeRead;
        this.coreData = new JSONObject();
    }

    public void addPathFile(String path) {
        fileIsExists(path);
        this.path = path;
    }

    private void fileIsExists(String path) {
        if (!new File(path).exists()) {
            Message.WriteMessger.nameNotAlreadyExistError(path, getAllClassName());
        }
    }

    @Override
    public boolean init() {
        if (!readFile(path)) {
            Message.WriteMessger.nameNotAlreadyExistError(path, this.getClass().getName());
            return false;
        }
        Message.WriteMessger.Console("The name: %s Load ok!\r\n%s", path, getAllClassName(), null);
        return true;
    }

    private boolean readFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return false;
        }
        try {
            this.coreData.clear();
            this.coreData.putAll(type.readContain(new BufferedReader(new FileReader(file))));
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Message.WriteMessger.ConfixErro("%s\r\nRead file fail!\r\n%s\r\n%s",
                    path, getAllClassName(), ex.toString());
            return false;
        }
    }

    public List<String> getList(String key) {
        return  Arrays.asList(getArrays(key));
    }

    public List<String> getList(JSONObject json, String key) {
        return Arrays.asList(getArrays(json, key));
    }

    private String getAllClassName() {
        return this.getClass().getName();
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

    public Integer getIntege(String key) {
        return getIntege(coreData, key, 10);
    }

    public Double getDouble(JSONObject json, String key) {
        try {
            return Double.parseDouble(getString(json, key));
        } catch (NumberFormatException e) {
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
        if (value instanceof JSONObject jSONObject) {
            return jSONObject;
        }
        return null;
    }

    public JSONObject getJson(String key) {
        Object value = getJson(coreData, key);
        if (value instanceof JSONObject jSONObject) {
            return jSONObject;
        }
        Message.WriteMessger.ShowAll("%s json not exists!!\r\n%s",
                key, getAllClassName(this), null);
        return null;
    }

    public JSONObject toJson() {
        return (JSONObject) coreData;
    }

    public String getClassName(Object object) {
        return object.getClass().getSimpleName();
    }

    public String getAllClassName(Object object) {
        return object.getClass().getName();
    }
}
