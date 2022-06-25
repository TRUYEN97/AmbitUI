/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.SFIS;

import Control.Functions.AbsFunction;
import Model.Factory.AbsProxy;

/**
 *
 * @author 21AK22
 */
public class SfisFunctionProxy extends AbsProxy<AbsFunction> {

    @Override
    public AbsFunction takeIt() {
        return new SfisFunction(getName());
    }

}
