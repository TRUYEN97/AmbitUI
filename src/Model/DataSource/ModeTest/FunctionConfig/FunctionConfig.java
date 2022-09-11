/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataSource.ModeTest.FunctionConfig;

import Model.AllKeyWord;
import Model.DataSource.AbsElementInfo;
import com.alibaba.fastjson.JSONObject;
import java.util.List;
import java.util.Set;

/**
 *
 * @author 21AK22
 */
public class FunctionConfig extends AbsElementInfo {

    private final FunctionName functionName;
    
    public FunctionConfig(JSONObject base, JSONObject config) {
        super(null, base, config);
        this.functionName = new FunctionName(getItemName(), getFunctionName());
    }
    
    public FunctionName getfFunctionName(){
        return functionName;
    }

    public final String getFunctionName() {
        return this.warehouse.getString(AllKeyWord.CONFIG.FUNC_NAME);
    }

    public final String getItemName() {
        return this.warehouse.getString(AllKeyWord.CONFIG.TEST_NAME);
    }

    public boolean isMutiTasking() {
        return this.warehouse.getBoolean(AllKeyWord.CONFIG.MULTI_TASK);
    }

    public int getRetry() {
        if (this.warehouse.getInteger(AllKeyWord.CONFIG.RETRY) == null) {
            return 1;
        }
        return this.warehouse.getInteger(AllKeyWord.CONFIG.RETRY);
    }

    public long getTimeOutFunction() {
        if (this.warehouse.getLong(AllKeyWord.CONFIG.TIME_OUT) == null) {
            return Long.MAX_VALUE;
        }
        return this.warehouse.getLong(AllKeyWord.CONFIG.TIME_OUT);
    }

    public long getTimeOutTest() {
        if (this.warehouse.getLong(AllKeyWord.TIME_OUT_TEST) == null) {
            return Long.MAX_VALUE;
        }
        return this.warehouse.getLong(AllKeyWord.TIME_OUT_TEST) * 1000;
    }

    public boolean isSkipFail() {
        return warehouse.getBoolean(AllKeyWord.CONFIG.FAIL_CONTNIUE);
    }

    boolean isActive() {
        return warehouse.getBoolean(AllKeyWord.CONFIG.FLAG);
    }

    public String getString(String key) {
        return warehouse.getString(key);
    }

    public List<String> getListString(String key) {
        return warehouse.getListJsonArray(key);
    }

    public Set<String> getListItem() {
        return warehouse.toJson().keySet();
    }

    public boolean isUntilMultiDone() {
        return warehouse.getBoolean(AllKeyWord.CONFIG.WAIT_MULTI_DONE);
    }

    public Object getObject(String key) {
        return warehouse.get(key);
    }

    public JSONObject getJson() {
        return this.warehouse.toJson();
    }

    public boolean isAlwaysRun() {
        return this.warehouse.getBoolean(AllKeyWord.CONFIG.ALWAYS_RUN);
    }

    public boolean isDiscreteFunc() {
        return this.warehouse.getBoolean(AllKeyWord.CONFIG.DEBUG_ABLE);
    }

}
