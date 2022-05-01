/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataSource.Setting;

import Model.DataSource.AbsElementInfo;
import com.alibaba.fastjson.JSONObject;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class ModeInfo extends AbsElementInfo{

    public ModeInfo(JSONObject base, JSONObject config) {
       super(KeyWord.MODE_KEY, base, config);
    }

    public String getModeType() {
        return this.warehouse.getString(KeyWord.TYPE_MODE);
    }

    public String getModeName() {
        return this.warehouse.getString(KeyWord.NAME);
    }

    public List<String> getDetail() {
        return this.warehouse.cvtArrays2List(this.warehouse.getJSONArray(KeyWord.DETAIL));
    }
    
    public List<String> getIniFunc() {
        return this.warehouse.cvtArrays2List(this.warehouse.getJSONArray(KeyWord.INIT_FUNC));
    }
    
    public List<String> getPrepare() {
        return this.warehouse.cvtArrays2List(this.warehouse.getJSONArray(KeyWord.PREPARE));
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
