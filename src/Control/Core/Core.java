/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Core;

import Control.Mode.LoadMode;
import Model.DataSource.Setting.Setting;
import View.DrawBoardUI.DrawBoardUI;
import View.UIView;

/**
 *
 * @author Administrator
 */
public class Core {

    private final Setting setting;
    private final DrawBoardUI drawBoardUI;
    private final LoadMode loadMode;
    private UIView view;

    public Core() {
        this.setting = Setting.getInstance();
        this.loadMode = new LoadMode(this.setting.getDefaultMode());
        this.drawBoardUI = new DrawBoardUI(loadMode);
    }

    public void run() {
        drawUI();
        showUI();
    }

    private void showUI() {
        java.awt.EventQueue.invokeLater(() -> {
            view.setVisible(true);
        });
    }

    private void drawUI() {
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
        this.view = new UIView(loadMode);
        drawBoardUI.setBoardUi(this.view.getBoardUI());
        drawBoardUI.setting();
        drawBoardUI.Draw();
    }

}
