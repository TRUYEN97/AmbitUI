package Model.DataSource.Setting;

import Model.DataSource.AbsJsonSource;
import Model.Interface.IInit;
import Model.DataWareHouse;
import com.alibaba.fastjson.JSONObject;
import static java.util.Objects.isNull;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Administrator
 */
public class Setting extends AbsJsonSource<ModeInfo> implements IInit {

    private static volatile Setting instaince;

    private Setting() {
        super();
    }

    public static Setting getInstance() {
        Setting ins = Setting.instaince;
        if (ins == null) {
            synchronized (Setting.class) {
                ins = Setting.instaince;
                if (ins == null) {
                    Setting.instaince = ins = new Setting();
                }
            }
        }
        return ins;
    }


    @Override
    protected boolean getData() {
        DataWareHouse wareHouse = readFile.getData();
        ModeInfo info;
        for (JSONObject modeInfo : wareHouse.getListJson(KeyWord.LOAD_MODE)) {
            info = new ModeInfo(wareHouse.toJson(), modeInfo);
            if (!isNull(info.getModeName())) {
                this.elements.add(info);
            }
        }
        return !this.elements.isEmpty();
    }
}
