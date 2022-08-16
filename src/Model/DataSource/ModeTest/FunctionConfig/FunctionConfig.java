/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataSource.ModeTest.FunctionConfig;

import Model.AllKeyWord;
import Model.DataSource.AbsJsonSource;
import Model.DataSource.DataWareHouse;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 21AK22
 */
public class FunctionConfig extends AbsJsonSource<FunctionElement> {

    private final List<FunctionName> functionInit;
    private final List<FunctionName> functionTest;
    private final List<FunctionName> funtionEnd;

    public FunctionConfig() {
        super();
        this.functionInit = new ArrayList<>();
        this.functionTest = new ArrayList<>();
        this.funtionEnd = new ArrayList<>();
    }

    @Override
    protected boolean getData() {
        DataWareHouse wareHouse = readFile.getData();
        clearAllList();
        getFunctionIn(wareHouse,
                wareHouse.getListJson(AllKeyWord.INIT), functionInit, null);
        getFunctionIn(wareHouse,
                wareHouse.getListJson(AllKeyWord.FUNCTIONS), functionTest, null);
        getFunctionIn(wareHouse,
                wareHouse.getListJson(AllKeyWord.END), funtionEnd, null);
        return !this.elements.isEmpty();
    }

    private void getFunctionIn(DataWareHouse baseData, List<JSONObject> listInfo, List<FunctionName> list, Integer times) {
        for (JSONObject modeInfo : listInfo) {
            if (!isActive(modeInfo)) {
                continue;
            }
            if (isLoopFunctions(modeInfo)) {
                getLoopFuction(baseData, modeInfo, list, times);
            } else if (modeInfo.containsKey(AllKeyWord.TEST_NAME)) {
                if (times != null) {
                    createNewItem(modeInfo, times);
                }
                FunctionElement funcElm = new FunctionElement(baseData.toJson(), modeInfo);
                put(funcElm.getItemName(), funcElm);
                list.add(new FunctionName(funcElm.getItemName(), funcElm.getFunctionName()));
            }
        }
    }

    private void createNewItem(JSONObject modeInfo, int times) {
        String oldName = modeInfo.getString(AllKeyWord.TEST_NAME);
        if (oldName.matches(".+_[0-9]+$")) {
            String newItemName = String.format("%s_%s",
                    oldName.substring(0, oldName.lastIndexOf("_")), times);
            modeInfo.put(AllKeyWord.TEST_NAME, newItemName);
        } else {
            String newItemName = String.format("%s_%s", oldName, times);
            modeInfo.put(AllKeyWord.TEST_NAME, newItemName);
        }
    }

    private void getLoopFuction(DataWareHouse baseData, JSONObject modeInfo, List<FunctionName> list, Integer times) {
        int heso = times == null ? 1 : times;
        DataWareHouse wareHouse = new DataWareHouse(modeInfo);
        final int begin = wareHouse.getInteger(AllKeyWord.BEGIN, 0) * heso;
        final int loopTimes = wareHouse.getInteger(AllKeyWord.LOOP_FUNC, 1) + begin;
        List<JSONObject> functions = wareHouse.getListJson(AllKeyWord.FUNCTIONS);
        for (int i = begin; i < loopTimes; i++) {
            getFunctionIn(baseData, functions, list, i);
        }
    }

    private static boolean isLoopFunctions(JSONObject modeInfo) {
        return modeInfo.containsKey(AllKeyWord.LOOP_FUNC) && modeInfo.containsKey(AllKeyWord.FUNCTIONS);
    }

    private static Boolean isActive(JSONObject modeInfo) {
        return modeInfo.getBoolean(AllKeyWord.FLAG);
    }

    public long getTimeOutTest() {
        Long timeout = this.readFile.getData().getLong(AllKeyWord.TIME_OUT_TEST);
        if (timeout == null) {
            return Long.MAX_VALUE;
        }
        return timeout * 1000;
    }

    public List<FunctionName> getCheckFunctions() {
        return functionInit;
    }

    public List<FunctionName> getTestFunctions() {
        return functionTest;
    }

    public List<String> getItemTestFunctions() {
        List<String> itemTest = new ArrayList<>();
        for (FunctionName funcName : this.functionTest) {
            itemTest.add(funcName.getItemName());
        }
        return itemTest;
    }

    public String getStationName() {
        return this.readFile.getData().getString(AllKeyWord.STATION_TYPE);
    }

    public List<FunctionName> getEndFuntions() {
        return funtionEnd;
    }

    private void clearAllList() {
        this.functionInit.clear();
        this.functionTest.clear();
        this.funtionEnd.clear();
    }

    public List<FunctionName> getSelectedItem(List<String> listItem) {
        List<FunctionName> result = new ArrayList<>();
        if (listItem == null) {
            result.addAll(functionTest);
            return result;
        }
        result.addAll(putSelectFunctionNames(listItem, functionTest));
        return result;
    }

    private List<FunctionName> putSelectFunctionNames(List<String> listItem, List<FunctionName> base) {
        List<FunctionName> result = new ArrayList<>();
        for (FunctionName functionName : base) {
            if (listItem.contains(functionName.getItemName())) {
                result.add(functionName);
            }
        }
        return result;
    }

}
