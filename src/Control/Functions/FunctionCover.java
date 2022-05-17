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
    private long startTime;
    private final double timeSpec;
    private final Thread thread;

    public FunctionCover(AbsFunction function) {
        this.function = function;
        this.thread = new Thread(function);
        this.timeSpec = function.getFuncConfig().getTimeOutFunction();
    }

    @Override
    public void run() {
        this.function.addLog(startFunction());
        this.thread.start();
        this.startTime = System.currentTimeMillis();
        while (this.thread.isAlive()) {
            if (isOutTime()) {
                this.thread.stop();
                this.function.addLog("This function has out of run time!");
                this.function.addLog(String.format("time: %s s\r\nSpec: %s s",
                        getRunTime(), timeSpec));
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ErrorLog.addError(this, ex.getMessage());
            }
        }
        this.function.addLog(endFunction());
    }

    public AbsFunction getFunction() {
        return function;
    }

    public double getRunTime() {
        return (System.currentTimeMillis() - this.startTime)/1000.0;
    }

    public boolean isMutiTasking() {
        return function.getFuncConfig().isMutiTasking();
    }

    public boolean isOutTime() {
        return getRunTime() >= timeSpec;
    }

    public boolean isSkipFail() {
        return function.getFuncConfig().isSkipFail();
    }

    private String startFunction() {
        return String.format("ITEM[%S]-FUNCTION[%S]",
                this.function.getItemName(), this.function.getFuncName());
    }

    private String endFunction() {
        return String.format("TIME[%S s]-RESULT[%S]",
                ((double) getRunTime()), this.function.isPass() ? "PASS" : "FAILED");
    }
}
