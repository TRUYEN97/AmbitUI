package Model.DataSource.Setting;

import Model.AllKeyWord;
import Model.DataSource.AbsJsonSource;
import Model.Interface.IInit;
import Model.DataSource.DataWareHouse;
import Model.DataSource.dhcp.DhcpConfig;
import com.alibaba.fastjson.JSONObject;
import java.awt.Color;
import static java.util.Objects.isNull;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Administrator
 */
public class Setting extends AbsJsonSource<String, ModeElement> implements IInit {

    private static volatile Setting instaince;
    private final DhcpConfig dhcpConfig;

    private Setting() {
        super();
        this.dhcpConfig = new DhcpConfig();
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
        for (JSONObject modeInfo : wareHouse.getListJson(AllKeyWord.LOAD_MODE)) {
            info = new ModeElement(wareHouse.toJson(), modeInfo);
            if (!isNull(info.getModeName())) {
                this.elements.add(info);
            }
        }
        JSONObject dhcpJsonConfig = wareHouse.getJson("dhcp");
        if (dhcpJsonConfig != null) {
            this.dhcpConfig.setData(dhcpJsonConfig);
        }
        return !this.elements.isEmpty();
    }

    public DhcpConfig getDhcpConfig() {
        return dhcpConfig;
    }

    public String getProgress() {
        return this.readFile.getData().getString(AllKeyWord.PROGRESS);
    }

    public String getLocalLogPath() {
        String local = this.readFile.getData().getString(AllKeyWord.LOCAL_LOG);
        if (local == null) {
            return "Log/TestLog";
        }
        return local;
    }
    public Color getTestColor() {
        String color = this.readFile.getData().getString(AllKeyWord.TEST_COLOR);
        if (color == null) {
            return Color.yellow;
        }
        return new Color(Integer.parseInt(color));
    }

    public String getUpdateLimitCommand() {
        return this.readFile.getData().getString(AllKeyWord.UPDATE_LIMIT_CMD);
    }

    public String getLimitPath() {
        return this.readFile.getData().getString(AllKeyWord.LIMIT_PATH);
    }

    public String getUutMolel() {
        return this.readFile.getData().getString(AllKeyWord.CONFIG.UUT_MODEL);
    }
}
