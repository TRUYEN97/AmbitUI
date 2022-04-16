/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.DrawBoardUI.SubUI;

import Control.Core.Core;
import Model.DataSource.Setting.ModeInfo;
import Model.Interface.IUpdate;
import View.DrawBoardUI.AbsUI;
import View.DrawBoardUI.FormDetail.AbsTabUI;
import View.DrawBoardUI.FormDetail.TabDetail;
import View.DrawBoardUI.UIWarehouse.Factory;
import java.awt.Color;
import javax.swing.border.LineBorder;

/**
 *
 * @author Administrator
 */
public abstract class AbsSubUi extends AbsUI implements IUpdate {

    protected final TabDetail tabDetail;

    protected AbsSubUi(String name) {
        super(name);
        this.setBorder(new LineBorder(Color.BLACK, 2));
        this.setToolTipText(name);
        tabDetail = new TabDetail(this);
    }

    public abstract void setText(String txt);

    @Override
    public void update() {
        ModeInfo modeInfo;
        Factory<AbsTabUI> factory;
        modeInfo = Core.getInstance().getCurrMode().getModeInfo();
        factory = Factory.getInstance();
        this.tabDetail.clear();
        for (String name : modeInfo.getDetail()) {
            this.tabDetail.addTab(factory.getUIType(name, name));
        }
    }

}
