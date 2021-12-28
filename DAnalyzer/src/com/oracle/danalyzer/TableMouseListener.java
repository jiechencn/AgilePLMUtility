// ==============================================================
// Copyright ©2017 by Oracle
// All Rights Reserved.
// ==============================================================


package com.oracle.danalyzer;


import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTable;


/**
 * @author jie.chen@oracle.com
 */

public class TableMouseListener extends MouseAdapter {
     
    private JTable table;
     
    /**
     *
     * @param table
     */
    public TableMouseListener(JTable table) {
        this.table = table;
    }
     
    /**
     *
     * @param event
     */
    @Override
    public void mousePressed(MouseEvent event) {
        // selects the row at which point the mouse is clicked
        Point point = event.getPoint();
        int currentRow = table.rowAtPoint(point);
        table.setRowSelectionInterval(currentRow, currentRow);
    }
}