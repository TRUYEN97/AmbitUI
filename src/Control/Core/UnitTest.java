/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Core;

import Control.Functions.AbsFunction;
import Model.DataModeTest.InputData;
import Model.ManagerUI.UIData;
import Model.ManagerUI.UIInput;
import Model.ManagerUI.UiStatus;
import View.subUI.SubUI.AbsSubUi;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 21AK22
 */
public class UnitTest implements Runnable {

    private final InputData InputData;
    private final List<AbsFunction> checks;
    private final List<AbsFunction> tests;
    private final List<AbsFunction> ends;
    private final Process process;
    private UiStatus uiStatus;
    private UIData Data;
    private UIInput input;
    private AbsSubUi subUi;

    UnitTest(InputData inputData) {
        this.InputData = inputData;
        this.process = new Process();
        this.checks = new ArrayList<>();
        this.tests = new ArrayList<>();
        this.ends = new ArrayList<>();
    }

    @Override
    public void run() {
        if (testting(checks) && test() && testting(ends)) {
            
        }
    }

    void setCheckFunction(List<AbsFunction> checkFunctions) {
        setFuncList(checks, checkFunctions);
    }

    void setTestFunction(List<AbsFunction> testFunctions) {
        setFuncList(tests, testFunctions);
    }

    void setEndFunction(List<AbsFunction> endFunctions) {
        setFuncList(ends, endFunctions);
    }

    private void setFuncList(List<AbsFunction> list, List<AbsFunction> testFunctions) {
        list.clear();
        for (AbsFunction testFunction : testFunctions) {
            testFunction.setUIStatus(this.uiStatus);
            list.add(testFunction);
        }
    }

    public void setup(UiStatus uiStatus) {
        this.uiStatus = uiStatus;
        this.Data = uiStatus.getData();
        this.input = uiStatus.getInput();
        this.subUi = uiStatus.getSubUi();
    }

    private boolean testting(List<AbsFunction> funcs) {
        process.setListFunc(funcs);
        process.run();
        return process.isPass();
    }

    private boolean test() {
        if (tests.isEmpty()) {
            return false;
        }
        return testting(tests);
    }
}
