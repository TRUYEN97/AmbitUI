/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Core;

import Control.CheckInput;
import Model.DataSource.Setting.Setting;
import Model.DataModeTest.DataCore;
import Model.DataSource.Setting.ModeInfo;
import View.UIView;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class Engine {

    private final Setting setting;
    private final Core core;
    private final List<ModeTest> modeTests;
    private final CheckInput checkInput;
    private final UIView view;
    private final DataCore dataCore;

    public Engine() {
        this.setting = Setting.getInstance();
        this.dataCore = new DataCore();
        this.modeTests = new ArrayList<>();
        this.core = new Core(new UIView(), this.dataCore);
        this.checkInput = new CheckInput(this.core, this.dataCore);
        this.view = this.core.getView();
        this.view.setCheckInput(this.checkInput);
    }

    public void run() {
        getAllMode();
        setMode();
        showUI();
    }

    private void getAllMode() {
        for (ModeInfo modeInfo : setting.getElments()) {
            this.modeTests.add(new ModeTest(modeInfo));
        }
    }

    private void setMode() {
        this.view.setMode(this.modeTests);
        if (!modeTests.isEmpty()) {
            this.core.setCurrMode(modeTests.get(0));
        }
    }

    private void showUI() {
        java.awt.EventQueue.invokeLater(() -> {
            this.view.setVisible(true);
        });
    }

}
