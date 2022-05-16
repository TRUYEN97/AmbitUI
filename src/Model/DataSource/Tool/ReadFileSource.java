/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DataSource.Tool;

import Model.Interface.IInit;
import Model.Interface.ITypeRead;
import Control.Message;
import Model.DataSource.DataWareHouse;
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
    private final DataWareHouse data;

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
            Message.WriteMessger.nameNotAlreadyExistError(path, getAllClassName());
            return false;
        }
        return true;
    }

    @Override
    public boolean init() {
        if (!readFile(path)) {
            return false;
        }
        Message.WriteMessger.Console("The name: %s Load ok!\r\n%s", path, getAllClassName(), null);
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
            this.data.setData(type.readContain(new BufferedReader(new FileReader(file))));
            return true;
        } catch (Exception ex) {
            Message.WriteMessger.Console("%s\r\nRead file fail!\r\n%s\r\n%s",
                    path, getAllClassName(), ex.getMessage());
            Message.WriteMessger.ConfixErro("%s\r\nRead file fail!\r\n%s",
                    path, getAllClassName(), null);
            return false;
        }
    }

    private String getAllClassName() {
        return this.getClass().getName();
    }

}
