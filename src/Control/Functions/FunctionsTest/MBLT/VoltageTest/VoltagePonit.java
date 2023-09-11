/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.MBLT.VoltageTest;

import Control.Functions.AbsFunction;
import Model.DataTest.FunctionParameters;

/**
 *
 * @author Administrator
 */
public class VoltagePonit extends AbsFunction{

    private final String value;
    
    public VoltagePonit(FunctionParameters functionParameters, String itemName, String value) {
        super(functionParameters, itemName);
        this.value = value;
    }
    
    @Override
    public boolean test() {
        addLog(LOG_KEYS.PC, "Item name: %s", getItemName());
        addLog(LOG_KEYS.PC, "value: %s", value);
        if(this.value == null || this.value.isBlank()){
            return false;
        }
        setResult(value);
        return true;
    }
    
}
