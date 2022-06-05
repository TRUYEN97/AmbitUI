/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.CreateFaJson;

import Control.Functions.AbsFunction;
import com.alibaba.fastjson.JSONObject;

/**
 *
 * @author Administrator
 */
public class CreateFaJson extends AbsFunction {

    CreateFaJson(String name) {
        super(name);
    }

    @Override
    public boolean test() {
        JSONObject data = getFaJsonData();
        if (data == null) {
            return false;
        }
        addLog(data.toJSONString());
        addLog("Create baseData !");
        JSONObject baseData = createBaseData();
        return true;
    }

    private JSONObject getFaJsonData() {
        String key = this.funcConfig.getValue("KEY_WORD");
        addLog("Get FA json data in signal!");
        addLog("Get with key: " + key);
        var data = this.uiData.getSignal(key);
        if (data == null) {
            addLog("FA json data == null!");
            return null;
        }
        return (JSONObject) data;
    }

    private JSONObject createBaseData() {
        JSONObject baseData = new JSONObject();
        for (var key : KeyWordFaAPI.BASE_KEY.values()) {
            System.out.println(key.getInputKey());
            System.out.println(key);
        }
        return baseData;
    }

}
