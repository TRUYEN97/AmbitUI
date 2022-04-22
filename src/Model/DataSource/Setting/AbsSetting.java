/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataSource.Setting;

import View.ManagerUI.DataWareHouse;
import com.alibaba.fastjson.JSONObject;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class AbsSetting {

    protected final DataWareHouse warehouse;

    protected AbsSetting() {
        this.warehouse = new DataWareHouse();
    }

    public AbsSetting(JSONObject json) {
        this.warehouse = new DataWareHouse();
        this.warehouse.setData(json);
    }

    protected void setData(JSONObject json) {
        this.warehouse.setData(json);
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
