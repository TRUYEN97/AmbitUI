/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.ManagerUI;

import Control.Mode.ModeTest;
import View.DrawBoardUI.SubUI.AbsSubUi;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class MyListUIRun extends MyListUI{
    private final List<String> listSN;

    public MyListUIRun() {
        this.listSN = new ArrayList<>();
    }

    public boolean containSN(String sn) {
        if (sn == null) {
            return false;
        }
        return listSN.contains(sn);
    }

    public void put(AbsSubUi ui, String sn) throws Exception {
        if (containSN(sn)) {
            throw new Exception(String.format("SN \" %s \" đã tồn tại", sn));
        }
        if (containIndex(ui.getName()) || containUI(ui)) {
            throw new Exception(
                    String.format("Index \" %s \" đã được sử dụng để test %s",
                            ui.getName(), getModeOf(ui)));
        }
        this.listUI.add(ui);
        this.listIndex.add(ui.getName());
        this.listSN.add(sn);
    }

    @Override
    public void put(AbsSubUi ui, ModeTest modeTest) throws Exception {
         throw new Exception("Don't use this func");
    }
    

    @Override
    public void remove(AbsSubUi ui) {
        int index = this.listUI.indexOf(ui);
        if (index == -1) {
            return;
        }
        this.listUI.remove(index);
        this.listIndex.remove(index);
        this.listSN.remove(index);
    }

    @Override
    public void clear() {
        super.clear();
        this.listSN.clear();
    }
    
}
