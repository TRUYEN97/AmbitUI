/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.DrawBoardUI;

import Model.UIWarehouse.FactoryUI;
import View.DrawBoardUI.SubUI.AbsSubUi;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author Administrator
 */
public class DrawBoardUI {

    private int x_axis = 1;
    private int y_axis = 1;
    private JPanel boardUi;
    private String typeUI;

    public DrawBoardUI() {
    }

    public void setBoardUi(JPanel boardUi) {
        if (boardUi == null) {
            return;
        }
        this.boardUi = boardUi;
    }

    public void setYXaxis(int y_axis, int x_axis) {
        if (checkOutSizeInt(x_axis) || checkOutSizeInt(y_axis)) {
            return;
        }
        this.x_axis = x_axis;
        this.y_axis = y_axis;
    }

    public void setTypeUI(String typeUI) {
        if (typeUI == null) {
            return;
        }
        this.typeUI = typeUI;
    }

    public List<AbsSubUi> Draw() {
        List<AbsSubUi> listUI = new ArrayList<>();
        if (this.boardUi == null) {
            return null;
        }
        this.boardUi.removeAll();
        GridLayout a = new GridLayout(y_axis, x_axis, 2, 2);
        this.boardUi.setLayout(a);
        int sumUI = this.x_axis * this.y_axis;
        if (sumUI <= 0) {
            return null;
        } else if (sumUI == 1) {
            listUI.add(drawOne("main"));
        } else {
            for (int y = 0; y < this.y_axis; y++) {
                for (int x = 1; x <= this.x_axis; x++) {
                    listUI.add(drawOne(String.format("%s%s", (char) ('A' + y), x)));
                }
            }
        }
        return listUI;
    }

    private static boolean checkOutSizeInt(int value) {
        return value < 1 || value / 2 >= Integer.MAX_VALUE / 2;
    }

    private AbsSubUi drawOne(String indexName) {
        if (indexName == null || indexName.isBlank()) {
            return null;
        }
        AbsSubUi subUi = FactoryUI.getInstance().getUIType(this.typeUI, indexName);
        this.boardUi.add(subUi);
        return subUi;
    }
}
