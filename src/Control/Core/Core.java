/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Core;

import Control.DrawBoardUI;
import Model.DataModeTest.InputData;
import Model.ManagerUI.UIManager;
import Model.ManagerUI.UIStatus.UiStatus;
import View.UIView;
import static java.util.Objects.isNull;

/**
 *
 * @author 21AK22
 */
public class Core {
    
    private ModeTest currMode;
    private final DrawBoardUI drawBoardUI;
    private final UIView view;
    private final UIManager uIManager;
    
    public Core(UIView view) {
        this.view = view;
        this.uIManager = new UIManager(this);
        this.drawBoardUI = new DrawBoardUI(this);
    }
    
    public void checkInput(InputData inputDate) {
        currMode.runTest(inputDate);
    }
    
    public UIView getView() {
        if (isNull(view.getCore())) {
            this.view.setCore(this);
        }
        return view;
    }
    
    public ModeTest getCurrMode() {
        return currMode;
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
    
    public UIManager getUiManager() {
        return uIManager;
    }
    
    private boolean updateMode(ModeTest modeTest) {
        if (modeTest != null && modeTest.init()) {
            this.currMode = modeTest;
            if (drawBoardUI.isNewFormUI() && uIManager.isNotTest()) {
                drawBoardUI.setting();
                drawBoardUI.Draw();
            }
            this.view.setPnName(this.currMode.getModeInfo().getPnName());
            return uIManager.update();
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
