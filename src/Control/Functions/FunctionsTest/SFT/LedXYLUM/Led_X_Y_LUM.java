/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.SFT.LedXYLUM;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import FileTool.FileService;
import Model.DataTest.FunctionParameters;
import Time.TimeBase;

/**
 *
 * @author Administrator
 */
public class Led_X_Y_LUM extends AbsFunction{

    private final FunctionBase functionBase;
    private final TimeBase timeBase;
    private final FileService service;
    public Led_X_Y_LUM(FunctionParameters parameter) {
        super(parameter, null);
        this.functionBase = new FunctionBase(parameter);
        this.timeBase = new TimeBase(TimeBase.UTC);
        this.service = new FileService();
    }

    @Override
    public boolean test() {
        return true;
    }
    
}
