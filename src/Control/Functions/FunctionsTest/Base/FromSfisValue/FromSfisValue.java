/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.FromSfisValue;

import Control.Functions.AbsFunction;
import Model.DataTest.FunctionParameters;

/**
 *
 * @author Administrator
 */
public class FromSfisValue extends AbsFunction{

    public FromSfisValue(FunctionParameters functionParameters) {
        this(functionParameters, null);
    }
    
    public FromSfisValue(FunctionParameters functionParameters, String itemName) {
        super(functionParameters, itemName);
    }

    
    @Override
    protected boolean test() {
        String key = this.config.getString("key");
        addLog(LOG_KEYS.CONFIG, "key: \"%s\"", key);
        if (key == null) {
            return false;
        }
        String value = this.processData.getString(key);
        addLog(LOG_KEYS.PC, "value: \"%s\"", value);
        if (value == null) {
            return false;
        }
        setResult(value);
        return true;
    }
    
}
