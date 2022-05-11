/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataSource.FunctionConfig;

import Model.DataSource.AbsJsonSource;
import Model.DataWareHouse;
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
    private final List<String> itemNames;

    private FunctionConfig() {
        super();
        this.itemNames = new ArrayList<>();
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
        FunctionElement info;
        for (JSONObject modeInfo : wareHouse.getListJson(FunctionConfigKeyWord.FUNCTIONS)) {
            info = new FunctionElement(wareHouse.toJson(), modeInfo);
            if (!isNull(info.getFunctionName())) {
                put(info.getFunctionName(), info);
                this.itemNames.add(info.getFunctionName());
            }
        }
        return !this.elements.isEmpty();
    }

    public List<String> getListFunction() {
        return itemNames;
    }

    public long getTimeOutTest() {
        Long timeout = this.readFile.getData().getLong(FunctionConfigKeyWord.TIME_OUT_TEST);
        if (timeout == null) {
            return Long.MAX_VALUE;
        }
        return timeout * 1000;
    }

}
