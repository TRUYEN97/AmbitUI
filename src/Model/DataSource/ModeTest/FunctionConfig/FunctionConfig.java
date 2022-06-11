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

    private final List<String> functionInit;
    private final List<String> functionTest;
    private final List<String> functionItemTest;
    private final List<String> funtionEnd;

    public FunctionConfig() {
        super();
        this.functionInit = new ArrayList<>();
        this.functionTest = new ArrayList<>();
        this.functionItemTest = new ArrayList<>();
        this.funtionEnd = new ArrayList<>();
    }

    @Override
    protected boolean getData() {
        DataWareHouse wareHouse = readFile.getData();
        clearAllList();
        getFunctionIn(wareHouse,
                wareHouse.getListJson(AllKeyWord.INIT), AllKeyWord.INIT);
        getFunctionIn(wareHouse,
                wareHouse.getListJson(AllKeyWord.FUNCTIONS), AllKeyWord.FUNCTIONS);
        getFunctionIn(wareHouse,
                wareHouse.getListJson(AllKeyWord.END), AllKeyWord.END);
        return !this.elements.isEmpty();
    }

    private void getFunctionIn(DataWareHouse baseData, List<JSONObject> listInfo, String type) {
        FunctionElement info;
        for (JSONObject modeInfo : listInfo) {
            info = new FunctionElement(baseData.toJson(), modeInfo);
            if (!isNull(info.getFunctionName()) && info.isActive()) {
                put(info.getFunctionName(), info);
                switch (type) {
                    case AllKeyWord.INIT -> {
                        this.functionInit.add(info.getFunctionName());
                        break;
                    }
                    case AllKeyWord.END -> {
                        this.funtionEnd.add(info.getFunctionName());
                        break;
                    }
                    default -> {
                        addAllFunction(info);
                        break;
                    }
                }
            }
        }
    }

    private void addAllFunction(FunctionElement info) {
        this.functionTest.add(info.getFunctionName());
        this.functionItemTest.add(info.getItemName());
    }

    public long getTimeOutTest() {
        Long timeout = this.readFile.getData().getLong(AllKeyWord.TIME_OUT_TEST);
        if (timeout == null) {
            return Long.MAX_VALUE;
        }
        return timeout * 1000;
    }

    public List<String> getCheckFunctions() {
        return functionInit;
    }

    public List<String> getTestFunctions() {
        return functionTest;
    }

    public List<String> getItemTestFunctions() {
        return functionItemTest;
    }
    

    public String getStationName() {
        return this.readFile.getData().getString(AllKeyWord.STATION);
    }

    public List<String> getEndFuntions() {
        return funtionEnd;
    }

    private void clearAllList() {
        this.functionInit.clear();
        this.functionItemTest.clear();
        this.functionTest.clear();
        this.funtionEnd.clear();
    }

}
