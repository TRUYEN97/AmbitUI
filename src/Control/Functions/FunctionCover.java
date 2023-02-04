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
import java.util.concurrent.TimeoutException;

/**
 *
 * @author 21AK22
 */
public class FunctionCover implements Runnable {

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
            int testTimes = allConfig.getInteger(AllKeyWord.CONFIG.RETRY, 0) + 1;
            for (int turn = 1; turn <= testTimes && !function.isPass() && !this.stop; turn++) {
                this.thread = new Thread(function);
                this.thread.start();
                checkOutTime();
                this.function.endTurn();
            }
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(e.getLocalizedMessage());
            this.functionData.addLog(e.getMessage());
        } finally {
            this.function.end();
            thread = null;
        }
    }

    public AbsFunction getFunction() {
        return function;
    }

    public boolean isMutiTasking() {
        return allConfig.getBoolean(AllKeyWord.CONFIG.MULTI_TASK, false);
    }

    public boolean isSkipFail() {
        return allConfig.getBoolean(AllKeyWord.CONFIG.FAIL_CONTNIUE, false);
    }

    public void stopTest(String mess) {
        while (thread != null && thread.isAlive()) {
            try {
                this.thread.join(500);
            } catch (InterruptedException ex) {
            }
            if (mess != null) {
                this.functionData.addLog(String.format("""
                                                       This function will be stopped! Because %s\r
                                                       Time: %.3f S""", mess,
                        functionData.getRunTime()));
                this.functionData.setFail(ErrorCodeElement.SIMPLE);
            }
            this.thread.stop();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            }
        }
        this.stop = true;
    }

    public boolean isWaitUntilMultiDone() {
        return allConfig.getBoolean(AllKeyWord.CONFIG.WAIT_MULTI_DONE, false);
    }

    public boolean isAlwaysRun() {
        return allConfig.getBoolean(AllKeyWord.CONFIG.ALWAYS_RUN, false);
    }

    public boolean isPass() {
        return this.function.isPass();
    }

    private void checkOutTime() {
        final Integer timeSpec = this.allConfig.getInteger(AllKeyWord.CONFIG.TIME_OUT, Integer.MAX_VALUE);
        while (thread != null && thread.isAlive()) {
            try {
                this.thread.join(1000);
                final double runtime = functionData.getRunTime();
                if (runtime >= timeSpec) {
                    String mess = String.format("""
                                                                it has timed out!\r
                                                                Spec: %s S\r
                                                                Try to stop!!""", timeSpec);
                    stopTest(mess);
                    Thread.sleep(1000);
                }
            } catch (Exception ex) {
                if (!(ex instanceof TimeoutException)) {
                    ex.printStackTrace();
                    ErrorLog.addError(this, ex.getLocalizedMessage());
                }
            }
        }
    }
}
