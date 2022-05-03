/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataSource.Limit;

import Model.DataSource.AbsJsonSource;
import Model.DataWareHouse;
import com.alibaba.fastjson.JSONObject;
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
        for (var itemName : limits.keySet()) {
            info = new LimitElement(itemName, wareHouse.toJson(), limits.getJSONObject(itemName));
            this.mapElemnts.put(itemName, info);
        }
        return !this.mapElemnts.isEmpty();
    }

}
