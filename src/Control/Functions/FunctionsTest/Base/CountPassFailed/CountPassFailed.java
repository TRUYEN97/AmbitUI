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
            if (containsIn(passItemName, itemName) && itemTestData.isPass()) {
                addOne(countPasss, itemName);
            } else if (containsIn(failedItemName, itemName) && !itemTestData.isPass()) {
                addOne(countFaileds, itemName);
            }
        }
        addLog(LOG_KEYS.PC, "Count pass items: %s", countPasss);
        addLog(LOG_KEYS.PC, "Count failed items: %s", countFaileds);
        setResult(String.format("pass: %s | failed: %s", countPasss, countFaileds));
        return true;
    }

    private void addOne(Map<String, Integer> itemCounts, String itemName) {
        if (itemCounts.containsKey(itemName)) {
            itemCounts.put(itemName, itemCounts.get(itemName) + 1);
        } else {
            itemCounts.put(itemName, 1);
        }
    }

    private boolean containsIn(List<String> items, String itemName) {
        for (String item : items) {
            if (itemName.startsWith(item)) {
                return true;
            }
        }
        return false;
    }

}
