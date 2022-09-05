/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataSource.Tool;

import Model.DataSource.ModeTest.ErrorCode.ErrorCodeSource;
import Model.ErrorLog;
import Model.DataSource.PcInformation;
import Model.Interface.IInit;
import Model.DataSource.Setting.Setting;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class LoadSource implements IInit {

    private final InfoBase info;
    private final List<IInit> sources;

    public LoadSource() {
        this.info = InfoBase.getInstance();
        this.info.setPath("Model/FilePathBase/config.json");
        this.info.init();
        this.sources = new ArrayList<>();
        this.sources.add(Setting.getInstance().setPath(this.info.getPathOfSetting()));
        this.sources.add(ErrorCodeSource.getInstance().setPath(this.info.getPathOfErroCode()));
        this.sources.add(PcInformation.getInstance());
    }

    @Override
    public boolean init() {
        System.out.println("Init base class....");
        for (IInit source : sources) {
            System.out.println(String.format("\r\nInit %s class.................!",
                    source.getClass().getSimpleName()));
            if (!source.init()) {
                String mess = String.format("Init source file failed! %s",
                        source.getClass().getSimpleName());
                ErrorLog.addError(mess);
                JOptionPane.showMessageDialog(null, mess);
                return false;
            } else {
                System.out.println(String.format("Init %s class.................Ok!",
                        source.getClass().getSimpleName()));
            }
        }
        System.out.println("Init base class done!");
        return true;
    }

}
