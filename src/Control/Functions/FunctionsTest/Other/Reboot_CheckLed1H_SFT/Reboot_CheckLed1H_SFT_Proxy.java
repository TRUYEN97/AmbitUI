/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Other.Reboot_CheckLed1H_SFT;

import Control.Functions.FunctionsTest.Other.CheckLed_W_1H_SFT.CheckLed_W_1H;
import Control.Functions.AbsFunction;
import Model.DataTest.FunctionParameters;
import Model.Factory.AbsProxy;

/**
 *
 * @author Administrator
 */
public class Reboot_CheckLed1H_SFT_Proxy extends AbsProxy<FunctionParameters, AbsFunction>{

    @Override
    public AbsFunction takeIt() {
        return new Reboot_CheckLed1H_SFT(getParameter());
    }
    
}
