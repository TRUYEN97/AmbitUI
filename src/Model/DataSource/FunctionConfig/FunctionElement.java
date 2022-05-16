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
        super(FunctionConfigKeyWord.KEYS, base, config);
    }

    public String getFunctionName() {
        return this.warehouse.getString(FunctionConfigKeyWord.FUNC_NAME);
    }

    public String getItemName() {
        return this.warehouse.getString(FunctionConfigKeyWord.ITEM_NAME);
    }

    public boolean isMutiTasking() {
        String multiTasking = this.warehouse.getString(FunctionConfigKeyWord.MULTI_TASK);
        return !(multiTasking == null || !multiTasking.equalsIgnoreCase(FunctionConfigKeyWord.ON));
    }

    public int getRetry() {
        if (this.warehouse.getInteger(FunctionConfigKeyWord.RETRY) == null) {
            return 1;
        }
        return this.warehouse.getInteger(FunctionConfigKeyWord.RETRY);
    }

    public long getTimeOutFunction() {
        if (this.warehouse.getLong(FunctionConfigKeyWord.TIME_OVER) == null) {
            return Long.MAX_VALUE;
        }
        return this.warehouse.getLong(FunctionConfigKeyWord.TIME_OVER) * 1000;
    }

    public long getTimeOutTest() {
        if (this.warehouse.getLong(FunctionConfigKeyWord.TIME_OUT_TEST) == null) {
            return Long.MAX_VALUE;
        }
        return this.warehouse.getLong(FunctionConfigKeyWord.TIME_OUT_TEST) * 1000;
    }

    public String getModeCancel() {
        return this.warehouse.getString(FunctionConfigKeyWord.CANCEL);
    }
    
    public boolean isSkipFail() {
        String flag = warehouse.getString(FunctionConfigKeyWord.FAIL_CONTNIUE);
        return flag != null && flag.equals(FunctionConfigKeyWord.ON);
    }

    boolean isActive() {
        String flag =  warehouse.getString(FunctionConfigKeyWord.FLAG);
        return flag != null && flag.equalsIgnoreCase(FunctionConfigKeyWord.ON);
    }

}
