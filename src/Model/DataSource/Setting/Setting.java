package Model.DataSource.Setting;

import Model.DataSource.AbsJsonSource;
import Model.Interface.IInit;
import Model.DataSource.DataWareHouse;
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
public class Setting extends AbsJsonSource<ModeElement> implements IInit {

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
        ModeElement info;
        for (JSONObject modeInfo : wareHouse.getListJson(KeyWord.LOAD_MODE)) {
            info = new ModeElement(wareHouse.toJson(), modeInfo);
            if (!isNull(info.getModeName())) {
                this.elements.add(info);
            }
        }
        return !this.elements.isEmpty();
    }

    public String getGiaiDoan() {
        return this.readFile.getData().getString(KeyWord.GIAI_DOAN);
    }

    public String getFunctionsLocalLog() {
        return this.readFile.getData().getString(KeyWord.LOCAL_FUNCTION_LOG);
    }

    public String getLocalLog() {
        return this.readFile.getData().getString(KeyWord.LOCAL_LOG);
    }
}
