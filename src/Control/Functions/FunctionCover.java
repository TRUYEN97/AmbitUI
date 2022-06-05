/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Functions;


/**
 *
 * @author 21AK22
 */
public class FunctionCover extends Thread {

    private final AbsFunction function;

    public FunctionCover(AbsFunction function) {
        this.function = function;
    }

    @Override
    public void run() {
        this.function.run();
    }

    public AbsFunction getFunction() {
        return function;
    }

    public boolean isMutiTasking() {
        return function.funcConfig.isMutiTasking();
    }

    public boolean isSkipFail() {
        return function.funcConfig.isSkipFail();
    }

}
