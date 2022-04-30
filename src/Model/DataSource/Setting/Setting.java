package Model.DataSource.Setting;

import Control.FileType.FileJson;
import Model.DataSource.AbsSource;
import Model.Interface.IInit;
import Model.DataSource.ReadFileSource;
import Model.ManagerUI.DataWareHouse;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.List;
import static java.util.Objects.isNull;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Administrator
 */
public class Setting extends AbsSource implements IInit {

    private static volatile Setting instaince;
    private final List<ModeInfo> modeInfos;

    private Setting() {
        this.readFile = new ReadFileSource(new FileJson());
        this.modeInfos = new ArrayList<>();
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
    public boolean init() {
        if (super.init()) {
            return getMode();
        }
        return false;
    }

    public List<ModeInfo> getModeInfos() {
        return modeInfos;
    }

    public int getCountMode() {
        return this.modeInfos.size();
    }

    private boolean getMode() {
        DataWareHouse wareHouse = readFile.getData();
        ModeInfo info;
        for (JSONObject modeInfo : wareHouse.getListJson(KeyWord.LOAD_MODE)) {
            info = new ModeInfo(wareHouse.toJson(), modeInfo);
            if (!isNull(info.getModeName())) {
                this.modeInfos.add(info);
            }
        }
        return !this.modeInfos.isEmpty();
    }
}
