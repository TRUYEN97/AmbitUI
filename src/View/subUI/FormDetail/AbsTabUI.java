/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package View.subUI.FormDetail;

import View.subUI.AbsUI;

/**
 *
 * @author Administrator
 */
public abstract class AbsTabUI extends AbsUI {

    private TabDetail boss;
    private String type;

    public AbsTabUI(String name, String type) {
        super(name);
        this.type = type;
    }

    public void setBoss(TabDetail boss) {
        this.boss = boss;
    }

    public String getType() {
        return type;
    }

    public TabDetail getBoss() {
        return this.boss;
    }

    public abstract void keyEvent(java.awt.event.KeyEvent evt);
}
