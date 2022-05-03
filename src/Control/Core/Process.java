/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Core;

import Control.Functions.AbsFunction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Administrator
 */
class Process {

    private final HashMap<Thread, AbsFunction> multiTasking;
    private final List<AbsFunction> functions;
    private boolean result;

    public Process() {
        this.multiTasking = new HashMap<>();
        this.functions = new ArrayList<>();
    }

    public void setListFunc(List<AbsFunction> functions) {
        this.functions.clear();
        this.functions.addAll(functions);
    }

    public boolean isPass() {
        return result;
    }

    public void run() {
        for (var function : this.functions) {
            if (function.isMutiTasking()) {
                multiTasking.put(new Thread(function), function);
            } else {
                if (hasFailed(function)) {
                    return;
                }
            }
            if (hasTaskFailed()) {
                return;
            }
        }
    }

    private boolean hasFailed(AbsFunction function) {
        function.run();
        if (!function.isPass()) {
            result = false;
            return true;
        }
        return false;
    }

    private boolean hasTaskFailed() {
        for (Thread thread : multiTasking.keySet()) {
            if (!thread.isAlive()) {
                if (hasFailed(multiTasking.remove(thread))) {
                    return true;
                }
            }
        }
        return false;
    }
}
