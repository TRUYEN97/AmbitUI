/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataSource;

import Model.Interface.IInit;
import Model.DataSource.Setting.Setting;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class LoadSource implements IInit {

    private final InfoBase info;
    private final List<AbsSource> sources;

    public LoadSource() {
        this.info = InfoBase.getInstance();
        this.info.setPath("Model/FilePathBase/config.json");
        this.info.init();
        this.sources = new ArrayList<>();
        this.sources.add(Setting.getInstance().setPath(this.info.getPathOfSetting()));
    }

    @Override
    public boolean init() {
        for (AbsSource source : sources) {
            if (!source.init()) {
                return false;
            }
        }
        return true;
    }

}
