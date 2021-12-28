package zigzagsoft.agile.service;

import zigzagsoft.agile.dao.ConnectionFactory;
import zigzagsoft.agile.privviewer.*;
import zigzagsoft.agile.util.PropUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;

/**
 * Created by jijichen on 7/4/2014.
 */
public class NodeService {
    Connection con;
    public NodeService(){
        String driver = PropUtil.getProperties("agile").getProperty("db.driver");
        String url = PropUtil.getProperties("agile").getProperty("db.url");
        String user = PropUtil.getProperties("agile").getProperty("db.user");
        String pwd = PropUtil.getProperties("agile").getProperty("db.password");
        con = ConnectionFactory.getInstance(driver).getConnection(url, user, pwd);
    };
    public LinkedHashMap loadPrivileges(){
        PreparedStatement psmt = null;
        ResultSet rst = null;
        LinkedHashMap privs = new LinkedHashMap();
        try {
            psmt = con.prepareStatement("select n.id, n.parentid, n.description, p.value " +
                    "from nodetable n, propertytable p " +
                    "where n.parentid=12075 and n.id=p.parentid and selection=451 and propertyid=40 " +
                    "order by description");
            
            rst = psmt.executeQuery();

            while (rst.next()) {
                int id = rst.getInt(1);
                int objClass = rst.getInt(2);
                String name = rst.getString(3);
                boolean enabled = rst.getBoolean(4);
                Privilege p = new Privilege(id, name, enabled);
                privs.put(id, p );

            }
            psmt.close();
            rst.close();
        }catch(Exception e){

        }
        return privs;
    }
    public LinkedHashMap loadRoles(){
        PreparedStatement psmt = null;
        ResultSet rst = null;
        LinkedHashMap roles = new LinkedHashMap();
        try {
            psmt = con.prepareStatement("select n.id, n.parentid, n.description, p.value " +
                    "from nodetable n, propertytable p " +
                    "where n.parentid=5006 and n.objtype=21 and n.id=p.parentid and selection=451 and propertyid=40 " +
                    "order by description");

            rst = psmt.executeQuery();

            while (rst.next()) {
                int id = rst.getInt(1);
                int objClass = rst.getInt(2);
                String name = rst.getString(3);
                boolean enabled = rst.getBoolean(4);
                Role r = new Role(id, name, enabled);
                roles.put(id, r );

            }
            psmt.close();
            rst.close();
        }catch(Exception e){

        }
        return roles;
    }
    public LinkedHashMap loadUsers(){
        PreparedStatement psmt = null;
        ResultSet rst = null;
        LinkedHashMap users = new LinkedHashMap();
        try {
            psmt = con.prepareStatement("select id, class, loginid, delete_flag " +
                    "from agileuser " +
                    "where system_account=0 " +
                    "order by loginid");

            rst = psmt.executeQuery();

            while (rst.next()) {
                int id = rst.getInt(1);
                int objClass = rst.getInt(2);
                String name = rst.getString(3);
                boolean enabled = !rst.getBoolean(4);
                User r = new User(id, name, enabled);
                users.put(id, r );

            }
            psmt.close();
            rst.close();
        }catch(Exception e){

        }
        return users;
        //select id, class, loginid, delete_flag from agileuser where system_account=0 order by loginid

    }
    public LinkedHashMap loadUserGroups(){
        PreparedStatement psmt = null;
        ResultSet rst = null;
        LinkedHashMap usergroups = new LinkedHashMap();
        try {
            psmt = con.prepareStatement("select id, class, name, delete_flag " +
                    "from user_group " +
                    "order by name");

            rst = psmt.executeQuery();

            while (rst.next()) {
                int id = rst.getInt(1);
                int objClass = rst.getInt(2);
                String name = rst.getString(3);
                boolean enabled = !rst.getBoolean(4);
                UserGroup r = new UserGroup(id, name, enabled);
                usergroups.put(id, r );

            }
            psmt.close();
            rst.close();
        }catch(Exception e){

        }
        return usergroups;
    }

    public LinkedHashMap loadUserGroupMap(){
        //
        PreparedStatement psmt = null;
        ResultSet rst = null;
        LinkedHashMap usergroupmaps = new LinkedHashMap();
        try {
            psmt = con.prepareStatement("select a.user_id, a.objectid " +
                    "from user_assignment a, agileuser u, user_group g " +
                    "where a.assignee_type=11605 and a.class=11885 and a.user_id=u.id and a.objectid=g.id " +
                    "order by u.loginid, g.name");

            rst = psmt.executeQuery();

            int pre_userid = -9999;
            int userid = -9999;

            LinkedHashMap groups = new LinkedHashMap();
            while (rst.next()) {

                userid = rst.getInt(1);
                int groupid = rst.getInt(2);

                if (userid!=pre_userid && pre_userid>0){ // new user begins
                    usergroupmaps.put(pre_userid, groups);
                    groups = new LinkedHashMap();
                }else{
                }
                groups.put(groupid, groupid);
                pre_userid = userid;
            }
            usergroupmaps.put(userid, groups); // the last one

            psmt.close();
            rst.close();
        }catch(Exception e){

        }
        return usergroupmaps;

    }


    public LinkedHashMap loadGroupRoleMap(){
        //
        PreparedStatement psmt = null;
        ResultSet rst = null;
        LinkedHashMap grouprolemaps = new LinkedHashMap();
        try {
            psmt = con.prepareStatement("select g.id, n.id " +
                    "from user_group g, user_assignment a, nodetable n " +
                    "where a.assignee_type=11885 and a. class=17726 and a.user_id=g.id and a.objectid=n.id and n.objtype=21 " +
                    "order by g.name, n.description");

            rst = psmt.executeQuery();

            int pre_groupid = -9999;
            int groupid = -9999;

            LinkedHashMap roles = new LinkedHashMap();
            while (rst.next()) {

                groupid = rst.getInt(1);
                int roleid = rst.getInt(2);

                if (groupid!=pre_groupid && pre_groupid>0){ // new user begins
                    grouprolemaps.put(pre_groupid, roles);
                    roles = new LinkedHashMap();
                }else{
                }
                roles.put(roleid, roleid);
                pre_groupid = groupid;
            }
            grouprolemaps.put(groupid, roles); // the last one

            psmt.close();
            rst.close();
        }catch(Exception e){

        }
        return grouprolemaps;

    }


    public LinkedHashMap loadRolePrivilegeMap(){
        //
        PreparedStatement psmt = null;
        ResultSet rst = null;
        LinkedHashMap rolePrivilegemaps = new LinkedHashMap();
        try {
            psmt = con.prepareStatement("select r.id, p.id " +
                    "from " +
                    "   (select * from nodetable where objtype=22) p, " +
                    "   (select * from nodetable where objtype=21) r, " +
                    "   (select * from adminmsatt where attid=409) pr " +
                    "where  r.id=pr.parentid and pr.value=p.id " +
                    "order by r.description, p.description");

            rst = psmt.executeQuery();

            int pre_roleid = -9999;
            int roleid = -9999;

            LinkedHashMap privileges = new LinkedHashMap();
            while (rst.next()) {

                roleid = rst.getInt(1);
                int privid = rst.getInt(2);

                if (roleid!=pre_roleid && pre_roleid>0){ // new user begins
                    rolePrivilegemaps.put(pre_roleid, privileges);
                    privileges = new LinkedHashMap();
                }else{
                }
                privileges.put(privid, privid);
                pre_roleid = roleid;
            }
            rolePrivilegemaps.put(roleid, privileges); // the last one

            psmt.close();
            rst.close();
        }catch(Exception e){

        }
        return rolePrivilegemaps;

    }



    public LinkedHashMap loadUserRoleMap() {
        PreparedStatement psmt = null;
        ResultSet rst = null;
        LinkedHashMap userrolemaps = new LinkedHashMap();
        try {
            psmt = con.prepareStatement("select a.user_id, a.objectid " +
                    "from agileuser u, user_assignment a, nodetable n " +
                    "where a.assignee_type=11605 and a.class=11640 and a.objectid=n.id and a.user_id=u.id " +
                    "order by u.loginid, n.description");

            rst = psmt.executeQuery();

            int pre_userid = -9999;
            int userid = -9999;

            LinkedHashMap roles = new LinkedHashMap();
            while (rst.next()) {

                userid = rst.getInt(1);
                int roleid = rst.getInt(2);

                if (userid!=pre_userid && pre_userid>0){ // new user begins
                    userrolemaps.put(pre_userid, roles);
                    roles = new LinkedHashMap();
                }else{
                }
                roles.put(roleid, roleid);
                pre_userid = userid;
            }
            userrolemaps.put(userid, roles); // the last one

            psmt.close();
            rst.close();
        }catch(Exception e){

        }
        return userrolemaps;
    }

    public LinkedHashMap loadPrivilgeCriteriaMap() {
        PreparedStatement psmt = null;
        ResultSet rst = null;
        LinkedHashMap privCriteriaMap = new LinkedHashMap();
        try {
            psmt = con.prepareStatement("select c.id, a.parentid, c.description " +
                    "from nodetable c, adminmsatt a " +
                    "where a.attid=412 and a.value=c.id and c.parentid=3642 and c.objtype=111");
            rst = psmt.executeQuery();
            while (rst.next()) {
                int criId = rst.getInt(1);
                int privId = rst.getInt(2);
                String criteria = rst.getString(3);
                Criteria c = new Criteria(criId, criteria, true);
                privCriteriaMap.put(privId, c);
            }

            psmt.close();
            rst.close();
        }catch(Exception e){

        }
        return privCriteriaMap;
    }

    public void release(){
        ConnectionFactory.close(con, null, null);
    }
}
