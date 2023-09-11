/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Socket;

import Control.Core.Core;
import Model.DataSource.ProgramInformation;
import Model.DataTest.InputData;
import Unicast.Client.Client;
import Unicast.Server.ClientHandler;
import Unicast.commons.Interface.IObjectClientReceiver;
import Unicast.commons.Interface.IObjectServerReceiver;
import javax.swing.JTextArea;
import javax.swing.Timer;

/**
 *
 * @author Administrator
 */
public class Receiver implements IObjectServerReceiver, IObjectClientReceiver {

    private final Core core;
    private final JTextArea textArea;
    private int status;

    public Receiver(Core Core, View.UIView view) {
        this.core = Core;
        this.textArea = view.getTextMess();
        this.status = 0;
        new Timer(1000, (e) -> {
            if (status == 0) {
                return;
            }
            if (status == 1) {
                showData("The program has a new version!");
            } else if (status == 2) {
                showData("The program will turn off!");
            }
            if (core.getUiManager().isNotTest()) {
                System.exit(0);
            }
        }).start();
    }

    @Override
    public void receiver(ClientHandler handler, String data) {
        if (data == null) {
            return;
        }
        if (data.trim().matches("^\\[.+\\].*")) {
            startTest(data);
        }
    }

    @Override
    public void receiver(Client client, String data) {
        if (data == null) {
            return;
        }
        data = data.trim();
        showData(data);
        if (data.equalsIgnoreCase("name")) {
            client.send(String.format("name:%s",ProgramInformation.getInstance().getAppName()));
        } else if (data.trim().matches("^\\[.+\\].*")) {
            startTest(data);
        } else if (data.startsWith("sn:")) {
            startTest(data.split(":")[1].trim().toUpperCase());
        } else if (data.equalsIgnoreCase("quit")) {
            status = 2;
        } else if (data.equalsIgnoreCase("update")) {
            status = 1;
        }

    }

    private void showData(String data) {
        if (this.textArea != null) {
            this.textArea.setText(String.format("Socket: %s", data));
        }
    }

    private void startTest(String data) {
        try {
            String sn = data.substring(data.indexOf("[") + 1, data.lastIndexOf("]"));
            InputData input = new InputData();
            input.setInput(sn);
            Thread.sleep(1000);
            this.core.checkInput(input);
        } catch (InterruptedException ex) {
        }
    }

}
