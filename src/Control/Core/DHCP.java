/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Core;

import Model.DataTest.ErrorLog;
import MyLoger.MyLoger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static org.dhcp4java.DHCPConstants.DHO_DHCP_LEASE_TIME;
import static org.dhcp4java.DHCPConstants.DHO_DHCP_SERVER_IDENTIFIER;
import static org.dhcp4java.DHCPConstants.DHO_ROUTERS;
import static org.dhcp4java.DHCPConstants.DHO_SUBNET_MASK;
import org.dhcp4java.DHCPOption;
import org.dhcp4java.DHCPPacket;

/**
 *
 * @author Administrator
 */
public class DHCP {

    private final MyLoger loger;
    private final String NETIP;
    private final String dhcpHost;
    private InetAddress host_Address;
    private DHCPOption[] commonOptions;

    public DHCP(String netIp) throws UnknownHostException {
        this.loger = new MyLoger();
        if (netIp == null || !netIp.contains(".")) {
            throw new NullPointerException("netIp is null or it's not addr!");
        }
        this.dhcpHost = netIp;
        this.NETIP = netIp.substring(0, netIp.lastIndexOf(".") + 1);
    }

    public boolean init() {
        DHCPPacket temp = new DHCPPacket();
        if (isNotHostAddress(this.dhcpHost)) {
            String mess = "the network card cannot be found to" + this.dhcpHost;
            JOptionPane.showMessageDialog(null, mess, "Tip",
                    JOptionPane.WARNING_MESSAGE);
            System.exit(0);
            return false;
        }
        try {
            temp.setOptionAsInetAddress(DHO_DHCP_SERVER_IDENTIFIER, this.dhcpHost);
            temp.setOptionAsInt(DHO_DHCP_LEASE_TIME, 3600 * 24 * 14);
            temp.setOptionAsInetAddress(DHO_SUBNET_MASK, "255.255.255.0");
            temp.setOptionAsInetAddress(DHO_ROUTERS, "0.0.0.0");
            this.commonOptions = temp.getOptionsArray();
            return true;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getMessage());
            return false;
        }
    }

    private boolean isNotHostAddress(String dhcpHost) {
        String HostName;
        try {
            HostName = InetAddress.getLocalHost().getHostAddress();
            if (HostName.isBlank()) {
                return false;
            }
            InetAddress[] addrs = InetAddress.getAllByName(dhcpHost);
            String temp;
            for (InetAddress addr : addrs) {
                temp = addr.getHostAddress();
                if (temp.contains(this.dhcpHost)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
