/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.MBLT.UsbAside;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Control.Functions.FunctionsTest.Base.FixtureActions.FixtureAction;
import Model.DataSource.ModeTest.FunctionConfig.FunctionElement;
import Model.DataTest.FunctionData.FunctionData;
import Model.ErrorLog;
import Model.ManagerUI.UIStatus.UiStatus;
import Time.WaitTime.Class.TimeMs;
import Time.WaitTime.Class.TimeS;
import Communicate.Comport.ComPort;
import Model.DataSource.ModeTest.FunctionConfig.FunctionName;

/**
 *
 * @author Administrator
 */
public class UsbAside extends AbsFunction {

    private final FixtureAction fixture;
    private final FunctionBase functionBase;

    public UsbAside(FunctionName itemName) {
        super(itemName);
        this.fixture = new FixtureAction(itemName);
        this.functionBase = new FunctionBase(itemName);
    }

    @Override
    public void setResources(FunctionElement functionElement, UiStatus uiStatus, FunctionData functionData) {
        super.setResources(functionElement, uiStatus, functionData); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        this.fixture.setResources(functionElement, uiStatus, functionData);
        this.functionBase.setResources(functionElement, uiStatus, functionData);
    }

    @Override
    public boolean test() {
        if (!this.fixture.test()) {
            return false;
        }
        String dutCom = this.allConfig.getString("DutCom");
        int dutBaud = this.allConfig.getInteger("DutBaudRate", 9600);
        int dutWait = this.allConfig.getInteger("DutWait", 1);
        addLog("Config", String.format("Test about %s s", dutWait));
        ComPort dut = this.functionBase.getComport(dutCom, dutBaud);
        if (dut == null) {
            return false;
        }
        try {
            TimeS timer = new TimeS(dutWait);
            String line;
            while (timer.onTime()) {
                line = dut.readAll(new TimeMs(500));
                addLog("DUT", line == null ? "" : line);
                if (line != null && !line.trim().isBlank()) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getMessage());
            return false;
        } finally {
            this.functionBase.disConnect(dut);
        }
    }

}
