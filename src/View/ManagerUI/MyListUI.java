/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.ManagerUI;

import Control.Core.Core;
import Control.Mode.ModeTest;
import Model.Interface.IUpdate;
import View.DrawBoardUI.SubUI.AbsSubUi;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class MyListUI implements IUpdate {

    protected final List<AbsSubUi> listUI;
    protected final List<String> listIndex;

    public MyListUI() {
        this.listUI = new ArrayList<>();
        this.listIndex = new ArrayList<>();
    }

    public boolean containUI(AbsSubUi ui) {
        if (ui == null) {
            return false;
        }
        return listUI.contains(ui);
    }

    public boolean containIndex(String index) {
        if (index == null) {
            return false;
        }
        return listIndex.contains(index);
    }

    public void put(AbsSubUi ui, ModeTest modeTest) throws Exception {
        if (containIndex(ui.getName()) || containUI(ui)) {
            throw new Exception(String.format("Index \" %s \" đã tồn tại", ui.getName()));
        }
        this.listUI.add(ui);
        this.listIndex.add(ui.getName());
    }

    public void remove(AbsSubUi ui) {
        int index = this.listUI.indexOf(ui);
        if (index == -1) {
            return;
        }
        this.listUI.remove(index);
        this.listIndex.remove(index);
    }

    public void clear() {
        this.listUI.clear();
        this.listIndex.clear();
    }

    public ModeTest getModeOf(AbsSubUi ui) {
        try {
            return ui.getMode();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ModeTest getModeOf(String index) {
        try {
            return this.listUI.get(this.listIndex.indexOf(index)).getMode();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public AbsSubUi getUIOf(String index) {
        try {
            return this.listUI.get(this.listIndex.indexOf(index));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean update() {
        for (int index = 0; index < this.listUI.size(); index++) {
            if (!this.listUI.get(index).update()) {
                return false;
            }
        }
        return true;
    }
}
