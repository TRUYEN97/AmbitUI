/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Core;

import Model.AllKeyWord;
import Model.DataTest.ErrorLog;
import Model.DataTest.InputData;
import Model.DataSource.ModeTest.ModeTestSource;
import Model.ManagerUI.UIStatus.Elemants.UiData;
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
    private final UiData uiData;
    private final AbsSubUi subUi;
    private final AbsTime myTimer;
    private final ModeTestSource testSource;

    public CellTest(UiStatus uiStatus, InputData inputData, ModeTestSource testSource) {
        this.process = new Process(uiStatus);
        this.subUi = uiStatus.getSubUi();
        this.uiData = uiStatus.getUiData();
        this.uiData.setInput(inputData);
        this.myTimer = new TimeMs();
        this.testSource = testSource;
        this.timer = new Timer(1000, (ActionEvent e) -> {
            if (!myTimer.onTime()) {
                String mess = String.format("out of time: (Test time: %.3f S) > (%s S)",
                        myTimer.getTime() / 1000.0,
                        this.testSource.getTimeOutTest() / 1000
                );
                ErrorLog.addError(mess);
                uiData.setMessage(mess);
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
        if (runFunctions(this.testSource.getCheckFunctions())) {
            if (runItemFunctions()) {
                runFunctions(this.testSource.getEndFunctions());
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
        this.stop();
    }

    private void prepare() {
        this.uiData.clear();
        this.timer.start();
        this.subUi.startTest();
    }

    private boolean runFunctions(List<String> functions) {
        process.setListFunc(functions);
        process.run();
        return process.isPass();
    }

    private boolean runItemFunctions() {
        List<String> testFunctions = getTestFuntions();
        if (testFunctions.isEmpty()) {
            return false;
        }
        this.uiData.putProductInfo(AllKeyWord.START_TIME, new TimeBase().getSimpleDateTime());
        boolean result = runFunctions(testFunctions);
        this.uiData.putProductInfo(AllKeyWord.FINISH_TIME, new TimeBase().getSimpleDateTime());
        this.uiData.putProductInfo(AllKeyWord.CYCLE_TIME, String.valueOf(myTimer.getTime()));
        return result;
    }

    private List<String> getTestFuntions() {
        if (this.uiData.getFunctionSelected() != null) {
            return this.uiData.getFunctionSelected();
        }
        return this.testSource.getTestFunctions();
    }
}
