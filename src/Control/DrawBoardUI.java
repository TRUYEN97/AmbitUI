/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import Control.Core.Core;
import Model.Factory.Factory;
import Model.ManagerUI.ManagerUI;
import View.subUI.SubUI.AbsSubUi;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 *
 * @author Administrator
 */
public class DrawBoardUI {

    private int colSize = 1;
    private int rowSize = 1;
    private JPanel boardUi;
    private String typeUI;
    private final Core loadMode;
    private final ManagerUI managerUI;

    public DrawBoardUI(Core loadMode, ManagerUI managerUI) {
        this.loadMode = loadMode;
        this.managerUI = managerUI;
    }

    public void setBoardUi(JPanel boardUi) {
        if (boardUi == null) {
            return;
        }
        this.boardUi = boardUi;
    }

    public boolean isNewFormUI() {
        return (!getType().equals(this.typeUI)
                || getColumn() != this.colSize
                || getRow() != this.rowSize);
    }

    public void setting() {
        setTypeUI(getType());
        setYXaxis(getRow(), getColumn());
    }

    public void Draw() {
        if (Objects.isNull(boardUi)) {
            return;
        }
        clear();
        if (isMutiThread()) {
            for (int row = 0; row < rowSize; row++) {
                for (int col = 1; col <= colSize; col++) {
                    drawOne(String.format("%s%s", (char) ('A' + row), col));
                }
            }
        } else {
            drawOne("main");
        }
        boardUi.updateUI();
    }

    private boolean isMutiThread() {
        return this.loadMode.getCurrMode().getModeInfo().isMutiThread();
    }

    private void clear() {
        this.managerUI.clear();
        this.boardUi.removeAll();
        this.boardUi.setLayout(new GridLayout(rowSize, colSize, 2, 2));
    }

    private static boolean checkOutSizeInt(int value) {
        return value < 1 || value / 2 >= Integer.MAX_VALUE / 2;
    }

    private void drawOne(String indexName) {
        if (indexName == null || indexName.isBlank()) {
            return;
        }
        AbsSubUi subUi = Factory.getInstance().getSubUI(this.typeUI, indexName);
        if (subUi != null) {
            this.boardUi.add(subUi);
            subUi.setLoadMode(this.loadMode);
            managerUI.addUI(subUi);
        }
    }

    private void setYXaxis(int row, int col) {
        if (checkOutSizeInt(col) || checkOutSizeInt(row)) {
            return;
        }
        this.colSize = col;
        this.rowSize = row;
    }

    private void setTypeUI(String typeUI) {
        if (typeUI == null) {
            return;
        }
        this.typeUI = typeUI;
    }

    private int getColumn() {
        return this.loadMode.getCurrMode().getModeInfo().getColumn();
    }

    private int getRow() {
        return this.loadMode.getCurrMode().getModeInfo().getRow();
    }

    private String getType() {
        return this.loadMode.getCurrMode().getModeInfo().getTypeUI();
    }
}
