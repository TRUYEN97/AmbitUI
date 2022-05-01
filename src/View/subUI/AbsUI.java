/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package View.subUI;

import javax.swing.JPanel;

/**
 *
 * @author Administrator
 */
public abstract class AbsUI extends JPanel {

    private final String name;

    protected AbsUI(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
