/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions;

import Model.AllKeyWord;
import Model.DataSource.ModeTest.ErrorCode.ErrorCodeElement;
import Model.DataSource.ModeTest.FunctionConfig.FuncAllConfig;
import Model.DataTest.FunctionData.FunctionData;
import Model.ErrorLog;

/**
 *
 * @author 21AK22
 */
public class FunctionCover extends Thread {

    private final AbsFunction function;
    private final FunctionData functionData;
    private final FuncAllConfig allConfig;
    private Thread thread;
    private boolean stop;

    public FunctionCover(AbsFunction function) {
        this.function = function;
        this.functionData = function.functionParameters.getFunctionData();
        this.allConfig = function.config;
    }

    @Override
    public void run() {
        try {
            if (!functionData.start()) {
                this.functionData.setFail(ErrorCodeElement.SIMPLE);
                return;
            }
            runTest();
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getMessage());
        } finally {
            this.functionData.end();
        }
    }

    private void runTest() {
        try {
            this.function.start();
            this.stop = false;
            int testTimes = allConfig.getInteger(AllKeyWord.RETRY, 0) + 1;
            for (int turn = 1; turn <= testTimes && !function.isPass() && !this.stop; turn++) {
                this.functionData.addLog(String.format("Turn run: %s ", turn));
                this.thread = new Thread() {
                    @Override
                    public void run() {
                        function.runTest();
                    }
                };
                this.thread.start();
                checkOutTime();
            }
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(e.getLocalizedMessage());
            this.functionData.addLog(e.getMessage());
        } finally {
            if (this.thread != null && this.thread.isAlive()) {
                this.thread.stop();
            }
            this.function.end();
        }
    }

    private void checkOutTime() {
        final Integer timeSpec = this.allConfig.getInteger(AllKeyWord.TIME_OVER, Integer.MAX_VALUE);
        while (this.thread != null && this.thread.isAlive()) {
            try {
                this.thread.join(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                ErrorLog.addError(this, ex.getMessage());
            }
            final double runtime = functionData.getRunTime();
            if (runtime >= timeSpec) {
                String mess = String.format("""
                                                                This function has out of run time!\r
                                                                Time: %.3f S\r
                                                                Spec: %s S\r
                                                                Try to stop!!""",
                        runtime, timeSpec);
                stopTest(mess);
            }
        }
    }

    public AbsFunction getFunction() {
        return function;
    }

    public boolean isMutiTasking() {
        return allConfig.getBoolean(AllKeyWord.MULTI_TASK, false);
    }

    public boolean isSkipFail() {
        return allConfig.getBoolean(AllKeyWord.FAIL_CONTNIUE, false);
    }

    public void stopTest(String mess) {
        if (this.thread != null && this.thread.isAlive()) {
            if (mess != null) {
                this.functionData.addLog("This function will be stop! Because " + mess);
                this.functionData.setFail(ErrorCodeElement.SIMPLE);
            }
            this.stop = true;
            this.thread.stop();
        }
    }

    public boolean isWaitUntilMultiDone() {
        return allConfig.getBoolean(AllKeyWord.WAIT_MULTI_DONE, false);
    }

    public boolean isAlwaysRun() {
        return allConfig.getBoolean(AllKeyWord.ALWAYSRUN, false);
    }

    public boolean isPass() {
        return this.function.isPass();
    }
}
