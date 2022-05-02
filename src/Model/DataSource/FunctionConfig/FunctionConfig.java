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
public class FunctionConfig extends AbsJsonSource<FunctionInfo>{

    private static volatile FunctionConfig instaince;
    private final List<String> funtionNames;

    private FunctionConfig() {
        super();
        this.funtionNames = new ArrayList<>();
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
        FunctionInfo info;
        for (JSONObject modeInfo : wareHouse.getListJson(KeyWord.FUNCTIONS)) {
            info = new FunctionInfo(wareHouse.toJson(), modeInfo);
            if (!isNull(info.getFunctionName())) {
                this.elements.add(info);
                this.funtionNames.add(info.getFunctionName());
            }
        }
        return !this.elements.isEmpty();
    }

    public List<String> getListFunction() {
        return this.funtionNames;
    }
    
}
