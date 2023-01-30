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
import Model.DataTest.FunctionData.ItemTestData;
import Model.DataTest.FunctionParameters;
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

    public CreateJsonApi(FunctionParameters parameters) {
        this(parameters, null);
    }

    public CreateJsonApi(FunctionParameters parameters, String item) {
        super(parameters, item);
        this.analysisBase = new AnalysisBase(parameters, item);
        this.fileBaseFunction = new FileBaseFunction(parameters, item);
    }

    @Override
    protected boolean test() {
        JSONObject root;
        JSONArray tests;
        boolean followLomit = this.config.getBoolean("followLimit", true);
        root = getRootJson();
        if (followLomit) {
            tests = getTestsDataFollowLomit(isPass(root));
        } else {
            tests = getTestsData();
        }
        if (tests == null) {
            return false;
        }
        root.put("tests", tests);
        return this.fileBaseFunction.saveJson(root);
    }

    private boolean isPass(JSONObject root) {
        return root.getString(AllKeyWord.SFIS.STATUS).equals(ItemTestData.PASS);
    }

    private JSONObject getRootJson() {
        JSONObject root = new JSONObject();
        List<String> keyBases;
        keyBases = config.getListJsonArray("BaseKeys");
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

    private JSONArray getTestsDataFollowLomit(boolean statusTest) {
        JSONArray tests = new JSONArray();
        JSONObject itemTest;
        List<String> testKeys = config.getListJsonArray("TestKeys");
        Limit limit = config.getLimits();
        for (String itemName : limit.getListItemName()) {
            itemTest = this.processData.getItemData(itemName, testKeys);
            if (itemTest != null) {
                addLog("PC", "ItemTest: " + itemName + " = " + itemTest.toJSONString());
                tests.add(itemTest);
            } else if (limit.getItem(itemName).getInteger(AllKeyWord.CONFIG.REQUIRED) == 1 && statusTest) {
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

    private JSONArray getTestsData() {
        JSONArray tests = new JSONArray();
        JSONObject itemTest;
        List<String> testKeys = config.getListJsonArray("TestKeys");
        for (FunctionData itemName : this.processData.getDataBoxs()) {
            itemTest = this.processData.getItemData(itemName.getFunctionName().getItemName(), testKeys);
            if (itemTest != null) {
                addLog("PC", "ItemTest: " + itemName + " = " + itemTest.toJSONString());
                tests.add(itemTest);
            } else {
                addLog("PC", "ItemTest: " + itemName + " = null");
            }
        }
        return tests;
    }

}
