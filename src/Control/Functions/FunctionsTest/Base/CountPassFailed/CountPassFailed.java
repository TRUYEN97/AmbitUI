/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.CountPassFailed;

import Control.Functions.AbsFunction;
import Model.DataTest.FunctionData.ItemTestData;
import Model.DataTest.FunctionParameters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class CountPassFailed extends AbsFunction {

    public CountPassFailed(FunctionParameters functionParameters) {
        this(functionParameters, null);
    }

    public CountPassFailed(FunctionParameters functionParameters, String itemName) {
        super(functionParameters, itemName);
    }

    @Override
    protected boolean test() {
        List<String> passItemName = this.config.getJsonList("passItemName");
        List<String> failedItemName = this.config.getJsonList("failedItemName");
        addLog("CONFIG", "Pass item name: %s", passItemName);
        addLog("CONFIG", "Failed item name: %s", failedItemName);
        if (passItemName.isEmpty() && failedItemName.isEmpty()) {
            return true;
        }
        Map<String, Integer> countPasss = new HashMap<>();
        Map<String, Integer> countFaileds = new HashMap<>();
        List<ItemTestData> itemTestDatas = processData.getListItemTestData();
        addLog(LOG_KEYS.PC, "Have %s item", itemTestDatas.size());
        for (ItemTestData itemTestData : itemTestDatas) {
            if (itemTestData.isTest()) {
                continue;
            }
            String itemName = itemTestData.getItemName();
            String baseItem;
            if ((baseItem = containsIn(passItemName, itemName)) != null && itemTestData.isPass()) {
                addOne(countPasss, baseItem);
            } else if ((baseItem = containsIn(failedItemName, itemName)) != null && !itemTestData.isPass()) {
                addOne(countFaileds, baseItem);
            }
        }
        String passCount = getStringRs(countPasss);
        String failCount = getStringRs(countFaileds);
        if (passCount != null) {
            addLog(LOG_KEYS.PC, "Passed items: %s", passCount);
        }
        if (failCount != null) {
            addLog(LOG_KEYS.PC, "Failed items: %s", failCount);
        }
        setResult(String.format("pass: %s - failed: %s", passCount, failCount));
        return true;
    }

    private String getStringRs(Map<String, Integer> countPasss) {
        if (countPasss.isEmpty()) {
            return null;
        }
        StringBuilder rs = new StringBuilder();
        for (String key : countPasss.keySet()) {
            rs.append(key).append(":").append(countPasss.get(key)).append(",");
        }
        rs.deleteCharAt(rs.length()-1);
        return rs.toString();
    }

    private void addOne(Map<String, Integer> itemCounts, String itemName) {
        if (itemCounts.containsKey(itemName)) {
            itemCounts.put(itemName, itemCounts.get(itemName) + 1);
        } else {
            itemCounts.put(itemName, 1);
        }
    }

    private String containsIn(List<String> items, String itemName) {
        for (String item : items) {
            if (itemName.startsWith(item)) {
                return item;
            }
        }
        return null;
    }

}
