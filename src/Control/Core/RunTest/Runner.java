/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Core.RunTest;

import Model.DataSource.ModeTest.FunctionConfig.FunctionName;
import Model.DataSource.Tool.TestTimer;
import Model.DataTest.ProcessTest.ProcessData;
import Model.ManagerUI.UIStatus.UiStatus;
import View.subUI.SubUI.AbsSubUi;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class Runner implements Runnable {

    private final List<FunctionName> checkFunctions;
    private final List<FunctionName> testFunctions;
    private final List<FunctionName> endFunctions;
    private final List<FunctionName> finalFunctions;
    private final ProcessData processData;
    private final Process process;
    private final AbsSubUi subUi;
    private final TestTimer testTimer;
    private int loopTest;
    private boolean isStop;

    Runner(UiStatus uiStatus, TestTimer testTimer) {
        this.checkFunctions = new ArrayList<>();
        this.testFunctions = new ArrayList<>();
        this.endFunctions = new ArrayList<>();
        this.finalFunctions = new ArrayList<>();
        this.processData = uiStatus.getProcessData();
        this.process = new Process(uiStatus);
        this.subUi = uiStatus.getSubUi();
        this.testTimer = testTimer;
        this.loopTest = 1;
        this.isStop = false;
    }

    public void setLocalDebug(boolean localdebug) {
        this.process.setLocalDebug(localdebug);
    }

    public void setLoopTest(int times) {
        this.loopTest = times;
    }

    public void setCheckFunctions(List<FunctionName> funcs) {
        add2List(checkFunctions, funcs);
    }

    public void setTestFunctions(List<FunctionName> funcs) {
        add2List(testFunctions, funcs);
    }

    public void setEndFunctions(List<FunctionName> funcs) {
        add2List(endFunctions, funcs);
    }

    public void setFinalFunctions(List<FunctionName> funcs) {
        add2List(finalFunctions, funcs);
    }

    private void add2List(List<FunctionName> list, List<FunctionName> funcs) {
        if (list == null || funcs == null || funcs.isEmpty()) {
            return;
        }
        list.clear();
        list.addAll(funcs);
    }

    private boolean runFunctions(List<FunctionName> functions) {
        process.setListFunc(functions);
        process.run();
        return process.isPass();
    }

    public void setMode(String mode) {
        processData.setMode(mode);
    }

    private void prepare() {
        this.processData.setStartTime();
        this.subUi.startTest();
    }

    @Override
    public void run() {
        this.testTimer.start(0);
        for (int i = 0; i < loopTest && !isStop; i++) {
            prepare();
            if (runFunctions(checkFunctions)) {
                try {
                    if (!isStop) {
                        runFunctions(testFunctions);
                    }
                } finally {
                    processData.setFinishTime();
                }
                try {
                    if (!isStop) {
                        runFunctions(endFunctions);
                    }
                } finally {
                    processData.setFinishTimeFinal();
                }
                runFunctions(finalFunctions);
            }
        }
        end();
    }

    public void stopTest(String mess) {
        this.isStop = true;
        this.processData.setMessage(mess);
        this.process.stop(mess);
    }

    private void end() {
        this.processData.endTest();
        this.subUi.endTest();
        this.testTimer.stop();
        clearAllFunctions();
        this.isStop = false;
    }

    private void clearAllFunctions() {
        this.processData.clearSignal();
        this.checkFunctions.clear();
        this.testFunctions.clear();
        this.endFunctions.clear();
        this.finalFunctions.clear();
    }

}
