/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.ManagerUI;

import Control.Core.UnitTest;
import View.subUI.SubUI.AbsSubUi;

/**
 *
 * @author 21AK22
 */
class UITest {

    private final UIData Data;
    private final UIInput input;
    private final AbsSubUi subUi;

    UITest(UIData Data, UIInput input, AbsSubUi subUi) {
        this.Data = Data;
        this.input = input;
        this.subUi = subUi;
    }

    boolean isTesting() {
        return false;
    }

    void setUnitTest(UnitTest unitTest) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
