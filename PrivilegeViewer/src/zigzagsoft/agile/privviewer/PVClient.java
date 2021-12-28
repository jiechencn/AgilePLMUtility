package zigzagsoft.agile.privviewer;

import zigzagsoft.agile.service.NodeService;
import zigzagsoft.agile.util.PVLicense;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;


/**
 * JTree basic tutorial and example
 *
 * @author wwww.codejava.net
 */

public class PVClient extends JFrame {

    DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
    private JLabel selectedLabel;
    private Dialog loadingDialog;
    private Label loadingLabel;
    private DefaultTreeModel dt = new DefaultTreeModel(root);
    private JTree tree = new JTree(dt);
    private JScrollPane pane;

    private void loadTree(){
        if (pane!=null) remove(pane);

        root = new DefaultMutableTreeNode("Root");

        DefaultMutableTreeNode usergroupRoot = new DefaultMutableTreeNode(new OneNode(PVConstant.NODETYPE_USERGROUPROOT, PVConstant.NODETYPE_USERGROUPROOT, PVConstant.ROOT_USERGROUP, true));
        DefaultMutableTreeNode userRoot = new DefaultMutableTreeNode(new OneNode(PVConstant.NODETYPE_USERROOT, PVConstant.NODETYPE_USERROOT, PVConstant.ROOT_USER, true));

        NodeService ns = new NodeService();
        showProgressLog("Loading Privileges", true);
        LinkedHashMap allPrivileges = ns.loadPrivileges();
        showProgressLog("Done", false);

        showProgressLog("Loading Roles", true);
        LinkedHashMap allRoles = ns.loadRoles();
        showProgressLog("Done", false);

        showProgressLog("Loading Users", true);
        LinkedHashMap allUsers = ns.loadUsers();
        showProgressLog("Done", false);

        showProgressLog("Loading UserGroups", true);
        LinkedHashMap allUserGroups = ns.loadUserGroups();
        showProgressLog("Done", false);

        showProgressLog("Loading User & UserGroup Mapping Data", true);
        LinkedHashMap allUserGroupMap = ns.loadUserGroupMap();
        showProgressLog("Done", false);

        showProgressLog("Loading UserGroup & Role Mapping Data", true);
        LinkedHashMap allGroupRoleMap = ns.loadGroupRoleMap();
        showProgressLog("Done", false);

        showProgressLog("Loading User & Role Mapping Data", true);
        LinkedHashMap allUserRoleMap = ns.loadUserRoleMap();
        showProgressLog("Done", false);

        showProgressLog("Loading Role & Privilege Mapping Data", true);
        LinkedHashMap allRolePrivilegeMap = ns.loadRolePrivilegeMap();
        showProgressLog("Done", false);

        showProgressLog("Loading Privilege & Criteria Mapping Data", true);
        LinkedHashMap allPrivilegeCriteriaMap = ns.loadPrivilgeCriteriaMap();
        showProgressLog("Done", false);


        ns.release();

        showProgressLog("Assembling User, UserGroup, Role & Privilege", true);
        Iterator usersIt = allUsers.values().iterator();
        while (usersIt.hasNext()) {
            User user = (User) usersIt.next();
            DefaultMutableTreeNode unode = new DefaultMutableTreeNode(new User(user.getId(), user.getTitle(), user.isEnabled()));
            DefaultMutableTreeNode uselfnode = new DefaultMutableTreeNode(new User(user.getId(), user.getTitle(), user.isEnabled()));
            unode.add(uselfnode);

            // add user's roles
            LinkedHashMap uroles = (LinkedHashMap) allUserRoleMap.get(user.getId());
            if (uroles != null && !uroles.isEmpty()) {
                Iterator uroleIt = uroles.values().iterator();
                while(uroleIt.hasNext()){
                    Role r = (Role) allRoles.get(uroleIt.next());
                    DefaultMutableTreeNode rnode = new DefaultMutableTreeNode(r);
                    uselfnode.add(rnode);

                    // add role's privileges
                    LinkedHashMap rprivileges = (LinkedHashMap) allRolePrivilegeMap.get(r.getId());
                    if (rprivileges != null && !rprivileges.isEmpty()) {
                        Iterator rprivilegeIt = rprivileges.values().iterator();
                        while (rprivilegeIt.hasNext()) {
                            Privilege p = (Privilege) allPrivileges.get(rprivilegeIt.next());
                            DefaultMutableTreeNode pnode = new DefaultMutableTreeNode(p);
                            rnode.add(pnode);
                            // add privilege's criteria
                            Criteria criteria = (Criteria) allPrivilegeCriteriaMap.get(p.getId());
                            if (criteria!=null) {
                                DefaultMutableTreeNode cnode = new DefaultMutableTreeNode(criteria);
                                pnode.add(cnode);
                            }
                        }
                    }
                }
            }

            // add user's groups
            LinkedHashMap groups = (LinkedHashMap) allUserGroupMap.get(user.getId());
            if (groups != null && !groups.isEmpty()) {
                Iterator groupIt = groups.values().iterator();
                while (groupIt.hasNext()) {
                    UserGroup ug = (UserGroup) allUserGroups.get(groupIt.next());
                    DefaultMutableTreeNode gnode = new DefaultMutableTreeNode(ug);
                    unode.add(gnode);

                    // add group's roles
                    LinkedHashMap groles = (LinkedHashMap) allGroupRoleMap.get(ug.getId());
                    if (groles != null && !groles.isEmpty()) {
                        Iterator groleIt = groles.values().iterator();
                        while(groleIt.hasNext()){
                            Role r = (Role) allRoles.get(groleIt.next());
                            DefaultMutableTreeNode rnode = new DefaultMutableTreeNode(r);
                            gnode.add(rnode);

                            // add role's privileges
                            LinkedHashMap rprivileges = (LinkedHashMap) allRolePrivilegeMap.get(r.getId());
                            if (rprivileges != null && !rprivileges.isEmpty()) {
                                Iterator rprivilegeIt = rprivileges.values().iterator();
                                while (rprivilegeIt.hasNext()) {
                                    Privilege p = (Privilege) allPrivileges.get(rprivilegeIt.next());
                                    DefaultMutableTreeNode pnode = new DefaultMutableTreeNode(p);
                                    rnode.add(pnode);
                                    // add privilege's criteria
                                    Criteria criteria = (Criteria) allPrivilegeCriteriaMap.get(p.getId());
                                    if (criteria!=null) {
                                        DefaultMutableTreeNode cnode = new DefaultMutableTreeNode(criteria);
                                        pnode.add(cnode);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            userRoot.add(unode);
        }
        showProgressLog("Done", false);


        showProgressLog("Assembling UserGroup, Role & Privilege ... ", true);
        Iterator usergroupsIt = allUserGroups.values().iterator();
        while (usergroupsIt.hasNext()) {
            UserGroup ug = (UserGroup) usergroupsIt.next();
            DefaultMutableTreeNode ugnode = new DefaultMutableTreeNode(new UserGroup(ug.getId(), ug.getTitle(), ug.isEnabled()));
            usergroupRoot.add(ugnode);

            // add group's roles
            LinkedHashMap groles = (LinkedHashMap) allGroupRoleMap.get(ug.getId());
            if (groles != null && !groles.isEmpty()) {
                Iterator groleIt = groles.values().iterator();
                while(groleIt.hasNext()){
                    Role r = (Role) allRoles.get(groleIt.next());
                    DefaultMutableTreeNode rnode = new DefaultMutableTreeNode(r);
                    ugnode.add(rnode);

                    // add role's privileges
                    LinkedHashMap rprivileges = (LinkedHashMap) allRolePrivilegeMap.get(r.getId());
                    if (rprivileges != null && !rprivileges.isEmpty()) {
                        Iterator rprivilegeIt = rprivileges.values().iterator();
                        while (rprivilegeIt.hasNext()) {
                            Privilege p = (Privilege) allPrivileges.get(rprivilegeIt.next());
                            DefaultMutableTreeNode pnode = new DefaultMutableTreeNode(p);
                            rnode.add(pnode);
                            // add privilege's criteria
                            Criteria criteria = (Criteria) allPrivilegeCriteriaMap.get(p.getId());
                            if (criteria!=null) {
                                DefaultMutableTreeNode cnode = new DefaultMutableTreeNode(criteria);
                                pnode.add(cnode);
                            }
                        }
                    }
                }
            }
        }
        showProgressLog("Done", false);

        root.add(userRoot);
        root.add(usergroupRoot);

        loadingDialog.setVisible(false);

        dt = new DefaultTreeModel(root);
        tree = new JTree(dt);
        dt.reload();

        pane = new JScrollPane(tree);
        add(pane);
        validateTree();

        OneNodeRender render = new OneNodeRender();
        tree.setCellRenderer(render);
        tree.setShowsRootHandles(true);
        tree.setRootVisible(false);

        tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                Object oe =  e.getNewLeadSelectionPath().getLastPathComponent();
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                String value = "";
                if (oe!=null) value = oe.toString();
                if (selectedNode!=null) value = selectedNode.toString();
                selectedLabel.setText(value);
            }
        });
    }
    public PVClient() {
        ImageIcon icon=new ImageIcon("res/z.png");
        setIconImage(icon.getImage());
        doLoading();
        selectedLabel = new JLabel();
        add(selectedLabel, BorderLayout.SOUTH);

        JMenuBar mb = new JMenuBar();
        this.setJMenuBar(mb);
        JMenu fileMenu = new JMenu(PVConstant.MENU_ACTION);
        mb.add(fileMenu);
        JMenuItem menuReload = new JMenuItem(PVConstant.MENU_ACTION_RELOAD);
        fileMenu.add(menuReload);
        JMenuItem menuAbout = new JMenuItem(PVConstant.MENU_ACTION_ABOUT);
        fileMenu.add(menuAbout);

        menuReload.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        doLoading();
                    }
                }
        );

        menuAbout.addActionListener(
                new ActionListener(){
                    public void actionPerformed(ActionEvent e){

                        JOptionPane.showMessageDialog(null, PVConstant.APP_NAME + " " + PVConstant.APP_VERSION
                                + "\n" + PVConstant.AGILE_VERSION
                                + "\n\n" + PVConstant.APP_DEVELOPER
                                + "\n" + PVConstant.APP_CONTACT
                                + "\n" + PVConstant.APP_COPYRIGHT
                                + "\n\n", PVConstant.APP_NAME
                                , JOptionPane.INFORMATION_MESSAGE);

                    }
                }
        );

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle(PVConstant.APP_NAME);
        this.setVisible(true);

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(500, 600);

    }

    private void doLoading() {
        if (!PVLicense.validate()) {
            JOptionPane.showMessageDialog(null, PVConstant.LICENSE_INVALID, PVConstant.APP_NAME
                    , JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (loadingDialog != null && loadingDialog.isVisible())
            return;

        loadingDialog = new Dialog(PVClient.this, PVConstant.APP_NAME,
                Dialog.ModalityType.MODELESS);
        loadingDialog.setBounds(100, 150, 200, 150);
        loadingDialog.setSize(300, 120);
        loadingDialog.setVisible(true);
        loadingDialog.setBackground(Color.black);
        //loadingDialog.setModal(true);
        //loadingDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
//        loadingDialog.setOpacity(4.7f);
        loadingDialog.setResizable(false);
        //loadingDialog.setTitle("title");
        //loadingDialog.setUndecorated(true);

        loadingDialog.toFront();
        loadingLabel = new Label("Waiting ...");
        loadingLabel.setForeground(Color.WHITE);
        loadingDialog.add(loadingLabel);
        //loadingDialog.setForeground(Color.WHITE);
        //loadTree();
        Task task = new Task();
        //task.addPropertyChangeListener(null);
        task.execute();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PVClient();
            }
        });
    }
    private void showProgressLog(String data, boolean begin){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (begin) {
            System.out.print(df.format(new Date()) + " " + data + " ... ");
            if (loadingLabel!=null) loadingLabel.setText(" " + data + " ... ");
        }
        else {
            System.out.println(data);
            //if (loadingLabel!=null) loadingLabel.setText(data);;
        }

    }


    class Task extends SwingWorker<Void, Void> {
        @Override
        public Void doInBackground() {
            try {
                loadTree();
            } catch (Exception ignore) {}
            return null;
        }

        @Override
        public void done() {
            Toolkit.getDefaultToolkit().beep();
        }
    }


}