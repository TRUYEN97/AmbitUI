/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base;

import Control.Functions.AbsFunction;
import Model.AllKeyWord;
import Model.DataSource.Setting.Setting;
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

    public Telnet getTelnet(String ip, int port) {
        Telnet telnet = new Telnet();
        addLog("Telnet", "Connect to host: " + ip);
        addLog("Telnet", "Port is: " + port);
        if (!telnet.connect(ip, port)) {
            addLog("Telnet", "Connect failed!");
            return null;
        }
        addLog("Telnet", telnet.readAll(new TimeMs(300)));
        return telnet;
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

    public boolean pingTo(String ip) {
        Cmd cmd = new Cmd();
        String command = String.format("arp -d %s", ip);
        String command1 = String.format("ping %s -n 1", ip);
        for (int i = 0; i < 200; i++) {
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
