/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions;

import Control.Core.ModeTest;
import Model.DataSource.ModeTest.ErrorCode.ErrorCodeElement;
import Model.DataSource.ModeTest.FunctionConfig.FunctionElement;
import Model.DataSource.Setting.Setting;
import Model.DataTest.FunctionData.FunctionData;
import Model.ErrorLog;
import Model.ManagerUI.UIStatus.UiStatus;
import View.subUI.SubUI.AbsSubUi;

/**
 *
 * @author 21AK22
 */
public class FunctionCover extends Thread {

    private final AbsFunction function;
    private final FunctionData functionData;
    private final FunctionElement funcConfig;
    private final ModeTest modeTest;
    private final AbsSubUi subUi;
    private final UiStatus uiStatus;
    private Thread thread;

    public FunctionCover(AbsFunction function, UiStatus uiStatus) {
        this.function = function;
        this.uiStatus = uiStatus;
        this.modeTest = uiStatus.getModeTest();
        this.subUi = uiStatus.getSubUi();
        this.functionData = new FunctionData();
        this.funcConfig = modeTest.getModeTestSource().getFunctionsConfig(function.getItemName());
    }

    @Override
    public void run() {
        this.function.setResources(this.funcConfig, uiStatus, functionData);
        this.functionData.start(createLogPath(), this.function.getFunctionName());
        try {
            this.thread = new Thread() {
                @Override
                public void run() {
                    runTest();
                }
            };
            this.thread.start();
            checkOutTime();
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getMessage());
        } finally {
            end();
        }
    }

    private String createLogPath() {
        String settingPath = Setting.getInstance().getFunctionsLocalLogPath();
        return String.format("%s/%s/%s_%s.txt",
                settingPath,this.subUi.getName(),
                this.function.getItemName(),this.function.getFunctionName());
    }

    private void checkOutTime() {
        final double timeSpec = this.funcConfig.getTimeOutFunction();
        while (this.thread.isAlive()) {
            try {
                this.thread.join(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                ErrorLog.addError(this, ex.getMessage());
            }
            if (getRunTime() >= timeSpec) {
                String mess = String.format("""
                                                                This function has out of run time!\r
                                                                Time: %.3f S\r
                                                                Spec: %s S\r
                                                                Try to stop!!""",
                        getRunTime(), timeSpec);
                this.functionData.addLog(mess);
                this.functionData.setFail(ErrorCodeElement.SIMPLE);
                this.thread.stop();
            }
        }
    }

    private double getRunTime() {
        return functionData.getRunTime();
    }

    private void runTest() {
        try {
            this.function.start();
            if (isModeSkip()) {
                this.functionData.addLog("Mode Skip: " + modeTest.toString());
                this.functionData.addLog("This function will be canceled because the mode is " + modeTest.toString());
                this.functionData.setResult("Canceled");
                this.functionData.setStatus(true);
                return;
            }
            int retry = getRetry() ;
            for (int turn = 1; turn <= retry && !function.isPass(); turn++) {
                this.functionData.addLog(String.format("Turn run: %s ", turn));
                this.function.setRetry(turn);
                this.function.runTest();
            }
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(e.getLocalizedMessage());
            this.functionData.addLog(e.getMessage());
        }
    }

    public AbsFunction getFunction() {
        return function;
    }

    public boolean isMutiTasking() {
        return funcConfig.isMutiTasking();
    }

    public boolean isSkipFail() {
        return funcConfig.isSkipFail();
    }

    private void end() {
        if (this.functionData == null || !this.functionData.isTesting()) {
            return;
        }
        this.function.end();
        this.functionData.end();
        this.thread.stop();
    }

    private int getRetry() {
        if (funcConfig != null) {
            return funcConfig.getRetry();
        }
        return 1;
    }

    private boolean isModeSkip() {
        String modeSkip = this.funcConfig.getModeCancel();
        return modeSkip != null && !modeSkip.isBlank() && modeSkip.equals(modeTest.toString());
    }

    public void stopTest(String mess) {
        if (this.thread != null && this.thread.isAlive()) {
            if (mess != null) {
                this.functionData.addLog("This function will be stop! Because " + mess);
            }
            this.functionData.setFail(ErrorCodeElement.SIMPLE);
            end();
        }
    }

    public boolean isWaitUntilMultiDone() {
        return funcConfig.isUntilMultiDone();
    }

    public boolean isAlwaysRun() {
        return funcConfig.isAlwaysRun();
    }
}
