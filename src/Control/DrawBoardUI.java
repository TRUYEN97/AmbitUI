/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import Control.Core.Core;
import Model.Factory.Factory;
import Model.ManagerUI.UIManager;
import View.UIView;
import View.subUI.SubUI.AbsSubUi;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.Objects;
import javax.swing.JPanel;

/**
 *
 * @author Administrator
 */
public class DrawBoardUI {

    private int colSize = 1;
    private int rowSize = 1;
    private String typeUI;
    private final Core core;
    private final UIManager uImanager;
    private final UIView view;

    public DrawBoardUI(Core core) {
        this.core = core;
        this.uImanager = core.getUiManager();
        this.view = core.getView();
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
        this.uImanager.clear();
        this.view.setBoardSubUISize(rowSize, colSize);
        if (isMultiThread()) {
            for (int row = 0; row < rowSize; row++) {
                for (int col = 1; col <= colSize; col++) {
                    drawOne(String.format("%s%s", (char) ('A' + row), col));
                }
            }
        } else {
            drawOne("main");
        }
    }

    private boolean isMultiThread() {
        return this.core.getCurrMode().getModeInfo().isMultiThread();
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
            this.view.addSubUi(subUi);
            subUi.setLoadMode(this.core);
            uImanager.addUI(subUi);
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
        return this.core.getCurrMode().getModeInfo().getColumn();
    }

    private int getRow() {
        return this.core.getCurrMode().getModeInfo().getRow();
    }

    private String getType() {
        return this.core.getCurrMode().getModeInfo().getTypeUI();
    }
}
