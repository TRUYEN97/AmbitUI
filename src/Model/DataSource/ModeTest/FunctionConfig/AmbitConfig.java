/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataSource.ModeTest.FunctionConfig;

import Model.AllKeyWord;
import Model.DataSource.AbsJsonSource;
import Model.DataSource.DataWareHouse;
import Model.DataTest.FunctionData.ItemTestData.TYPE;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 21AK22
 */
public class AmbitConfig extends AbsJsonSource<FunctionName, FunctionConfig> {

    private final List<FunctionName> initFunctions;
    private final List<FunctionName> testFunctions;
    private final List<FunctionName> endFunctions;
    private final List<FunctionName> finalFunctions;
    private final List<FunctionName> discreteFunctions;
    private final ConfigCount configCount;

    public AmbitConfig() {
        super();
        this.initFunctions = new ArrayList<>();
        this.testFunctions = new ArrayList<>();
        this.endFunctions = new ArrayList<>();
        this.finalFunctions = new ArrayList<>();
        this.discreteFunctions = new ArrayList<>();
        this.configCount = new ConfigCount();
    }

    @Override
    protected boolean getData() {
        DataWareHouse wareHouse = readFile.getData();
        clearAllList();
        getFunctionIn(wareHouse,
                wareHouse.getListJson(AllKeyWord.CONFIG.INIT),
                initFunctions, discreteFunctions, null, TYPE.INIT);
        getFunctionIn(wareHouse,
                wareHouse.getListJson(AllKeyWord.CONFIG.FUNCTIONS),
                testFunctions, discreteFunctions, null, TYPE.TEST);
        getFunctionIn(wareHouse,
                wareHouse.getListJson(AllKeyWord.CONFIG.END),
                endFunctions, discreteFunctions, null, TYPE.END);
        getFunctionIn(wareHouse,
                wareHouse.getListJson(AllKeyWord.CONFIG.FINAL),
                finalFunctions, discreteFunctions, null, TYPE.FINAL);
        return !this.elements.isEmpty();
    }

    private void getFunctionIn(DataWareHouse baseData, List<JSONObject> listInfo,
            List<FunctionName> testFunctions, List<FunctionName> debugFunctions, Integer times, TYPE type) {
        for (JSONObject modeInfo : listInfo) {
            if (!isActive(modeInfo)) {
                continue;
            }
            if (isLoopFunctions(modeInfo)) {
                getLoopFuction(baseData, modeInfo, testFunctions, debugFunctions, times, type);
            } else if (modeInfo.containsKey(AllKeyWord.CONFIG.TEST_NAME)) {
                JSONObject modeInfoClone = (JSONObject) modeInfo.clone();
                createNewItem(modeInfoClone, times);
                FunctionConfig funcElm = new FunctionConfig(baseData.toJson(), modeInfoClone, type);
                FunctionName functionName = funcElm.getfFunctionName();
                put(functionName, funcElm);
                testFunctions.add(functionName);
                if (funcElm.isDiscreteFunc()) {
                    debugFunctions.add(functionName);
                }
            }
        }
    }

    private void createNewItem(JSONObject modeInfo, Integer times) {
        String oldName = modeInfo.getString(AllKeyWord.CONFIG.TEST_NAME);
        modeInfo.put(AllKeyWord.CONFIG.TEST_NAME, this.configCount.getItemCountName(oldName, times));
    }

    private void getLoopFuction(DataWareHouse baseData, JSONObject modeInfo,
            List<FunctionName> list, List<FunctionName> debugFunctions, Integer times, TYPE type) {
        int heso = times == null ? 1 : times;
        DataWareHouse wareHouse = new DataWareHouse(modeInfo);
        final int begin = wareHouse.getInteger(AllKeyWord.CONFIG.BEGIN, 0) * heso;
        final int loopTimes = wareHouse.getInteger(AllKeyWord.CONFIG.LOOP_FUNC, 1) + begin;
        List<JSONObject> functions = wareHouse.getListJson(AllKeyWord.CONFIG.FUNCTIONS);
        for (int i = begin; i < loopTimes; i++) {
            getFunctionIn(baseData, functions, list, debugFunctions, i, type);
        }
    }

    private static boolean isLoopFunctions(JSONObject modeInfo) {
        return modeInfo.containsKey(AllKeyWord.CONFIG.LOOP_FUNC) && modeInfo.containsKey(AllKeyWord.CONFIG.FUNCTIONS);
    }

    private static Boolean isActive(JSONObject modeInfo) {
        return modeInfo.getBoolean(AllKeyWord.CONFIG.FLAG);
    }

    public long getTimeOutTest() {
        Long timeout = this.readFile.getData().getLong(AllKeyWord.TIME_OUT_TEST);
        if (timeout == null) {
            return Long.MAX_VALUE;
        }
        return timeout * 1000;
    }

    public List<FunctionName> getCheckFunctions() {
        return new ArrayList<>(initFunctions);
    }

    public List<FunctionName> getTestFunctions() {
        return new ArrayList<>(testFunctions);
    }

    public List<FunctionName> getEndFuntions() {
        return new ArrayList<>(endFunctions);
    }

    public List<FunctionName> getFinalFuntions() {
        return new ArrayList<>(finalFunctions);
    }

    public List<FunctionName> getDebugFunctions() {
        return new ArrayList<>(discreteFunctions);
    }

    public String getStationName() {
        return this.readFile.getData().getString(AllKeyWord.STATION_TYPE);
    }

    private void clearAllList() {
        this.initFunctions.clear();
        this.testFunctions.clear();
        this.endFunctions.clear();
        this.finalFunctions.clear();
        this.discreteFunctions.clear();
    }

    public List<FunctionName> getSelectedItem(List<FunctionName> listItem) {
        if (listItem == null) {
            return getDebugFunctions();
        }
        if (discreteFunctions.containsAll(listItem)) {
            return listItem;
        }
        return null;
    }

}
