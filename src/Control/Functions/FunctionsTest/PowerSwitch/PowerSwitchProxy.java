/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.PowerSwitch;

import Model.Factory.AbsProxy;

/**
 *
 * @author Administrator
 */
public class PowerSwitchProxy extends AbsProxy<PowerSwitchFunc>{

    @Override
    public PowerSwitchFunc takeIt() {
        return new PowerSwitchFunc(getTypeName());
    }
    
}
