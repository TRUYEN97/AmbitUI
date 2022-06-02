/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * TabOpion.java
 *
 * Created on Mar 29, 2022, 5:04:17 AM
 */
package View.subUI.FormDetail.TabFaApi;

import com.alibaba.fastjson.JSONObject;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class FormOption extends javax.swing.JPanel {

    /**
     * Creates new form TabOpion
     */
    private final ArrayList<TabOption> listTab;
    
    public FormOption() {
        initComponents();
        this.listTab = new ArrayList<>();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabPanel = new javax.swing.JTabbedPane();

        setLayout(new java.awt.BorderLayout());

        tabPanel.setBackground(new java.awt.Color(153, 153, 255));
        tabPanel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tabPanel.setName("tabPanel"); // NOI18N
        tabPanel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tabPanelKeyTyped(evt);
            }
        });
        add(tabPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

private void tabPanelKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabPanelKeyTyped
// TODO add your handling code here:
    if (evt.getKeyChar() == KeyEvent.VK_DELETE) {
        int indexDel = 0;
        for (TabOption tabOption : this.listTab) {
            if (tabOption.isVisible()) {
                this.tabPanel.removeTabAt(indexDel);
                this.listTab.remove(tabOption);
                return;
            }
            indexDel++;
        }
    }
}//GEN-LAST:event_tabPanelKeyTyped
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane tabPanel;
    // End of variables declaration//GEN-END:variables

    public void removeAllTab() {
        this.tabPanel.removeAll();
        this.listTab.clear();
    }
    
    public JSONObject getData() {
        JSONObject result = new JSONObject();
        for (TabOption tabOption : listTab) {
            result.put(tabOption.getName(), tabOption.getData());
        }
        return result;
    }
    
    public void addNewTab(String tabName, TabOption tab) {
        if (this.tabPanel.indexOfTab(tabName) != -1) {
            JOptionPane.showMessageDialog(null,
                    String.format("%s đã tồn tại!\r\n %s already exist!",
                            tabName, tabName));
            return;
        }
        this.tabPanel.addTab(tabName, tab);
        this.tabPanel.setSelectedIndex(tabPanel.indexOfComponent(tab));
        this.listTab.add(tab);
        this.updateUI();
    }
    
    public TabOption getTab(String tabName) {
        int indexitem = this.tabPanel.indexOfTab(tabName);
        if (indexitem != -1) {
            return this.getTab(indexitem);
        }
        return null;
    }
    
    public TabOption getTab(int index) {
        if (index < 0 || index >= this.tabPanel.getTabCount()) {
            return null;
        }
        return this.listTab.get(index);
    }
    
    public boolean isEmpty() {
        if (listTab.isEmpty()) {
            return true;
        }
        for (TabOption tab : listTab) {
            if (tab.isInputEmpty()) {
                return true;
            }
        }
        return false;
    }
}
