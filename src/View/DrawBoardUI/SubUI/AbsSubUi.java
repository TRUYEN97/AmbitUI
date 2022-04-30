/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.DrawBoardUI.SubUI;

import Control.Mode.LoadMode;
import Control.Mode.ModeTest;
import Model.DataSource.Setting.ModeInfo;
import Model.Factory.Factory;
import Model.Interface.IUpdate;
import View.DrawBoardUI.AbsUI;
import View.DrawBoardUI.FormDetail.TabDetail;
import java.awt.Color;
import javax.swing.border.LineBorder;

/**
 *
 * @author Administrator
 */
public abstract class AbsSubUi extends AbsUI implements IUpdate {

    protected final TabDetail tabDetail;
    protected LoadMode loadMode;

    protected AbsSubUi(String name) {
        super(name);
        this.setBorder(new LineBorder(Color.BLACK, 2));
        this.setToolTipText(name);
        tabDetail = new TabDetail(this);
    }

    public void setLoadMode(LoadMode loadMode) {
        this.loadMode = loadMode;
    }

    public abstract void setText(String txt);

    @Override
    public boolean update() {
        try {
            ModeInfo modeInfo;
            Factory factory;
            modeInfo = this.loadMode.getCurrMode().getModeInfo();
            factory = Factory.getInstance();
            this.tabDetail.clear();
            for (String name : modeInfo.getDetail()) {
                this.tabDetail.addTab(factory.getTabUI(name));
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ModeTest getMode() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
