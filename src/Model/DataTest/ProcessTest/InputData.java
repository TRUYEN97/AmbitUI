/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataTest.ProcessTest;

import Model.AllKeyWord;
import com.alibaba.fastjson.JSONObject;
import java.util.Set;

/**
 *
 * @author 21AK22
 */
public class InputData {

    private final JSONObject data;

    public InputData() {
        this.data = new JSONObject();
//        this.data.put(PCNAME, PcInformation.getInstance().getPcName());
        this.data.put(AllKeyWord.STATION_NAME, "REPAIR-7801");
        this.data.put(AllKeyWord.STATION_TYPE, "REPAIR");
        this.data.put(AllKeyWord.VERSION, "1.0.0");
    }

    public void setInput(String input) {
        this.data.put(AllKeyWord.SN, input);
    }

    public String getInput() {
        return this.data.getString(AllKeyWord.SN);
    }

    public String getIndex() {
        return this.data.getString(AllKeyWord.INDEX);
    }

    public void setIndex(String index) {
        this.data.put(AllKeyWord.INDEX, index);
    }

    public String getValue(String key) {
        return this.data.getString(key);
    }

    public JSONObject toJson() {
        return data;
    }

    public Set<String> getkeySet() {
        return this.data.keySet();
    }

    public void put(String key, String value) {
        this.data.put(key, value);
    }

}
