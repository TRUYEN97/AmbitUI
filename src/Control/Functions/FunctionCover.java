/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions;

import Model.DataModeTest.ErrorLog;

/**
 *
 * @author 21AK22
 */
public class FunctionCover extends Thread {

    private final AbsFunction function;
    private final double timeSpec;
    private final Thread thread;

    public FunctionCover(AbsFunction function) {
        this.function = function;
        this.thread = new Thread(function);
        this.timeSpec = function.getFuncConfig().getTimeOutFunction();
    }

    public void init() {
        this.function.createDataBox();
    }

    @Override
    public void run() {
        try {
            this.function.addLog(startFunction());
            this.thread.start();
            while (this.thread.isAlive()) {
                if (isOutTime()) {
                    this.thread.stop();
                    this.function.addLog("This function has out of run time!");
                    this.function.addLog(String.format("Time: %s s\r\nSpec: %s s",
                            function.getRunTime(), timeSpec));
                }
                try {
                    this.thread.join(1000);
                } catch (InterruptedException ex) {
                    ErrorLog.addError(this, ex.getMessage());
                }
            }
        } finally {
            if (this.function.getResult() == null) {
                this.function.setRsutlt(function.isPass() ? "PASS" : "FAIL");
            }
            this.function.addLog(endFunction());
            this.function.end();
        }
    }

    public AbsFunction getFunction() {
        return function;
    }

    public boolean isMutiTasking() {
        return function.getFuncConfig().isMutiTasking();
    }

    public boolean isOutTime() {
        return function.getRunTime() >= timeSpec;
    }

    public boolean isSkipFail() {
        return function.getFuncConfig().isSkipFail();
    }

    private String startFunction() {
        return String.format("Item[%S]---Function[%S]---Mode[%s]",
                this.function.getItemName(), this.function.getFuncName(),
                this.function.getModeTest());
    }

    private String endFunction() {
        return String.format("Time[%S s]---Result[%S]",
                ((double) function.getRunTime()), this.function.isPass() ? "PASS" : "FAILED");
    }
}
