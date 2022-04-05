/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.DrawBoardUI.SubUI;

import View.DrawBoardUI.AbsUI;
import java.awt.Color;
import javax.swing.border.LineBorder;

/**
 *
 * @author Administrator
 */
public abstract class AbsSubUi extends AbsUI<AbsSubUi> {

    protected AbsSubUi(String name) {
        super(name);
        this.setBorder(new LineBorder(Color.BLACK, 1));
        this.setToolTipText(name);
    }
    public abstract void setText(String txt);

//    private static TitledBorder createBorder(String name) {
//        return BorderFactory.createTitledBorder(
//                null, name,
//                TitledBorder.CENTER,
//                TitledBorder.DEFAULT_POSITION,
//                new Font("Segoe UI", 1, 14));
//    }

}
