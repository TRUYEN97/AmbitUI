/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.ON_OFF.FixturePing;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Control.Functions.FunctionsTest.Base.FixtureActions.FixtureAction;
import Model.DataSource.ModeTest.FunctionConfig.FunctionElement;
import Model.DataTest.FunctionData.FunctionData;
import Model.ErrorLog;
import Model.ManagerUI.UIStatus.UiStatus;

/**
 *
 * @author Administrator
 */
public class FixturePing extends AbsFunction {

    private final FixtureAction fixtureAction;
    private final FunctionBase functionBase;
    private final AnalysisBase analysisBase;

    public FixturePing(String itemName) {
        super(itemName);
        this.functionBase = new FunctionBase(itemName);
        this.fixtureAction = new FixtureAction(itemName);
        this.analysisBase = new AnalysisBase(itemName);
    }

    @Override
    public void setResources(FunctionElement funcConfig, UiStatus uiStatus, FunctionData functionData) {
        super.setResources(funcConfig, uiStatus, functionData); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        this.fixtureAction.setResources(funcConfig, uiStatus, functionData);
        this.analysisBase.setResources(funcConfig, uiStatus, functionData);
    }

    @Override
    public boolean test() {
        try {
            int times = this.allConfig.getInteger("CycleTimes");
            addLog("CONFIG", "Times: " + times);
            String ip = this.analysisBase.getIp();
            addLog("IP: " + ip);
            if (ip == null) {
                return false;
            }
            for (int i = 1; i <= times; i++) {
                addLog(String.format("cycle Times: %d - %d ", i, times));
                if (!fixtureAction.test() || !this.functionBase.pingTo(ip, 50)) {
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
