/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.BaseFunction;

import Control.Functions.AbsFunction;
import Model.AllKeyWord;
import Time.WaitTime.Class.TimeS;
import AbstractStream.AbsStreamReadable;
import Communicate.ISender;
import Communicate.Impl.Cmd.Cmd;
import Communicate.Impl.Comport.ComPort;
import Communicate.Impl.FtpClient.FtpClient;
import Communicate.Impl.Telnet.Telnet;
import DHCP.DhcpData;
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
        String readUntil = this.config.getString("ReadUntil");
        readUntil = readUntil == null ? "root@eero-test:/#" : readUntil;
        addLog("PC", "ReadUntil: %s", readUntil);
        addLog("PC", "Connect to ip: %s", ip);
        addLog("PC", "Port is: %s", port);
        telnet.setDebug(true);
        if (!pingTo(ip, 120) || !telnet.connect(ip, port)) {
            addLog("PC", "Connect failed!");
            return null;
        }
        addLog("Telnet", telnet.readUntil(new TimeS(5), readUntil));
        if (!telnet.sendCommand("\r\n")) {
            return null;
        }
        String response = telnet.readUntil(new TimeS(5), readUntil);
        addLog("Telnet", response);
        return response == null ? null : telnet;
    }

    public String getIp() {
        if (this.modeTest.isOnDHCP()) {
            String mac = this.processData.getString(AllKeyWord.SFIS.MAC);
            if (mac == null) {
                addLog("It's DHCP mode but MAC is null!");
                return null;
            }
            addLog("PC", "Get IP from the DHCP with MAC is \"%s\"", mac);
            addLog("Setting", "MAC length = %s", DhcpData.getInstance().getMACLength());
            return DhcpData.getInstance().getIP(mac);
        }
        addLog("Get IP from the function config with key is \"IP\".");
        return config.getString("IP");
    }

    public ComPort getComport(String com, Integer baud) {
        ComPort comPort = new ComPort();
        comPort.setDebug(true);
        addLog("ComPort", "Connect to: " + com);
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
            addLog("PC", "*************** Reboot soft failed! *********************");
            return false;
        }
    }

    private boolean sendRebootDUT(int waitTime, String ip) {
        TimeS waitToSleepTime = new TimeS(waitTime);
        do {
            if (!pingTo(ip, 1)) {
                addLog("PC", "*************** Shut down ok! [%.3f S]*****************", waitToSleepTime.getTime());
                return true;
            }
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        } while (waitToSleepTime.onTime());
        addLog("PC", "*************** Shut down failed! [%.3f S]*****************", waitToSleepTime.getTime());
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

    public boolean insertCommand(ISender sender, String command) {
        String name = sender.getClass().getSimpleName();
        addLog(name, "insert command: " + command);
        if (command == null || !sender.insertCommand(command)) {
            addLog(name, "insert command \" %s \" failed!", command);
            return false;
        }
        return true;
    }
    
    public boolean sendCommand(ISender sender, String command) {
        String name = sender.getClass().getSimpleName();
        addLog(name, "Send command: " + command);
        if (command == null || !sender.sendCommand(command)) {
            addLog(name, "send command \" %s \" failed!", command);
            return false;
        }
        return true;
    }

    public boolean pingTo(String ip, int times) {
        if (ip == null || times <= 0) {
            addLog("Error", "IP == null ", ip);
            return false;
        }
        if (times <= 0) {
            addLog("Error", "Ping times <= 0", ip);
            return false;
        }
        addLog("PC", "Ping to IP: %s - %s S", ip, times);
        Cmd cmd = new Cmd();
        String command1 = String.format("ping %s -n 1", ip);
        TimeS timer = new TimeS(times);
        try {
            for (int i = 1; timer.onTime(); i++) {
                addLog("Cmd", "------------------------------------ " + i);
                try {
                    if (insertCommand(cmd, command1)) {
                        String response = cmd.readAll();
                        addLog("Cmd", response.trim());
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
        } finally {
            addLog("PC", "ping time: %.3f S", timer.getTime());
        }
        return false;
    }
}
