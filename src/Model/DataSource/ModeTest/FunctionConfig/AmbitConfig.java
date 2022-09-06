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
public class AmbitConfig extends AbsJsonSource<FunctionName, FunctionConfig> {

    private final List<FunctionName> functionInit;
    private final List<FunctionName> functionTest;
    private final List<FunctionName> funtionEnd;
    private final List<FunctionName> discreteFunctions;

    public AmbitConfig() {
        super();
        this.functionInit = new ArrayList<>();
        this.functionTest = new ArrayList<>();
        this.funtionEnd = new ArrayList<>();
        this.discreteFunctions = new ArrayList<>();
    }

    @Override
    protected boolean getData() {
        DataWareHouse wareHouse = readFile.getData();
        clearAllList();
        getFunctionIn(wareHouse,
                wareHouse.getListJson(AllKeyWord.INIT),
                functionInit, discreteFunctions, null);
        getFunctionIn(wareHouse,
                wareHouse.getListJson(AllKeyWord.FUNCTIONS),
                functionTest, discreteFunctions, null);
        getFunctionIn(wareHouse,
                wareHouse.getListJson(AllKeyWord.END),
                funtionEnd, discreteFunctions, null);
        return !this.elements.isEmpty();
    }

    private void getFunctionIn(DataWareHouse baseData, List<JSONObject> listInfo,
            List<FunctionName> testFunctions, List<FunctionName> debugFunctions, Integer times) {
        for (JSONObject modeInfo : listInfo) {
            if (!isActive(modeInfo)) {
                continue;
            }
            if (isLoopFunctions(modeInfo)) {
                getLoopFuction(baseData, modeInfo, testFunctions, debugFunctions, times);
            } else if (modeInfo.containsKey(AllKeyWord.TEST_NAME)) {
                if (times != null) {
                    createNewItem(modeInfo, times);
                }
                FunctionConfig funcElm = new FunctionConfig(baseData.toJson(), modeInfo);
                FunctionName functionName = funcElm.getfFunctionName();
                put(functionName, funcElm);
                testFunctions.add(functionName);
                if (funcElm.isDiscreteFunc()) {
                    debugFunctions.add(functionName);
                }
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

    private void getLoopFuction(DataWareHouse baseData, JSONObject modeInfo,
            List<FunctionName> list, List<FunctionName> debugFunctions, Integer times) {
        int heso = times == null ? 1 : times;
        DataWareHouse wareHouse = new DataWareHouse(modeInfo);
        final int begin = wareHouse.getInteger(AllKeyWord.BEGIN, 0) * heso;
        final int loopTimes = wareHouse.getInteger(AllKeyWord.LOOP_FUNC, 1) + begin;
        List<JSONObject> functions = wareHouse.getListJson(AllKeyWord.FUNCTIONS);
        for (int i = begin; i < loopTimes; i++) {
            getFunctionIn(baseData, functions, list, debugFunctions, i);
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

    public List<FunctionName> getDebugFunctions() {
        return new ArrayList<>(discreteFunctions);
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
        this.discreteFunctions.clear();
    }

    public List<FunctionName> getSelectedItem(List<FunctionName> listItem) {
        if (listItem == null) {
            return discreteFunctions;
        }
        if (discreteFunctions.containsAll(listItem)) {
            return listItem;
        }
        return null;
    }

    private List<FunctionName> putSelectFunctionNames(List<FunctionName> listItem, List<FunctionName> base) {
        List<FunctionName> result = new ArrayList<>();
        for (FunctionName functionName : listItem) {
            if (base.contains(functionName)) {
                result.add(functionName);
            }
        }
        return result;
    }

}
