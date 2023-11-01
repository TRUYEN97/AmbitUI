/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Runin.PowerSwitchSnmp;

import Control.Functions.AbsFunction;
import Model.DataTest.FunctionParameters;
import Model.Factory.AbsProxy;

/**
 *
 * @author Administrator
 */
public class PowerSwitchSnmpProxy extends AbsProxy<FunctionParameters, AbsFunction>{


    @Override
    public AbsFunction takeIt() {
        return new PowerSwitchFuncSnmp(getParameter());
    }
    
}
