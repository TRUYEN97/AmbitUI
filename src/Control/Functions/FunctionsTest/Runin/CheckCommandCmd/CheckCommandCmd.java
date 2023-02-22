/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Runin.CheckCommandCmd;

import Communicate.Impl.Cmd.Cmd;
import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Model.DataTest.FunctionParameters;
import Model.ErrorLog;
import Time.WaitTime.Class.TimeS;

/**
 *
 * @author Administrator
 */
public class CheckCommandCmd extends AbsFunction {

    private final FunctionBase functionBase;
    private final AnalysisBase analysisBase;

    public CheckCommandCmd(FunctionParameters functionParameters, String itemName) {
        super(functionParameters, itemName);
        this.functionBase = new FunctionBase(functionParameters, itemName);
        this.analysisBase = new AnalysisBase(functionParameters, itemName);
    }

    public CheckCommandCmd(FunctionParameters functionParameters) {
        this(functionParameters, null);
    }

    @Override
    protected boolean test() {
        try (Cmd cmd = new Cmd()) {
            if (cmd == null) {
                return false;
            }
            if (!this.functionBase.sendCommand(cmd, config.getString("command"))) {
                return false;
            }
            String startkey = config.getString("Startkey");
            String endkey = config.getString("Endkey");
            String regex = config.getString("Regex");
            int time = config.getInteger("Time", 10);
            String value = this.analysisBase.getValue(cmd, startkey, endkey, regex, new TimeS(time));
            if (value == null) {
                return false;
            }
            setResult(value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getMessage());
            return false;
        }
    }

}
