/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base;

import Control.Functions.AbsFunction;
import Model.AllKeyWord;
import Model.DataSource.Setting.Setting;
import Time.WaitTime.AbsTime;
import Time.WaitTime.Class.TimeMs;
import commandprompt.Communicate.Cmd.Cmd;
import commandprompt.Communicate.DHCP.DhcpData;
import commandprompt.Communicate.Telnet.Telnet;

/**
 *
 * @author Administrator
 */
public class BaseFunction extends AbsFunction {

    public BaseFunction(String itemName) {
        super(itemName);
    }

    @Override
    protected boolean test() {
        addLog("Messager", "This is not a function test!");
        return false;
    }

    public String getIp() {
        if (Setting.getInstance().isOnDHCP()) {
//            String mac = getMac();
            String mac = "649714048d60";
            if (mac == null) {
                addLog("It's DHCP mode but MAC is null!");
                return null;
            }
            addLog(String.format("Get IP from the DHCP with MAC is \"%s\"", mac));
            return DhcpData.getInstance().getIP(mac);
        }
        addLog("Get IP from the function config with key is \"IP\".");
        return allConfig.getString("IP");
    }

    public String getValue(Telnet telnet, String startkey, String endkey) {
        return getValue(telnet, startkey, endkey, null);
    }

    public String getValue(Telnet telnet, String startkey, String endkey, AbsTime time) {
        String line;
        String value = null;
        try {
            while ((line = time == null ? telnet.readLine() : telnet.readLine(time)) != null) {
                addLog("Telnet", line);
                value = subString(line, startkey, endkey);
                if (value != null) {
                    break;
                }
            }
            return value;
        } finally {
            addLog("CONFIG", String.format("Start key: \"%s\"", startkey));
            addLog("CONFIG", String.format("End key: \"%s\"", endkey));
            addLog("PC", String.format("Value: \"%s\"", value));
        }

    }

    public Integer string2Integer(String value) {
        if (value == null) {
            addLog("ERROR", "Can't convert null to integer!");
            return null;
        }
        try {
            int result = Integer.valueOf(value);
            addLog("PC", "Convert sucessed! value: " + result);
            return result;
        } catch (NumberFormatException e) {
            addLog("ERROR", e.getLocalizedMessage());
            return null;
        }
    }
    
    public Double string2Double(String value) {
        if (value == null) {
            addLog("ERROR", "Can't convert null to Double!");
            return null;
        }
        try {
            double result = Double.valueOf(value);
            addLog("PC", "Convert sucessed! value: " + result);
            return result;
        } catch (NumberFormatException e) {
            addLog("ERROR", e.getLocalizedMessage());
            return null;
        }
    }

    public boolean isNumber(String value) {
        if (value == null) {
            addLog("PC", value + " is not a number");
            return false;
        }
        try {
            Double.valueOf(value);
            addLog("PC", value + " is a number");
            return true;
        } catch (NumberFormatException e) {
            addLog("PC", value + " is not a number");
            return false;
        }
    }

    public String subString(String line, String startkey, String endkey) {
        if (!stringAvailable(startkey) && !stringAvailable(endkey)) {
            return line.trim();
        } else if (stringAvailable(startkey) && line.contains(startkey)
                && !stringAvailable(endkey)) {
            int index = line.indexOf(startkey) + startkey.length();
            return line.substring(index).trim();
        } else if (stringAvailable(endkey) && line.contains(endkey)
                && !stringAvailable(startkey)) {
            int index = line.indexOf(endkey);
            return line.substring(0, index).trim();
        } else if (stringAvailable(startkey) && line.contains(startkey)
                && stringAvailable(endkey) && line.contains(endkey)) {
            int start = line.indexOf(startkey) + startkey.length();
            int end = line.lastIndexOf(endkey);
            return line.substring(start, end).trim();
        }
        return null;
    }

    private boolean stringAvailable(String str) {
        return str != null && !str.isBlank();
    }

    public Telnet getTelnet(String ip, int port) {
        Telnet telnet = new Telnet();
        addLog("Telnet", "Connect to host: " + ip);
        addLog("Telnet", "Port is: " + port);
        if (!pingTo(ip, 4) || !telnet.connect(ip, port)) {
            addLog("Telnet", "Connect failed!");
            return null;
        }
        addLog("Telnet", telnet.readAll(new TimeMs(300)));
        return telnet;
    }

    public boolean rebootSoft(String ip) {

        Telnet telnet = null;
        try {
            telnet = getTelnet(ip, 23);
            addLog("Telnet", "send reboot...");
            if (!sendCommand(telnet, "reboot")) {
                addLog("Telnet", "send reboot failed!");
                return false;
            }
            addLog("Telnet", telnet.readAll());
            if (pingTo(ip, 1)) {
                addLog("Telnet", "Reboot failed!");
                return false;
            }
            addLog("Telnet", "Reboot ok!");
            return true;
        } finally {
            if (telnet != null) {
                telnet.disConnect();
            }
        }

    }

    public String getMac() {
        String mac = this.productData.getString(AllKeyWord.MAC);
        if (mac == null || mac.length() != 17 || mac.length() != 12) {
            addLog("MAC is invalid: " + mac);
            return null;
        }
        addLog("Get mac= " + mac);
        return mac;
    }

    public boolean sendCommand(Telnet telnet, String command) {
        addLog("Telnet", "Send command: " + command);
        if (command == null || !telnet.sendCommand(command)) {
            addLog("Telnet", "send command \" " + command + "\" failed!");
            return false;
        }
        telnet.readLine();
        return true;
    }

    public boolean sendCommand(Cmd cmd, String command) {
        addLog("Cmd", "Send command: " + command);
        if (command == null || !cmd.sendCommand(command)) {
            addLog("Cmd", "send command \" " + command + "\" failed!");
            return false;
        }
        return true;
    }

    public boolean pingTo(String ip, int times) {
        Cmd cmd = new Cmd();
        String command = String.format("arp -d %s", ip);
        String command1 = String.format("ping %s -n 1", ip);
        for (int i = 0; i < times; i++) {
            if (sendCommand(cmd, command) && sendCommand(cmd, command1)) {
                String response = cmd.readAll().trim();
                addLog("Cmd", response);
                addLog("Cmd", "------------------------------------");
                if (response.contains("TTL=")) {
                    return true;
                }
            } else {
                break;
            }
        }
        return false;
    }
}
