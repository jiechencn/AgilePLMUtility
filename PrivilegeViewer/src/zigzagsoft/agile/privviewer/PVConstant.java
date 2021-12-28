package zigzagsoft.agile.privviewer;

/**
 * Created by jijichen on 7/3/2014.
 */
public interface PVConstant {
    public static String APP_NAME = "Privilege Viewer";
    public static String APP_VERSION = "0.1.20140705";
    public static String APP_DEVELOPER = "Developed by Jie Chen";
    public static String APP_CONTACT = "Email: jie.chen.cn@outlook.com";
    public static String APP_COPYRIGHT = "(c) 2014 www.ZigzagSoft.net";
    public static String AGILE_VERSION = "Supported Agile PLM 9.2.x, 9.3.x";

    public static String LICENSE_INVALID = "Invalid license";

    public static String MENU_ACTION = "Action";
    public static String MENU_ACTION_RELOAD = "Refresh";
    public static String MENU_ACTION_ABOUT = "About";


    public static String ROOT_USER = "User";
    public static String ROOT_USERGROUP = "User Group";

    public static int NODETYPE_USERGROUPROOT = 0;
    public static int NODETYPE_USERROOT = 1;
    public static int NODETYPE_USERGROUP = 2;
    public static int NODETYPE_USERGROUP_2 = 3;
    public static int NODETYPE_USER = 4;
    public static int NODETYPE_USER_2 = 5;
    public static int NODETYPE_ROLE = 6;
    public static int NODETYPE_ROLE_2 = 7;
    public static int NODETYPE_PRIVILEGE = 8;
    public static int NODETYPE_PRIVILEGE_2 = 9;

    public static int NODETYPE_CRITERIA = 10;


    public String AGILE_PROP_FILE = "agile" ; // agile.properties
    public String PV_PROP_FILE = "pv" ; // agile.properties
}
