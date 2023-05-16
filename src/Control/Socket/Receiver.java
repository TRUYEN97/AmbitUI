/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Socket;

import Control.Core.Core;
import Model.DataTest.InputData;
import Unicast.Server.ClientHandler;
import Unicast.commons.Interface.IObjectClientReceiver;
import Unicast.commons.Interface.IObjectServerReceiver;
import javax.swing.JTextArea;

/**
 *
 * @author Administrator
 */
public class Receiver implements IObjectServerReceiver, IObjectClientReceiver {

    private final Core core;
    private final JTextArea textArea;

    public Receiver(Core Core, View.UIView view) {
        this.core = Core;
        this.textArea = view.getTextMess();
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
    public void receiver(String data) {
        if (data == null) {
            return;
        }
        showData(data);
        if (data.trim().matches("^\\[.+\\].*")) {
            startTest(data);
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
