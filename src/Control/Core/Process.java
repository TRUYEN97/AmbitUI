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
    private final List<AbsFunction> functions;
    private boolean result;

    public Process() {
        this.multiTasking = new ArrayList<>();
        this.functions = new ArrayList<>();
        this.result = true;
    }

    public void setListFunc(List<AbsFunction> functions) {
        this.functions.clear();
        this.functions.addAll(functions);
    }

    @Override
    public boolean isPass() {
        return result;
    }

    @Override
    public void run() {
        FunctionCover cover;
        for (var function : this.functions) {
            cover = new FunctionCover(function);
            cover.start();
            multiTasking.add(cover);
            if (!cover.isMutiTasking()) {
                while (cover.isAlive()) {
                    if (hasTaskFailed()) {
                        return;
                    }
                }
            }
        }
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
