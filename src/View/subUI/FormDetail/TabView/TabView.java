/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * PanelTabView.java
 *
 * Created on Feb 7, 2022, 9:57:51 AM
 */
package View.subUI.FormDetail.TabView;

import Model.DataTest.DataBoxs.FunctionData;
import View.subUI.FormDetail.AbsTabUI;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class TabView extends AbsTabUI {

    /**
     * Creates new form PanelTabView
     *
     * @param type
     */
    public TabView(String type) {
        super("View", type, 1000);
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbStatus = new javax.swing.JLabel();

        setBackground(new java.awt.Color(204, 204, 255));

        lbStatus.setBackground(new java.awt.Color(153, 153, 255));
        lbStatus.setFont(new java.awt.Font("Segoe UI", 1, 34)); // NOI18N
        lbStatus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbStatus.setLabelFor(this);
        lbStatus.setText("READY");
        lbStatus.setName("lbStatus"); // NOI18N
        lbStatus.setOpaque(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbStatus, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lbStatus;
    // End of variables declaration//GEN-END:variables

    @Override
    public void keyEvent(KeyEvent evt) {
    }

    @Override
    public void startTest() {
        super.startTest(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        this.lbStatus.setBackground(Color.yellow);
    }

    @Override
    public void endTest() {
        super.endTest(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        showMess(this.uiStatus.getUiData().getMassage());
        if (this.uiStatus.getUiData().isPass()) {
            this.lbStatus.setBackground(Color.GREEN);
        } else {
            this.lbStatus.setBackground(Color.red);
        }
    }

    @Override
    public void updateData() {
        if (!this.isVisible()) {
            return;
        }
        if (this.uiStatus.isTesting()) {
            showItemTesting();
        } else {
            showMess(this.uiStatus.getUiData().getMassage());
        }

    }

    private void showMess(String text) {
        String mess = String.format("<html>%s</html>",text);
        this.lbStatus.setText(mess);
    }

    private void showItemTesting() {
        StringBuilder mess = new StringBuilder("<html>");
        List<FunctionData> dataBoxs = this.uiStatus.getUiData().getDataBoxs();
        for (FunctionData dataBox : dataBoxs) {
            if (dataBox.isTesting()) {
                mess.append(String.format("<tr><td>%s</td></tr>", dataBox.getItemFunction()));
            }
        }
        mess.append("</html>");
        this.lbStatus.setText(mess.toString());
    }
}
