/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataSource.Setting;

import Model.WareHouse.DataWareHouse;
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

    public String getModeType() {
        return this.warehouse.getString(KeyWord.TYPE_MODE);
    }

    public List<String> getDetail() {
        return this.warehouse.cvtArrays2List(this.warehouse.getJSONArray(KeyWord.DETAIL));
    }

    public int getRow() {
        return this.warehouse.getInteger(KeyWord.ROW);
    }

    public int getColumn() {
        return this.warehouse.getInteger(KeyWord.COLUMN);
    }

    public String getCheckSN() {
        return this.warehouse.getString(KeyWord.CHECK_SN_FAIL);
    }

    public String getCheckFixture() {
        return this.warehouse.getString(KeyWord.CHECK_FIXTURE);
    }

    public String getSFIS() {
        return this.warehouse.getString(KeyWord.SFIS);
    }

    public String getFtpServer() {
        return this.warehouse.getString(KeyWord.FPT_SERVER);
    }

    public String getCusServer() {
        return this.warehouse.getString(KeyWord.CUS_SERVER);
    }

    public String getUpApi() {
        return this.warehouse.getString(KeyWord.UP_API);
    }

    public String getTypeUI() {
        return this.warehouse.getString(KeyWord.TYPE_UI);
    }

    public JSONObject toJson() {
        return this.warehouse.toJson();
    }
}
