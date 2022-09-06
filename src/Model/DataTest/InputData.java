/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataTest;

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
        this.data.put(AllKeyWord.VERSION, "1.1.1");
    }

    public void setInput(String input) {
        this.data.put(AllKeyWord.SFIS.SFIS_SN, input.toUpperCase());
    }

    public String getInput() {
        return this.data.getString(AllKeyWord.SFIS.SFIS_SN);
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
