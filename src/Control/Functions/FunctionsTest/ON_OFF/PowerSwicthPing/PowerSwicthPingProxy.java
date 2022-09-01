/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.ON_OFF.PowerSwicthPing;

import Control.Functions.AbsFunction;
import Model.DataSource.ModeTest.FunctionConfig.FunctionName;
import Model.Factory.AbsProxy;

/**
 *
 * @author Administrator
 */
public class PowerSwicthPingProxy extends AbsProxy<FunctionName, AbsFunction>{

    @Override
    public AbsFunction takeIt() {
        return new PowerSwicthPing(getID());
    }
    
}
