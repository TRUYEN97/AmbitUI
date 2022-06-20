/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Core;

import Model.DataSource.Setting.Setting;
import Model.DataTest.DhcpData;
import Model.DataTest.ErrorLog;
import MyLoger.MyLoger;
import Time.TimeBase;
import View.UIView;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import javax.swing.JOptionPane;
import org.dhcp4java.DHCPBadPacketException;
import org.dhcp4java.DHCPConstants;
import static org.dhcp4java.DHCPConstants.DHO_DHCP_LEASE_TIME;
import static org.dhcp4java.DHCPConstants.DHO_DHCP_SERVER_IDENTIFIER;
import static org.dhcp4java.DHCPConstants.DHO_ROUTERS;
import static org.dhcp4java.DHCPConstants.DHO_SUBNET_MASK;
import org.dhcp4java.DHCPOption;
import org.dhcp4java.DHCPPacket;
import org.dhcp4java.DHCPResponseFactory;

/**
 *
 * @author Administrator
 */
public class DHCP implements Runnable{

    private static volatile DHCP instance;
    private final MyLoger loger;
    private final DhcpData dhcpData;
    private String dhcpHost;
    private UIView view;
    private InetAddress host_Address;
    private DHCPOption[] commonOptions;

    private DHCP() {
        this.loger = new MyLoger();
        this.dhcpData = DhcpData.getInstance();
    }

    public static DHCP getInstance() {
        DHCP ins = DHCP.instance;
        if (ins == null) {
            synchronized (DHCP.class) {
                ins = DHCP.instance;
                if (ins == null) {
                    DHCP.instance = ins = new DHCP();
                }
            }
        }
        return ins;
    }

    public void setView(UIView view) {
        this.view = view;
    }

    public boolean setNetIP(String netIp) {
        if (netIp == null || !netIp.matches("\\b((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.|$)){4}\\b")) {
            JOptionPane.showMessageDialog(null, "Net IP is null or it's not addr! \r\n" + netIp);
            System.exit(0);
        }
        this.dhcpHost = netIp;
        return this.dhcpData.setNetIP(netIp);
    }

    private boolean init() {
        DHCPPacket temp = new DHCPPacket();
        this.loger.begin(createFilePath(), true);
        if (isNotHostAddress(this.dhcpHost)) {
            String mess = "The network card cannot be found to" + this.dhcpHost;
            JOptionPane.showMessageDialog(null, mess, "Tip",
                    JOptionPane.WARNING_MESSAGE);
            System.exit(0);
            return false;
        }
        try {
            temp.setOptionAsInetAddress(DHO_DHCP_SERVER_IDENTIFIER, this.host_Address);
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
                if (temp.equals(this.dhcpHost)) {
                    this.host_Address = addr;
                    return false;
                }
            }
            return true;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void run() {
        try {
            init();
            DatagramSocket socket;
            socket = new DatagramSocket(DHCPConstants.BOOTP_REQUEST_PORT, host_Address);
            DatagramPacket pac = new DatagramPacket(new byte[1500], 1500);
            DHCPPacket dhcp;
            while (true) {
                socket.receive(pac);
                dhcp = DHCPPacket.getPacket(pac);
                String mac = bytesToHex(dhcp.getChaddr()).substring(0, 12);
                System.out.println("DHCP requests mac: " + mac);
                view.showMessager("DHCP requests mac: " + mac);
                String ip = dhcpData.getIP(mac);
                if (ip == null) {
                    continue;
                }
                view.showMessager(String.format("DHCP: %s\r\nIP: %s", mac, ip));
                byte rev = dhcp.getDHCPMessageType();
                switch (rev) {
                    case DHCPConstants.DHCPDISCOVER -> {
                        DatagramPacket dp;
                        dp = replyPort(dhcp, ip, socket);
                        loger.addLog("///////////////////////////////////////");
                        loger.addLog("dhcp discover");
                        loger.addLog(host_Address.getHostAddress());
                        loger.addLog("--------start--------------");
                        loger.addLog("DHCP PROT: " + dp.getPort());
                        loger.addLog("DHCP ADDRESS: " + dp.getAddress().toString());
                        loger.addLog("DHCP SOCK ADDRESS: " + dp.getSocketAddress().toString());
                        loger.addLog(Arrays.toString(dp.getData()));
                        loger.addLog("---------end--------------");
                    }
                    case DHCPConstants.DHCPREQUEST -> {
                        replyPort(dhcp, ip, socket);
                        loger.addLog("dhcp request");
                        loger.addLog(ip + " OK");
                    }
                }
            }
        } catch (IOException | DHCPBadPacketException e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getMessage());
            JOptionPane.showMessageDialog(null, e.toString(), "Tip", JOptionPane.WARNING_MESSAGE);
            System.exit(0);
        }
    }

    private DatagramPacket replyPort(DHCPPacket dhcp, String ip, DatagramSocket socket) throws IOException, UnknownHostException {
        DHCPPacket d;
        DatagramPacket dp;
        d = DHCPResponseFactory.makeDHCPOffer(dhcp, InetAddress.getByName(ip), 3600 * 24 * 7, host_Address, "", commonOptions);
        byte[] res = d.serialize();
        dp = new DatagramPacket(res, res.length, InetAddress.getByName("255.255.255.255"), DHCPConstants.BOOTP_REPLY_PORT);
        socket.send(dp);
        return dp;
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() < 2) {
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    private File createFilePath() {
        String localLog = Setting.getInstance().getLocalLogPath();
        return new File(String.format("%s/DHCP/%s.txt",
                localLog, new TimeBase().getDate()));
    }

}
