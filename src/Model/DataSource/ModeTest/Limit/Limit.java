/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataSource.ModeTest.Limit;

import FileTool.FileService;
import Model.AllKeyWord;
import Model.ErrorLog;
import Model.DataSource.AbsJsonSource;
import Model.DataSource.DataWareHouse;
import com.alibaba.fastjson.JSONObject;
import Communicate.Cmd.Cmd;
import static java.util.Objects.isNull;
import java.util.Set;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class Limit extends AbsJsonSource<String, LimitElement> {

    private String updateCmd;

    public Limit() {
        super();
    }

    @Override
    public boolean init() {
        return update() && super.init();
    }

    public AbsJsonSource setUpdateCommand(String cmd) {
        this.updateCmd = cmd;
        return this;
    }

    public Set<String> getListItemName() {
        return mapElemnts.keySet();
    }

    public LimitElement getItem(String itemName) {
        return this.mapElemnts.get(itemName);
    }

    @Override
    protected boolean getData() {
        DataWareHouse wareHouse = readFile.getData();
        JSONObject limits = wareHouse.getJson(AllKeyWord.LIMITS);
        if (isNull(limits)) {
            return false;
        }
        return getAllElemant(limits, wareHouse);
    }

    private boolean getAllElemant(JSONObject limits, DataWareHouse wareHouse) {
        LimitElement info;
        for (var itemName : limits.keySet()) {
            info = new LimitElement(itemName, wareHouse.toJson(), limits.getJSONObject(itemName));
            this.mapElemnts.put(itemName, info);
        }
        return !this.mapElemnts.isEmpty();
    }

    private boolean update() {
        Cmd cmd = new Cmd();
        cmd.sendCommand(updateCmd);
        String newLimit = getNewLimit(cmd.readAll());
        if (newLimit == null) {
            return false;
        }
        return saveToFile(readFile.getPath(), newLimit);
    }

    private String getNewLimit(String response) {
        if (response == null) {
            return null;
        }
        if (!(response.contains("{") && response.contains("}")
                && (response.indexOf("{") < response.lastIndexOf("}")))) {
            return null;
        }
        String newLimit = getJsonInString(response);
        if (isNotJson(newLimit)) {
            return null;
        }
        return newLimit;
    }

    private String getJsonInString(String response) {
        return response.substring(response.indexOf("{"),
                response.lastIndexOf("}") + 1);
    }

    private boolean isNotJson(String newLimit) {
        try {
            return JSONObject.parseObject(newLimit) == null;
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getMessage());
            return false;
        }
    }

    private boolean saveToFile(String path, String newLimit) {
        return new FileService().saveFile(path, newLimit);
    }

}
