package zigzagsoft.agile.privviewer;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

/**
 * Created by jijichen on 7/4/2014.
 */
public class OneNodeRender extends Object implements TreeCellRenderer {
    JLabel label;

    OneNodeRender() {
        label = new JLabel();
        //label.setOpaque(true);
    }

    public Component getTreeCellRendererComponent
            (JTree tree, Object value, boolean selected,
             boolean expanded, boolean leaf, int row, boolean hasFocus) {

        Object o = ((DefaultMutableTreeNode) value).getUserObject();
        if (o instanceof OneNode) {
            OneNode node = (OneNode) o;
            label.setIcon(node.getIcon());
            if (node.isEnabled()) {
                label.setText(node.getTitle());
                label.setForeground(Color.BLACK);
            } else {
                label.setText("<html><strike>" + node.getTitle() + "</strike></html>");
                label.setForeground(Color.LIGHT_GRAY);
            }

        } else {
            label.setIcon(null);
            label.setText("" + value);
        }
        return label;
    }
}
