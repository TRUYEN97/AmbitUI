/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.ManagerUI;

import Control.Core.CellTest;

/**
 *
 * @author 21AK22
 */
class UITest {

    private final UiStatus uiStatus;
    private Thread thread;

    UITest(UiStatus uiStatus) {
        this.uiStatus = uiStatus;
    }

    boolean isTesting() {
        return this.thread != null && this.thread.isAlive();
    }

    void setUnitTest(CellTest cellTest) {
        if (isTesting()) {
            return;
        }
        this.thread = new Thread(cellTest);
        this.thread.start();
    }

}
