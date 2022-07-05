/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataTest;

import Model.ErrorLog;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class DhcpData {

    private static volatile DhcpData instance;
    private final Map<String, String> idMac;
    private String netIP;

    private DhcpData() {
        this.idMac = new HashMap<>();
        this.idMac.put("649714048d60", "192.168.0.0");
    }

    public static DhcpData getInstance() {
        DhcpData ins = DhcpData.instance;
        if (ins == null) {
            synchronized (DhcpData.class) {
                ins = DhcpData.instance;
                if (ins == null) {
                    DhcpData.instance = ins = new DhcpData();
                }
            }
        }
        return ins;
    }

    public void put(String mac, int id) {
        String ip = this.netIP + id;
        this.idMac.put(mac, ip);
    }

    public String getIP(String mac) {
        return this.idMac.get(mac);
    }

    public boolean setNetIP(String netIp) {
        try {
            this.netIP = netIp.substring(0, netIp.lastIndexOf(".") + 1);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getMessage());
            return false;
        }
    }
}
