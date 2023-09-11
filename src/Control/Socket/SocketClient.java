/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Socket;

import Control.Core.Core;
import Model.DataSource.Setting.Setting;
import Unicast.Client.Client;
import View.UIView;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;

/**
 *
 * @author Administrator
 */
public class SocketClient extends Thread {

    private final Setting setting;
    private final SocketConfig config;
    private final Map<String, Client> Clients;
    private final Core core;
    private final UIView view;
    private JLabel socketLabel;

    public SocketClient(Core core, UIView view) {
        this.setting = Setting.getInstance();
        this.config = this.setting.getSocketConfig();
        this.Clients = new HashMap<>();
        this.core = core;
        this.view = view;
    }

    public void setSocketLabel(JLabel socketLabel) {
        this.socketLabel = socketLabel;
    }

    @Override
    public void run() {
        for (ClientConfig clientConfig : this.config.getClientConfigs()) {
            Client client = new Client(clientConfig.getHost(),
                    clientConfig.getPort(),
                    new Receiver(core, view));
            this.Clients.put(clientConfig.getName(), client);
        }
        if (!this.config.isOnSocket() || this.Clients.isEmpty()) {
            setLabelColor(Color.RED);
            return;
        }
        for (String name : this.Clients.keySet()) {
            new ClientRunner(this.Clients.get(name)).start();
        }
        while (true) {
            boolean rs = false;
            for (var client : this.Clients.values()) {
                if (client.isConnect()) {
                    rs = true;
                    break;
                }
            }
            if (rs) {
                setLabelColor(Color.GREEN);
            } else {
                setLabelColor(Color.YELLOW);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            }
        }
    }

    private void setLabelColor(Color color) {
        if (this.socketLabel != null) {
            this.socketLabel.setBackground(color);
        }
    }

    private class ClientRunner extends Thread {

        private final Client client;

        public ClientRunner(Client client) {
            this.client = client;
        }

        @Override
        public void run() {
            while (true) {
                if (this.client.isConnect() || this.client.connect()) {
                    this.client.run();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                }
            }
        }

    }
}
