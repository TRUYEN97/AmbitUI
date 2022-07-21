/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Core;

import Model.DataSource.ModeTest.FunctionConfig.FunctionName;
import Model.DataSource.ModeTest.ModeTestSource;
import Model.DataTest.ProcessTest.ProcessData;
import Model.DataTest.ProcessTest.ProcessTestSignal;
import Model.DataTest.ProcessTest.ProductData;
import Model.ErrorLog;
import Model.ManagerUI.UIStatus.UiStatus;
import Time.WaitTime.AbsTime;
import Time.WaitTime.Class.TimeMs;
import View.subUI.SubUI.AbsSubUi;
import java.util.List;
import javax.swing.Timer;

/**
 *
 * @author 21AK22
 */
public class CellTest {

    private final Process process;
    private final ProcessData processData;
    private final ProcessTestSignal testSignal;
    private final ProductData productData;
    private final AbsSubUi subUi;
    private final MyTime myTime;
    private final UiStatus uiStatus;
    private ModeTestSource testSource;
    private Runner runner;

    public CellTest(UiStatus uiStatus) {
        this.uiStatus = uiStatus;
        this.process = new Process(uiStatus);
        this.subUi = uiStatus.getSubUi();
        this.processData = uiStatus.getProcessData();
        this.testSignal = this.processData.getSignal();
        this.productData = this.processData.getProductData();
        this.myTime = new MyTime();
    }

    public void start() {
        testSource = this.uiStatus.getModeTest().getModeTestSource();
        runner = new Runner();
        runner.start();
    }

    public void stopRun(String mess) {
        if (isTesting()) {
            end(mess);
            runner.stop();
        }
    }

    public double getTestTime() {
        return myTime.getTime();
    }

    private void end(String mess) {
        this.process.stop(mess);
        this.subUi.endTest();
        this.testSignal.clear();
        this.productData.clear();
        this.myTime.stopCheck();
    }

    private void prepare() {
        myTime.startCheck();
        processData.setStartTime();
        subUi.startTest();
    }

    private boolean runFunctions(List<FunctionName> functions) {
        process.setListFunc(functions);
        process.run();
        return process.isPass();
    }

    private List<FunctionName> getTestFuntions() {
        if (this.testSignal.getFunctionSelected() != null) {
            return this.testSignal.getFunctionSelected();
        }
        return this.testSource.getTestFunctions();
    }

    public boolean isTesting() {
        return runner != null && runner.isAlive();
    }

    private class Runner extends Thread {

        @Override
        public void run() {
            int loopTest = testSource.getLoopTest();
            for (int i = 0; i < loopTest; i++) {
                try {
                    prepare();
                    if (runFunctions(testSource.getCheckFunctions())) {
                        try {
                            runFunctions(getTestFuntions());
                        } finally {
                            processData.setFinishTime();
                        }
                        runFunctions(testSource.getEndFunctions());
                    }
                } finally {
                    end(null);
                }
            }
        }

    }

    private class MyTime {

        private final Timer timer;
        private final AbsTime myTimer;

        private MyTime() {
            this.myTimer = new TimeMs();
            this.timer = new Timer(1000, (e) -> {
                if (!myTimer.onTime()) {
                    String mess = String.format("out of time: (Test time: %.3f S) > (%s S)",
                            myTimer.getTime() / 1000.0,
                            testSource.getTimeOutTest() / 1000
                    );
                    ErrorLog.addError(mess);
                    processData.setMessage(mess);
                    stopRun(mess);
                }
            });
        }

        private void startCheck() {
//            this.timer.start();
            this.myTimer.start(testSource.getTimeOutTest());
        }

        private void stopCheck() {
            this.timer.stop();
        }

        private double getTime() {
            return this.myTimer.getTime();
        }

    }
}
