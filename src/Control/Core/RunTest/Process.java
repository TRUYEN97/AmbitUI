/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Core.RunTest;

import Control.Functions.FunctionCover;
import Model.DataSource.ModeTest.FunctionConfig.FunctionConfig;
import Model.DataSource.ModeTest.FunctionConfig.FunctionName;
import Model.DataTest.FunctionData.FunctionData;
import Model.DataTest.FunctionParameters;
import Model.ErrorLog;
import Model.Factory.Factory;
import Model.Interface.IFunction;
import Model.ManagerUI.UIStatus.UiStatus;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
class Process implements IFunction {

    private final List<FunctionCover> multiTasking;
    private final List<FunctionName> functions;
    private final Factory factory;
    private final UiStatus uiStatus;
    private boolean justFunctionAlwayRun;
    private boolean test;
    private boolean pass;

    public Process(UiStatus uiStatus) {
        this.multiTasking = new ArrayList<>();
        this.functions = new ArrayList<>();
        this.factory = Factory.getInstance();
        this.uiStatus = uiStatus;
        this.pass = false;
        this.test = false;
    }

    public void setListFunc(List<FunctionName> functions) {
        this.functions.clear();
        this.functions.addAll(functions);
    }

    private FunctionCover createFuncCover(FunctionName functionName) {
        FunctionParameters parameters = getFunctionConfig(functionName);
        return new FunctionCover(
                this.factory.getFunc(functionName.getFunctionName(),
                        parameters));
    }

    @Override
    public void run() {
        try {
            this.justFunctionAlwayRun = false;
            this.test = true;
            this.pass = true;
            FunctionCover funcCover;
            FunctionName functionName;
            for (int i = 0; i < functions.size(); i++) {
                functionName = functions.get(i);
                if (justFunctionAlwayRun && !isAlwaysRun(functionName)) {
                    continue;
                }
                funcCover = createFuncCover(functionName);
                if (funcCover.isWaitUntilMultiDone()) {
                    waitUntilMultiTaskDone();
                }
                multiTasking.add(funcCover);
                funcCover.start();
                if (funcCover.isMutiTasking()) {
                    continue;
                }
                funcCover.join();
                if (hasTaskFailed()) {
                    justFunctionAlwayRun = true;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            ErrorLog.addError(this, ex.getMessage());
        } finally {
            waitUntilMultiTaskDone();
            this.functions.clear();
            test = false;
        }
    }

    private void waitUntilMultiTaskDone() {
        while (!multiTasking.isEmpty()) {
            if (hasTaskFailed()) {
                justFunctionAlwayRun = true;
            }
        }
    }

    void stop(String mess) {
        if (multiTasking.isEmpty()) {
            return;
        }
        for (FunctionCover functionCover : multiTasking) {
            functionCover.stopTest(mess);
        }
        this.functions.clear();
    }

    private boolean hasTaskFailed() {
        List<FunctionCover> funcRemoves = new ArrayList<>();
        try {
            for (FunctionCover cover : multiTasking) {
                if (!cover.isAlive()) {
                    funcRemoves.add(cover);
                    if (!cover.isPass()) {
                        this.pass = false;
                        return !cover.isSkipFail();
                    }
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(this, e.getLocalizedMessage());
            return true;
        } finally {
            multiTasking.removeAll(funcRemoves);
        }
    }

    @Override
    public boolean isTesting() {
        return test;
    }

    private FunctionParameters getFunctionConfig(FunctionName functionName) {
        FunctionConfig config = uiStatus.getModeTest().getModeTestSource().getFunctionsConfig(functionName);
        if (config == null) {
            JOptionPane.showMessageDialog(null,
                    String.format("Missing %s in the function config!", functionName));
            System.exit(0);
        }
        return new FunctionParameters(config, uiStatus,
                new FunctionData(uiStatus, config));
    }

    private boolean isAlwaysRun(FunctionName functionName) {
        FunctionConfig config = uiStatus.getModeTest().getModeTestSource().getFunctionsConfig(functionName);
        return config.isAlwaysRun();
    }

    @Override
    public boolean isPass() {
        return pass && !justFunctionAlwayRun;
    }
}
