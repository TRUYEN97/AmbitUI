/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataSource.FunctionConfig;

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

    private static volatile FunctionConfig instaince;
    private final List<String> functionName;
    private final List<String> ItemfunctionName;
    private final List<String> initFucntions;;
    private final List<String> endFucntions;

    private FunctionConfig() {
        super();
        this.functionName = new ArrayList<>();
        this.ItemfunctionName = new ArrayList<>();
        this.initFucntions = new ArrayList<>();
        this.endFucntions = new ArrayList<>();
    }

    public static FunctionConfig getInstance() {
        FunctionConfig ins = FunctionConfig.instaince;
        if (ins == null) {
            synchronized (FunctionConfig.class) {
                ins = FunctionConfig.instaince;
                if (ins == null) {
                    FunctionConfig.instaince = ins = new FunctionConfig();
                }
            }
        }
        return ins;
    }

    @Override
    protected boolean getData() {
        DataWareHouse wareHouse = readFile.getData();
        getFunctionIn(wareHouse,
                wareHouse.getListJson(FuncKeyWord.INIT), FuncKeyWord.INIT);
        getFunctionIn(wareHouse,
                wareHouse.getListJson(FuncKeyWord.FUNCTIONS), FuncKeyWord.FUNCTIONS);
        getFunctionIn(wareHouse,
                wareHouse.getListJson(FuncKeyWord.END), FuncKeyWord.END);
        return !this.elements.isEmpty();
    }

    private void getFunctionIn(DataWareHouse baseData, List<JSONObject> listInfo, String type) {
        FunctionElement info;
        for (JSONObject modeInfo : listInfo) {
            info = new FunctionElement(baseData.toJson(), modeInfo);
            if (!isNull(info.getFunctionName()) && info.isActive()) {
                put(info.getFunctionName(), info);
                addAllFunction(info);
                switch (type) {
                    case FuncKeyWord.INIT ->
                        this.initFucntions.add(info.getFunctionName());
                    case FuncKeyWord.END ->
                        this.endFucntions.add(info.getFunctionName());
                }
            }
        }
    }

    private void addAllFunction(FunctionElement info) {
        this.functionName.add(info.getFunctionName());
        this.ItemfunctionName.add(info.getItemName());
    }

    public List<String> getListFunction() {
        return functionName;
    }

    public long getTimeOutTest() {
        Long timeout = this.readFile.getData().getLong(FuncKeyWord.TIME_OUT_TEST);
        if (timeout == null) {
            return Long.MAX_VALUE;
        }
        return timeout * 1000;
    }

    public List<String> getItemFunctions() {
        return ItemfunctionName;
    }

    public List<String> getInitFunctions() {
        return initFucntions;
    }

    public List<String> getEndFunctions() {
        return endFucntions;
    }

}
