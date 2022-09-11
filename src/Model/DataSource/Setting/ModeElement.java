/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataSource.Setting;

import Model.AllKeyWord;
import Model.DataSource.AbsElementInfo;
import com.alibaba.fastjson.JSONObject;
import java.io.File;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class ModeElement extends AbsElementInfo {

    public ModeElement(JSONObject base, JSONObject config) {
        super(null, base, config);
    }

    public String getStationName() {
        return this.warehouse.getString(AllKeyWord.STATION_TYPE);
    }

    public String getModeType() {
        return this.warehouse.getString(AllKeyWord.TYPE_MODE);
    }

    public String getPnName() {
        return this.warehouse.getString(AllKeyWord.NAME_APP);
    }

    public String getAmbitConfigPath() {
        String path = this.warehouse.getString(AllKeyWord.AMBIT_CONFIG);
        if (path == null || !new File(path).exists()) {
            return null;
        }
        return path;
    }

    public String getModeName() {
        return this.warehouse.getString(AllKeyWord.NAME);
    }

    public List<String> getDetail() {
        return this.warehouse.getListJsonArray(AllKeyWord.DETAIL);
    }

    public List<String> getIniFunc() {
        return this.warehouse.getListJsonArray(AllKeyWord.CHANGE_FUNC);
    }

    public int getRow() {
        Integer mun = this.warehouse.getInteger(AllKeyWord.ROW);
        if (mun == null) {
            return 1;
        }
        return mun;
    }

    public int getColumn() {
        Integer mun = this.warehouse.getInteger(AllKeyWord.COLUMN);
        if (mun == null) {
            return 1;
        }
        return mun;
    }

    public String getTypeUI() {
        return this.warehouse.getString(AllKeyWord.TYPE_UI);
    }

    public JSONObject toJson() {
        return this.warehouse.toJson();
    }

    public boolean isMultiThread() {
        return (getColumn() * getRow()) > 1;
    }

    public boolean canDebug() {
        return this.warehouse.getBoolean(AllKeyWord.CONFIG.DEBUG_ABLE);
    }

    public int getLoopTest() {
        Integer value = this.warehouse.getInteger(AllKeyWord.CONFIG.LOOP_TEST);
        return value == null || value < 1 ? 1 : value;
    }

    public String getLocalLimitPath() {
        return this.warehouse.getString(AllKeyWord.LIMIT_PATH);
    }
}
