/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.subUI.SubUI;

import Control.Core.Core;
import Model.DataSource.Setting.ModeElement;
import Model.Factory.Factory;
import Model.Interface.IUpdate;
import View.UIView;
import View.subUI.AbsUI;
import View.subUI.FormDetail.TabDetail;
import java.awt.Color;
import javax.swing.border.LineBorder;

/**
 *
 * @author Administrator
 */
public abstract class AbsSubUi extends AbsUI implements IUpdate {

    protected final TabDetail tabDetail;
    protected UIView view;
    protected Core loadMode;

    protected AbsSubUi(String name) {
        super(name);
        this.setBorder(new LineBorder(Color.BLACK, 2));
        this.setToolTipText(name);
        tabDetail = new TabDetail(this);
    }

    public void setLoadMode(Core loadMode) {
        this.loadMode = loadMode;
    }

    public abstract void setText(String txt);

    @Override
    public boolean update() {
        try {
            ModeElement modeInfo;
            Factory factory;
            modeInfo = this.loadMode.getCurrMode().getModeInfo();
            factory = Factory.getInstance();
            if (isOldDetails(modeInfo)) {
                return true;
            }
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

    private boolean isOldDetails(ModeElement modeInfo) {
        return this.tabDetail.getListTab().equals(modeInfo.getDetail());
    }

    public void setUiView(UIView view) {
        if (view == null) {
            return;
        }
        this.view = view;
    }

}
