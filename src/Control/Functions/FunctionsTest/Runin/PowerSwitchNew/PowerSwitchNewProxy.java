/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Runin.PowerSwitchNew;

import Control.Functions.FunctionsTest.Runin.PowerSwitchSnmp.PowerSwitchFuncSnmp;
import Control.Functions.AbsFunction;
import Model.DataTest.FunctionParameters;
import Model.Factory.AbsProxy;

/**
 *
 * @author Administrator
 */
public class PowerSwitchNewProxy extends AbsProxy<FunctionParameters, AbsFunction>{


    @Override
    public AbsFunction takeIt() {
        return new PowerSwitchFuncSnmp(getParameter());
    }
    
}
