/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * TagLog.java
 *
 * Created on Feb 7, 2022, 6:28:13 PM
 */
package View.subUI.FormDetail.TabItem;

import Model.DataTest.FunctionData.FunctionData;
import View.subUI.FormDetail.AbsTabUI;
import View.subUI.FormDetail.ItemLog;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Administrator
 */
public class TabItem extends AbsTabUI {

    private static final String STAUS = "Staus";
    private static final String TIME = "Time";
    private static final String ITEM = "Item";
    private static final String STT = "STT";
    private static final String ERROR_CODE = "Error code";
    private static final String CUS_ERROR_CODE = "Cus error code";
    private final Vector<String> testColumn;
    private final Vector<String> listFunc;
    private final Map<FunctionData, ItemLog> itemLogs;
    private DefaultTableModel tableModel;

    /**
     * Creates new form TagLog
     *
     * @param type
     */
    public TabItem(String type) {
        super("Item", type, 1000);
        initComponents();
        this.testColumn = new Vector<>();
        this.listFunc = new Vector<>();
        this.itemLogs = new HashMap<>();
        addTestClomn();
        addListClomn();
        initTable(this.testColumn);
    }

    private void addListClomn() {
        this.listFunc.add(STT);
        this.listFunc.add(ITEM);
    }

    private void addTestClomn() {
        this.testColumn.add(STT);
        this.testColumn.add(ITEM);
        this.testColumn.add(TIME);
        this.testColumn.add(STAUS);
        this.testColumn.add(CUS_ERROR_CODE);
        this.testColumn.add(ERROR_CODE);
    }

    private void initTable(Vector<String> column) {
        int maxWith = (int) ((this.getWidth() - 1) / 6);
        int minWith = (int) (maxWith / 3);
        int[] sizeColumn = {minWith, maxWith, minWith, minWith, maxWith, maxWith};
        this.tableItem.setModel(
                new javax.swing.table.DefaultTableModel(null, column) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });
        this.tableItem.getTableHeader().setReorderingAllowed(true);//
        this.tableItem.setShowGrid(true);
        this.tableModel = (DefaultTableModel) this.tableItem.getModel();
        for (int i = 0; i < column.size(); i++) {
            setPropertiesColumn(i, sizeColumn[i], JLabel.CENTER, JLabel.CENTER);
        }
    }

    private void setPropertiesColumn(int index, int width, int alignment, int header) {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(alignment);
        this.tableItem.getColumnModel().getColumn(index).setMinWidth(width);
        this.tableItem.getColumnModel().getColumn(index).setCellRenderer(renderer);
        this.tableItem.getColumnModel().getColumn(index).setResizable(false);
        DefaultTableCellRenderer renderer1 = new DefaultTableCellRenderer();
        renderer1.setHorizontalTextPosition(header);
        this.tableItem.getColumnModel().getColumn(index).setHeaderRenderer(renderer);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tableItem = new javax.swing.JTable();

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        tableItem.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        tableItem.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tableItem.setModel(new javax.swing.table.DefaultTableModel(
            null,
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tableItem.setName("tableItem"); // NOI18N
        tableItem.setRowHeight(30);
        tableItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableItemMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableItem);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tableItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableItemMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() > 1) {
            showItemLogSelected();
        }
    }//GEN-LAST:event_tableItemMouseClicked

    private void showItemLogSelected() {
        FunctionData dataBox = this.uiStatus.getProcessData().getDataBox(getNameITem(this.tableItem.getSelectedRow()));
        if (dataBox == null) {
            return;
        }
        if (itemLogs.containsKey(dataBox)) {
            itemLogs.get(dataBox).showLog();
        } else {
            ItemLog itemLog = new ItemLog(this);
            itemLog.setDataBox(dataBox);
            itemLog.showLog();
            itemLogs.put(dataBox, itemLog);
        }

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tableItem;
    // End of variables declaration//GEN-END:variables

    private void showListFunction() {
        initTable(listFunc);
        for (String funcName : this.uiStatus.getModeTest().getModeTestSource().getItemTestFunctions()) {
            this.tableModel.addRow(new Object[]{this.tableModel.getRowCount(), funcName});
        }
    }

    @Override
    public void keyEvent(KeyEvent evt) {
        if (!isVisible()) {
            return;
        }
        if (evt.getKeyChar() == CTRL_S && !this.uiStatus.isTesting()) {
            showListFunction();
        }
    }
    private static final int CTRL_S = 19;

    @Override
    public void startTest() {
        this.itemLogs.clear();
        initTable(testColumn);
        super.startTest();
    }

    @Override
    public void endTest() {
        updateData();
        super.endTest();
    }

    @Override
    public void updateData() {
        if (!this.isVisible()) {
            return;
        }
        List<FunctionData> dataBoxs = this.uiStatus.getProcessData().getDataBoxs();
        for (FunctionData dataBox : dataBoxs) {
            int row = dataBoxs.indexOf(dataBox);
            if (row > this.tableModel.getRowCount() - 1) {
                this.tableModel.addRow(new Object[]{this.tableModel.getRowCount()});
                editRow(dataBox.getItemFunctionName(), row, ITEM);
                editRow(String.format("%.3f S", dataBox.getRunTime()),
                        row, TIME);
                editRow(getStatus(dataBox), row, STAUS);
            } else {
                editRow(dataBox.getItemFunctionName(), row, ITEM);
                editRow(String.format("%.3f S", dataBox.getRunTime()),
                        row, TIME);
                editRow(getStatus(dataBox), row, STAUS);
                editRow(dataBox.getErrorCode(), row, ERROR_CODE);
                editRow(dataBox.getCusErrorCode(), row, CUS_ERROR_CODE);
            }
        }
    }

    private void editRow(Object value, int row, String colmn) {
        if (row < 0 || !this.testColumn.contains(colmn)) {
            return;
        }
        this.tableModel.setValueAt(value, row, this.testColumn.indexOf(colmn));
    }

    private String getStatus(FunctionData functionData) {
        if (functionData.isTesting()) {
            return "Testing";
        }
        return functionData.getStausTest();
    }

    private String getNameITem(int row) {
        if (row == -1) {
            return null;
        }
        return this.tableModel.getValueAt(row, this.listFunc.indexOf(ITEM)).toString();
    }
}
