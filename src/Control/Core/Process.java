/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Core;

import Control.Functions.AbsFunction;
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
            cover.start();
            multiTasking.add(cover);
            if (!cover.isMutiTasking()) {
                try {
                    cover.join();
                    hasTaskFailed();
                } catch (InterruptedException ex) {
                    System.out.println(ex);
                }
            }
        }
        while (!multiTasking.isEmpty()) {
            try {
                hasTaskFailed();
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                System.out.println(ex);
            }
        }
        System.out.println("endd");
    }

    private boolean isFuncPass(FunctionCover function) {
        if (function.getFunction().isPass()) {
            return true;
        }
        return result = false;
    }

    private boolean hasTaskFailed() {
        List<FunctionCover> deleteFunc = new ArrayList<>();
        try {
            for (FunctionCover cover : multiTasking) {
                if (!cover.isAlive() || cover.isOutTime()) {
                    deleteFunc.add(cover);
                    if (!isFuncPass(cover)) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        } finally {
            multiTasking.removeAll(deleteFunc);
        }
    }
}
