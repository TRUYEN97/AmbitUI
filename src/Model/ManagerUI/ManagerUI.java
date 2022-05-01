/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.ManagerUI;

import Control.Core.Core;
import Model.Interface.IUpdate;
import View.subUI.SubUI.AbsSubUi;
import java.util.ArrayList;
import java.util.List;
import static java.util.Objects.isNull;

/**
 *
 * @author Administrator
 */
public class ManagerUI implements IUpdate {

    private final Core loadMode;
    private final List<UiStatus> uiStatuses;

    public ManagerUI(Core loadMode) {
        this.uiStatuses = new ArrayList<>();
        this.loadMode = loadMode;
    }

    public boolean containUI(AbsSubUi ui) {
        if (isNull(ui)) {
            return false;
        }
        for (UiStatus uiStatuse : uiStatuses) {
            if (uiStatuse.isUI(ui) || uiStatuse.isName(ui.getName())) {
                return true;
            }
        }
        return false;
    }

    public boolean addUI(AbsSubUi subUi) {
        if (isNull(subUi) || containUI(subUi)) {
            return false;
        }
        UiStatus uiStatus = new UiStatus(subUi, loadMode);
        uiStatus.update();
        return this.uiStatuses.add(uiStatus);
    }

    @Override
    public boolean update() {
        for (UiStatus uiStatuse : uiStatuses) {
            if (!uiStatuse.isTesting() && !uiStatuse.update()) {
                return false;
            }
        }
        return true;
    }

    public void clear() {
        this.uiStatuses.clear();
    }

    public boolean isNotTest() {
        for (UiStatus uiStatuse : uiStatuses) {
            if (uiStatuse.isTesting()) {
                return false;
            }
        }
        return true;
    }
}
