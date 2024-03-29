/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.MBLT.UsbAside;

import Communicate.Impl.Comport.ComPort;
import Control.Functions.AbsFunction;
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
public class UsbAside extends AbsFunction {

    private final FixtureAction fixture;
    private final FunctionBase functionBase;

    public UsbAside(FunctionParameters parameters) {
        this(parameters, null);
    }

    public UsbAside(FunctionParameters parameters, String item) {
        super(parameters, item);
        this.fixture = new FixtureAction(parameters, item);
        this.functionBase = new FunctionBase(parameters, item);
    }

    @Override
    public boolean test() {
        if (!this.fixture.test()) {
            return false;
        }
        String dutCom = this.config.getString("DutCom");
        int dutBaud = this.config.getInteger("DutBaudRate", 9600);
        int dutWait = this.config.getInteger("DutWait", 1);
        addLog("Config", String.format("Test about %s s", dutWait));
        try ( ComPort dut = this.functionBase.getComport(dutCom, dutBaud)) {
            if (dut == null) {
                return false;
            }
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
        }
    }

}
