/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package View.subUI.SubUI.SmallUI;

import View.subUI.FormDetail.FormShow;
import View.subUI.SubUI.AbsSubUi;
import java.awt.Color;

/**
 *
 * @author Administrator
 */
public class SmallUI extends AbsSubUi {
    
    private final FormShow formShow;

    /**
     * Creates new form SmallUI
     *
     * @param indexName
     */
    public SmallUI(String indexName) {
        super(indexName, 500);
        initComponents();
        this.lbTime.setText(indexName);
        this.formShow = new FormShow();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbTime = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        lbTime.setBackground(new java.awt.Color(102, 102, 255));
        lbTime.setFont(new java.awt.Font("Tahoma", 1, 14));
        lbTime.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbTime.setText("00:00");
        lbTime.setOpaque(true);
        lbTime.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbTimeMouseClicked(evt);
            }
        });
        add(lbTime, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void lbTimeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbTimeMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() > 1) {
            this.formShow.showDetail(tabDetail);
        }
    }//GEN-LAST:event_lbTimeMouseClicked

    @Override
    public void startTest() {
        super.startTest(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        this.lbTime.setBackground(Color.yellow);
    }

    
    
    @Override
    public void endTest() {
        super.endTest(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        if (this.uiStatus.getProcessData().isPass()) {
            this.lbTime.setBackground(Color.GREEN);
        } else {
            this.lbTime.setBackground(Color.red);
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JLabel lbTime;
    // End of variables declaration//GEN-END:variables

    @Override
    public void setText(String txt) {
        this.lbTime.setText(txt);
    }
    
    @Override
    public void updateData() {
        lbTime.setText(getTestTime());
    }
}
