// ==============================================================
// Copyright ©2017 by Oracle
// All Rights Reserved.
// ==============================================================

package com.oracle.danalyzer;

import java.awt.Component;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * @author jie.chen@oracle.com
 */
public class PasswordFieldTableCellRenderer extends JPasswordField implements TableCellRenderer {

    /**
     *
     */
    public PasswordFieldTableCellRenderer() {
        super();
        super.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    }

    /**
     *
     * @param table
     * @param value
     * @param isSelected
     * @param hasFocus
     * @param row
     * @param column
     * @return
     */
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        setValue(value);

        return this;
    }

    /**
     *
     * @param value
     */
    protected void setValue(Object value) {
        setText((value == null) ? "" : value.toString());
    }

    /**
     *
     */
    public void updateUI() {
        super.updateUI();
        setForeground(null);
        setBackground(null);
    }

}
