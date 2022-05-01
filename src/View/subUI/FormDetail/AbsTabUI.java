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
public abstract class AbsTabUI extends AbsUI{
    
    private TabDetail boss;

    public AbsTabUI(String index) {
        super(index);
    }

    public void setBoss(TabDetail boss) {
        this.boss = boss;
    }

    public TabDetail getBoss() {
        return this.boss;
    }
    
    public abstract void eventData(Object value);
}
