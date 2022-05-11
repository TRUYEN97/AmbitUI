/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.ManagerUI;

import Control.Core.CellTest;
import Model.DataSource.FunctionConfig.FunctionConfig;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 *
 * @author 21AK22
 */
class UITest {

    private final UiStatus uiStatus;
    private Thread thread;
    private Timer timer;
    private long startTime;

    UITest(UiStatus uiStatus) {
        this.uiStatus = uiStatus;
        this.timer = new Timer(10000, new ActionListener() {
            private final long timeOut = FunctionConfig.getInstance().getTimeOutTest();

            @Override
            public void actionPerformed(ActionEvent e) {
                if (isTesting()) {
                    if (System.currentTimeMillis() - startTime >= timeOut) {
                        thread.stop();
                        timer.stop();
                    }
                } else {
                    timer.stop();
                }
            }
        });
    }

    boolean isTesting() {
        return this.thread != null && this.thread.isAlive();
    }

    void setUnitTest(CellTest unitTest) {
        if (isTesting()) {
            return;
        }
        startTime = System.currentTimeMillis();
        this.thread = new Thread(unitTest);
        this.thread.start();
        this.timer.start();
    }

}
