/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Mode;

import Model.Interface.IInit;
import Model.DataSource.Setting.ModeInfo;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class ModeTest implements IInit {

    private final String name;
    private final ModeInfo modeInfo;
    private final List<IInit> funcInit;

    public ModeTest(ModeInfo modeTest) {
        this.name = modeTest.getModeName();
        this.modeInfo = modeTest;
        this.funcInit = new ArrayList<>();
    }

    @Override
    public String toString() {
        return getName();
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean init() {
        for (IInit iInit : funcInit) {
            if (!iInit.init()) {
                return false;
            }
        }
        return true;
    }

    public ModeInfo getModeInfo() {
        return modeInfo;
    }
}
