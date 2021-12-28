package zigzagsoft.agile.privviewer;

import javax.swing.*;

/**
 * Created by jijichen on 7/4/2014.
 */
public class User extends OneNode {

    public User(int id,  String title, boolean enabled) {
        super(id, enabled?PVConstant.NODETYPE_USER:PVConstant.NODETYPE_USER_2, title,enabled);
    }
}
