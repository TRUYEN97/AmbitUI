/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataSource.FunctionConfig;

import Model.DataSource.AbsJsonSource;
import Model.DataWareHouse;
import com.alibaba.fastjson.JSONObject;
import static java.util.Objects.isNull;

/**
 *
 * @author 21AK22
 */
public class FuntionConfig extends AbsJsonSource<FunctionInfo>{

    private static volatile FuntionConfig instaince;

    private FuntionConfig() {
        super();
    }

    public static FuntionConfig getInstance() {
        FuntionConfig ins = FuntionConfig.instaince;
        if (ins == null) {
            synchronized (FuntionConfig.class) {
                ins = FuntionConfig.instaince;
                if (ins == null) {
                    FuntionConfig.instaince = ins = new FuntionConfig();
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
            }
        }
        return !this.elements.isEmpty();
    }
    
}
