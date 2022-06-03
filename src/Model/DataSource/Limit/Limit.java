/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataSource.Limit;

import Model.DataSource.AbsJsonSource;
import Model.DataSource.DataWareHouse;
import Model.DataSource.Setting.Setting;
import Model.Interface.IInit;
import Time.WaitTime.Class.TimeS;
import com.alibaba.fastjson.JSONObject;
import commandprompt.AbstractStream.SubClass.ReadStream;
import commandprompt.Communicate.Cmd.Cmd;
import java.util.Arrays;
import java.util.List;
import static java.util.Objects.isNull;

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
        update();
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
        LimitElement info;
        JSONObject limits = wareHouse.getJson(KeyWord.LIMITS);
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

    private void update() {
        String command = Setting.getInstance().getUpdateLimitCommand();
        Cmd cmd = new Cmd();
        cmd.sendCommand(command);
        String response = cmd.readAll(new TimeS(10));
        System.out.println("Updata Limit ok");
        System.out.println(response);
    }

}
