/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.BaseFunction;

import Control.Functions.AbsFunction;
import Model.AllKeyWord;
import Model.ErrorLog;
import Time.WaitTime.Class.TimeMs;
import Time.WaitTime.Class.TimeS;
import AbstractStream.AbsStreamReadable;
import Communicate.Cmd.Cmd;
import Communicate.Comport.ComPort;
import Communicate.ISender;
import Communicate.Telnet.Telnet;
import Communicate.FtpClient.FtpClient;
import Communicate.IConnect;

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

    public FtpClient initFtp(String user, String passWord, String host, int port) {
        addLog("PC", "Connect to ftp!!");
        addLog("Config", "User: " + user);
        addLog("Config", "PassWord: " + passWord);
        addLog("Config", "Host: " + host);
        addLog("Config", "Port: " + port);
        FtpClient ftp;
        try {
            ftp = new FtpClient();
            if (ftp.connect(host, port) && ftp.login(user, passWord)) {
                addLog("PC", "Connect to ftp ok!!");
                this.testSignal.addConnector(ftp);
                return ftp;
            }
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getLocalizedMessage());
        }
        addLog("PC", "Connect to ftp failed..");
        return null;
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
        addLog("Telnet", telnet.readUntil("root@eero-test:/#", new TimeMs(300)));
        this.testSignal.addConnector(telnet);
        return telnet;
    }

    public ComPort getComport(String com, Integer baud) {
        ComPort comPort = new ComPort();
        addLog("ComPort", "Connect to : " + com);
        addLog("ComPort", "BaudRate is: " + baud);
        if (!comPort.connect(com, baud)) {
            addLog("ComPort", String.format("Connect %s failed", com));
            return null;
        }
        addLog("ComPort", String.format("Connect %s ok", com));
        this.testSignal.addConnector(comPort);
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
            this.disConnect(telnet);
        }

    }

    public String getMac() {
        String mac = this.productData.getString(AllKeyWord.MAC);
        if (mac == null || ((mac.length() != 17 && mac.contains(":")) || (mac.length() != 12 && !mac.contains(":")))) {
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
        for (int i = 1; i <= times; i++) {
            addLog("Cmd", "------------------------------------ " + i);
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

    public boolean disConnect(IConnect connect) {
        if (connect == null) {
            addLog("PC", "Can't disconnect with null !");
            return false;
        }
        if (this.testSignal.disConnect(connect)) {
            addLog("PC", String.format("Disconnect %s ok", connect.getClass().getSimpleName()));
            return true;
        }
        addLog("PC", String.format("Disconnect %s failed !", connect.getClass().getSimpleName()));
        return false;
    }
}
