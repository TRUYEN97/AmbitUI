/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Core;

import Control.Functions.AbsFunction;
import Model.DataModeTest.InputData;
import Model.ManagerUI.UIData;
import Model.ManagerUI.UIInput;
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
    private UIData Data;
    private UIInput input;
    private AbsSubUi subUi;

    UnitTest(InputData inputData) {
        this.InputData = inputData;
        this.checks = new ArrayList<>();
        this.tests = new ArrayList<>();
        this.ends = new ArrayList<>();
    }

    public void setData(UIData Data) {
        this.Data = Data;
    }

    public void setInput(UIInput input) {
        this.input = input;
    }

    public void setSubUi(AbsSubUi subUi) {
        this.subUi = subUi;
    }

    @Override
    public void run() {
        if (check() && test() && end()) {

        }
    }

    void setCheckFunction(List<AbsFunction> checkFunctions) {
        this.checks.clear();
        this.checks.addAll(checkFunctions);
    }

    void setTestFunction(List<AbsFunction> testFunctions) {
        this.tests.clear();
        this.tests.addAll(testFunctions);
    }

    void setEndFunction(List<AbsFunction> endFunctions) {
        this.ends.clear();
        this.ends.addAll(endFunctions);
    }

    private boolean check() {
        for (var check : checks) {
            check.run();
            if (check.isPass()) {
                return false;
            }
        }
        return true;
    }

    private boolean test() {
        if (tests.isEmpty()) {
            return false;
        }
        for (var check : tests) {
            check.run();
            if (check.isPass()) {
                return false;
            }
        }
        return true;
    }

    private boolean end() {
        for (var check : ends) {
            check.run();
            if (check.isPass()) {
                return false;
            }
        }
        return true;
    }

}
