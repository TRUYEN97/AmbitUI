/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Core;

import Model.AllKeyWord;
import Model.DataSource.ModeTest.FunctionConfig.FunctionName;
import Model.ErrorLog;
import Model.DataTest.InputData;
import Model.DataSource.ModeTest.ModeTestSource;
import Model.DataTest.ProcessTest.ProcessData;
import Model.DataTest.ProcessTestSignal;
import Model.DataTest.ProductData;
import Model.ManagerUI.UIStatus.UiStatus;
import Time.TimeBase;
import Time.WaitTime.AbsTime;
import Time.WaitTime.Class.TimeMs;
import View.subUI.SubUI.AbsSubUi;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.Timer;

/**
 *
 * @author 21AK22
 */
public class CellTest extends Thread {

    private final Process process;
    private final Timer timer;
    private final ProcessData processData;
    private final ProcessTestSignal testSignal;
    private final ProductData productData;
    private final AbsSubUi subUi;
    private final AbsTime myTimer;
    private final ModeTestSource testSource;

    public CellTest(UiStatus uiStatus, InputData inputData, ModeTestSource testSource) {
        this.process = new Process(uiStatus);
        this.subUi = uiStatus.getSubUi();
        this.processData = uiStatus.getProcessData();
        this.testSignal = uiStatus.getSignal();
        this.productData = uiStatus.getProductData();/
        this.myTimer = new TimeMs();
        this.testSource = testSource;
        this.timer = new Timer(1000, (ActionEvent e) -> {
            if (!myTimer.onTime()) {
                String mess = String.format("out of time: (Test time: %.3f S) > (%s S)",
                        myTimer.getTime() / 1000.0,
                        this.testSource.getTimeOutTest() / 1000
                );
                ErrorLog.addError(mess);
                processData.setMessage(mess);
                end(mess);
            }
        }) {
            @Override
            public void start() {
                super.start();
                myTimer.start(testSource.getTimeOutTest());
            }
        };
    }

    @Override
    public void run() {
        prepare();
        this.processData.setStartTime(new TimeBase().getSimpleDateTime());
        if (runFunctions(this.testSource.getCheckFunctions())) {
            try {
                int loopTest = this.testSource.getLoopTest();
                for (int i = 0; i < loopTest; i++) {
                    runItemFunctions();
                    runFunctions(this.testSource.getEndFunctions());
                }
            } finally {
                this.processData.setFinishTime(new TimeBase().getSimpleDateTime());
            }
        }
        end(null);
    }

    public double getTestTime() {
        return myTimer.getTime();
    }

    private void end(String mess) {
        this.timer.stop();
        this.process.stop(mess);
        this.subUi.endTest();
        this.testSignal.clear();
        this.stop();
    }

    private void prepare() {
        this.processData.clear();
        this.timer.start();
        this.subUi.startTest();
    }

    private boolean runFunctions(List<FunctionName> functions) {
        process.setListFunc(functions);
        process.run();
        return process.isPass();
    }

    private boolean runItemFunctions() {
        List<FunctionName> testFunctions = getTestFuntions();
        if (testFunctions.isEmpty()) {
            return false;
        }
        return runFunctions(testFunctions);
    }

    private List<FunctionName> getTestFuntions() {
        if (this.testSignal.getFunctionSelected() != null) {
            return this.testSignal.getFunctionSelected();
        }
        return this.testSource.getTestFunctions();
    }
}
