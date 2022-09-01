/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.ON_OFF.AsideOnOffPing;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.DutPing.DutPing;
import Control.Functions.FunctionsTest.MBLT.UsbAside.UsbAside;
import Model.DataSource.ModeTest.FunctionConfig.FunctionElement;
import Model.DataSource.ModeTest.FunctionConfig.FunctionName;
import Model.DataTest.FunctionData.FunctionData;
import Model.ErrorLog;
import Model.ManagerUI.UIStatus.UiStatus;

/**
 *
 * @author Administrator
 */
public class AsideOnOffPing extends AbsFunction{

    private final UsbAside aside;
    private final DutPing dutPing;
    public AsideOnOffPing(FunctionName itemName) {
        super(itemName);
        this.aside = new UsbAside(itemName);
        this.dutPing = new DutPing(itemName);
    }

    @Override
    public void setResources(FunctionElement functionElement, UiStatus uiStatus, FunctionData functionData) {
        super.setResources(functionElement, uiStatus, functionData); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        this.aside.setResources(functionElement, uiStatus, functionData);
        this.dutPing.setResources(functionElement, uiStatus, functionData);
    }
    

    @Override
    protected boolean test() {
         try {
            int times = this.allConfig.getInteger("CycleTimes",1);
            addLog("CONFIG", "Times: " + times);
            for (int i = 1; i <= times; i++) {
                addLog(String.format("cycle Times: %d - %d ", i, times));
                if (!this.aside.test() || !this.dutPing.test()) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            addLog(e.getMessage());
            ErrorLog.addError(this, e.getMessage());
            return false;
        }
    }
    
}
