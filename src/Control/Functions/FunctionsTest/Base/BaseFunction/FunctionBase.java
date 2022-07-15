/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.BaseFunction;

import Control.Functions.AbsFunction;
import Model.AllKeyWord;
import Time.WaitTime.Class.TimeMs;
import Time.WaitTime.Class.TimeS;
import commandprompt.AbstractStream.AbsStreamReadable;
import commandprompt.Communicate.Cmd.Cmd;
import commandprompt.Communicate.Comport.ComPort;
import commandprompt.Communicate.ISender;
import commandprompt.Communicate.Telnet.Telnet;

/**
 *
 * @author Administrator
 */
public class FunctionBase extends AbsFunction {

    public FunctionBase(String itemName) {
        super(itemName);
    }

    @Override
    protected boolean test() {
        addLog("Messager", "This is not a function test!");
        return false;
    }

    public Telnet getTelnet(String ip, int port) {
        return getTelnet(ip, port, null);
    }

    public Telnet getTelnet(String ip, int port, AbsStreamReadable streamReadable) {
        Telnet telnet;
        if (streamReadable == null) {
            telnet = new Telnet();
        } else {
            addLog("Telnet", "Read input: " + streamReadable.getClass().getSimpleName());
            telnet = new Telnet(streamReadable);
        }
        addLog("Telnet", "Connect to host: " + ip);
        addLog("Telnet", "Port is: " + port);
        if (!pingTo(ip, 4) || !telnet.connect(ip, port)) {
            addLog("Telnet", "Connect failed!");
            return null;
        }
        addLog("Telnet", telnet.readAll(new TimeMs(300)));
        return telnet;
    }

    public ComPort getComport(String com, Integer baud) {
        ComPort comPort = new ComPort();
        addLog("ComPort", "Connect to : " + com);
        addLog("ComPort", "BaudRate is: " + baud);
        if (!comPort.connect(com, baud)) {
            addLog("ComPort", "Connect failed!");
            return null;
        }
        return comPort;
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
            addLog("Telnet", telnet.readAll(new TimeS(3)));
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

    public boolean sendCommand(ISender sender, String command) {
        String name = sender.getClass().getSimpleName();
        addLog(name, "Send command: " + command);
        if (command == null || !sender.sendCommand(command)) {
            addLog(name, "send command \" " + command + "\" failed!");
            return false;
        }
        return true;
    }

    public boolean pingTo(String ip, int times) {
        Cmd cmd = new Cmd();
        String command1 = String.format("ping %s -n 1", ip);
        for (int i = 0; i < times; i++) {
            addLog("Cmd", "------------------------------------");
            try {
                if (sendCommand(cmd, command1)) {
                    String response = cmd.readAll().trim();
                    addLog("Cmd", response);
                    if (response.contains("TTL=")) {
                        return true;
                    }
                } else {
                    break;
                }
            } finally {
                addLog("Cmd", "------------------------------------");
            }
        }
        return false;
    }
}
