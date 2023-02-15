/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Core;

import Control.CheckInput;
import Model.DataSource.ProgramInformation;
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
    private final ProgramInformation programInfo;
    private final DhcpRunner dhcpRunner;

    public Engine() {
        this.setting = Setting.getInstance();
        this.modeTests = new ArrayList<>();
        this.core = new Core(new UIView());
        this.view = this.core.getView();
        this.programInfo = ProgramInformation.getInstance();
        this.checkInput = new CheckInput(core, view);
        this.view.setCheckInput(checkInput);
        this.dhcpRunner = DhcpRunner.getInstance();
    }

    public void run() {
        initProgramInfo();
        initDHCP();
        getAllMode();
        setMode();
        showUI();
    }
    
    private void getAllMode() {
        for (ModeElement modeInfo : setting.getElments()) {
            ModeTest modeTest = new ModeTest(modeInfo);
            if (modeTest.update()) {
                this.modeTests.add(modeTest);
            }else{
                break;
            }
        }
    }

    private void setMode() {
        this.view.setMode(this.modeTests);
        if (!modeTests.isEmpty()) {
            this.core.setCurrMode(modeTests.get(0));
        }
    }

    private void showUI() {
        this.view.setTitle(String.format("AmbitUI - Ver: %s", programInfo.getVersion()));
        this.view.showIp(programInfo.getIp());
        this.view.showPcName(programInfo.getPcName());
        this.view.showGiaiDoan(setting.getProgress());
        java.awt.EventQueue.invokeLater(() -> {
            this.view.setVisible(true);
        });
    }

    private void initDHCP() {
        String netIP;
        if ((netIP = this.setting.getDhcpNetIP()) == null) {
            return;
        }
        this.dhcpRunner.setTextArea(this.view.getTextMess());
        DhcpRunner.getInstance().run(netIP);
    }

    private void initProgramInfo() {
        this.programInfo.setVersion("V1.1.4.0");
    }
}
