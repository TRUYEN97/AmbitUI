package Model.DataSource.Setting;

import Control.FileType.FileJson;
import Model.Interface.IInit;
import Model.DataSource.ReadFileSource;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.List;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Administrator
 */
public class Setting extends AbsSetting implements IInit {

    private static volatile Setting instaince;
    private final ReadFileSource readFile;
    private final List<JSONObject> modeInfos;

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

    public List<JSONObject> getModeInfos() {
        return modeInfos;
    }

    public boolean setFile(String path) {
        if (path == null) {
            return false;
        }
        this.readFile.addPathFile(path);
        return true;
    }

    public JSONObject getModeSetting(String name) {
        for (JSONObject modeInfo : modeInfos) {
            if (modeInfo.containsKey(KeyWord.NAME) && modeInfo.getString(name).equals(name)) {
                return modeInfo;
            }
        }
        return null;
    }

    @Override
    public boolean init() {
        boolean success = readFile.init();
        if (success) {
            this.setData(readFile.toJson());
            getMode();
        }
        return success;
    }

    public int getCountMode() {
        return getModeInfos().size();
    }

    private void getMode() {
        for (var elem : getLoadMode()) {
            JSONObject json = (JSONObject) elem;
            if (json != null && json.containsKey(KeyWord.NAME)) {
                modeInfos.add(json);
            }
        }
    }

    private JSONArray getLoadMode() {
        return this.warehouse.getJSONArray(KeyWord.LOAD_MODE);
    }
}
