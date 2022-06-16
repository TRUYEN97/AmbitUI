/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataSource.ModeTest.ErrorCode;

import Model.DataSource.AbsElementInfo;
import com.alibaba.fastjson.JSONObject;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class ErrorCodeElement extends AbsElementInfo {

    public static final String LOW = "low";
    public static final String HIGH = "high";
    public static final String SIMPLE = "simple";

    public ErrorCodeElement(List<String> keys, JSONObject base, JSONObject config) {
        super(keys, base, config);
    }

    public JSONObject getSimpleError() {
        return this.warehouse.getJson(SIMPLE);
    }

    public JSONObject getTooLowError() {
        return this.warehouse.getJson(LOW);
    }

    public JSONObject getTooHighError() {
        return this.warehouse.getJson(HIGH);
    }

    public JSONObject getErrorType(String type) {
        return this.warehouse.getJson(type);
    }
}
