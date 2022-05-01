/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Core;

import Control.Core.Core;
import Control.Functions.AbsFunction;
import Model.Interface.IInit;
import Model.DataSource.Setting.ModeInfo;
import Model.Factory.Factory;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class ModeTest implements IInit {

    private final String name;
    private final ModeInfo modeInfo;
    private final List<AbsFunction> inits;
    private final List<AbsFunction> prepares;
    private final List<AbsFunction> ends;
    private final Factory factory;
    private Core core;

    public ModeTest(ModeInfo modeTest) {
        this.name = modeTest.getModeName();
        this.modeInfo = modeTest;
        this.inits = new ArrayList<>();
        this.prepares = new ArrayList<>();
        this.ends = new ArrayList<>();
        this.factory = Factory.getInstance();
        setupInitFunc(this.inits, this.modeInfo.getIniFunc());
        setupInitFunc(this.prepares, this.modeInfo.getPrepare());
        setupInitFunc(this.ends, this.modeInfo.getEnd());
    }

    public void setLoadMode(Core core) {
        this.core = core;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean init() {
        for (AbsFunction iInit : inits) {
            iInit.run();
            if (!iInit.isPass()) {
                return false;
            }
        }
        return true;
    }

    public void run() {
        if (prepare()) {
            test();
            end();
        }
    }

    private boolean prepare() {
        for (AbsFunction function : this.prepares) {
            function.run();
            if (!function.isPass()) {
                return false;
            }
        }
        return true;
    }

    private void test() {
        System.out.println("test");
    }

    private void end() {
        for (AbsFunction function : this.prepares) {
            function.run();
            if (!function.isPass()) {
                return;
            }
        }
    }

    public ModeInfo getModeInfo() {
        return this.modeInfo;
    }

    private void setupInitFunc(List<AbsFunction> list, List<String> functions) {
        for (String type : functions) {
            list.add(this.factory.getFunc(type));
        }
    }

}
