/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Launch;

import Control.Core.Core;
import Control.Mode.ModeTest;
import Model.DataSource.Setting.ModeInfo;
import Model.DataSource.Setting.Setting;
import View.ComboboxMode;
import com.alibaba.fastjson.JSONObject;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.util.Vector;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.SwingConstants;

/**
 *
 * @author Administrator
 */
public class LaunchMode {

    private JComboBox listMode;
    private final Setting setting;
    private final Core core;

    public LaunchMode(Core core) {
        this.core = core;
        this.setting = Setting.getInstance();
    }

    public void setListMode(JComboBox listMode) {
        this.listMode = listMode;
        initCbb();
        core.setCurrMode((ModeTest) this.listMode.getSelectedItem());
    }

    private void initCbb() {
        this.listMode.setModel(new ComboboxMode(createListMode()));
        this.listMode.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList jList, Object o, int i, boolean b, boolean b1) {
                JLabel rendrlbl = (JLabel) super.getListCellRendererComponent(jList, o, i, b, b1);    //todo: override
                rendrlbl.setHorizontalAlignment(SwingConstants.CENTER);
                return rendrlbl;
            }
        });
        this.listMode.addItemListener((java.awt.event.ItemEvent evt) -> {
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                ModeTest item = (ModeTest) evt.getItem();
                this.core.setCurrMode(item);
                System.out.println(item.getName());
            }
        });
    }

    private Vector<ModeTest> createListMode() {
        Vector<ModeTest> result = new Vector<>();
        for (var info : this.setting.getModeInfos()) {
            result.add(new ModeTest(new ModeInfo(info)));
        }
        return result;
    }

}
