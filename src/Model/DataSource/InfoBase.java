/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DataSource;

import Model.Interface.IInit;
import Model.ManagerUI.DataWareHouse;
import Control.Message;
import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 *
 * @author Administrator
 */
public class InfoBase implements IInit {

    private static volatile InfoBase infoBase;
    private String path;
    private final DataWareHouse data;

    private InfoBase() {
        this.data = new DataWareHouse();
    }

    public static InfoBase getInstance() {
        InfoBase ins = InfoBase.infoBase;
        if (ins == null) {
            synchronized (InfoBase.class) {
                ins = InfoBase.infoBase;
                if (ins == null) {
                    InfoBase.infoBase = ins = new InfoBase();
                }
            }
        }
        return ins;
    }

    public String getPathOfLimit() {
        return getValueOf("limitPath");
    }

    public String getPathOfErroCode() {
        return getValueOf("ErroCodePath");
    }

    public String getPathOfAmbitconfig() {
        return getValueOf("ambitconfigPath");
    }

    public String getPathOfSfis() {
        return getValueOf("Sfis");
    }

    public String getPathOfSetting() {
        return getValueOf("Setting");
    }

    public void setPath(String path) {
        try {
            getInputStream(path);
            this.path = path;
        } catch (Exception e) {
            e.printStackTrace();
            Message.WriteMessger.nameNotAlreadyExistError(path, this.getClass().getName());
        }
    }

    public String getCommandForLimit() {
        if (getString("CommandLimit") == null) {
            showMessagerCommandErro("CommandLimit");
        }
        return getString("CommandLimit");
    }

    private String getValueOf(String key) {
        if (getString(key) == null) {
            showMessagerPathErro(key);
        }
        return getString(key);
    }

    private void showMessagerPathErro(String namePath) {
        Message.WriteMessger.ConfixErro("Path of %s not realydy!\r\n%s", namePath,
                this.getClass().getName(), null);
    }

    private void showMessagerCommandErro(String nameCmd) {
        Message.WriteMessger.ConfixErro("Command of %s not realydy!\r\n%s", nameCmd,
                this.getClass().getName(), null);
    }

    private String getString(String key) {
        String value = this.data.getString(key);
        if (value == null) {
            Message.WriteMessger.ConfixErro("key not exists!: %s", key,
                    this.getClass().getName(), null);
            return null;
        }
        return value;
    }

    private BufferedReader getBuffereReader(String path) {
        return new BufferedReader(getInputStream(path));
    }

    private InputStreamReader getInputStream(String path) {
        return new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(path));
    }

    @Override
    public boolean init() {
        BufferedReader reader;
        try {
            reader = getBuffereReader(path);
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\r\n");
            }
            this.data.setData(JSONObject.parseObject(builder.toString()));
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Message.WriteMessger.ConfixErro("%s\r\nConvert String to json fail\r\n%s\r\n%s", path, this.getClass().getName(), ex.toString());
        }
        return false;
    }
}
