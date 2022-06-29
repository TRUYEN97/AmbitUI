/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DataSource.Tool;

import Model.ErrorLog;
import Model.Interface.IInit;
import Model.Interface.ITypeRead;
import Model.DataSource.DataWareHouse;
import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 *
 * @author Administrator
 */
public class ReadFileSource implements IInit {

    protected static final String NEW_LINE = "\r\n";
    private String path;
    private final ITypeRead type;
    protected final DataWareHouse data;

    public ReadFileSource(ITypeRead typeRead) {
        this.type = typeRead;
        this.data = new DataWareHouse();
    }

    public boolean setPath(String path) {
        if (fileIsExists(path)) {
            this.path = path;
            return true;
        }
        return false;
    }

    private boolean fileIsExists(String path) {
        if (!new File(path).exists()) {
            ErrorLog.addError(this, path + " not exists");
            return false;
        }
        return true;
    }

    @Override
    public boolean init() {
        if (!readFile(path)) {
            return false;
        }
        System.out.println(String.format("The name: %s Load ok!\r\n%s",
                path, getAllClassName()));
        return true;
    }

    public DataWareHouse getData() {
        return data;
    }

    private boolean readFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return false;
        }
        try {
            this.data.clear();
            this.data.putAll(type.readContain(new BufferedReader(new FileReader(file))));
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            ErrorLog.addError(this, ex.getMessage());
            return false;
        }
    }

    private String getAllClassName() {
        return this.getClass().getName();
    }

    public String getPath() {
        return path;
    }

    public boolean setData(JSONObject limit) {
        this.data.clear();
        return this.data.putAll(limit);
    }

    public boolean setData(String newlimit) {
        return setData(JSONObject.parseObject(newlimit));
    }

}
