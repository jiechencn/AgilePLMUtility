package zigzagsoft.agile.privviewer;

import javax.swing.*;

/**
 * Created by jijichen on 7/4/2014.
 */
public class Role extends OneNode{
    public Role( int id, String title, boolean enabled) {
        super(id, enabled?PVConstant.NODETYPE_ROLE:PVConstant.NODETYPE_ROLE_2, title, enabled);
    }
}
