/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import Control.Core.ModeTest;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author Administrator
 */
public class ComboboxMode extends DefaultComboBoxModel<ModeTest>{

    public ComboboxMode(ModeTest[] items) {
        super(items);
    }

    public ComboboxMode(Vector<ModeTest> v) {
        super(v);
    }
    
}
