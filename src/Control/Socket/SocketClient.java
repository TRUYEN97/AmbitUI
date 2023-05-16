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
import javax.swing.JLabel;

/**
 *
 * @author Administrator
 */
public class SocketClient extends Thread {

    private final Client client;
    private final Setting setting;
    private final SocketConfig config;
    private JLabel socketLabel;

    public SocketClient(Core core, UIView view) {
        this.setting = Setting.getInstance();
        this.config = this.setting.getSocketConfig();
        this.client = new Client(this.config.getHost(), this.config.getPort(), new Receiver(core, view));
    }

    public void setSocketLabel(JLabel socketLabel) {
        this.socketLabel = socketLabel;
    }

    @Override
    public void run() {
        if (!this.config.isOnSocket()) {
            setLabelColor(Color.RED);
            return;
        }
        setLabelColor(Color.YELLOW);
        while (true) {
            while (!this.client.isConnect()) {
                this.client.connect();
                try {
                    setLabelColor(Color.YELLOW);
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                }
            }
            setLabelColor(Color.GREEN);
            this.client.run();
        }
    }

    private void setLabelColor(Color color) {
        if (this.socketLabel != null) {
            this.socketLabel.setBackground(color);
        }
    }

}
