/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Runin.TelnetReadUntilKey;

import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Model.ErrorLog;
import Time.WaitTime.Class.TimeS;
import AbstractStream.SubClass.ReadStreamOverTime;
import Communicate.Impl.Telnet.Telnet;
import Model.DataTest.FunctionParameters;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class TelnetReadUntilKey extends AbsFunction {

    private final FunctionBase baseFunc;
    private final AnalysisBase analysisBase;

    public TelnetReadUntilKey(FunctionParameters parameters) {
        this(parameters, null);
    }

    public TelnetReadUntilKey(FunctionParameters parameters, String item) {
        super(parameters, item);
        this.baseFunc = new FunctionBase(parameters, item);
        this.analysisBase = new AnalysisBase(parameters, item);
    }

    @Override
    protected boolean test() {
        String ip = this.analysisBase.getIp();
        try ( Telnet telnet = this.baseFunc.getTelnet(ip, 23, new ReadStreamOverTime())) {
            addLog("IP: " + ip);
            if (ip == null || telnet == null) {
                return false;
            }
            return runTest(telnet);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getMessage());
            return false;
        }
    }

    private boolean runTest(Telnet telnet) {
        List<String> commands = this.config.getListJsonArray("command");
        if (commands.isEmpty()) {
            addLog("ERROR", "Commands is empty!");
            return false;
        }
        String readUntil = this.config.getString("keyWord");
        String spec = this.config.getString("keyWord");
        Integer time = this.config.getInteger("Time");
        if (time == null) {
            addLog("Config", "Time == null !!");
            return false;
        }
        for (String command : commands) {
            addLog("Config", "Waiting for about" + time + " s");
            if (!this.baseFunc.sendCommand(telnet, command)
                    || !this.analysisBase.isResponseContainKeyAndShow(telnet, spec, readUntil, new TimeS(time))) {
                return false;
            }
        }
        return true;
    }

}
