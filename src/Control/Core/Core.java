/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Core;

import Control.DrawBoardUI;
import Model.DataModeTest.InputData;
import Model.DataSource.AbsJsonSource;
import Model.DataSource.FunctionConfig.FunctionConfig;
import Model.DataSource.Setting.ModeElement;
import Model.DataSource.Setting.Setting;
import Model.ManagerUI.UIManager;
import View.UIView;
import java.awt.HeadlessException;
import java.io.File;
import static java.util.Objects.isNull;
import javax.swing.JOptionPane;

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
            if (!updateFunctionsConfig(modeTest)) {
                return false;
            }
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

    private boolean updateFunctionsConfig(ModeTest modeTest) throws HeadlessException {
        if (!checkAmbitConfig(modeTest)) {
            JOptionPane.showMessageDialog(null, "Update functionsConfig failed!");
            return false;
        }
        if (!checkStationName(modeTest)) {
            JOptionPane.showMessageDialog(null,
                    "Station setting and station functionsConfig different!");
            return false;
        }
        return true;
    }

    private boolean isCurrentMode(ModeTest item) {
        return this.getCurrMode() != null && getCurrMode().equals(item);
    }

    private void backUpMode() {
        this.view.setSelectMode(getCurrMode());
    }

    private boolean checkAmbitConfig(ModeTest modeTest) {
        File newFileConfig = modeTest.getModeInfo().getAmbitConfigFile();
        if (currMode != null) {
            File oldFileConfig = currMode.getModeInfo().getAmbitConfigFile();
            if (newFileConfig == null || oldFileConfig == null) {
                return false;
            }
            if (newFileConfig.getPath().equals(oldFileConfig.getPath())) {
                return true;
            }
        }
        AbsJsonSource source = FunctionConfig.getInstance().setPath(newFileConfig.getPath());
        return (source != null && source.init());
    }

    private boolean checkStationName(ModeTest modeTest) {
        String settingStation = modeTest.getModeInfo().getStationName();
        String ambitConfigStation = FunctionConfig.getInstance().getStationName();
        if (settingStation == null || ambitConfigStation == null) {
            return false;
        }
        return settingStation.equalsIgnoreCase(ambitConfigStation);
    }
}
