/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions;

import Control.Core.ModeTest;
import Model.DataSource.FunctionConfig.FunctionConfig;
import Model.DataSource.FunctionConfig.FunctionElement;
import Model.DataSource.Limit.Limit;
import Model.ManagerUI.UIStatus.Elemants.UiData;
import Model.ManagerUI.UIStatus.UiStatus;
import View.subUI.SubUI.AbsSubUi;
import Model.Interface.IFunction;
import Model.DataModeTest.DataBoxs.FunctionData;
import Model.DataModeTest.ErrorLog;
import Model.DataSource.Setting.Setting;
import java.io.File;

/**
 *
 * @author 21AK22
 */
public abstract class AbsFunction implements IFunction {

    protected final FunctionElement funcConfig;
    protected final Limit limit;
    protected UiData uiData;
    protected ModeTest modeTest;
    protected AbsSubUi subUi;
    private FunctionData functionData;
    private Thread thread;
    private final double timeSpec;

    protected AbsFunction(String itemName) {
        this.limit = Limit.getInstance();
        this.funcConfig = FunctionConfig.getInstance().getElement(itemName);
        this.timeSpec = this.funcConfig.getTimeOutFunction();
    }

    public void setUIStatus(UiStatus uiStatus) {
        this.uiData = uiStatus.getUiData();
        this.subUi = uiStatus.getSubUi();
        this.modeTest = uiStatus.getModeTest();
    }

    @Override
    public void run() {
        try {
            this.thread = new Thread() {
                @Override
                public void run() {
                    createFuncData();
                    runTest();
                }
            };
            this.thread.start();
            while (this.thread.isAlive()) {
                try {
                    this.thread.join(1000);
                } catch (InterruptedException ex) {
                    ErrorLog.addError(this, ex.getMessage());
                }
                if (isOutTime()) {
                    this.thread.stop();
                    String mess = String.format("""
                                                This function has out of run time!\r
                                                Time: %.3f S\r
                                                Spec: %s S\r
                                                Try to stop!!""",
                            getRunTime(), timeSpec);
                    addLog(mess);
                    setResult("OutTime");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getMessage());
        } finally {
            end();
        }

    }

    private boolean isOutTime() {
        return getRunTime() >= timeSpec;
    }

    private void runTest() {
        this.functionData.start(createLogPath());
        try {
            if (isModeSkip()) {
                this.functionData.addLog("Mode Skip: " + modeTest.toString());
                this.functionData.addLog("This function will be canceled because the mode is " + modeTest.toString());
                this.functionData.setResult("Canceled");
                this.functionData.setPass();
                return;
            }
            for (int turn = 0; turn < getRetry(); turn++) {
                this.functionData.addLog(String.format("Turn %s:", turn));
                if (test()) {
                    this.functionData.setPass();
                    return;
                }
            }
            this.functionData.setFail();
        } catch (Exception e) {
            ErrorLog.addError(e.getLocalizedMessage());
            this.addLog(e.getMessage());
        }
    }

    public void setResult(String result) {
        this.functionData.setResult(result);
    }

    protected abstract boolean test();

    @Override
    public boolean isPass() {
        return functionData.isPass();
    }

    public String getItemName() {
        return this.funcConfig.getItemName();
    }

    public String getFuncName() {
        return this.funcConfig.getFunctionName();
    }

    protected void addLog(Object str) {
        this.functionData.addLog(str);
    }

    protected double getRunTime() {
        return functionData.getRunTime();
    }

    protected String getResult() {
        return this.functionData.getResultTest();
    }

    private void createFuncData() {
        this.functionData = this.uiData.createFuncData();
        this.functionData.setFuncconfig(funcConfig);
    }

    private void end() {
        if (this.functionData == null) {
            return;
        }
        this.functionData.end();
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

    private String createLogPath() {
        String settingPath = Setting.getInstance().getFunctionsLocalLog();
        return String.format("%s%s%s", settingPath,File.separator, this.subUi.getName());
    }
}
