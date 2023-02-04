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
import Communicate.ISender;
import Communicate.IConnect;
import Communicate.Impl.Cmd.Cmd;
import Communicate.Impl.Comport.ComPort;
import Communicate.Impl.FtpClient.FtpClient;
import Communicate.Impl.Telnet.Telnet;
import Model.DataTest.FunctionParameters;
import java.io.IOException;

/**
 *
 * @author Administrator
 */
public class FunctionBase extends AbsFunction {

    public FunctionBase(FunctionParameters parameters) {
        super(parameters, null);
    }

    public FunctionBase(FunctionParameters parameters, String item) {
        super(parameters, item);
    }

    @Override
    protected boolean test() {
        addLog("Messager", "This is not a function test!");
        return false;
    }

    public FtpClient initFtp(String user, String passWord, String host, int port) throws IOException {
        addLog("PC", "Connect to ftp!!");
        addLog("Config", "User: " + user);
        addLog("Config", "PassWord: " + passWord);
        addLog("Config", "Host: " + host);
        addLog("Config", "Port: " + port);
        FtpClient ftp = FtpClient.getConnection(host, port, user, passWord);
        if (ftp == null) {
            addLog("PC", "Connect to ftp failed..");
            return null;
        }
        ftp.setDebug(true);
        addLog("PC", "Connect to ftp ok!!");
        return ftp;
    }

    public Telnet getTelnet(String ip, int port) {
        return getTelnet(ip, port, null);
    }

    public Telnet getTelnet(String ip, int port, AbsStreamReadable streamReadable) {
        Telnet telnet;
        if (streamReadable == null) {
            telnet = new Telnet();
        } else {
            addLog("PC", "Read input: " + streamReadable.getClass().getSimpleName());
            telnet = new Telnet(streamReadable);
        }
        telnet.setDebug(true);
        addLog("PC", "Connect to host: " + ip);
        addLog("PC", "Port is: " + port);
        if (!pingTo(ip, 4) || !telnet.connect(ip, port)) {
            addLog("PC", "Connect failed!");
            return null;
        }
        addLog("Telnet", telnet.readUntil("root@eero-test:/#", new TimeS(5)));
        if (!telnet.sendCommand("\r\n")) {
            return null;
        }
        String response = telnet.readUntil("root@eero-test:/#", new TimeS(5));
        addLog("Telnet", response);
        return response == null ? null : telnet;
    }

    public ComPort getComport(String com, Integer baud) {
        ComPort comPort = new ComPort();
        comPort.setDebug(true);
        addLog("ComPort", "Connect to : " + com);
        addLog("ComPort", "BaudRate is: " + baud);
        if (!comPort.connect(com, baud)) {
            addLog("ComPort", String.format("Connect %s failed", com));
            return null;
        }
        addLog("ComPort", String.format("Connect %s ok", com));
        return comPort;
    }

    public boolean rebootSoft(String ip, int waitTime, int pingTime) throws IOException {
        try ( Telnet telnet = getTelnet(ip, 23)) {
            if (telnet == null) {
                return false;
            }
            if (!sendCommand(telnet, "reboot")) {
                addLog("Telnet", "send command reboot failed!");
                return false;
            }
            addLog("Telnet", telnet.readAll(new TimeS(10)));
            addLog("PC", "Wait about %s S", waitTime);
            if (sendRebootDUT(waitTime, ip) && pingTo(ip, pingTime)) {
                addLog("PC", "*************** Reboot soft ok! *********************");
                return true;
            }
            addLog("PC", "Out of time to reboot!");
            addLog("PC", "*************** Reboot soft failed! *********************");
            return false;
        }
    }

    private boolean sendRebootDUT(int waitTime, String ip) {
        TimeS waitToSleepTime = new TimeS(waitTime);
        do {
            if (!pingTo(ip, 1)) {
                addLog("PC", "*************** Shut down ok! [%s S]*****************", waitToSleepTime.getTime());
                return true;
            }
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        } while (waitToSleepTime.onTime());
        addLog("PC", "*************** Shut down failed! [%s S]*****************", waitToSleepTime.getTime());
        return false;
    }

    public String getMac() {
        String mac = this.productData.getString(AllKeyWord.SFIS.MAC);
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
        String arp = String.format("arp -d %s", ip);
        for (int i = 1; i <= times; i++) {
            addLog("Cmd", "------------------------------------ " + i);
            try {
                sendCommand(cmd, arp);
                addLog("Cmd", cmd.readAll().trim());
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
