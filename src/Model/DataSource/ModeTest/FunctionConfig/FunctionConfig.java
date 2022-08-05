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
import static java.util.Objects.isNull;

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
                wareHouse.getListJson(AllKeyWord.INIT), functionInit);
        getFunctionIn(wareHouse,
                wareHouse.getListJson(AllKeyWord.FUNCTIONS), functionTest);
        getFunctionIn(wareHouse,
                wareHouse.getListJson(AllKeyWord.END), funtionEnd);
        return !this.elements.isEmpty();
    }

    private void getFunctionIn(DataWareHouse baseData, List<JSONObject> listInfo, List<FunctionName> list) {
        FunctionElement info;
        for (JSONObject modeInfo : listInfo) {
            info = new FunctionElement(baseData.toJson(), modeInfo);
            if (!isNull(info.getItemName()) && info.isActive()) {
                put(info.getItemName(), info);
                list.add(new FunctionName(info.getItemName(), info.getFunctionName()));
            }
        }
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
