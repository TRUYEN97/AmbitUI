/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Core;

import Control.Functions.AbsFunction;

/**
 *
 * @author 21AK22
 */
public class FunctionCover extends Thread {

    private long start;
    private final AbsFunction function;

    public FunctionCover(AbsFunction function) {
        super(function);
        this.function = function;
    }

    public AbsFunction getFunction() {
        return function;
    }

    @Override
    public void run() {
        start = System.currentTimeMillis();
        super.run();
        System.out.println("ggggggggg");
    }

    public long getRunTime() {
        return (System.currentTimeMillis() - start) / 1000;
    }

    boolean isMutiTasking() {
        return function.isMutiTasking();
    }

    boolean isOutTime() {
        return getRunTime() >= function.getTimeOut();
    }
}
