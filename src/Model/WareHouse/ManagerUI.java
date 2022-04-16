/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.WareHouse;

import Control.Message;
import Control.Mode.ModeTest;
import View.DrawBoardUI.SubUI.AbsSubUi;

/**
 *
 * @author Administrator
 */
public class ManagerUI {

    private static volatile ManagerUI instance;
    private final MyListUI listSubUI;
    private final MyListUIRun listUIRun;

    private ManagerUI() {
        this.listSubUI = new MyListUI();
        this.listUIRun = new MyListUIRun();
    }

    public static ManagerUI getInstance() {
        ManagerUI temp = ManagerUI.instance;
        if (temp == null) {
            synchronized (ManagerUI.class) {
                temp = ManagerUI.instance;
                if (temp == null) {
                    ManagerUI.instance = temp = new ManagerUI();
                }
            }
        }
        return temp;
    }

    public void addSubUI(AbsSubUi subUi) {
        if (subUi == null) {
            return;
        }
        try {
            this.listSubUI.put(subUi,null);
        } catch (Exception ex) {
            ex.printStackTrace();
            Message.ShowWarning.show(ex.getMessage());
        }
    }

    public void reset() {
        this.listSubUI.clear();
    }

    public MyListUI getListUI() {
        return this.listSubUI;
    }
}
