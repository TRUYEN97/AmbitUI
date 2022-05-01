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
public class FunctionInfo extends AbsElementInfo {

    public FunctionInfo(JSONObject base, JSONObject config) {
        super(KeyWord.KEYS, base, config);
    }

    public String getFunctionName() {
        return this.warehouse.getString(KeyWord.FUNC_NAME);
    }

}
