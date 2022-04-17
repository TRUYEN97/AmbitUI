/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.DrawBoardUI;

import Model.Factory.Factory;
import Model.Factory.FactoryType;
import Model.WareHouse.ManagerUI;
import View.DrawBoardUI.SubUI.AbsSubUi;
import java.awt.GridLayout;
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

    public DrawBoardUI(JPanel boardUi) {
        this.boardUi = boardUi;
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

    public void Draw() {
        if (this.boardUi == null) {
            return;
        }
        ManagerUI managerUI = ManagerUI.getInstance();
        managerUI.reset();
        this.boardUi.removeAll();
        this.boardUi.setLayout(new GridLayout(y_axis, x_axis, 2, 2));
        int sumUI = this.x_axis * this.y_axis;
        if (sumUI == 1) {
            managerUI.addSubUI(drawOne("main"));
        } else if (sumUI > 1) {
            for (int row = 0; row < this.y_axis; row++) {
                for (int col = 1; col <= this.x_axis; col++) {
                    managerUI.addSubUI(drawOne(String.format("%s%s", (char) ('A' + row), col)));
                }
            }
        }
    }

    private static boolean checkOutSizeInt(int value) {
        return value < 1 || value / 2 >= Integer.MAX_VALUE / 2;
    }

    private AbsSubUi drawOne(String indexName) {
        if (indexName == null || indexName.isBlank()) {
            return null;
        }
        AbsSubUi subUi = Factory.getInstance().getSubUI(this.typeUI, indexName);
        if (subUi != null) {
            this.boardUi.add(subUi);
        }
        return subUi;
    }
}