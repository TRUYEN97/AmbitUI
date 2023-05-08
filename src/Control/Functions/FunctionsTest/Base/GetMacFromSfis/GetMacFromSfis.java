/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.GetMacFromSfis;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Model.DataTest.FunctionParameters;

/**
 *
 * @author Administrator
 */
public class GetMacFromSfis extends AbsFunction {

    private final FunctionBase baseFunc;

    public GetMacFromSfis(FunctionParameters functionParameters) {
        this(functionParameters, null);
    }
    
    

    public GetMacFromSfis(FunctionParameters parameters, String item) {
        super(parameters, item);
        this.baseFunc = new FunctionBase(parameters, item);
    }

    @Override
    public boolean test() {
        try {
            String mac = this.baseFunc.getMac();
            if (mac == null) {
                addLog(LOG_KEYS.PC, "MAC is null!!");
                return false;
            }
            setResult(mac);
            return true;
        } finally {
            addLog("MAC range 100000000000-ffffffffffff");
        }
    }

}
