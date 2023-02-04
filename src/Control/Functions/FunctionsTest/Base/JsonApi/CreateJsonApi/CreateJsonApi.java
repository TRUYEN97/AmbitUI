/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.JsonApi.CreateJsonApi;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.FileBaseFunction;
import Model.AllKeyWord;
import Model.DataSource.ModeTest.Limit.Limit;
import Model.DataTest.FunctionData.ItemTestData;
import Model.DataTest.FunctionParameters;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class CreateJsonApi extends AbsFunction {

    private final FileBaseFunction fileBaseFunction;

    public CreateJsonApi(FunctionParameters parameters) {
        this(parameters, null);
    }

    public CreateJsonApi(FunctionParameters parameters, String item) {
        super(parameters, item);
        this.fileBaseFunction = new FileBaseFunction(parameters, item);
    }

    @Override
    protected boolean test() {
        JSONObject root;
        JSONArray tests;
        boolean followLimit = this.config.getBoolean("followLimit", true);
        boolean limitErrorCode = this.config.getBoolean("limitErrorCode", true);
        addLog("PC", String.format("Follow limit: %s", followLimit));
        addLog("PC", String.format("Use the limit errorcode: %s", limitErrorCode));
        root = getRootJson(limitErrorCode);
        tests = getTestsDataFollowLomit(isPass(root), followLimit, limitErrorCode);
        if (tests == null) {
            return false;
        }
        root.put("tests", tests);
        return this.fileBaseFunction.saveJson(root);
    }

    private boolean isPass(JSONObject root) {
        return root.getString(AllKeyWord.SFIS.STATUS).equals(ItemTestData.PASS);
    }

    private JSONObject getRootJson(boolean limitErrorCode) {
        JSONObject root = new JSONObject();
        List<String> keyBases;
        keyBases = config.getListJsonArray("BaseKeys");
        for (String keyBase : keyBases) {
            addValueTo(root, keyBase, limitErrorCode);
        }
        return root;
    }

    private void addValueTo(Map data, String key, boolean limitErrorCode) {
        String value;
        if (key == null) {
            return;
        }
        if (key.equalsIgnoreCase("serial")) {
            value = this.processData.getString("mlbsn");
        } else if (!limitErrorCode && key.equalsIgnoreCase(AllKeyWord.CONFIG.ERROR_CODE)) {
            value = this.processData.getString(AllKeyWord.SFIS.ERRORCODE);
        } else {
            value = this.processData.getString(key);
        }
        addLog("PC", "Root: " + key + " = " + value);
        data.put(key, value == null ? "" : value);
        addLog("PC", "-----------------------------------------");
    }

    private JSONArray getTestsDataFollowLomit(boolean statusTest, boolean followLimit, boolean limitErrorCode) {
        JSONArray tests = new JSONArray();
        JSONObject itemTest;
        List<String> testKeys = config.getListJsonArray("TestKeys");
        Limit limit = config.getLimits();
        if (limit == null) {
            return null;
        }
        Set<String> limitItems = limit.getListItemName();
        List<ItemTestData> itemTestDatas = this.processData.getListItemTestData();
        if (statusTest && followLimit && isItemTestNotEnough(limitItems, itemTestDatas, limit)) {
            return null;
        }
        for (ItemTestData itemTestData : itemTestDatas) {
            String itemName = itemTestData.getItemName();
            if (itemName == null) {
                continue;
            }
            itemTest = itemTestData.getData(testKeys, limitErrorCode);
            if (!followLimit || (limitItems != null && checkLimitContain(limitItems, itemName))) {
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
        }
        return tests;
    }

    private boolean checkLimitContain(Set<String> limitItems, String itemName) {
        return itemName != null && (limitItems.contains(itemName) || limitItems.contains(getBaseItem(itemName)));
    }

    private String getBaseItem(String itemName) {
        if (itemName.matches(".+_[0-9]+$")) {
            return itemName.substring(0, itemName.lastIndexOf("_"));
        }
        return itemName;
    }

    private boolean isItemTestNotEnough(Set<String> limitItems, List<ItemTestData> itemTestDatas, Limit limit) {
        Set<String> itemTestNames = new HashSet<>();
        for (ItemTestData itemTestData : itemTestDatas) {
            if (itemTestData == null) {
                continue;
            }
            itemTestNames.add(itemTestData.getItemName());
        }
        addLog("PC", String.format("ItemNames: %s", itemTestNames));
        addLog("PC", String.format("Limit itemNames: %s", limitItems));
        boolean rs = false;
        for (String limitItem : limitItems) {
            if (limit.getItem(limitItem).getInteger(AllKeyWord.CONFIG.REQUIRED) == 1 && !itemTestNames.contains(limitItem)) {
                addLog("pc", String.format("Missing item: %s", limitItem));
                rs = true;
            }
        }
        return rs;
    }

}
