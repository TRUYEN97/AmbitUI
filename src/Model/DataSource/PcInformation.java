/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataSource;

import Model.DataTest.ErrorLog;
import Model.Interface.IInit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

/**
 *
 * @author Administrator
 */
public class PcInformation implements IInit {

    private static volatile PcInformation instance;
    private String pcName;
    private String ip;

    private PcInformation() {
    }

    public static PcInformation getInstance() {
        PcInformation ins = PcInformation.instance;
        if (ins == null) {
            synchronized (PcInformation.class) {
                ins = PcInformation.instance;
                if (ins == null) {
                    PcInformation.instance = ins = new PcInformation();
                }
            }
        }
        return ins;
    }

    @Override
    public boolean init() {
        this.pcName = getComputerName();
        System.out.println("Pc name:" + this.pcName);
        this.ip = getHostIp();
        System.out.println("Ip:" + this.ip);
        return !(ip == null || ip.isBlank() || pcName == null || pcName.isBlank());
    }

    public String getPcName() {
        return pcName;
    }

    public String getIp() {
        return ip;
    }

    private String getHostIp() {
        try {
            if (InetAddress.getLocalHost() != null) {
                return InetAddress.getLocalHost().getHostAddress();
            }
            return "";
        } catch (UnknownHostException e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getLocalizedMessage());
            return "";
        }
    }

    private String getComputerName() {
        String os = getSystemName();
        if (os.startsWith("Windows")) {
            return getWindowsHostName();
        } else if (os.startsWith("Mac")) {
            return getMacHostName();
        }
        return "";
    }

    private String getWindowsHostName() {
        InetAddress ia;
        try {
            if (InetAddress.getLocalHost() == null) {
                return "";
            }
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getLocalizedMessage());
            return "";
        }
    }

    private String getMacHostName() {
        try {
            String cmd = "networksetup -getcomputername";
            Process proc = Runtime.getRuntime().exec(cmd);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            StringBuilder name = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                name.append(line).append("\r\n");
            }
            return name.toString();
        } catch (IOException e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getLocalizedMessage());
            return "";
        }
    }

    private String getSystemName() {
        Properties sysProperty = System.getProperties();
        return sysProperty.getProperty("os.name");
    }
}
