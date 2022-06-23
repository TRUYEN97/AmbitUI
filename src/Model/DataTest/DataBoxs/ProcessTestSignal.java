/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataTest.DataBoxs;

import Model.DataSource.ModeTest.FunctionConfig.FunctionName;
import Model.DataTest.ErrorLog;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author 21AK22
 */
public class ProcessTestSignal {

    private final HashMap<String, Object> signal;
    private static final String FUNC_SELECTED = "FuncSelected";

    public ProcessTestSignal() {
        this.signal = new HashMap<>();
    }

    public void put(String key, Object value) {
        this.signal.put(key, value);
    }

    public String getString(String key) {
        return (String) this.signal.get(key);
    }

    public void clear() {
        this.signal.clear();
    }

    public List<FunctionName> getFunctionSelected() {
        try {
            var list = this.signal.get(FUNC_SELECTED);
            if (list != null && list instanceof List) {
                return (List<FunctionName>) list;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getLocalizedMessage());
            return null;
        }
    }

    public Object get(String key) {
        return signal.get(key);
    }

}
