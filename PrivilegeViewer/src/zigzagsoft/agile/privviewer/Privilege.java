package zigzagsoft.agile.privviewer;

import javax.swing.*;

/**
 * Created by jijichen on 7/4/2014.
 */
public class Privilege extends OneNode {
    public Privilege(int id, String title, boolean enabled) {

        super(id, enabled?PVConstant.NODETYPE_PRIVILEGE:PVConstant.NODETYPE_PRIVILEGE_2, title, enabled);
    }
}
