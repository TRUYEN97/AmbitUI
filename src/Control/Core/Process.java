/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Core;

import Control.Functions.AbsFunction;
import Model.Interface.IFunction;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.Timer;

/**
 *
 * @author Administrator
 */
class Process implements IFunction {

    private final HashMap<Thread, AbsFunction> multiTasking;
    private final List<AbsFunction> functions;
    private final Timer timer;
    private boolean result;

    public Process() {
        this.multiTasking = new HashMap<>();
        this.functions = new ArrayList<>();
        this.result = true;
        this.timer = new Timer(5000, (ActionEvent e) -> {
            for (Thread thread : multiTasking.keySet()) {
                
            }
        });
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
        for (var function : this.functions) {
            if (function.isMutiTasking()) {
                multiTasking.put(new Thread(function), function);
            } else {
                function.run();
                if (isPass(function)) {
                    return;
                }
            }
            if (hasTaskFailed()) {
                return;
            }
        }
    }

    private boolean isPass(AbsFunction function) {
        if (function.isPass()) {
            return true;
        }
        return result = false;
    }

    private boolean hasTaskFailed() {
        for (Thread thread : multiTasking.keySet()) {
            if (!thread.isAlive()) {
                if (!isPass(multiTasking.remove(thread)) ) {
                    return true;
                }
            }
        }
        return false;
    }
}
