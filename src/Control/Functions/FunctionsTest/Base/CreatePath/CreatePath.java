/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.CreatePath;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.FileBaseFunction;
import Model.DataTest.FunctionParameters;

/**
 *
 * @author Administrator
 */
public class CreatePath extends AbsFunction {

    private final FileBaseFunction fileBaseFunction;

    public CreatePath(FunctionParameters functionParameters, String itemName) {
        super(functionParameters, itemName);
        this.fileBaseFunction = new FileBaseFunction(functionParameters, itemName);
    }

    public CreatePath(FunctionParameters functionParameters) {
        this(functionParameters, null);
    }

    @Override
    protected boolean test() {
        String path = this.fileBaseFunction.createDefaultStringPath(this.processData.isPass());
        setResult(path);
        return true;
    }

}
