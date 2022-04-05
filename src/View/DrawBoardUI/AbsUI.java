/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package View.DrawBoardUI;

import javax.swing.JPanel;

/**
 *
 * @author Administrator
 * @param <T>
 */
public abstract class AbsUI<T> extends JPanel {

    private String index;

    protected AbsUI(String index) {
        this.index = index;
    }

    public String getIndex() {
        return this.index;
    }
}
