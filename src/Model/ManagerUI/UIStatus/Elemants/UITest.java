/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.ManagerUI.UIStatus.Elemants;

import Control.Core.CellTest;
import Model.ManagerUI.UIStatus.UiStatus;

/**
 *
 * @author 21AK22
 */
public class UITest {

    private final UiStatus uiStatus;
    private Thread thread;

    public UITest(UiStatus uiStatus) {
        this.uiStatus = uiStatus;
    }

    public boolean isTesting() {
        return this.thread != null && this.thread.isAlive();
    }

    public void setUnitTest(CellTest cellTest) {
        if (isTesting()) {
            return;
        }
        this.thread = new Thread(cellTest);
        this.thread.start();
    }

}
