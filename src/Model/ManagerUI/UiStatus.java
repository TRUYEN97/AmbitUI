/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.ManagerUI;

import Control.Core.Core;
import Control.Core.ModeTest;
import Control.Core.UnitTest;
import Control.Functions.AbsFunction;
import Model.Interface.IUpdate;
import View.subUI.SubUI.AbsSubUi;
import java.util.List;
import static java.util.Objects.isNull;

/**
 *
 * @author 21AK22
 */
public class UiStatus implements IUpdate {

    private final AbsSubUi subUi;
    private final String name;
    private final Core core;
    private final UIInput input;
    private final UIData Data;
    private final UITest test;
    private ModeTest modeTest;

    UiStatus(AbsSubUi subUi, Core core) {
        this.subUi = subUi;
        this.name = subUi.getName();
        this.core = core;
        this.Data = new UIData();
        this.input = new UIInput();
        this.test = new UITest(this);
    }
    
    public UIData getData() {
        return Data;
    }

    public AbsSubUi getSubUi() {
        return subUi;
    }

    public Core getCore() {
        return core;
    }

    public UIInput getInput() {
        return input;
    }

    public ModeTest getModeTest() {
        return modeTest;
    }

    public boolean isTesting() {
        return this.test.isTesting();
    }

    @Override
    public boolean update() {
        if (!this.isTesting() && this.subUi.update()) {
            this.modeTest = core.getCurrMode();
            return true;
        }
        return false;
    }

    boolean isName(String name) {
        if (isNull(name)) {
            return false;
        }
        return this.name.equals(name);
    }

    boolean isUI(AbsSubUi ui) {
        if (isNull(ui)) {
            return false;
        }
        return this.subUi.equals(ui);
    }

    void setModeTest(ModeTest modeTest) {
        this.modeTest = modeTest;
    }

    public String getName() {
        return name;
    }

    public void setUnitTest(UnitTest unitTest) {
        this.test.setUnitTest(unitTest);
    }

    public List<AbsFunction> getFunctionSelected() {
        return this.input.getFunctionSelected();
    }
}
