/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Runin.RebootSoft;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction;
import Model.DataTest.FunctionData.FunctionData;
import Model.ManagerUI.UIStatus.UiStatus;

/**
 *
 * @author Administrator
 */
public class RebootSoft extends AbsFunction {

    private final BaseFunction baseFunc;

    public RebootSoft(String itemName) {
        super(itemName);
        this.baseFunc = new BaseFunction(itemName);
    }

    @Override
    public void setResources(UiStatus uiStatus, FunctionData functionData) {
        super.setResources(uiStatus, functionData); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        this.baseFunc.setResources(uiStatus, functionData);
    }

    @Override
    protected boolean test() {
        String ip = this.baseFunc.getIp();
        addLog("IP: " + ip);
        if (ip == null) {
            return false;
        }
        return cycleReboot(ip);
    }

    private boolean cycleReboot(String ip) {
        int times = this.allConfig.getInteger("Times");
        addLog("CONFIG", "Times: " + times);
        for (int i = 1; i <= times; i++) {
            addLog(String.format("cycle Times: %d - %d ", i, times));
            if (!this.baseFunc.rebootSoft(ip) || !this.baseFunc.pingTo(ip, 200)) {
                return false;
            }
        }
        return true;
    }

}
