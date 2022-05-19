/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Core;

import Control.Functions.FunctionCover;
import Control.Functions.AbsFunction;
import Model.DataModeTest.ErrorLog;
import Model.Interface.IFunction;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
class Process implements IFunction {
    
    private final List<FunctionCover> multiTasking;
    private final List<FunctionCover> functions;
    private boolean result;
    
    public Process() {
        this.multiTasking = new ArrayList<>();
        this.functions = new ArrayList<>();
        this.result = true;
    }
    
    public void setListFunc(List<AbsFunction> functions) {
        this.functions.clear();
        this.result = true;
        for (AbsFunction function : functions) {
            this.functions.add(new FunctionCover(function));
        }
    }
    
    @Override
    public boolean isPass() {
        return result;
    }
    
    @Override
    public void run() {
        for (FunctionCover cover : this.functions) {
            cover.init();
            cover.start();
            multiTasking.add(cover);
            if (!cover.isMutiTasking()) {
                try {
                    cover.join();
                    if (hasTaskFailed()) {
                        break;
                    }
                } catch (InterruptedException ex) {
                    ErrorLog.addError(this, ex.getMessage());
                    break;
                }
            }
        }
        while (!multiTasking.isEmpty()) {
            try {
                if (hasTaskFailed()) {
                    break;
                }
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                break;
            }
        }
    }
    
    void stop() {
        for (FunctionCover functionCover : multiTasking) {
            functionCover.stop();
        }
    }
    
    private boolean hasTaskFailed() {
        List<FunctionCover> funcRemoves = new ArrayList<>();
        try {
            for (FunctionCover cover : multiTasking) {
                if (!cover.isAlive() || cover.isOutTime()) {
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
