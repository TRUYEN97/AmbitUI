/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Runin.SendCommand;

import Control.Functions.AbsFunction;
import Model.DataSource.Setting.Setting;
import Time.WaitTime.Class.TimeMM;
import Time.WaitTime.Class.TimeMs;
import commandprompt.Communicate.DHCP.DhcpData;
import commandprompt.Communicate.Telnet.Telnet;

/**
 *
 * @author Administrator
 */
public class SendCommand extends AbsFunction {

    public SendCommand(String itemName) {
        super(itemName);
    }

    @Override
    protected boolean test() {
        addLog("Get IP!");
        String ip = getIp();
        addLog("IP: " + ip);
        if (ip == null) {
            return false;
        }
        return sendCommand(ip);
    }

    private String getIp() {
        if (Setting.getInstance().isOnDHCP()) {
            return getFromDHCP();
        }
        return allConfig.getString("IP");
    }

    private String getFromDHCP() {
        //            String mac = productData.getString(AllKeyWord.MAC);
        String mac = "649714048d60";
        if (mac == null) {
            return null;
        }
        return DhcpData.getInstance().getIP(mac);
    }

    private boolean sendCommand(String ip) {
        Telnet telnet = new Telnet();
        if (!connect(ip, telnet) || !sendCommand(telnet)) {
            return false;
        }
        String value = getValue(telnet);
        addLog("Telnet", "Value is: " + value);
        if (value == null) {
            return false;
        }
        setResult(value);
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
        String response = telnet.readAll(new TimeMs(100));
        addLog("Telnet", response);
        String[] lines = response.split("\r\n");
        if (lines.length != 3) {
            return null;
        }
        return lines[1].trim();
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
}
