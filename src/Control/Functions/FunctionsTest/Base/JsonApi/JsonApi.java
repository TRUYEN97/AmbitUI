/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.JsonApi;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.AnalysisBase;
import Model.DataTest.FunctionData.FunctionData;
import Model.ManagerUI.UIStatus.UiStatus;
import com.alibaba.fastjson.JSONObject;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class JsonApi extends AbsFunction{

    private final AnalysisBase analysisBase;

    public JsonApi(String itemName) {
        super(itemName);
        this.analysisBase = new AnalysisBase(itemName);
    }

    @Override
    public void setResources(UiStatus uiStatus, FunctionData functionData) {
        super.setResources(uiStatus, functionData); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        this.analysisBase.setResources(uiStatus, functionData);
    }
    @Override
    protected boolean test() {
        JSONObject result = new JSONObject();
        List<String> keyBases = allConfig.getListJsonArray("BaseKeys");
        for (String keyBase : keyBases) {
            addValueTo(result, keyBase);
        }
        List<String> TestKeys = allConfig.getListJsonArray("TestKeys");
        return true;
    }
    
    private void addValueTo(Map data, String key)
    {
        addLog("Get","-----------------------------------------");
        addLog("Get","key is "+ key);
        if(key == null){
            return;
        }
        String value = this.processData.getString(key);
        data.put(key, value);
        addLog("Get","Value is "+ value);
        addLog("Get","-----------------------------------------");
        
    }
    
}
