/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.CheckProduct;

import Control.Functions.AbsFunction;
import Model.DataTest.FunctionParameters;
import com.alibaba.fastjson.JSONObject;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class CheckProduct extends AbsFunction {

    public CheckProduct(FunctionParameters functionParameters, String itemName) {
        super(functionParameters, itemName);
    }

    public CheckProduct(FunctionParameters functionParameters) {
        this(functionParameters, null);
    }

    @Override
    protected boolean test() {
        addLog("PC", "Check product info!");
        List<JSONObject> elems = this.config.getListJsonArray("elems");
        if (elems == null || elems.isEmpty()) {
            addLog("PC", "Nothing to check!");
            return true;
        }
        for (JSONObject elem : elems) {
            String key = elem.getString("key");
            String target = elem.getString("target");
            String mess = elem.getString("messenger");
            String value = this.processData.getString(key);
            addLog("CONFIG", "key: %s - value: %s - target: %s ", key, value, target);
            if ((value == null && target == null) || isContainTarget(value, target)) {
                addLog("PC", "Compare success!");
            } else {
                addLog("PC", "Compare failed!");
                addLog("PC", "Messenger: %s", mess);
                this.processData.setMessage(mess);
                return false;
            }
        }
        addLog("PC", "Check done!");
        return true;
    }

    private boolean isContainTarget(String value, String targets){
        if (targets == null) {
            return false;
        }
        String[] specs = targets.split(",|\\|");
        for (String spec : specs) {
            if (value != null && value.trim().equalsIgnoreCase(spec.trim())) {
                return true;
            }
        }
        return false;
    }
}
