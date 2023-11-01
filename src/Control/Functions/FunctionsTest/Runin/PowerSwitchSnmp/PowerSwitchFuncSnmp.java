/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Runin.PowerSwitchSnmp;

import Communicate.Impl.Cmd.Cmd;
import Communicate.Impl.Telnet.Telnet;
import Control.Functions.AbsFunction;
import Control.Functions.FunctionsTest.Base.BaseFunction.AnalysisBase;
import Control.Functions.FunctionsTest.Base.BaseFunction.FunctionBase;
import Model.ErrorLog;
import Model.DataTest.FunctionParameters;
import Time.WaitTime.Class.TimeS;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class PowerSwitchFuncSnmp extends AbsFunction {

    private final FunctionBase baseFunc;
    private final AnalysisBase analysisBase;

    public PowerSwitchFuncSnmp(FunctionParameters parameters) {
        this(parameters, null);
    }

    public PowerSwitchFuncSnmp(FunctionParameters parameters, String item) {
        super(parameters, item);
        this.baseFunc = new FunctionBase(parameters, item);
        this.analysisBase = new AnalysisBase(parameters, item);
    }

    @Override
    protected boolean test() {
        try {
            String ip = getIp();
            addLog("CONFIG", "ip: %s", ip);
            if (ip == null || ip.isBlank()) {
                return false;
            }
            int column = this.uIInfo.getCOLUMN();
            addLog("CONFIG", "Column: " + column);
            try ( Cmd cmd = new Cmd()) {
                List<Integer> delays = this.config.getJsonList("Delay", List.of(5, 5));
                addLog("CONFIG", "Delay time: %s", delays);
                if (delays == null || delays.isEmpty()) {
                    return false;
                }
                List<String> cmds = this.config.getJsonList("command");
                addLog("CONFIG", "Run commands: %s", cmds);
                int times = this.config.getInteger("cycle", 1);
                addLog("CONFIG", "cycle times: %s", times);
                String spec = this.config.getString("keyWord");
                addLog("CONFIG", "spec: %s", spec);
                String command = String.format("snmpset -v1 -c public %s .1.3.6.1.4.1.23273.4.%d.1 s", ip, column + 15);
                int dl = delays.size();
                addLog("************************** begin *************************");
                for (int i = 0; i < times; i++) {
                    addLog("++++++++++++++++++++++++++++++++");
                    addLog(LOG_KEYS.PC, String.format("cycle Times: %d - %d ", i + 1, times));
                    for (int j = 0; j < cmds.size(); j++) {
                        addLog("-------------------------------------------");
                        if (!this.baseFunc.sendCommand(cmd, String.format("%s %s", command, cmds.get(j)))) {
                            return false;
                        }
                        String res = cmd.readAll();
                        addLog("Cmd", res);
                        if (res == null || !res.contains(spec)) {
                            return false;
                        }
                        if (i < dl) {
                            Thread.sleep(delays.get(j) * 1000);
                        } else {
                            Thread.sleep(delays.get(dl - 1) * 1000);
                        }
                    }
                }
                addLog("************************** end *************************");
            }
            return true;
        } catch (Exception e) {
            addLog(e.getMessage());
            ErrorLog.addError(this, e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private String getIp() {
        String ip = this.config.getString("IP");
        addLog("CONFIG", "base ip: " + ip);
        int id = this.uIInfo.getROW();
        addLog("CONFIG", "index of row: " + id);
        if (ip == null || !ip.matches("^(?:\\d{1,3}\\.){3}\\d{1,3}$")) {
            return null;
        }
        ip = ip.trim();
        String[] subIps = ip.split("\\.");
        return String.format("%s.%s.%s.%s", subIps[0], subIps[1], subIps[2], Integer.parseInt(subIps[3]) + id);
    }

    public boolean doSomethings() {
        return true;
    }

}
