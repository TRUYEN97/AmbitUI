/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataTest.FunctionData;

import Model.DataSource.ModeTest.FunctionConfig.FunctionName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class ItemTestManager {

    private final Map<FunctionName, ItemTestData> itemTests;
    private final List<ItemTestData> listItemTests;
    private ItemTestData thisItem;

    public ItemTestManager() {
        this.itemTests = new HashMap<>();
        this.listItemTests = new ArrayList<>();
    }

    public List<ItemTestData> getListItemTests() {
        return listItemTests;
    }

    public FunctionName getItemFunctionName() {
        return this.thisItem.getItemTestName();
    }

    public Map<FunctionName, ItemTestData> getItemTests() {
        return itemTests;
    }

    public void addItemtest(ItemTestData itemTest) {
        if (this.itemTests.isEmpty()) {
            this.thisItem = itemTest;
        }
        if (!this.itemTests.containsKey(itemTest.getItemTestName())) {
            this.listItemTests.add(itemTest);
            this.itemTests.put(itemTest.getItemTestName(), itemTest);
        }
    }

    public ItemTestData getThisItem() {
        return thisItem;
    }

    public FunctionName getItemTestName() {
        return thisItem.getItemTestName();
    }

    public ItemTestData getFirstFail() {
        if (listItemTests.size() == 1) {
            if (!this.thisItem.isPass()) {
                return thisItem;
            }
            return null;
        }
        for (ItemTestData itemTest : listItemTests) {
            if (!itemTest.isPass() && !itemTest.equals(thisItem)) {
                return itemTest;
            }
        }
        return null;
    }

    public ItemTestData getItemTest(String itemName) {
        return itemTests.get(itemName);
    }

}
