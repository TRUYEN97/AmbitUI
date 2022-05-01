/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.ManagerUI;

import Control.Core.Core;
import Control.Core.ModeTest;
import Model.Interface.IUpdate;
import View.subUI.SubUI.AbsSubUi;
import static java.util.Objects.isNull;

/**
 *
 * @author 21AK22
 */
public class UiStatus implements IUpdate {

    private final AbsSubUi subUi;
    private final String name;
    private final Core loadMode;
    private ModeTest modeTest;
    private boolean isTesting;

    public UiStatus(AbsSubUi subUi, Core loadMode) {
        this.subUi = subUi;
        this.name = subUi.getName();
        this.loadMode = loadMode;
    }

    public AbsSubUi getSubUi() {
        return subUi;
    }

    public ModeTest getModeTest() {
        return modeTest;
    }

    public boolean isTesting() {
        return isTesting;
    }

    void setModeTest(ModeTest modeTest) {
        this.modeTest = modeTest;
    }

    void setIsTesting(boolean isTesting) {
        this.isTesting = isTesting;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean update() {
        if (this.subUi.update()) {
            this.modeTest = loadMode.getCurrMode();
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
}
