/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import Control.Core.Core;
import Model.DataTest.InputData;
import View.UIView;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.IOException;
import static java.util.Objects.isNull;

/**
 *
 * @author Administrator
 */
public class CheckInput {

    private static final int CTRL_V = 22;
    private final StringBuilder data;
    private final Core core;
    private final UIView view;
    private InputData inputData;

    public CheckInput(Core core, UIView view) {
        this.data = new StringBuilder();
        this.core = core;
        this.view = view;
    }

    public synchronized void inputAnalysis(char input) throws HeadlessException {
        if (isNull(this.view) || isNull(this.core)) {
            return;
        }
        switch (input) {
            case KeyEvent.VK_ENTER -> {
                if (!data.isEmpty()) {
                    inputData = new InputData();
                    inputData.setInput(this.data.toString());
                    core.checkInput(inputData);
                    clear();
                }
            }
            case KeyEvent.VK_BACK_SPACE -> {
                if (data.length() > 0) {
                    data.deleteCharAt(data.length() - 1);
                }
            }
            case KeyEvent.VK_DELETE -> {
                clear();
            }
            case CTRL_V -> {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                DataFlavor flavor = DataFlavor.stringFlavor;
                if (clipboard.isDataFlavorAvailable(flavor)) {
                    try {
                        data.append(clipboard.getData(flavor));
                    } catch (UnsupportedFlavorException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            default -> {
                if ( isMuneric(input)|| isText(input)) {
                    data.append(input);
                }
            }
        }
        this.view.setInputText(this.data.toString());
    }

    private void clear() {
        data.delete(0, data.length());
    }

    private boolean isMuneric(char input) {
        return (input >= '0' && input <= '9');
    }
    
    private boolean isText(char input) {
        return (input >= 'A' && input <= 'z');
    }
}
