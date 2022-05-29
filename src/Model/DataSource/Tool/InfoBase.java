/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DataSource.Tool;

import Model.DataModeTest.ErrorLog;
import Model.Interface.IInit;
import Model.DataSource.DataWareHouse;
import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
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
            ErrorLog.addError(this, e.getLocalizedMessage());
        }
    }

    public String getCommandForLimit() {
        return getString("CommandLimit");
    }

    private String getValueOf(String key) {
        return getString(key);
    }

    private String getString(String key) {
        return this.data.getString(key);
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
            this.data.putAll(JSONObject.parseObject(builder.toString()));
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
           ErrorLog.addError(this, ex.getLocalizedMessage());
        }
        return false;
    }
}
