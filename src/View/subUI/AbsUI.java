/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package View.subUI;

import Model.ManagerUI.UIStatus.UiStatus;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author Administrator
 */
public abstract class AbsUI extends JPanel{

    private final String name;
    private final Timer timer;
    protected UiStatus uiStatus;

    protected AbsUI(String name) {
        this.name = name;
        this.timer = new Timer(1000, (ActionEvent e) -> {
            updateData();
        });
    }

    public void startTest() {
        this.timer.start();
    }
    
    public void endTest() {
        this.timer.stop();
    }

    protected abstract void updateData();

    public void setUiStatus(UiStatus uiStatus) {
        this.uiStatus = uiStatus;
    }

    public UiStatus getUiStatus() {
        return uiStatus;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
