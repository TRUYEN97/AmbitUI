/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataTest;

import Control.Core.ModeTest;
import Model.DataSource.ModeTest.FunctionConfig.FunctionConfig;
import Model.DataTest.FunctionData.FunctionData;
import Model.ManagerUI.UIStatus.UiStatus;

/**
 *
 * @author 21AK22
 */
public class FunctionParameters {
    private final FunctionConfig functionElement;
    private final UiStatus uiStatus;
    private final FunctionData functionData;
    private final ModeTest modeTest;

    public FunctionParameters(FunctionConfig functionElement, UiStatus uiStatus, FunctionData functionData) {
        this.functionElement = functionElement;
        this.uiStatus = uiStatus;
        this.functionData = functionData;
        this.uiStatus.getProcessData().addFunctionData(functionData);
        this.modeTest = this.uiStatus.getModeTest();
    }

    public FunctionConfig getFunctionConfig() {
        return functionElement;
    }

    public UiStatus getUiStatus() {
        return uiStatus;
    }

    public FunctionData getFunctionData() {
        return functionData;
    }

    public ModeTest getModeTest() {
        return modeTest;
    }
    
    
}
