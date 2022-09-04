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

    private final Map<String, ItemTestData> itemTests;
    private final List<ItemTestData> listItemTests;
    private ItemTestData thisItem;

    public ItemTestManager() {
        this.itemTests = new HashMap<>();
        this.listItemTests = new ArrayList<>();
    }

    public List<ItemTestData> getListItemTests() {
        return listItemTests;
    }

    public String getFunctionName() {
        return this.thisItem.getItemName();
    }

    public Map<String, ItemTestData> getItemTests() {
        return itemTests;
    }

    public void addItemtest(ItemTestData itemTest) {
        if (this.itemTests.isEmpty()) {
            this.thisItem = itemTest;
        }
        if (!this.itemTests.containsKey(itemTest.getItemName())) {
            this.listItemTests.add(itemTest);
            this.itemTests.put(itemTest.getItemName(), itemTest);
        }
    }

    public ItemTestData getThisItem() {
        return thisItem;
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

    public ItemTestData getItemTest(FunctionName itemName) {
        return itemTests.get(itemName);
    }

}
