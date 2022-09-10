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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author 21AK22
 */
public class FunctionCover implements Runnable {

    private final AbsFunction function;
    private final FunctionData functionData;
    private final FuncAllConfig allConfig;
    private final ExecutorService pool;
    private Future future;
    private boolean stop;

    public FunctionCover(AbsFunction function) {
        this.function = function;
        this.functionData = function.functionParameters.getFunctionData();
        this.allConfig = function.config;
        this.pool = Executors.newSingleThreadExecutor();
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
            this.functionData.closeLoger();
            this.pool.shutdownNow();
        }
    }

    private void runTest() {
        try {
            this.function.start();
            this.stop = false;
            int testTimes = allConfig.getInteger(AllKeyWord.RETRY, 0) + 1;
            for (int turn = 1; turn <= testTimes && !function.isPass() && !this.stop; turn++) {
                future = this.pool.submit(function);
                checkOutTime(future);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(e.getLocalizedMessage());
            this.functionData.addLog(e.getMessage());
            this.functionData.setFail(ErrorCodeElement.SIMPLE);
        } finally {
            this.function.end();
            future = null;
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
        if (this.future != null) {
            if (mess != null) {
                this.functionData.addLog("This function will be stopped! Because " + mess);
                this.functionData.setFail(ErrorCodeElement.SIMPLE);
            }
            this.future.cancel(true);
            this.stop = true;
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

    private void checkOutTime(Future<?> future) {
        final Integer timeSpec = this.allConfig.getInteger(AllKeyWord.TIME_OVER, Integer.MAX_VALUE);
        while (future != null && (!future.isDone() || this.function.isTesting())) {
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
            try {
                future.get(1, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException ex) {
                if (!(ex instanceof TimeoutException)) {
                    ex.printStackTrace();
                    ErrorLog.addError(this, ex.getMessage());
                }
            }
        }
    }
}
