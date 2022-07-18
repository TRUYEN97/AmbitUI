/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.JsonApi.CreateJsonApi;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Control.Functions.FunctionsTest.Base.BaseFunction.FileBaseFunction;
import Model.AllKeyWord;
import Model.DataSource.ModeTest.Limit.Limit;
import Model.DataTest.FunctionData.FunctionData;
import Model.ManagerUI.UIStatus.UiStatus;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class CreateJsonApi extends AbsFunction {

    private final AnalysisBase analysisBase;
    private final FileBaseFunction fileBaseFunction;

    public CreateJsonApi(String itemName) {
        super(itemName);
        this.analysisBase = new AnalysisBase(itemName);
        this.fileBaseFunction = new FileBaseFunction(itemName);
    }

    @Override
    public void setResources(UiStatus uiStatus, FunctionData functionData) {
        super.setResources(uiStatus, functionData); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        this.analysisBase.setResources(uiStatus, functionData);
        this.fileBaseFunction.setResources(uiStatus, functionData);
    }

    @Override
    protected boolean test() {
        JSONObject root = getRootJson();
        JSONArray tests = getTestsData();
        if (tests == null) {
            return false;
        }
        root.put("tests", tests);
        return this.fileBaseFunction.saveJson(root);
    }

    private JSONObject getRootJson() {
        JSONObject root = new JSONObject();
        List<String> keyBases = allConfig.getListJsonArray("BaseKeys");
        for (String keyBase : keyBases) {
            addValueTo(root, keyBase);
        }
        return root;
    }

    private void addValueTo(Map data, String key) {
        String value = this.processData.getString(key);
        addLog("PC", "Root: " + key + " = " + value);
        data.put(key, value == null ? "" : value);
        addLog("PC", "-----------------------------------------");
    }

    private JSONArray getTestsData() {
        JSONArray tests = new JSONArray();
        JSONObject itemTest;
        List<String> testKeys = allConfig.getListJsonArray("TestKeys");
        Limit limit = allConfig.getLimits();
        for (String itemName : limit.getListItemName()) {
            itemTest = this.processData.getItemData(itemName, testKeys);
            if (itemTest != null) {
                addLog("PC", "ItemTest: " + itemName + " = " + itemTest.toJSONString());
                tests.add(itemTest);
            } else if (limit.getItem(itemName).getInteger(AllKeyWord.REQUIRED) == 1) {
                String mess = String.format("Missing \"%s\" item test!", itemName);
                addLog("PC", mess);
                JOptionPane.showMessageDialog(null, mess);
                return null;
            } else {
                addLog("PC", "ItemTest: " + itemName + " = null");
            }
        }
        return tests;
    }

}
