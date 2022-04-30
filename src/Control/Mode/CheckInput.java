/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Mode;

import Model.DataModeTest.DataInputMode;
import Model.DataSource.Setting.ModeInfo;
import Model.ManagerUI.ManagerUI;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 *
 * @author Administrator
 */
public class CheckInput {

    private static final int CTRL_V = 22;
    private final DataInputMode dataInput;
    private ModeInfo modeInfo;

    public CheckInput() {
        this.dataInput = new DataInputMode();
    }

    public boolean inputAnalysis(char input) throws HeadlessException {
        StringBuilder dataString = this.dataInput.getBuilder();
        switch (input) {
            case KeyEvent.VK_ENTER -> {
                if (!dataString.isEmpty()) {
                    return checkInput(dataString.toString().trim().toUpperCase());
                }
            }
            case KeyEvent.VK_BACK_SPACE -> {
                if (dataString.length() > 0) {
                    dataString.deleteCharAt(dataString.length() - 1);
                }
            }
            case KeyEvent.VK_DELETE -> {
                dataString.delete(0, dataString.length());
            }
            case CTRL_V -> {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                DataFlavor flavor = DataFlavor.stringFlavor;
                if (clipboard.isDataFlavorAvailable(flavor)) {
                    try {
                        dataString.append(clipboard.getData(flavor));
                    } catch (UnsupportedFlavorException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            default -> {
                if (input >= '0' && input <= 'z') {
                    dataString.append(input);
                }
            }
        }
        return false;
    }

    private boolean checkInput(String input) {
        if (isIndex(input)) {
            addIndex(input);
        } else {
            addSn(input);
        }
        return inputOK();
    }

    public String getDataInput() {
        return this.dataInput.getBuilder().toString();
    }

    private boolean hasSn() {
        String sn = this.dataInput.getSnInput();
        return !(sn == null || sn.isBlank());
    }

    private boolean hasIndex() {
        String index = this.dataInput.getIndex();
        return !(index == null || index.isBlank());
    }

    private void addIndex(String input) {
        this.dataInput.setIndex(input);
    }

    private void addSn(String input) {
        this.dataInput.setSnInput(input);
    }

    private boolean isIndex(String input) {
        return ManagerUI.getInstance().isIndex(input);
    }

    private boolean inputOK() {
        if (modeInfo.isMutiThread()) {
            return hasIndex() && hasSn();
        }
        return hasSn();
    }

}
