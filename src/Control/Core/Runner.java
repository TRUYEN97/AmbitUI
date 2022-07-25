/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Core;

import Model.DataSource.ModeTest.FunctionConfig.FunctionName;
import Model.DataTest.ProcessTest.ProcessData;
import Time.WaitTime.AbsTime;
import Time.WaitTime.Class.TimeMs;
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
    private final AbsTime myTimer;
    private int loopTest;
    
    Runner(ProcessData processData, Process process, AbsSubUi subUi) {
        this.checkFunctions = new ArrayList<>();
        this.testFunctions = new ArrayList<>();
        this.endFunctions = new ArrayList<>();
        this.processData = processData;
        this.process = process;
        this.subUi = subUi;
        this.myTimer = new TimeMs();
        this.loopTest = 1;
    }
    
    public void setLoopTest(int times) {
        this.loopTest = times;
    }
    
    public void setCheckFunction(List<FunctionName> funcs) {
        this.checkFunctions.addAll(funcs);
    }
    
    public void setTestFunction(List<FunctionName> funcs) {
        this.testFunctions.addAll(funcs);
    }
    
    public void setEndFunction(List<FunctionName> funcs) {
        this.endFunctions.addAll(funcs);
    }
    
    private boolean runFunctions(List<FunctionName> functions) {
        process.setListFunc(functions);
        process.run();
        return process.isPass();
    }
    
    private void end(String mess) {
        this.process.stop(mess);
        this.subUi.endTest();
        this.processData.clearSignal();
    }
    
    private void prepare() {
        processData.setStartTime();
        subUi.startTest();
    }
    
    @Override
    public void run() {
        myTimer.start(0);
        for (int i = 0; i < loopTest; i++) {
            try {
                prepare();
                if (runFunctions(checkFunctions)) {
                    try {
                        runFunctions(testFunctions);
                    } finally {
                        processData.setFinishTime();
                    }
                    runFunctions(endFunctions);
                }
            } finally {
                end(null);
            }
        }
    }
    
    void reset() {
        this.checkFunctions.clear();
        this.testFunctions.clear();
        this.endFunctions.clear();
    }
    
    double getTime() {
        return this.myTimer.getTime();
    }
    
}
