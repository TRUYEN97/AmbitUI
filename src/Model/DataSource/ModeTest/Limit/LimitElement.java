/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataSource.ModeTest.Limit;

import Model.DataSource.AbsElementInfo;
import com.alibaba.fastjson.JSONObject;

/**
 *
 * @author Administrator
 */
public class LimitElement extends AbsElementInfo {

    private final String itemName;

    LimitElement(String itemName, JSONObject base, JSONObject element) {
        super(KeyWord.KEYS, base, base);
        this.itemName = itemName;
    }

    public String getModeName() {
        return itemName;
    }

}
