/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Mode;

import Control.DrawBoardUI;
import Model.ManagerUI.ManagerUI;
import View.UIView;
import static java.util.Objects.isNull;

/**
 *
 * @author 21AK22
 */
public class LoadMode {

    private ModeTest currMode;
    private final CheckInput checkInput;
    private final DrawBoardUI drawBoardUI;
    private final UIView view;

    public LoadMode(UIView view) {
        this.checkInput = new CheckInput();
        this.drawBoardUI = new DrawBoardUI(this);
        this.drawBoardUI.setBoardUi(view.getBoardUI());
        this.view = view;
    }

    public void checkInput(char input) {
        if (this.checkInput.inputAnalysis(input)) {

        }
    }

    public UIView getView() {
        if (isNull(view.getLoadMode())) {
            this.view.setLoadMode(this);
        }
        return view;
    }

    public ModeTest getCurrMode() {
        return currMode;
    }

    public String getDataInput() {
        return this.checkInput.getDataInput();
    }

    public void setCurrMode(ModeTest item) {
        if (isCurrentMode(item)) {
            return;
        }
        if (updateMode(item)) {
            this.view.setSelectMode(getCurrMode());
        } else {
            backUpMode();
        }
    }

    private boolean updateMode(ModeTest modeTest) {
        if (modeTest != null && modeTest.init()) {
            this.currMode = modeTest;
            if (drawBoardUI.isNewFormUI() && ManagerUI.getInstance().isNotTest()) {
                drawBoardUI.setting();
                drawBoardUI.Draw();
            }
            return ManagerUI.getInstance().getListUI().update();
        }
        return false;
    }

    private boolean isCurrentMode(ModeTest item) {
        return this.getCurrMode() != null && getCurrMode().equals(item);
    }

    private void backUpMode() {
        this.view.setSelectMode(getCurrMode());
    }
}
