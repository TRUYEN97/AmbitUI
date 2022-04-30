/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Core;

import Control.Mode.LoadMode;
import Model.DataSource.Setting.Setting;
import Control.DrawBoardUI;
import Control.Mode.ModeTest;
import Model.DataSource.Setting.ModeInfo;
import View.UIView;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class Core {

    private final Setting setting;
    private final LoadMode loadMode;
    private final List<ModeTest> modeTests;
    private UIView view;

    public Core() {
        this.setting = Setting.getInstance();
        this.modeTests = new ArrayList<>();
        this.loadMode = new LoadMode(new UIView());
    }

    public void run() {
        getAllMode();
        showUI();
    }

    private void getAllMode() {
        for (ModeInfo modeInfo : setting.getModeInfos()) {
            this.modeTests.add(new ModeTest(modeInfo));
        }
    }

    private void showUI() {
        this.view = loadMode.getView();
        this.view.setMode(this.modeTests);
        java.awt.EventQueue.invokeLater(() -> {
            view.setVisible(true);
        });
    }

}
