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
    private final ProcessData processData;
    private final Process process;
    private final AbsSubUi subUi;
    private final TestTimer testTimer;
    private int loopTest;
    private boolean testing;

    Runner(UiStatus uiStatus, TestTimer testTimer) {
        this.checkFunctions = new ArrayList<>();
        this.testFunctions = new ArrayList<>();
        this.endFunctions = new ArrayList<>();
        this.processData = uiStatus.getProcessData();
        this.process = new Process(uiStatus);
        this.subUi = uiStatus.getSubUi();
        this.testTimer = testTimer;
        this.loopTest = 1;
        this.testing = false;
    }

    public void setLoopTest(int times) {
        this.loopTest = times;
    }

    public void setCheckFunction(List<FunctionName> funcs) {
        this.checkFunctions.clear();
        this.checkFunctions.addAll(funcs);
    }

    public void setTestFunction(List<FunctionName> funcs) {
        this.testFunctions.clear();
        this.testFunctions.addAll(funcs);
    }

    public void setEndFunction(List<FunctionName> funcs) {
        this.endFunctions.clear();
        this.endFunctions.addAll(funcs);
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
        this.testing = true;
        processData.setStartTime();
        subUi.startTest();
    }

    @Override
    public void run() {
        this.testTimer.start(0);
        for (int i = 0; i < loopTest; i++) {
            prepare();
            if (runFunctions(checkFunctions)) {
                try {
                    runFunctions(testFunctions);
                } finally {
                    processData.setFinishTime();
                }
                runFunctions(endFunctions);
            }
        }
        end();
    }

    public void end(String mess) {
        this.processData.setMessage(mess);
        this.process.stop(mess);
        end();
    }

    private void end() {
        this.testing = false;
        this.subUi.endTest();
        this.testTimer.stop();
        clearAllFunctions();
    }

    private void clearAllFunctions() {
        this.processData.clearSignal();
        this.checkFunctions.clear();
        this.testFunctions.clear();
        this.endFunctions.clear();
    }

    boolean isTesting() {
        return testing;
    }

}
