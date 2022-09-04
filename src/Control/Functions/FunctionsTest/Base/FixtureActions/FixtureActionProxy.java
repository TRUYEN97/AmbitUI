/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.FixtureActions;

import Control.Functions.AbsFunction;
import Model.DataTest.FunctionParameters;
import Model.Factory.AbsProxy;

/**
 *
 * @author Administrator
 */
public class FixtureActionProxy extends AbsProxy<FunctionParameters, AbsFunction>{

    @Override
    public AbsFunction takeIt() {
        return new FixtureAction(getParameter());
    }
    
}
