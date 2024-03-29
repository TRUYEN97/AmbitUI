/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View.subUI.FormDetail.TabItem.ShowLog;

import Model.AllKeyWord;
import Model.DataSource.ModeTest.FunctionConfig.FunctionName;
import View.subUI.FormDetail.TabItem.TabItem;
import Model.DataTest.FunctionData.FunctionData;
import Model.ManagerUI.UIStatus.UiStatus;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Queue;
import javax.swing.Timer;
import javax.swing.text.DefaultCaret;

/**
 *
 * @author Administrator
 */
public class ShowLog extends javax.swing.JFrame {

    /**
     * Creates new form ItemLog
     */
    private FunctionData dataBox;
    private UiStatus uiStatus;
    private Queue<String> queueLog;
    private final Timer timer;
    private final TabItem tabItem;
    private final StringBuffer buffer;

    public ShowLog(TabItem tabItem) {
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ShowLog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        initComponents();
        this.tabItem = tabItem;
        this.buffer = new StringBuffer();
        this.timer = new Timer(500, (ActionEvent e) -> {
            if (queueLog == null) {
                return;
            }
            ShowLog.this.timer.stop();
            while (!queueLog.isEmpty()) {
                ShowLog.this.buffer.append(queueLog.poll());
            }
            ShowLog.this.txtLog.append(buffer.toString());
            buffer.setLength(0);
            if (dataBox.isTesting()) {
                ShowLog.this.timer.start();
            }
        });
    }

    public void setlog(String data) {
        this.txtLog.setText(data);
    }

    public void appenLog(String data) {
        this.txtLog.append(data);
    }

    public void showLog() {
        String index = uiStatus.getName();
        FunctionName funcName = dataBox.getFunctionName();
        String sn = uiStatus.getProcessData().getString(AllKeyWord.SFIS.SN);
        String mlbsn = uiStatus.getProcessData().getString(AllKeyWord.SFIS.MLBSN);
        String mac = uiStatus.getProcessData().getString(AllKeyWord.SFIS.MAC);
        String title = String.format("%s %s %s %s %s", index, funcName,
                sn == null ? "" : sn, mlbsn == null ? "" : mlbsn, mac == null ? "" : mac);
        this.setTitle(title);
        setVisible(true);
        if (dataBox.isTesting() && this.queueLog == null) {
            this.queueLog = dataBox.getLoger().getQueueLog();
            this.timer.start();
        } else {
            this.txtLog.setText(dataBox.getLog());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        txtLog = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        txtLog.setEditable(false);
        txtLog.setColumns(20);
        txtLog.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtLog.setRows(5);
        txtLog.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtLogMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(txtLog);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        stopTimer();
        this.tabItem.removeShowLog(this.dataBox);
        this.dispose();
    }//GEN-LAST:event_formWindowClosed

    private void txtLogMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtLogMouseClicked
        // TODO add your handling code here:
        if (evt.getButton() == MouseEvent.BUTTON3 && evt.getClickCount() > 1) {
            if (this.txtLog.getDocument().getLength() > 0) {
                txtLog.setCaretPosition(this.txtLog.getDocument().getLength());
            }
            DefaultCaret caret = (DefaultCaret) txtLog.getCaret();
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        }
    }//GEN-LAST:event_txtLogMouseClicked

    /**
     * @param args the command line arguments
     */
    public void stopTimer() {
        this.timer.stop();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea txtLog;
    // End of variables declaration//GEN-END:variables

    public void setDataBox(FunctionData dataBox, UiStatus uiStatus) {
        this.uiStatus = uiStatus;
        this.dataBox = dataBox;
    }
}
