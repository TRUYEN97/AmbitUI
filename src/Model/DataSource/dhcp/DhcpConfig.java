/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataSource.dhcp;

import DHCP.MacUtil;
import Model.AllKeyWord;
import Model.DataSource.DataWareHouse;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class DhcpConfig {

    private final DataWareHouse wareHouse;

    public DhcpConfig() {
        this.wareHouse = new DataWareHouse();
    }
    
    public void setData(Map data){
        if (data == null) {
            return;
        }
        this.wareHouse.putAll(data);
    }

    public boolean isOnDHCP() {
        boolean isOn = this.wareHouse.getBoolean(AllKeyWord.CONFIG.FLAG, false);
        String netIp = getNetIP();
        return isOn && netIp != null && MacUtil.isIPAddr(netIp);
    }

    public String getNetIP() {
        return this.wareHouse.getString("netIP");
    }

    public int getMacLength() {
        return this.wareHouse.getInteger("mac_length", 17);
    }

    public int getleaseTime() {
        return this.wareHouse.getInteger("lease_time", 3600 * 24 * 7);
    }
}
