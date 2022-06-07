/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.ManagerUI.UIStatus;

import Model.ManagerUI.UIStatus.Elemants.UiData;
import Control.Core.Core;
import Control.Core.ModeTest;
import Control.Core.CellTest;
import Model.DataModeTest.InputData;
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
    private final UiData uiData;
    private Thread test;
    private CellTest cellTest;
    private ModeTest modeTest;

    public UiStatus(AbsSubUi subUi, Core core) {
        this.subUi = subUi;
        this.name = subUi.getName();
        this.core = core;
        this.uiData = new UiData(this);
    }

    public UiData getUiData() {
        return uiData;
    }

    public AbsSubUi getSubUi() {
        return subUi;
    }

    public ModeTest getModeTest() {
        return this.modeTest;
    }

    public CellTest getCellTest() {
        return cellTest;
    }

    public boolean isTesting() {
        return this.test != null && this.test.isAlive();
    }

    @Override
    public boolean update() {
        if (!this.isTesting()) {
            this.modeTest = core.getCurrMode();
            if (this.subUi.update()) {
                return true;
            }
        }
        return false;
    }

    public boolean isName(String name) {
        if (isNull(name)) {
            return false;
        }
        return this.name.equals(name);
    }

    public boolean isUI(AbsSubUi ui) {
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

    public void setUnitTest(CellTest unitTest) {
        this.cellTest = unitTest;
        this.test = new Thread(unitTest);
        this.test.start();
    }

    public List<String> getFunctionSelected() {
        return this.uiData.getFunctionSelected();
    }

    public void setInput(InputData inputData) {
        this.uiData.setInput(inputData);
    }

}
