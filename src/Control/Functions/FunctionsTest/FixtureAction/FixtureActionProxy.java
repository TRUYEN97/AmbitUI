/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.FixtureAction;

import Control.Functions.AbsFunction;
import View.subUI.UIWarehouse.AbsProxy;

/**
 *
 * @author Administrator
 */
public class FixtureActionProxy extends AbsProxy<AbsFunction> {

    public FixtureActionProxy(String type) {
        super(type);
    }

    @Override
    public AbsFunction takeIt() {
        return new FixtureAction(getName());
    }

}
