/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Core;

import Control.CheckInput;
import Model.DataSource.PcInformation;
import Model.DataSource.Setting.Setting;
import Model.DataSource.Setting.ModeElement;
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
    private final PcInformation pcInfor;

    public Engine() {
        this.setting = Setting.getInstance();
        this.modeTests = new ArrayList<>();
        this.core = new Core(new UIView());
        this.view = this.core.getView();
        this.pcInfor = PcInformation.getInstance();
        this.checkInput = new CheckInput(core, view);
        this.view.setCheckInput(checkInput);
    }

    public void run() {
        getAllMode();
        setMode();
        showUI();
    }

    private void getAllMode() {
        for (ModeElement modeInfo : setting.getElments()) {
            this.modeTests.add(new ModeTest(modeInfo, this.core));
        }
    }

    private void setMode() {
        this.view.setMode(this.modeTests);
        if (!modeTests.isEmpty()) {
            this.core.setCurrMode(modeTests.get(0));
        }
    }

    private void showUI() {
        this.view.showIp(pcInfor.getIp());
        this.view.showPcName(pcInfor.getPcName());
        this.view.showGiaiDoan(setting.getGiaiDoan());
        java.awt.EventQueue.invokeLater(() -> {
            this.view.setVisible(true);
        });
    }

}
