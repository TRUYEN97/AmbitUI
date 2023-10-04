/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Core;

import Control.DrawBoardUI;
import Model.AllKeyWord;
import Model.DataTest.InputData;
import Model.ErrorLog;
import Model.ManagerUI.UIManager;
import Model.ManagerUI.UIStatus.UiStatus;
import View.UIView;
import java.awt.HeadlessException;
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
    private int updateStatus;

    public Core(UIView view) {
        this.view = view;
        this.uIManager = new UIManager(this);
        this.drawBoardUI = new DrawBoardUI(this);
        this.updateStatus = 0;
    }

    public int getUpdateStatus() {
        return updateStatus;
    }

    public void setUpdateStatus(int updateStatus) {
        this.updateStatus = updateStatus;
    }

    public void checkInput(InputData inputData) {
        if(this.updateStatus != 0 && uIManager.isNotTest()){
            System.exit(0);
        }
        if (inputData != null && checkIndex(inputData)) {
            UiStatus uiStatus = uIManager.getUiStatus(inputData.getIndex());
            if (uiStatus.update()) {
                uiStatus.startTest(inputData);
            } else {
                String mess = String.format("%s update mode fail!", inputData.getIndex());
                ErrorLog.addError(this, mess);
                JOptionPane.showMessageDialog(null, mess);
            }
        }
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
            this.view.setPnName(this.currMode.getModeConfig().getPnName());
            DhcpRunner.getInstance().setUseDHCP(this.currMode.isUseDHCP());
            return uIManager.update();
        }
        return false;
    }

    private boolean checkIndex(InputData inputData) {
        if (isIndexEmpty(inputData)) {
            if (this.currMode.getModeTestSource().isMultiThread()) {
                getIndex(inputData);
            } else {
                inputData.setIndex("");
            }
        }
        return this.uIManager.isIndexFree(inputData.getIndex()) && this.isInputNoOverlap(inputData);

    }

    private boolean isIndexEmpty(InputData inputData) {
        String index = inputData.getIndex();
        return index == null || index.isBlank();
    }

    private boolean getIndex(InputData inputData) throws HeadlessException {
        String index = JOptionPane.showInputDialog(null,
                String.format("Input index for \"%s\"", inputData.getInput()));
        if (uIManager.isIndexFree(index)) {
            inputData.setIndex(index);
            return true;
        }
        return false;
    }

    private boolean isCurrentMode(ModeTest item) {
        return this.getCurrMode() != null && getCurrMode().equals(item);
    }

    private void backUpMode() {
        this.view.setSelectMode(getCurrMode());
    }

    private boolean isInputNoOverlap(InputData inputData) {
        String sn = inputData.getInput();
        if (sn == null || sn.isBlank()) {
            return false;
        }
        for (UiStatus uiStatuse : this.uIManager.getUiStatuses()) {
            String testSn = uiStatuse.getProcessData().getString(AllKeyWord.SFIS.SN);
            if (uiStatuse.isTesting() && testSn != null && testSn.equalsIgnoreCase(sn)) {
                String index = uiStatuse.getName(); 
                this.view.showSfisText(String.format(" %s is testing at %s\r\n %s đang test ở %s", sn, index, sn, index));
                return false;
            }
        }
        return true;
    }

}
