/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Runin.PowerSwitchNew;

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
public class PowerSwitchFuncNew extends AbsFunction {

    private final FunctionBase baseFunc;
    private final AnalysisBase analysisBase;

    public PowerSwitchFuncNew(FunctionParameters parameters) {
        this(parameters, null);
    }

    public PowerSwitchFuncNew(FunctionParameters parameters, String item) {
        super(parameters, item);
        this.baseFunc = new FunctionBase(parameters, item);
        this.analysisBase = new AnalysisBase(parameters, item);
    }

    @Override
    protected boolean test() {
        try {
            String ip = getIp();
            if (ip == null || ip.isBlank()) {
                return false;
            }
            int port = this.config.getInteger("port");
            addLog("CONFIG", "Port: " + port);
            try ( Telnet telnet = this.baseFunc.getTelnet(ip, port)) {
                List<Integer> delays = this.config.getJsonList("Delay", List.of(5, 5));
//                addLog("CONFIG", "Delay time: %s", delays);
//                if (delays == null || delays.isEmpty()) {
//                    return false;
//                }
                List<String> ints = this.config.getJsonList("init");
                addLog("CONFIG", "Init commands: %s", ints);
                List<String> cmds = this.config.getJsonList("command");
                addLog("CONFIG", "Run commands: %s", cmds);
                int times = this.config.getInteger("cycle", 1);
                addLog("CONFIG", "cycle time: %s", times);
                String readUntil = this.config.getString("ReadUntil");
                String spec = this.config.getString("keyWord");
                Integer time = this.config.getInteger("Time");
                addLog("************************* Init ****************************");
                for (String cmd : ints) {
                    addLog("-------------------------------------------");
                    if (!this.baseFunc.sendCommand(telnet, cmd)
                            || !this.analysisBase.isResponseContainKeyAndShow(telnet, spec, readUntil, new TimeS(time))) {
                        return false;
                    }
                }
                String cmd;
                int dl = delays.size();
                addLog("************************** Loop *************************");
                for (int i = 0; i < times; i++) {
                    addLog("++++++++++++++++++++++++++++++++");
                    addLog(LOG_KEYS.PC, String.format("cycle Times: %d - %d ", i + 1, times));
                    for (int j = 0; j < cmds.size(); j++) {
                        addLog("-------------------------------------------");
                        cmd = cmds.get(j);
                        if (!this.baseFunc.sendCommand(telnet, cmd)
                                || !this.analysisBase.isResponseContainKeyAndShow(telnet, spec, readUntil, new TimeS(time))) {
                            return false;
                        }
                        if (i < dl) {
                            Thread.sleep(delays.get(j) * 1000);
                        } else {
                            Thread.sleep(delays.get(dl - 1) * 1000);
                        }
                    }
                }

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
        int id = this.uIInfo.getID();
        addLog("CONFIG", "index of switch: " + id);
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
