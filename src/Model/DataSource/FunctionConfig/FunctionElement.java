/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataSource.FunctionConfig;

import Model.DataSource.AbsElementInfo;
import com.alibaba.fastjson.JSONObject;
import java.util.List;

/**
 *
 * @author 21AK22
 */
public class FunctionElement extends AbsElementInfo {

    public FunctionElement(JSONObject base, JSONObject config) {
        super(FuncKeyWord.KEYS, base, config);
    }

    public String getFunctionName() {
        return this.warehouse.getString(FuncKeyWord.FUNC_NAME);
    }

    public String getItemName() {
        return this.warehouse.getString(FuncKeyWord.ITEM_NAME);
    }

    public boolean isMutiTasking() {
        return this.warehouse.getBoolean(FuncKeyWord.MULTI_TASK);
    }

    public int getRetry() {
        if (this.warehouse.getInteger(FuncKeyWord.RETRY) == null) {
            return 1;
        }
        return this.warehouse.getInteger(FuncKeyWord.RETRY);
    }

    public long getTimeOutFunction() {
        if (this.warehouse.getLong(FuncKeyWord.TIME_OVER) == null) {
            return Long.MAX_VALUE;
        }
        return this.warehouse.getLong(FuncKeyWord.TIME_OVER);
    }

    public long getTimeOutTest() {
        if (this.warehouse.getLong(FuncKeyWord.TIME_OUT_TEST) == null) {
            return Long.MAX_VALUE;
        }
        return this.warehouse.getLong(FuncKeyWord.TIME_OUT_TEST) * 1000;
    }

    public String getModeCancel() {
        return this.warehouse.getString(FuncKeyWord.CANCEL);
    }
    
    public boolean isSkipFail() {
        return warehouse.getBoolean(FuncKeyWord.FAIL_CONTNIUE);
    }

    boolean isActive() {
        return  warehouse.getBoolean(FuncKeyWord.FLAG);
    }

    public String getValue(String key) {
        return warehouse.getString(key);
    }

    public List<String> getListString(String key) {
        return warehouse.getList(key);
    }

}
