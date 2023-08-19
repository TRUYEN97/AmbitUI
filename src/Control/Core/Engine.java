/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Core;

import Control.CheckInput;
import Control.Socket.SocketClient;
import FileTool.FileService;
import Model.DataSource.ProgramInformation;
import Model.DataSource.Setting.Setting;
import Model.DataSource.Setting.ModeElement;
import Model.DataSource.dhcp.DhcpConfig;
import View.UIView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

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
    private final SocketClient client;

    public Engine(String version) throws Exception {
        this.setting = Setting.getInstance();
        this.modeTests = new ArrayList<>();
        this.core = new Core(new UIView());
        this.view = this.core.getView();
        this.programInfo = ProgramInformation.getInstance();
        if(version != null){
            this.programInfo.setVersion(version);
        }
        this.checkInput = new CheckInput(core, view);
        this.view.setCheckInput(checkInput);
        this.dhcpRunner = DhcpRunner.getInstance();
        this.client = new SocketClient(core, view);
    }

    public void run() {
        if (!initProgramInfo()) {
            System.exit(0);
        }
        initDHCP();
        initSocket();
        getAllMode();
        setMode();
        showUI();
    }

    private void initSocket() {
        this.client.setSocketLabel(this.view.getSocketLabel());
        this.client.start();
    }

    private void getAllMode() {
        for (ModeElement modeInfo : setting.getElments()) {
            ModeTest modeTest = new ModeTest(modeInfo);
            if (modeTest.update()) {
                this.modeTests.add(modeTest);
            } else {
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
        this.view.showIp(programInfo.getIpV4());
        this.view.showPcName(programInfo.getPcName());
        this.view.showGiaiDoan(setting.getProgress());
        java.awt.EventQueue.invokeLater(() -> {
            this.view.setVisible(true);
        });
    }

    private void initDHCP() {
        DhcpConfig dhcpConfig = this.setting.getDhcpConfig();
        if (dhcpConfig == null || !dhcpConfig.isOnDHCP()) {
            return;
        }
        this.dhcpRunner.setDhcpLabel(this.view.getDhcpLabel());
        this.dhcpRunner.setTextArea(this.view.getTextMess());
        this.dhcpRunner.run(
                dhcpConfig.getNetIP(),
                dhcpConfig.getleaseTime(),
                dhcpConfig.getMacLength());
    }

    private boolean initProgramInfo() {
        if (this.programInfo.getVersion() == null && new File(VERSION_PATH).exists()) {
            this.programInfo.setVersion(new FileService().readFile(new File(VERSION_PATH)));
        }
        String Dutmodel = this.setting.getDutMolel();
        if (Dutmodel == null || Dutmodel.isBlank()) {
            JOptionPane.showMessageDialog(null, "Not set DUT model!");
            return false;
        }
        this.programInfo.setDutModel(Dutmodel);
        return true;
    }
    public static final String VERSION_PATH = "init/version.txt";
}
