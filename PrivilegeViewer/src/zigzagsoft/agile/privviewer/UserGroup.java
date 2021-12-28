package zigzagsoft.agile.privviewer;

import javax.swing.*;

/**
 * Created by jijichen on 7/4/2014.
 */
public class UserGroup extends OneNode {
    public UserGroup(int id, String title, boolean enabled) {
        super(id, enabled?PVConstant.NODETYPE_USERGROUP:PVConstant.NODETYPE_USERGROUP_2, title, enabled);
    }
}
