package Model.DataSource.Setting;

import Model.AllKeyWord;
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
public class Setting extends AbsJsonSource<String, ModeElement> implements IInit {

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
        for (JSONObject modeInfo : wareHouse.getListJson(AllKeyWord.LOAD_MODE)) {
            info = new ModeElement(wareHouse.toJson(), modeInfo);
            if (!isNull(info.getModeName())) {
                this.elements.add(info);
            }
        }
        return !this.elements.isEmpty();
    }

    public String getDhcpNetIP() {
        return this.readFile.getData().getString(AllKeyWord.DHCP);
    }

    public String getGiaiDoan() {
        return this.readFile.getData().getString(AllKeyWord.GIAI_DOAN);
    }

    public String getFunctionsLocalLogPath() {
        return this.readFile.getData().getString(AllKeyWord.LOCAL_FUNCTION_LOG);
    }

    public String getLocalLogPath() {
        return this.readFile.getData().getString(AllKeyWord.LOCAL_LOG);
    }

    public String getFtpHost() {
        return this.readFile.getData().getString(AllKeyWord.FTP_HOST);
    }

    public int getFtpPort() {
        return this.readFile.getData().getInteger(AllKeyWord.FTP_PORT);
    }

    public String getFtpPassWord() {
        return this.readFile.getData().getString(AllKeyWord.FTP_PASSWORD);
    }

    public String getFtpUser() {
        return this.readFile.getData().getString(AllKeyWord.FTP_USER);
    }

    public String getUpdateLimitCommand() {
        return this.readFile.getData().getString(AllKeyWord.LIMIT_CMD);
    }

    public boolean isOnDHCP() {
        return getDhcpNetIP() != null;
    }
}
