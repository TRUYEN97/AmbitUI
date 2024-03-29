/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * Detail.java
 *
 * Created on Nov 30, 2021, 9:54:11 AM
 */
package View.subUI.FormDetail;

import Model.ManagerUI.UIStatus.UiStatus;

/**
 *
 * @author Administrator
 */
public class FormShow extends javax.swing.JFrame {

    private TabDetail tabDetail;

    /**
     * Creates new form Detail
     *
     */
    public FormShow() {
        initComponents();
    }

    public void showDetail(TabDetail tabDetail) {
        this.tabDetail = tabDetail;
        java.awt.EventQueue.invokeLater(() -> {
            add(tabDetail);
            UiStatus uiStatus = tabDetail.getBoss().getUiStatus();
            if (uiStatus != null) {
                setTitle(String.format("%s  ->  %s :  %s",
                        tabDetail.getBoss().getName(),
                        uiStatus.getModeTest().getModeName(),
                        uiStatus.getModeTest().getModeType()));
            } else {
                setTitle(String.format("%s", tabDetail.getBoss().getName()));
            }
            setVisible(true);
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        setSize(new java.awt.Dimension(757, 403));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
