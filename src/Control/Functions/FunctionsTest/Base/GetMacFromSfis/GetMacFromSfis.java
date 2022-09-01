/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.GetMacFromSfis;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Model.DataSource.ModeTest.FunctionConfig.FunctionElement;
import Model.DataSource.ModeTest.FunctionConfig.FunctionName;
import Model.DataTest.FunctionData.FunctionData;
import Model.ManagerUI.UIStatus.UiStatus;

/**
 *
 * @author Administrator
 */
public class GetMacFromSfis extends AbsFunction {

    private final FunctionBase baseFunc;

    public GetMacFromSfis(FunctionName itemName) {
        super(itemName);
        this.baseFunc = new FunctionBase(itemName);
    }

    @Override
    public void setResources(FunctionElement funcConfig, UiStatus uiStatus, FunctionData functionData) {
        super.setResources(funcConfig, uiStatus, functionData); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        this.baseFunc.setResources(funcConfig, uiStatus, functionData);
    }

    @Override
    public boolean test() {
        try {
            String mac = this.baseFunc.getMac();
            if (mac == null) {
                return false;
            }
            setResult(mac);
            return true;
        } finally {
            addLog("MAC range 100000000000-ffffffffffff");
        }
    }

}
