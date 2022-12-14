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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author 21AK22
 */
public class CellTest {

    private final UiStatus uiStatus;
    private final Runner runner;
    private final ExecutorService pool;
    private Future future;
    private final UiInformartion informartion;

    public CellTest(UiStatus uiStatus, TestTimer testTimer) {
        this.uiStatus = uiStatus;
        this.runner = new Runner(uiStatus, testTimer);
        this.informartion = uiStatus.getInfo();
        this.pool = Executors.newSingleThreadExecutor();
    }

    public void start() {
        if (isTesting()) {
            return;
        }
        ModeTestSource testSource = this.uiStatus.getModeTest().getModeTestSource();
        runner.setMode(this.uiStatus.getModeTest().getModeType());
        runner.setLoopTest(testSource.getLoopTest());
        runner.setCheckFunctions(testSource.getCheckFunctions());
        runner.setTestFunctions(testSource.getTestFunctions());
        runner.setEndFunctions(testSource.getEndFunctions());
        runner.setFinalFunctions(testSource.getFinalFunctions());
        this.runner.setLocalDebug(false);
        this.future = this.pool.submit(runner);
        this.informartion.addTestCount();
    }

    public boolean isTesting() {
        return future != null && !future.isDone();
    }

    public void testDebugItem(List<FunctionName> listItem) {
        if (isTesting()) {
            return;
        }
        ModeTestSource testSource = this.uiStatus.getModeTest().getModeTestSource();
        List<FunctionName> functions = testSource.getSelectedItem(listItem);
        if (functions == null || functions.isEmpty()) {
            return;
        }
        this.runner.setTestFunctions(functions);
        this.runner.setLocalDebug(true);
        this.runner.setMode("Debug");
        this.future = this.pool.submit(runner);
    }
 

    public void testDebugItem() {
        if (isTesting()) {
            return;
        }
        ModeTestSource testSource = this.uiStatus.getModeTest().getModeTestSource();
        List<FunctionName> functions = testSource.getDebugFunctions();
        if (functions == null || functions.isEmpty()) {
            return;
        }
        this.runner.setTestFunctions(functions);
        this.runner.setFinalFunctions(testSource.getFinalFunctions());
        this.runner.setLocalDebug(false);
        this.runner.setMode("Debug");
        this.future = this.pool.submit(runner);
    }

    public void stopTest() {
        if (!isTesting()) {
            return;
        }
        runner.end("STOP TEST");
        this.future.cancel(true);
    }
}
