/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.MBLT.VoltageTest;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Model.DataSource.ModeTest.FunctionConfig.FunctionConfig;
import Model.DataSource.ModeTest.FunctionConfig.FunctionName;
import Model.DataTest.FunctionData.FunctionData;
import Model.DataTest.FunctionParameters;
import Model.ManagerUI.UIStatus.UiStatus;

/**
 *
 * @author Administrator
 */
public class checkVolt extends AbsFunction {

    private final AnalysisBase analysisBase;
    private String value;

    public checkVolt(FunctionParameters parameters, String item) {
        super(parameters, item);
        this.analysisBase = new AnalysisBase(parameters, item);
    }
    
    public checkVolt(FunctionParameters parameters) {
        this(parameters, null);
    }

    @Override
    protected boolean test() {
        if (value == null) {
            return false;
        }
        return checkVolt(value);
    }

    public void setValue(String value) {
        this.value = value;
    }

    private boolean checkVolt(String value) {
        if (!this.analysisBase.isNumber(value)) {
            return false;
        }
        setResult(value);
        return true;
    }

}
