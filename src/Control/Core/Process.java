/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Core;

import Control.Functions.FunctionCover;
import Model.DataSource.ModeTest.FunctionConfig.FunctionName;
import Model.ErrorLog;
import Model.Factory.Factory;
import Model.Interface.IFunction;
import Model.ManagerUI.UIStatus.UiStatus;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
class Process implements IFunction {

    private final List<FunctionCover> multiTasking;
    private final List<FunctionName> functions;
    private final Factory factory;
    private final UiStatus uiStatus;
    private boolean result;

    public Process(UiStatus uiStatus) {
        this.multiTasking = new ArrayList<>();
        this.functions = new ArrayList<>();
        this.factory = Factory.getInstance();
        this.uiStatus = uiStatus;
    }

    public void setListFunc(List<FunctionName> functions) {
        this.functions.clear();
        this.result = true;
        this.functions.addAll(functions);
    }

    private FunctionCover createFuncCover(FunctionName function) {
        FunctionCover func = new FunctionCover(
                this.factory.getFunc(function.getFunctions(),
                        function.getItemName()), uiStatus);
        multiTasking.add(func);
        return func;
    }

    @Override
    public boolean isPass() {
        return result;
    }

    @Override
    public void run() {
        FunctionCover funcCover;
        for (FunctionName functionName : functions) {
            funcCover = createFuncCover(functionName);
            if (funcCover.isWaitUntilMultiDone()) {
                waitUntilMultiTaskDone();
            }
            funcCover.start();
            if (funcCover.isMutiTasking()) {
                continue;
            }
            try {
                funcCover.join();
                if (hasTaskFailed()) {
                    break;
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                ErrorLog.addError(this, ex.getMessage());
            }
        }
        waitUntilMultiTaskDone();
    }

    private void waitUntilMultiTaskDone() {
        while (!multiTasking.isEmpty()) {
            try {
                if (hasTaskFailed()) {
                    break;
                }
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                ErrorLog.addError(this, ex.getMessage());
            }
        }
    }

    void stop(String mess) {
        functions.clear();
        if (multiTasking.isEmpty()) {
            return;
        }
        result = false;
        for (FunctionCover functionCover : multiTasking) {
            functionCover.stopTest(mess);
        }
    }

    private boolean hasTaskFailed() {
        List<FunctionCover> funcRemoves = new ArrayList<>();
        try {
            for (FunctionCover cover : multiTasking) {
                if (!cover.isAlive()) {
                    funcRemoves.add(cover);
                    if (!cover.getFunction().isPass()) {
                        result = false;
                        return !cover.isSkipFail();
                    }
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        } finally {
            multiTasking.removeAll(funcRemoves);
        }
    }
}
