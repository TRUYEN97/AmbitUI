/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Runin.PowerSwitch;

import Control.Functions.AbsFunction;
import Model.ErrorLog;
import Communicate.PowerSwitch.PowerSwitch;
import Model.DataTest.FunctionParameters;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class PowerSwitchFunc extends AbsFunction {

    public PowerSwitchFunc(FunctionParameters parameters) {
        super(parameters, null);
    }
    
    public PowerSwitchFunc(FunctionParameters parameters, String item) {
        super(parameters, item);
    }

    @Override
    protected boolean test() {
        try {
            String host = getHost();
            addLog("CONFIG", "host: " + host);
            String user = this.allConfig.getString("user");
            addLog("CONFIG", "user: " + user);
            String pass = this.allConfig.getString("password");
            addLog("CONFIG", "passWord: " + pass);
            int times = this.allConfig.getInteger("Times");
            addLog("CONFIG", "Times: " + times);
            int index = this.uIInfo.getCOLUMN();
            addLog("CONFIG", "index of switch: " + index);
            int delay = this.allConfig.getInteger("Delay");
            addLog("CONFIG", "Delay time: " + delay + " s");
            PowerSwitch powerSwitch;
            powerSwitch = new PowerSwitch(host, user, pass);
            for (int i = 1; i <= times; i++) {
                addLog(String.format("cycle Times: %d - %d ", i, times));
                if (!run(powerSwitch, index, delay) || !doSomethings()) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getLocalizedMessage());
            addLog(e.getMessage());
            ErrorLog.addError(this, e.getMessage());
            return false;
        }

    }

    private String getHost() {
        if (isNotIP()) {
            return null;
        }
        return createNewIp();
    }

    private boolean run(PowerSwitch powerSwitch, int index, int delayS) {
        List<String> commands = allConfig.getListJsonArray("Command");
        for (String command : commands) {
            try {
                if (command.equalsIgnoreCase("on")) {
                    if (!powerSwitch.setOn(index)) {
                        return false;
                    }
                } else if (command.equalsIgnoreCase("off")) {
                    if (!powerSwitch.setOff(index)) {
                        return false;
                    }
                } else if (command.equalsIgnoreCase("cycle")) {
                    if (!powerSwitch.setCycle(index)) {
                        return false;
                    }
                } else if (command.equalsIgnoreCase("onAll")) {
                    if (!powerSwitch.setOnAll()) {
                        return false;
                    }
                } else if (command.equalsIgnoreCase("offAll")) {
                    if (!powerSwitch.setOffAll()) {
                        return false;
                    }
                }
            } finally {
                try {
                    addLog("POWER_SWITCH", powerSwitch.getResult());
                    Thread.sleep(delayS * 1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    ErrorLog.addError(this, ex.getLocalizedMessage());
                    addLog(ex.getLocalizedMessage());
                }
            }
        }
        return true;

    }

    private String createNewIp() throws NumberFormatException {
        Integer oldIp = Integer.valueOf(getIP());
        Integer row = uIInfo.getROW();
        return String.format("%s.%s", getNetIp(), oldIp + row - 1);
    }

    private String getIP() {
        String ipDefault = this.allConfig.getString("host");
        return ipDefault.substring(ipDefault.lastIndexOf(".") + 1, ipDefault.length());
    }

    private String getNetIp() {
        String ipDefault = this.allConfig.getString("host");
        return ipDefault.substring(0, ipDefault.lastIndexOf("."));
    }

    private boolean isNotIP() {
        String ip = this.allConfig.getString("host");
        return ip == null || !ip.matches("\\b((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.|$)){4}\\b");
    }

    public boolean doSomethings() {
        return true;
    }

}
