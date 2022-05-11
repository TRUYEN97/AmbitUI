/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.GetMacFromSfis;

import Control.Functions.AbsFunction;
import View.subUI.UIWarehouse.AbsProxy;

/**
 *
 * @author Administrator
 */
public class GetMacFormSfisProxy extends AbsProxy<AbsFunction> {


    public GetMacFormSfisProxy(String type) {
        super(type);
    }
    
    @Override
    public GetMacFromSfis takeIt() {
        return new GetMacFromSfis(this.getName());
    }

}
