/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataSource;

import Model.Interface.IInit;
import Model.DataSource.Setting.Setting;

/**
 *
 * @author Administrator
 */
public class LoadSource implements IInit {

    private final InfoBase info;
    private final Setting setting;

    public LoadSource() {
        this.info = InfoBase.getInstance();
        this.info.setPath("Model/FilePathBase/config.json");
        this.setting = Setting.getInstance();
    }

    @Override
    public boolean init() {
        this.info.init();
        this.setting.setFile(this.info.getPathOfSetting());
        this.setting.init();
        return true;
    }

}
