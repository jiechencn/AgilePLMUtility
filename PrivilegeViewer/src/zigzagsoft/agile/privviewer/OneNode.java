package zigzagsoft.agile.privviewer;

import javax.swing.*;
import java.awt.*;

public class OneNode extends Object {
    private static String icons[] = new String[]{"res/usergrouproot.png","res/userroot.png",
            "res/usergroup.png", "res/usergroup2.png",
            "res/user.png", "res/user2.png",
            "res/role.png", "res/role2.png",
            "res/privilege.png","res/privilege2.png",
            "res/criteria.png"};

    private static final ImageIcon[] treeIcons;

    static{
        treeIcons= new ImageIcon[icons.length];
        for (int i=0;i<icons.length;i++){
            treeIcons[i] = new ImageIcon(icons[i]);
        }
    }
    public boolean isEnabled() {
        return enabled;
    }

    private boolean enabled;
    private String title;
    private ImageIcon icon;
    private int type;

    public int getId() {
        return id;
    }

    private int id;

    public int getType() {
        return type;
    }

    OneNode(int id, int type, String title, boolean enabled) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.icon = treeIcons[type];
        this.enabled = enabled;

    }

    String getTitle() {
        return title;
    }

    ImageIcon getIcon() {
        return icon;
    }


    public String toString() {
        return title;
    }
}
