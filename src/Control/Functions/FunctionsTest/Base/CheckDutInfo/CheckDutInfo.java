/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.CheckDutInfo;

import Control.Functions.AbsFunction;
import Model.DataSource.Setting.Setting;
import Time.WaitTime.Class.TimeMs;
import commandprompt.Communicate.DHCP.DhcpData;
import commandprompt.Communicate.Telnet.Telnet;

/**
 *
 * @author Administrator
 */
public class CheckDutInfo extends AbsFunction {

    public CheckDutInfo(String itemName) {
        super(itemName);
    }

    @Override
    protected boolean test() {
        Telnet telnet = new Telnet();
        addLog("Get IP!");
        String ip = getIp();
        addLog("IP: " + ip);
        if (ip == null) {
            return false;
        }
        return check(ip);
    }

    private String getIp() {
        if (Setting.getInstance().isOnDHCP()) {
            return getFromDHCP();
        }
        return allConfig.getString("IP");
    }

    private String getFromDHCP() {
        //            String mac = productData.getString(AllKeyWord.MAC);
        String mac = "64:97:14:04:8d:60";
        if (mac == null) {
            return null;
        }
        return DhcpData.getInstance().getIP(mac);
    }

    private boolean check(String ip) {
        Telnet telnet = new Telnet();
        if (!connect(ip, telnet) || !sendCommand(telnet)) {
            return false;
        }
        return checkValue(getValue(telnet));
    }

    private boolean checkValue(String value) {
        addLog("Value is: " + value);
        String spec = productData.getString("compareWith");
        addLog("Spec is: " + spec);
        if (spec != null && value != null && spec.equals(value)) {
            setResult(value);
            return true;
        }
        return true;
    }

    private boolean sendCommand(Telnet telnet) {
        String command = allConfig.getString("command");
        addLog("PC", "Send command: " + command);
        if (!telnet.sendCommand(command)) {
            addLog("PC", "send command \" " + command + "\" failed!");
            return false;
        }
        return true;
    }

    private boolean connect(String ip, Telnet telnet) {
        addLog("CONFIG", "Connect to host: " + ip);
        addLog("CONFIG", "Connect to port: " + 23);
        if (!telnet.connect(ip, 23)) {
            addLog("Telnet", "Connect failed!");
            return false;
        }
        addLog("Telnet", telnet.readAll(new TimeMs(200)));
        return true;
    }

    private String getValue(Telnet telnet) {
        String key = allConfig.getString("keyWord");
        addLog("CONFIG", "key word: " + key);
        String line;
        while ((line = telnet.readLine(new TimeMs(100))) != null) {
            line = line.trim();
            addLog("Telnet", line);
            if (line.startsWith(key + "=") && line.contains("=")) {
                return line.substring(line.lastIndexOf("=") + 1, line.length()).trim();
            }
        }
        return null;
    }
}
