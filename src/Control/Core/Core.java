/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Core;

import Control.Mode.ModeTest;
import Model.DataSource.LoadSource;
import Model.ManagerUI.ManagerUI;
import View.UIView;

/**
 *
 * @author Administrator
 */
public class Core {

    private static volatile Core instance;
    private final LoadSource loadSource;
    private ModeTest currMode;

    private Core() {
        this.loadSource = new LoadSource();
        this.loadSource.init();
    }

    public static Core getInstance() {
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
        if (this.currMode.checkInput(input)) {
            this.currMode.run();
        }
    }

    public static void main(String[] args) {
        Core core = Core.getInstance();
        core.showUI();
    }

    private void showUI() {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UIView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(() -> {
            new UIView().setVisible(true);
        });
    }
}
