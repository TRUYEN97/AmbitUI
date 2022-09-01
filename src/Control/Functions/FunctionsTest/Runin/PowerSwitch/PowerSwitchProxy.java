/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Runin.PowerSwitch;

import Control.Functions.AbsFunction;
import Model.DataSource.ModeTest.FunctionConfig.FunctionName;
import Model.Factory.AbsProxy;

/**
 *
 * @author Administrator
 */
public class PowerSwitchProxy extends AbsProxy<FunctionName, AbsFunction>{


    @Override
    public AbsFunction takeIt() {
        return new PowerSwitchFunc(getID());
    }
    
}
