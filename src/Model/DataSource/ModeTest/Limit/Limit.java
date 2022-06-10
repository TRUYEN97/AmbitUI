/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataSource.ModeTest.Limit;

import Model.DataTest.ErrorLog;
import Model.DataSource.AbsJsonSource;
import Model.DataSource.DataWareHouse;
import Model.DataSource.Setting.Setting;
import Model.DataSource.Tool.FileService;
import Time.WaitTime.Class.TimeS;
import com.alibaba.fastjson.JSONObject;
import commandprompt.Communicate.Cmd.Cmd;
import java.util.Arrays;
import java.util.List;
import static java.util.Objects.isNull;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class Limit extends AbsJsonSource<LimitElement> {

    private static volatile Limit instaince;

    private Limit() {
        super();
    }

    public static Limit getInstance() {
        Limit ins = Limit.instaince;
        if (ins == null) {
            synchronized (Limit.class) {
                ins = Limit.instaince;
                if (ins == null) {
                    Limit.instaince = ins = new Limit();
                }
            }
        }
        return ins;
    }

    @Override
    public boolean init() {
        if (update()) {
            return true;
        }
        JOptionPane.showMessageDialog(null,
                "Update limit file failed!\r\nRead limit in local file.");
        return super.init();
    }

    public List<String> getListItemName() {
        return (List<String>) Arrays.asList((String[]) this.mapElemnts.keySet().toArray());
    }

    public LimitElement getItem(String itemName) {
        return this.mapElemnts.get(itemName);
    }

    @Override
    protected boolean getData() {
        DataWareHouse wareHouse = readFile.getData();
        JSONObject limits = wareHouse.getJson(LimitKeyWord.LIMITS);
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
        String command = Setting.getInstance().getUpdateLimitCommand();
        Cmd cmd = new Cmd();
        cmd.sendCommand(command);
        String newLimit = getNewLimit(cmd.readAll(new TimeS(10)));
        if (newLimit == null) {
            return false;
        }
        if (!saveToFile(readFile.getPath(), newLimit)) {
            return false;
        }
        return readFile.setData(newLimit) && resetData() && getData();
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
        System.out.println(newLimit);
        return new FileService().saveFile(path, newLimit);
    }

}
