/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.FaAPI.CreateFaJson;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Control.Functions.FunctionsTest.Base.BaseFunction.FileBaseFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Control.Functions.FunctionsTest.FaAPI.CreateFaJson.KeyWordFaAPI.FUNC_KEY;
import Model.AllKeyWord;
import Model.DataSource.ModeTest.FunctionConfig.FunctionElement;
import Model.DataSource.ModeTest.FunctionConfig.FunctionName;
import Model.DataTest.FunctionData.FunctionData;
import Model.ManagerUI.UIStatus.UiStatus;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 *
 * @author Administrator
 */
public class CreateFaJson extends AbsFunction {

    private final FunctionBase baseFunc;
    private final AnalysisBase analysisBase;
    private final FileBaseFunction fileBaseFunction;

    public CreateFaJson(FunctionName itemName) {
        super(itemName);
        this.baseFunc = new FunctionBase(itemName);
        this.analysisBase = new AnalysisBase(itemName);
        this.fileBaseFunction = new FileBaseFunction(itemName);
    }

     @Override
    public void setResources(FunctionElement funcConfig, UiStatus uiStatus, FunctionData functionData) {
        super.setResources(funcConfig, uiStatus, functionData); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        this.baseFunc.setResources(funcConfig, uiStatus, functionData);
        this.analysisBase.setResources(funcConfig, uiStatus, functionData);
        this.fileBaseFunction.setResources(funcConfig, uiStatus, functionData);
    }

    @Override
    public boolean test() {
        JSONObject data = getFaJsonData();
        if (data == null) {
            return false;
        }
        addLog(data.toJSONString());
        JSONObject baseData = createBaseData();
        baseData.put("tests", createFunctionData(data));
        return this.fileBaseFunction.saveJson(baseData);
    }

    private JSONObject getFaJsonData() {
        String key = this.allConfig.getString("KEY_WORD");
        addLog("Get FA json data in signal!");
        addLog("Get with key: " + key);
        var data = this.testSignal.getObject(key);
        if (data == null) {
            addLog("FA json data == null!");
            return null;
        }
        return (JSONObject) data;
    }

    private JSONObject createBaseData() {
        JSONObject baseData = new JSONObject();
        addLog("Create base data ");
        for (var key : KeyWordFaAPI.BASE_KEY.values()) {
            String value = productData.getString(key.getInputKey());
            addTo(value, baseData, key.toString());
        }
        baseData.put("status", "passed");
        addToKeyValueLog("status", "passed");
        baseData.put("mode", "debug");
        addToKeyValueLog("mode", "debug");
        addLog("Create base data done!");
        return baseData;
    }

    private JSONArray createFunctionData(JSONObject data) {
        JSONArray funcData = new JSONArray();
        addLog("Create function!");
        for (String location : data.keySet()) {
            funcData.add(createLocalData(location, data));
        }
        addLog("Create function data done!");
        return funcData;
    }

    private JSONObject createLocalData(String location, JSONObject data) {
        JSONObject localtionData;
        localtionData = new JSONObject();
        addLog("Create data for location: " + location);
        getValueOfProductData(localtionData);
        getDataOfLocation(data.getJSONObject(location), localtionData);
        localtionData.put("child_unlinked", "False");
        addToKeyValueLog("child_unlinked", "False");
        String stationType = getStionType(productData.getString(AllKeyWord.FAIL_PC));
        localtionData.put("failed_station_type", stationType);
        addToKeyValueLog("failed_station_type", stationType);
        String result = getResult(localtionData);
        localtionData.put("post_repair_result", result);
        addToKeyValueLog("post_repair_result", result);
        return localtionData;
    }

    private void getDataOfLocation(JSONObject locationData, JSONObject funcData) {
        for (var key : FUNC_KEY.values()) {
            Object value = locationData.get(key.getInputKey());
            addTo(value, funcData, key.toString());
        }
    }

    private void addTo(Object value, JSONObject funcData, String key) {
        if (value != null) {
            funcData.put(key, value);
            addToKeyValueLog(key, value);
        }
    }

    private void addToKeyValueLog(String key, Object value) {
        addLog(String.format("  - Key: %s -- Value: %s", key, value));
    }

    private void getValueOfProductData(JSONObject funcData) {
        for (var key : FUNC_KEY.values()) {
            String value = this.productData.getString(key.getInputKey());
            addTo(value, funcData, key.toString());
        }
        String count = this.productData.getString(AllKeyWord.COUNTTEST);
        funcData.put("debug_count", Integer.valueOf(count));

    }

    private String getResult(JSONObject funcData) {
        if (funcData.getString(FUNC_KEY.repair_action.toString()).equals("scrap")) {
            return "scrap";
        } else {
            return "passed";
        }
    }

    private String getStionType(String pcName) {
        return pcName.substring(0, pcName.lastIndexOf("-"));
    }

}
