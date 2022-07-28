/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Core;

import Model.DataSource.ModeTest.ModeTestSource;
import Model.ManagerUI.UIStatus.UiStatus;
import java.util.List;

/**
 *
 * @author 21AK22
 */
public class CellTest {

    private final UiStatus uiStatus;
    private ModeTestSource testSource;
    private final Runner runner;
    private Thread thread;

    public CellTest(UiStatus uiStatus) {
        this.uiStatus = uiStatus;
        this.runner = new Runner(uiStatus.getProcessData(),
                new Process(uiStatus), uiStatus.getSubUi());
    }

    public void start() {
        if (isTesting()) {
            return;
        }
        testSource = this.uiStatus.getModeTest().getModeTestSource();
        runner.setLoopTest(this.testSource.getLoopTest());
        runner.setCheckFunction(this.testSource.getCheckFunctions());
        runner.setTestFunction(this.testSource.getTestFunctions());
        runner.setEndFunction(this.testSource.getEndFunctions());
        thread = new Thread(runner);
        thread.start();
    }

    public double getTestTime() {
        return runner.getTime();
    }

    public boolean isTesting() {
        return thread != null && thread.isAlive();
    }

    public void testDebugItem(List<String> listItem) {
        if (isTesting()) {
            return;
        }
        this.testSource = this.uiStatus.getModeTest().getModeTestSource();
        this.runner.setTestFunction(this.testSource.getSelectedItem(listItem));
        this.thread = new Thread(runner);
        this.thread.start();
    }
}
