/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Mode;

import Model.DataModeTest.DataInputMode;
import Model.DataSource.Setting.ModeInfo;
import Model.ManagerUI.ManagerUI;

/**
 *
 * @author Administrator
 */
public class CheckInput {

    private final DataInputMode dataInput;
    private final ModeInfo modeInfo;

    public CheckInput(ModeInfo modeInfo) {
        this.dataInput = new DataInputMode();
        this.modeInfo = modeInfo;
    }

    public boolean checkInput(String input) {
        if ( isIndex(input)) {
            addIndex(input);
        } else {
            addSn(input);
        }
        return inputOK();
    }

    private boolean hasSn() {
        String sn = this.dataInput.getSnInput();
        return !(sn == null || sn.isBlank());
    }

    private boolean hasIndex() {
        String index = this.dataInput.getIndex();
        return !(index == null || index.isBlank());
    }

    private void addIndex(String input) {
        this.dataInput.setIndex(input);
    }

    private void addSn(String input) {
        this.dataInput.setSnInput(input);
    }

    private boolean isIndex(String input) {
        return ManagerUI.getInstance().isIndex(input);
    }

    private boolean inputOK() {
        if (modeInfo.isMutiThread()) {
            return hasIndex() && hasSn();
        }
        return hasSn();
    }
}
