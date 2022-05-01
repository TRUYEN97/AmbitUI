/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Core;

import Control.DrawBoardUI;
import Model.DataModeTest.DataCore;
import Model.ManagerUI.ManagerUI;
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
    private final ManagerUI managerUI;
    private final DataCore dataCore;

    public Core(UIView view, DataCore dataCore) {
        this.managerUI = new ManagerUI(this);
        this.drawBoardUI = new DrawBoardUI(this, this.managerUI);
        this.drawBoardUI.setBoardUi(view.getBoardUI());
        this.view = view;
        this.dataCore = dataCore;
    }

    public void run() {
       this.currMode.run();
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

    public ManagerUI getManagerUI() {
        return managerUI;
    }

    public DataCore getDataCore() {
        return dataCore;
    }

    private boolean updateMode(ModeTest modeTest) {
        if (modeTest != null && modeTest.init()) {
            this.currMode = modeTest;
            if (drawBoardUI.isNewFormUI() && managerUI.isNotTest()) {
                drawBoardUI.setting();
                drawBoardUI.Draw();
            }
            return managerUI.update();
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
