package Control.Functions.FunctionsTest.Base.JsonApi.UploadLog;

import Control.Functions.AbsFunction;
import Model.DataTest.FunctionParameters;
import Model.Factory.AbsProxy;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Administrator
 */
public class UploadLogProxy extends AbsProxy<FunctionParameters, AbsFunction>{

    @Override
    public AbsFunction takeIt() {
        return new UploadLog(getParameter());
    }
    
}
