/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Core;

import DHCP.DHCP;
import Model.DataSource.Setting.Setting;
import Model.ErrorLog;
import Time.TimeBase;
import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 *
 * @author Administrator
 */
public class DhcpRunner {

    private static volatile DhcpRunner instance;
    private final DHCP dhcp;
    private Thread thread;

    private DhcpRunner() {
        this.dhcp = DHCP.getgetInstance();
    }

    public void run(String netIP) {
        if (netIP == null || isRuning()) {
            return;
        }
        initDHCP(netIP);
        this.thread = new Thread(this.dhcp);
        this.thread.start();
    }

    public void stop() {
        if (!isRuning()) {
            return;
        }
        this.thread.stop();
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

    public boolean setMacLength(int macLength) {
        if (isRuning()) {
            return this.dhcp.setMacLenth(macLength);
        }
        return true;
    }

    public void setTextArea(JTextArea textArea) {
        this.dhcp.setView(textArea);
    }

    private void initDHCP(String netIP) {
        if (!this.dhcp.setNetIP(netIP) || !this.dhcp.init(createFilePath())) {
            String mess = String.format("Can't start the DHCP!\r\n%s", netIP);
            ErrorLog.addError(this, mess);
            JOptionPane.showMessageDialog(null, mess);
            System.exit(0);
        }
    }

    private File createFilePath() {
        String localLog = Setting.getInstance().getLocalLogPath();
        return new File(String.format("%s/DHCP/%s.txt",
                localLog, new TimeBase(TimeBase.UTC).getDate()));
    }
}
