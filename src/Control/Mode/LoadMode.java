/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Mode;

import View.ManagerUI.ManagerUI;

/**
 *
 * @author 21AK22
 */
public class LoadMode {
    private ModeTest currMode;
    private final CheckInput checkInput;
    public LoadMode(ModeTest modeTest) {
        this.checkInput = new CheckInput();
        this.currMode = modeTest;
    }
    

    public void checkInput(char input) {
        if(this.checkInput.inputAnalysis(input))
        {
            
        }
    }
    
    public boolean setCurrMode(ModeTest modeTest) {
        if (modeTest != null  && modeTest.init()) {
            this.currMode = modeTest;
            return ManagerUI.getInstance().getListUI().update();
        }
        return false;
    }

    public ModeTest getCurrMode() {
        return currMode;
    }

    public String getDataInput() {
        return this.checkInput.getDataInput();
    }
   
}
