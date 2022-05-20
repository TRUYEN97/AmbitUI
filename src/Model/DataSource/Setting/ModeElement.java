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
public class ModeElement extends AbsElementInfo {

    public ModeElement(JSONObject base, JSONObject config) {
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

    public List<String> getCheckFunctions() {
        return this.warehouse.cvtArrays2List(this.warehouse.getJSONArray(KeyWord.PREPARE));
    }

    public List<String> getEndFunctions() {
        return this.warehouse.cvtArrays2List(this.warehouse.getJSONArray(KeyWord.END));
    }

    public int getRow() {
        Integer mun = this.warehouse.getInteger(KeyWord.ROW);
        if (mun == null) {
            return 1;
        }
        return mun;
    }

    public int getColumn() {
        Integer mun = this.warehouse.getInteger(KeyWord.COLUMN);
        if (mun == null) {
            return 1;
        }
        return mun;
    }

    public String getTypeUI() {
        return this.warehouse.getString(KeyWord.TYPE_UI);
    }

    public JSONObject toJson() {
        return this.warehouse.toJson();
    }

    public boolean isMultiThread() {
        return (getColumn() * getRow()) > 1;
    }

    public boolean isDiscreteTest() {
        String str = this.warehouse.getString(KeyWord.DISCRETE_TEST);
        return !(str == null || str.equalsIgnoreCase("on"));
    }
}
