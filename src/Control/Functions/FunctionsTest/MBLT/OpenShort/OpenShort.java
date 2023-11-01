/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.MBLT.OpenShort;

import Communicate.Impl.Comport.ComPort;
import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Control.Functions.FunctionsTest.Base.FixtureActions.FixtureAction;
import Model.ErrorLog;
import Time.WaitTime.Class.TimeMs;
import Time.WaitTime.Class.TimeS;
import Model.DataTest.FunctionParameters;

/**
 *
 * @author Administrator
 */
public class OpenShort extends AbsFunction {

    private final FixtureAction fixtureAction;
    private final FunctionBase functionBase;
    private final AnalysisBase analysisBase;

    public OpenShort(FunctionParameters parameters) {
        this(parameters, null);
    }

    public OpenShort(FunctionParameters parameters, String item) {
        super(parameters, item);
        this.functionBase = new FunctionBase(parameters, item);
        this.fixtureAction = new FixtureAction(parameters, item);
        this.analysisBase = new AnalysisBase(parameters, item);
    }

    @Override
    public boolean test() {
        if ((turn > 1 && !reConnectPortFixture()) || !stopAutoboot()) {
            return false;
        }
        return getCurrent();
    }

    public boolean stopAutoboot() {
        String dutCom;
        int dutBaud;
        dutCom = this.config.getString("DutCom");
        dutBaud = this.config.getInteger("DutBaudRate", 9600);
        try ( ComPort dut = this.functionBase.getComport(dutCom, dutBaud)) {
            if (dut == null) {
                return false;
            }
            String keyWord = this.config.getString("DutKey");
            addLog("Config", String.format("Key word: %s", keyWord));
            int time = this.config.getInteger("DutWait", 1);
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
        }
    }

    public boolean reConnectPortFixture() {
        return this.fixtureAction.test();
    }

    public boolean getCurrent() {
        try(ComPort fixture = this.fixtureAction.getComport()) {
            String command = this.config.getString("CurrentTest");
            if (fixture == null || !this.functionBase.sendCommand(fixture, command)) {
                return false;
            }
            String regex = this.config.getString("CurrentRegex");
            String value = this.analysisBase.getValue(fixture, regex, new TimeS(1), null);
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
        }
    }
}
