/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.ManagerUI;

import Model.ManagerUI.UIStatus.UiStatus;
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
public class UIManager implements IUpdate {

    private final Core core;
    private final List<UiStatus> uiStatuses;

    public UIManager(Core loadMode) {
        this.uiStatuses = new ArrayList<>();
        this.core = loadMode;
    }

    public UiStatus getUiStatus(String index) {
        for (UiStatus uiStatuse : uiStatuses) {
            if (uiStatuse.isName(index)) {
                return uiStatuse;
            }
        }
        return null;
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

    public boolean addUI(AbsSubUi subUi, int row, int column) {
        if (isNull(subUi) || containUI(subUi)) {
            return false;
        }
        UiStatus uiStatus = new UiStatus(subUi, core, this.uiStatuses.size(), row, column);
        subUi.setUiStatus(uiStatus);
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

    public int getIndexOf(String index) {
        return this.getIndexOf(getUiStatus(index));
    }

    public int getIndexOf(UiStatus uiStatus) {
        if (isNull(uiStatus)) {
            return -1;
        }
        return this.uiStatuses.indexOf(uiStatus);
    }

    public boolean isIndexFree(String index) {
        UiStatus status = getUiStatus(index);
        if (isNull(status)) {
            return false;
        }
        return !status.isTesting();
    }
}
