/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataSource.Setting;

import Model.ManagerUI.DataWareHouse;
import com.alibaba.fastjson.JSONObject;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class ModeInfo {
    private final DataWareHouse warehouse;

    public ModeInfo(JSONObject base, JSONObject config) {
        this.warehouse = new DataWareHouse();
        setSetting(base);
        setSetting(config);
    }

    private void setSetting(JSONObject data) {
        for (String key : data.keySet()) {
            if (KeyWord.MODE_KEY.contains(key)) {
                this.warehouse.put(key, data.get(key));
            }
        }
    }

    public String getModeType() {
        return this.warehouse.getString(KeyWord.TYPE_MODE);
    }

    public String getModeName() {
        return this.warehouse.getString(KeyWord.NAME);
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

    public List<String> getDetail() {
        return this.warehouse.cvtArrays2List(this.warehouse.getJSONArray(KeyWord.DETAIL));
    }
    
    public List<String> getPrepare() {
        return this.warehouse.cvtArrays2List(this.warehouse.getJSONArray(KeyWord.PREPARE));
    }
    
    public List<String> getInit() {
        return this.warehouse.cvtArrays2List(this.warehouse.getJSONArray(KeyWord.INIT_FUNC));
    }
    
    public List<String> getEnd() {
        return this.warehouse.cvtArrays2List(this.warehouse.getJSONArray(KeyWord.END));
    }

    public int getRow() {
        return this.warehouse.getInteger(KeyWord.ROW);
    }

    public int getColumn() {
        return this.warehouse.getInteger(KeyWord.COLUMN);
    }

    public String getTypeUI() {
        return this.warehouse.getString(KeyWord.TYPE_UI);
    }

    public JSONObject toJson() {
        return this.warehouse.toJson();
    }
    
    public boolean isMutiThread() {
        return (getColumn() * getRow()) > 1;
    }
}
