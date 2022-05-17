/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.subUI.SubUI;

import Model.DataSource.Setting.ModeElement;
import Model.Factory.Factory;
import Model.Interface.IUpdate;
import Model.ManagerUI.UIStatus.UiStatus;
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
    

    protected AbsSubUi(String name) {
        super(name);
        this.setBorder(new LineBorder(Color.BLACK, 2));
        this.setToolTipText(name);
        tabDetail = new TabDetail(this);
    }
    
    public abstract void setText(String txt);

    @Override
    public boolean update() {
        try {
            ModeElement modeInfo;
            Factory factory;
            modeInfo = this.uiStatus.getModeTest().getModeInfo();
            factory = Factory.getInstance();
            if (isOldDetails(modeInfo)) {
                return true;
            }
            this.tabDetail.clear();
            for (String name : modeInfo.getDetail()) {
                this.tabDetail.addTab(factory.getTabUI(name));
            }
            this.tabDetail.setUiStatus(uiStatus);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void startTest() {
        super.startTest();
        this.tabDetail.startTest();
    }

    @Override
    public void endTest() {
        super.endTest(); 
        this.tabDetail.endTest();
    }
    
    

    @Override
    public void setUiStatus(UiStatus uiStatus) {
        super.setUiStatus(uiStatus);
        this.tabDetail.setUiStatus(uiStatus);
    }
    

    public void setUiView(UIView view) {
        if (view == null) {
            return;
        }
        this.view = view;
    }

    private boolean isOldDetails(ModeElement modeInfo) {
        return this.tabDetail.getListTab().equals(modeInfo.getDetail());
    }

}
