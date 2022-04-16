/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Core;

import Control.Launch.LaunchMode;
import Control.Mode.ModeTest;
import Model.DataSource.Setting.Setting;
import View.DrawBoardUI.UIWarehouse.BigUIProxy;
import View.DrawBoardUI.UIWarehouse.Factory;
import View.DrawBoardUI.UIWarehouse.SmallProxy;
import Model.WareHouse.ManagerUI;
import View.DrawBoardUI.DrawBoardUI;
import View.DrawBoardUI.UIWarehouse.TabItemProxy;
import View.DrawBoardUI.UIWarehouse.TabLogProxy;
import View.DrawBoardUI.UIWarehouse.TabViewProxy;
import java.awt.event.KeyEvent;
import javax.swing.JComboBox;
import javax.swing.JPanel;

/**
 *
 * @author Administrator
 */
public class Core {

    private static volatile Core instance;
    private final Factory factoryUI;
    private final DrawBoardUI drawBoardUI;
    private final LaunchMode launchMode;
    private final Setting setting;
    private ModeTest currMode;

    private Core() {
        this.factoryUI = Factory.getInstance();
        this.drawBoardUI = new DrawBoardUI();
        this.setting = Setting.getInstance();
        this.launchMode = new LaunchMode(this);
    }
    
    public static Core getInstance()
    {
        Core temp = Core.instance;
        if (temp == null) {
            synchronized (Core.class) {
                temp = Core.instance;
                if (temp == null) {
                    Core.instance = temp = new Core();
                }
            }
        }
        return temp;
    }

    public void setDrawBoardUI(JPanel panel) {
        this.drawBoardUI.setBoardUi(panel);
        this.drawBoardUI.setTypeUI(this.setting.getTypeUI());
        this.drawBoardUI.setYXaxis(this.setting.getRow(), this.setting.getColumn());
        this.drawBoardUI.Draw();
    }

    public void setComboBox(JComboBox cbb) {
        this.launchMode.setListMode(cbb);
    }

    public void setCurrMode(ModeTest modeTest) {
        if (modeTest.init()) {
            this.currMode = modeTest;
            ManagerUI.getInstance().getListUI().update();
        }
    }

    public ModeTest getCurrMode() {
        return currMode;
    }

    public void input(String input) {
        
    }

}
