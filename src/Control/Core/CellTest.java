/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Core;

import Control.Functions.AbsFunction;
import Model.DataModeTest.ErrorLog;
import Model.DataModeTest.InputData;
import Model.DataSource.FunctionConfig.FunctionConfig;
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
public class CellTest implements Runnable {

    private final List<AbsFunction> checks;
    private final List<AbsFunction> tests;
    private final List<AbsFunction> ends;
    private final Process process;
    private final UiStatus uiStatus;
    private final Timer timer;
    private final UiData uiData;
    private final AbsSubUi subUi;
    private final AbsTime myTimer;

    public CellTest(UiStatus uiStatus, List<AbsFunction> checkFunctions, List<AbsFunction> testFunctions, List<AbsFunction> listEnd) {
        this.process = new Process();
        this.uiStatus = uiStatus;
        this.uiData = uiStatus.getUiData();
        this.subUi = uiStatus.getSubUi();
        this.checks = setFuncList(checkFunctions);
        this.tests = setFuncList(testFunctions);
        this.ends = setFuncList(listEnd);
        this.myTimer = new TimeMs();
        this.timer = new Timer(1000, (ActionEvent e) -> {
            if (!myTimer.onTime()) {
                String mess = String.format("Out of time: (test time: %.3f S) > (%s S)",
                        myTimer.getTime() / 1000.0,
                        FunctionConfig.getInstance().getTimeOutTest() / 1000
                );
                ErrorLog.addError(mess);
                uiData.setMessage(mess);
                end();
            }
        }) {
            @Override
            public void start() {
                super.start();
                myTimer.start(FunctionConfig.getInstance().getTimeOutTest());
            }

        };
    }

    @Override
    public void run() {
        prepare();
        if (runFunctions(checks)) {
            runItemFunctions();
            runFunctions(ends);
        }
        end();
    }

    public double getTestTime() {
        return myTimer.getTime();
    }

    private void end() {
        this.timer.stop();
        this.process.stop();
        this.subUi.endTest();
    }

    private void prepare() {
        this.uiData.clear();
        this.timer.start();
        this.subUi.startTest();
    }

    private List<AbsFunction> setFuncList(List<AbsFunction> testFunctions) {
        for (AbsFunction testFunction : testFunctions) {
            testFunction.setUIStatus(this.uiStatus);
        }
        return testFunctions;
    }

    private boolean runFunctions(List<AbsFunction> funcs) {
        process.setListFunc(funcs);
        process.run();
        return process.isPass();
    }

    private boolean runItemFunctions() {
        if (tests.isEmpty()) {
            return false;
        }
        this.uiData.putProductInfo(InputData.START_TIME, new TimeBase().getSimpleDateTime());
        boolean result = runFunctions(tests);
        this.uiData.putProductInfo(InputData.FINISH_TIME, new TimeBase().getSimpleDateTime());
        return result;
    }
}
