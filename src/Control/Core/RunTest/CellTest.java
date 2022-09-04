/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Core.RunTest;

import Model.DataSource.ModeTest.FunctionConfig.FunctionName;
import Model.DataSource.ModeTest.ModeTestSource;
import Model.DataSource.Tool.TestTimer;
import Model.DataTest.UiInformartion;
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
    private final UiInformartion informartion;

    public CellTest(UiStatus uiStatus, TestTimer testTimer) {
        this.uiStatus = uiStatus;
        this.runner = new Runner(uiStatus, testTimer);
        this.informartion = uiStatus.getInfo();
    }

    public void start() {
        if (isTesting()) {
            return;
        }
        testSource = this.uiStatus.getModeTest().getModeTestSource();
        runner.setMode(this.uiStatus.getModeTest().getModeType());
        runner.setLoopTest(this.testSource.getLoopTest());
        runner.setCheckFunction(this.testSource.getCheckFunctions());
        runner.setTestFunction(this.testSource.getTestFunctions());
        runner.setEndFunction(this.testSource.getEndFunctions());
        thread = new Thread(runner);
        thread.start();
        this.informartion.addTestCount();
    }

    public boolean isTesting() {
        return thread != null && thread.isAlive() && runner.isTesting();
    }

    public void testDebugItem(List<FunctionName> listItem) {
        if (isTesting()) {
            return;
        }
        this.informartion.setReadyStatus(false);
        this.testSource = this.uiStatus.getModeTest().getModeTestSource();
        List<FunctionName> functions = this.testSource.getSelectedItem(listItem);
        if (functions == null || functions.isEmpty()) {
            return;
        }
        this.runner.setTestFunction(functions);
        this.runner.setMode("Debug");
        this.thread = new Thread(runner);
        this.thread.start();
    }

    public void stopTest() {
        if (!isTesting()) {
            return;
        }
        runner.end("STOP TEST");
    }
}
