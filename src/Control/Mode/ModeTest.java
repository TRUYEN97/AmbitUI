/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Mode;

import Model.Interface.IPrepare;
import Model.Interface.IEnd;
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
    private final List<IInit> inits;
    private final List<IPrepare> prepares;
    private final List<IEnd> ends;
    private final Factory factory;

    public ModeTest(ModeInfo modeTest) {
        this.name = modeTest.getModeName();
        this.modeInfo = modeTest;
        this.inits = new ArrayList<>();
        this.prepares = new ArrayList<>();
        this.ends = new ArrayList<>();
        this.factory = Factory.getInstance();
        setupInitFunc();
    }

    private void setupInitFunc() {
        for (String type : this.modeInfo.getInit()) {
            this.inits.add(this.factory.getInitFunc(type));
        }
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean init() {
        for (IInit iInit : inits) {
            if (!iInit.init()) {
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
        System.out.println("init");
        for (IPrepare prepare : prepares) {
            if (!prepare.prepare()) {
                return false;
            }
        }
        return true;
    }

    private void test() {
        System.out.println("test");
    }

    private void end() {
        System.out.println("end");
        for (IEnd end : ends) {
        }
    }

    public ModeInfo getModeInfo() {
        return this.modeInfo;
    }
   
}
