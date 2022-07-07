/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions.FunctionsTest.Base.DutTelnet;

import Control.Functions.AbsFunction;
import Model.DataSource.Setting.Setting;
import Time.WaitTime.Class.TimeMs;
import commandprompt.Communicate.DHCP.DhcpData;
import commandprompt.Communicate.Telnet.Telnet;

/**
 *
 * @author Administrator
 */
public class DutTelnet extends AbsFunction {
    
    public DutTelnet(String itemName) {
        super(itemName);
    }
    
    @Override
    protected boolean test() {
        addLog("Get IP!");
        String ip = getIp();
        addLog("IP: " + ip);
        if (ip == null) {
            return false;
        }
        return telnetTo(ip);
    }
    
    private String getIp() {
        if (Setting.getInstance().isOnDHCP()) {
            return getFromDHCP();
        }
        return allConfig.getString("IP");
    }
    
    private String getFromDHCP() {
        //            String mac = productData.getString(AllKeyWord.MAC);
        String mac = "649714048d60";
        if (mac == null) {
            return null;
        }
        return DhcpData.getInstance().getIP(mac);
    }
    
    private boolean telnetTo(String ip) {
        Telnet telnet = new Telnet();
        addLog("CONFIG", "Connect to host: " + ip);
        addLog("CONFIG", "Connect to port: " + 23);
        if (!telnet.connect(ip, 23)) {
            addLog("Telnet", "Connect failed!");
            return false;
        }
        addLog("Telnet", "Connect success!");
        addLog("Telnet", telnet.readAll(new TimeMs(100)));
        telnet.disConnect();
        return true;
    }
}
