/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Core;

import DHCP.DHCP;
import Model.DataSource.Setting.Setting;
import Model.ErrorLog;
import java.awt.Color;
import java.io.File;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 *
 * @author Administrator
 */
public class DhcpRunner {

    private static volatile DhcpRunner instance;
    private final DHCP dhcp;
    private JLabel lbDhcp;
    private Thread thread;
    private static final int LEASE_TIME = 3600 * 24 * 7;
    private static final int MAC_LENGTH = 17;

    private DhcpRunner() {
        this.dhcp = DHCP.getgetInstance();
    }

    public void setDhcpLabel(JLabel lbDhcp) {
        this.lbDhcp = lbDhcp;
    }

    public void setUseDHCP(boolean userDHCP) {
        if (lbDhcp == null) {
            return;
        }
        if (!isRuning()) {
            this.lbDhcp.setBackground(Color.red);
        } else if (!userDHCP) {
            this.lbDhcp.setBackground(Color.orange);
        }else{
            this.lbDhcp.setBackground(Color.GREEN);
        }
    }

    public void run(String netIP) {
        run(netIP, LEASE_TIME);
    }

    public void run(String netIP, int leaseTime) {
        run(netIP, LEASE_TIME, MAC_LENGTH);
    }

    public void run(String netIP, int leaseTime, int maclength) {
        if (netIP == null || isRuning()) {
            return;
        }
        initDHCP(netIP, leaseTime, maclength);
        this.thread = new Thread(this.dhcp);
        this.thread.start();
    }

    public boolean isRuning() {
        return this.thread != null && this.thread.isAlive();
    }

    public static DhcpRunner getInstance() {
        DhcpRunner ins = DhcpRunner.instance;
        if (ins == null) {
            synchronized (DhcpRunner.class) {
                ins = DhcpRunner.instance;
                if (ins == null) {
                    DhcpRunner.instance = ins = new DhcpRunner();
                }
            }
        }
        return ins;
    }

    public void setTextArea(JTextArea textArea) {
        this.dhcp.setView(textArea);
    }

    private void initDHCP(String netIP, int leaseTime, int macLength) {
        if (!this.dhcp.setNetIP(netIP)
                || !this.dhcp.init(createFilePath(), leaseTime)
                || !this.dhcp.setMacLenth(macLength)) {
            String mess = String.format("Can't start the DHCP!\r\n%s", netIP);
            ErrorLog.addError(this, mess);
            JOptionPane.showMessageDialog(null, mess);
            System.exit(0);
        }
    }

    private File createFilePath() {
        String localLog = Setting.getInstance().getLocalLogPath();
        return new File(String.format("%s/DHCP",
                localLog));
    }
}
