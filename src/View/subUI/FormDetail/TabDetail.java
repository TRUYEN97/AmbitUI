/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * PanelDetail.java
 *
 * Created on Jan 24, 2022, 5:18:06 PM
 */
package View.subUI.FormDetail;

import Model.DataTest.ErrorLog;
import Model.DataSource.Setting.ModeElement;
import Model.Factory.Factory;
import Model.Interface.IUpdate;
import Model.ManagerUI.UIStatus.UiStatus;
import View.subUI.AbsUI;
import View.subUI.SubUI.AbsSubUi;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class TabDetail extends AbsUI implements IUpdate {

    private final HashMap<String, AbsTabUI> tabElements;
    private final AbsSubUi boss;

    /**
     * Creates new form PanelDetail
     *
     * @param boss
     */
    public TabDetail(AbsSubUi boss) {
        super(boss.getName(),-1);
        this.tabElements = new HashMap<>();
        initComponents();
        this.boss = boss;
    }

    public AbsSubUi getBoss() {
        return this.boss;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabDetail = new javax.swing.JTabbedPane();

        setLayout(new java.awt.BorderLayout());

        tabDetail.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tabDetail.setName("tabDetail"); // NOI18N
        tabDetail.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabDetailStateChanged(evt);
            }
        });
        tabDetail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tabDetailKeyTyped(evt);
            }
        });
        add(tabDetail, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void tabDetailKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabDetailKeyTyped
        // TODO add your handling code here:
        for (String name : tabElements.keySet()) {
            tabElements.get(name).keyEvent(evt);
        }
    }//GEN-LAST:event_tabDetailKeyTyped

    private void tabDetailStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabDetailStateChanged
        // TODO add your handling code here:
        for (String key : tabElements.keySet()) {
            if (tabElements.get(key).isVisible()) {
                tabElements.get(key).updateData();
            }
        }
    }//GEN-LAST:event_tabDetailStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane tabDetail;
    // End of variables declaration//GEN-END:variables

    public void clear() {
        this.tabDetail.removeAll();
        this.tabElements.clear();
    }

    @Override
    public void setUiStatus(UiStatus uiStatus) {
        super.setUiStatus(uiStatus);
        for (String name : tabElements.keySet()) {
            tabElements.get(name).setUiStatus(uiStatus);
        }
    }

    @Override
    public void startTest() {
        for (String item : tabElements.keySet()) {
            tabElements.get(item).startTest();
        }
    }

    @Override
    public void endTest() {
        for (String item : tabElements.keySet()) {
            tabElements.get(item).endTest();
        }
    }

    private void addTab(AbsTabUI tabUI) {
        tabUI.setUiStatus(uiStatus);
        this.tabDetail.addTab(tabUI.getName(), tabUI);
        this.tabElements.put(tabUI.getType(), tabUI);
    }

    @Override
    public void updateData() {
    }

    @Override
    public boolean update() {
        try {
            addAllTab(getListTab());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLog.addError(e.getMessage());
            return false;
        }
    }

    private void addAllTab(List<AbsTabUI> tabTemp) {
        clear();
        for (AbsTabUI tabUI : tabTemp) {
            addTab(tabUI);
        }
    }

    private List<AbsTabUI> getListTab() {
        ModeElement modeInfo = this.uiStatus.getModeTest().getModeConfig();
        Factory factory = Factory.getInstance();
        List<AbsTabUI> tabTemp = new ArrayList();
        for (String type : modeInfo.getDetail()) {
            if (tabElements.containsKey(type)) {
                tabTemp.add(tabElements.get(type));
            } else {
                tabTemp.add(factory.getTabUI(type));
            }
        }
        return tabTemp;
    }

}
