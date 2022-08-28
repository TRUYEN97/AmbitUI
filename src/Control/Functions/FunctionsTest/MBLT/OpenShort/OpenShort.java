/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.MBLT.OpenShort;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Control.Functions.FunctionsTest.Base.FixtureActions.FixtureAction;
import Model.DataSource.ModeTest.FunctionConfig.FunctionElement;
import Model.DataTest.FunctionData.FunctionData;
import Model.ErrorLog;
import Model.ManagerUI.UIStatus.UiStatus;
import Time.WaitTime.Class.TimeMs;
import Time.WaitTime.Class.TimeS;
import Communicate.Comport.ComPort;

/**
 *
 * @author Administrator
 */
public class OpenShort extends AbsFunction {

    private final FixtureAction fixtureAction;
    private final FunctionBase functionBase;
    private final AnalysisBase analysisBase;

    public OpenShort(String itemName) {
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
        this.functionBase.setResources(funcConfig, uiStatus, functionData);
    }

    @Override
    public boolean test() {
        if ((retry > 1 && !reConnectPortFixture()) || !stopAutoboot()) {
            return false;
        }
        return getCurrent();
    }

    public boolean stopAutoboot() {
        String dutCom;
        int dutBaud;
        dutCom = this.allConfig.getString("DutCom");
        dutBaud = this.allConfig.getInteger("DutBaudRate", 9600);
        ComPort dut = this.functionBase.getComport(dutCom, dutBaud);
        if (dut == null) {
            return false;
        }
        try {
            String keyWord = this.allConfig.getString("DutKey");
            addLog("Config", String.format("Key word: %s", keyWord));
            int time = this.allConfig.getInteger("DutWait", 1);
            addLog("Config", String.format("Test about %s s", time));
            TimeS timer = new TimeS(time);
            String line;
            while (timer.onTime()) {
                line = dut.readLine(new TimeMs(500));
                addLog("DUT", line == null ? "" : line);
                if (line != null && line.trim().endsWith(keyWord)) {
                    dut.insertCommand("reset");
                    return true;
                }
                dut.insertCommand("reboot");
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

    public boolean reConnectPortFixture() {
        return this.fixtureAction.test();
    }

    public boolean getCurrent() {
        ComPort fixture = this.fixtureAction.getComport();
        try {
            String command = this.allConfig.getString("CurrentTest");
            if (fixture == null || !this.functionBase.sendCommand(fixture, command)) {
                return false;
            }
            String regex = this.allConfig.getString("CurrentRegex");
            String value = this.analysisBase.getValue(fixture, regex, new TimeS(1));
            if (this.analysisBase.isNumber(value)) {
                double ampe = Double.parseDouble(value) / 1000.0;
                addLog("PC", String.format("Current: %s A", ampe));
                setResult(ampe);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getMessage());
            return false;
        } finally {
            if (fixture != null) {
                fixture.disConnect();
            }
        }
    }
}
