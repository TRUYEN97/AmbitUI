/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.GetMacFromSfis;

import Control.Functions.AbsFunction;
import Model.DataSource.ModeTest.FunctionConfig.FunctionName;
import Model.Factory.AbsProxy;

/**
 *
 * @author Administrator
 */
public class GetMacFormSfisProxy extends AbsProxy<FunctionName, AbsFunction> {


    public GetMacFormSfisProxy() {
    }
    
    @Override
    public GetMacFromSfis takeIt() {
        return new GetMacFromSfis(getID());
    }

}
