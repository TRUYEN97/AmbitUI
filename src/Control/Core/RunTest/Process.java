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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
class Process implements IFunction {

    private final Map<Future, FunctionCover> multiTasking;
    private final List<FunctionName> functions;
    private final Factory factory;
    private final UiStatus uiStatus;
    private final ExecutorService pool;
    private boolean justFunctionAlwayRun;
    private boolean test;
    private boolean stop;
    private boolean pass;
    private boolean localdebug;

    public Process(UiStatus uiStatus) {
        this.multiTasking = new HashMap<>();
        this.functions = new ArrayList<>();
        this.factory = Factory.getInstance();
        this.uiStatus = uiStatus;
        this.pool = Executors.newCachedThreadPool();
        this.pass = false;
        this.stop = false;
        this.test = false;
    }

    public Process() {
        this.multiTasking = null;
        this.functions = null;
        this.factory = null;
        this.uiStatus = null;
        this.pool = null;
    }

    public void setLocalDebug(boolean localdebug) {
        this.localdebug = localdebug;
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
            this.stop = false;
            if (functions.isEmpty()) {
                return;
            }
            FunctionCover funcCover;
            Future future;
            for (FunctionName functionName : functions) {
                if (this.stop) {
                    break;
                }
                if (justFunctionAlwayRun && !isAlwaysRun(functionName) && !localdebug) {
                    continue;
                }
                funcCover = createFuncCover(functionName);
                if (funcCover.isWaitUntilMultiDone()) {
                    waitUntilMultiTaskDone();
                }
                future = this.pool.submit(funcCover);
                multiTasking.put(future, funcCover);
                if (funcCover.isMutiTasking()) {
                    continue;
                }
                future.get();
                if (hasTaskFailed()) {
                    justFunctionAlwayRun = true;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            ErrorLog.addError(this, ex.getMessage());
            this.pass = false;
        } finally {
            waitUntilMultiTaskDone();
            this.functions.clear();
            this.test = false;
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
        for (Future future : multiTasking.keySet()) {
            multiTasking.get(future).stopTest(mess);
        }
        this.functions.clear();
    }

    private boolean hasTaskFailed() {
        List<Future> futureRemoves = new ArrayList<>();
        FunctionCover cover;
        try {
            for (Future future : multiTasking.keySet()) {
                if (future.isDone()) {
                    futureRemoves.add(future);
                    cover = multiTasking.get(future);
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
            for (Future futureRemove : futureRemoves) {
                multiTasking.remove(futureRemove);
            }
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
