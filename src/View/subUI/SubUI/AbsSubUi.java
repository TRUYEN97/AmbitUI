/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.subUI.SubUI;

import Model.DataSource.Tool.IgetTime;
import Model.DataTest.FunctionData.ItemTestData;
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
    private IgetTime testTimer;

    protected AbsSubUi(String name, int time) {
        super(name, time);
        this.setBorder(new LineBorder(Color.BLACK, 2));
        tabDetail = new TabDetail(this);
    }

    public abstract void setText(String txt);

    @Override
    public boolean update() {
        return this.tabDetail.update();
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
        updateData();
        showEnd(getTestColor(), this.uiStatus.getProcessData().isPass());
    }

    private Color getTestColor() {
        if (this.uiStatus.getProcessData().isPass()) {
            return Color.green;
        } else {
            ItemTestData testData = this.uiStatus.getProcessData().getFirstFail();
            ItemTestData.TYPE type = testData.getItemType();
            switch (type) {
                case INIT -> {
                    return new Color(0xCC00FF);
                }
                case TEST -> {
                    return Color.red;
                }
                case END -> {
                    return Color.red;
                }
                case FINAL -> {
                    return new Color(0x33FFCC); //009999
                }
            }
        }
        return null;
    }

    protected abstract void showEnd(Color color, boolean isPass);

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

    public void showSfisMessager(String string) {
        if (this.view == null) {
            return;
        }
        this.view.showSfisText(String.format("%s: %s", getName(), string));
    }

    protected String getTestTime() {
        if (testTimer == null) {
            return null;
        }
        long time = (long) (testTimer.getRuntime());
        long hour = time / 3600;
        long minute = (time - hour * 3600) / 60;
        long second = time % 60;
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    public void setClock(IgetTime myTimer) {
        this.testTimer = myTimer;
    }
}
