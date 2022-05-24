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
        super(FuncKeyWord.KEYS, base, config);
    }

    public String getFunctionName() {
        return this.warehouse.getString(FuncKeyWord.FUNC_NAME);
    }

    public String getItemName() {
        return this.warehouse.getString(FuncKeyWord.ITEM_NAME);
    }

    public boolean isMutiTasking() {
        String multiTasking = this.warehouse.getString(FuncKeyWord.MULTI_TASK);
        return !(multiTasking == null || !multiTasking.equalsIgnoreCase(FuncKeyWord.ON));
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
        String flag = warehouse.getString(FuncKeyWord.FAIL_CONTNIUE);
        return flag != null && flag.equals(FuncKeyWord.ON);
    }

    boolean isActive() {
        String flag =  warehouse.getString(FuncKeyWord.FLAG);
        return flag != null && flag.equalsIgnoreCase(FuncKeyWord.ON);
    }

}
