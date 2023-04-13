/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package View.subUI.SubUI.SmallUI;

import Model.DataTest.FunctionData.FunctionData;
import Model.DataTest.FunctionData.ItemTestData;
import View.subUI.FormDetail.FormShow;
import View.subUI.SubUI.AbsSubUi;
import java.awt.Color;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class SmallUI extends AbsSubUi {

    private final FormShow formShow;
    private List<FunctionData> list;
    private static final String DEFAULT_FORM = "<html><center>%s<br>%s<br>%s</html>";

    /**
     * Creates new form SmallUI
     *
     * @param indexName
     */
    public SmallUI(String indexName) {
        super(indexName, 1000);
        initComponents();
        this.lbTime.setToolTipText(indexName);
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
        lbTime.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lbTime.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbTime.setText("00:00");
        lbTime.setOpaque(true);
        lbTime.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbTimeMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lbTimeMouseEntered(evt);
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

    private final String LABLEL_NAME_HTML = "<center><u><b><span style=\"font-size: 14px\">%s</span></u></b><br>";
    private void lbTimeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbTimeMouseEntered
        // TODO add your handling code here:
        StringBuilder messHtml = new StringBuilder("<html>");
        StringBuilder mess = new StringBuilder();
        messHtml.append(String.format(LABLEL_NAME_HTML, getName()));
        mess.append(String.format("%s\r\n", this.uiStatus.getModeTest().getModeName()));
        mess.append(String.format("----------------- %s ------------------\r\n", getName()));
        if (this.uiStatus.isTesting()) {
            messHtml.append("<table>");
            messHtml.append(String.format("<tr><td><center><span style=\"font-size: 14px\">Mode: %s</td></span></tr>", this.uiStatus.getModeTest().getModeName()));
            List<FunctionData> dataBoxs = this.uiStatus.getProcessData().getDataBoxs();
            String itemName;
            for (FunctionData dataBox : dataBoxs) {
                if (dataBox.isTesting()) {
                    itemName = dataBox.getFunctionName().getItemName();
                    mess.append("   - ").append(itemName).append("\r\n");
                    messHtml.append(String.format("<tr><td><center><span style=\"font-size: 16px\">%s</span></td></tr>", itemName));
                }
            }
            messHtml.append("</table></html>");
        } else {
            String text = this.uiStatus.getProcessData().getMassage();
            messHtml.append(String.format("<span style=\"font-size: 16px\">%s</span></html>",
                    text == null ? "Finished!" : text.replaceAll("\r\n", "<br>")));
            mess.append(String.format(" \"%s\"", text));
        }
        this.lbTime.setToolTipText(messHtml.toString());
        this.view.showSfisText(mess.toString());
    }//GEN-LAST:event_lbTimeMouseEntered

    @Override
    public void startTest() {
        this.list = uiStatus.getProcessData().getDataBoxs();
        super.startTest(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        this.lbTime.setBackground(this.uiStatus.getModeTest().getTestColor());
        if (this.formShow.isVisible()) {
            this.formShow.dispose();
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
        lbTime.setText(String.format(DEFAULT_FORM,
                getName(), list == null ? 0 : list.size(), getTestTime()));
    }

    @Override
    protected void showEnd(Color testColor, boolean isPass) {
        if (!isPass) {
            ItemTestData testData = this.uiStatus.getProcessData().getFirstFail();
            lbTime.setText(String.format(DEFAULT_FORM,
                    getName(), testData.getLocalErrorCode(), getTestTime()));
        }
        lbTime.setBackground(testColor);
    }
}
