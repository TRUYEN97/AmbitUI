/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataSource.FunctionConfig;

import Model.DataSource.AbsElementInfo;
import com.alibaba.fastjson.JSONObject;

/**
 *
 * @author 21AK22
 */
public class FunctionElement extends AbsElementInfo {

    public FunctionElement(JSONObject base, JSONObject config) {
        super(KeyWord.KEYS, base, config);
    }

    public String getFunctionName() {
        return this.warehouse.getString(KeyWord.FUNC_NAME);
    }
    
    public String getItemName() {
        return this.warehouse.getString(KeyWord.ITEM_NAME);
    }

    public boolean isMutiTasking() {
        String multiTasking = this.warehouse.getString(KeyWord.MULTI_TASK);
        return !(multiTasking == null || !multiTasking.equalsIgnoreCase("on"));
    }

    public int getRetry() {
        if (this.warehouse.getInteger(KeyWord.RETRY) == null) {
            return 1;
        }
        return this.warehouse.getInteger(KeyWord.RETRY);
    }

    public long getTimeOut() {
        if (this.warehouse.getLong(KeyWord.TIME_OUT_TEST) == null) {
            return Long.MAX_VALUE;
        }
        return this.warehouse.getLong(KeyWord.TIME_OUT_TEST);
    }

    public String getModeSkip() {
        return this.warehouse.getString(KeyWord.SKIP);
    }

}
