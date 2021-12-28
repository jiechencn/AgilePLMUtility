// ==============================================================
// Copyright ©2017 by Oracle
// All Rights Reserved.
// ==============================================================
package com.oracle.danalyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.InvalidSearchFilterException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapName;

/**
 * @author jie.chen@oracle.com
 */
public class DAnalyzer implements DAConstants {

    private InitialDirContext dirCtx = null;
    private AttrsBean attrsBean = null;
    private ArrayList output = new ArrayList();

    /**
     * <p>
     * Construction of <code>DAnalyzer</code>
     *
     * @param attsMap The Map used to bind principal with LDAP server. The
     * <code>attsMap</code> is a group of key-value pairs.
     */
    public DAnalyzer(String action, Map attsMap) {
        log2Output("+", action, "-", "-", "");
        log2Output(">", "Connect LDAP", (String) attsMap.get(ATT_PRINCIPAL), "-", "");
        attrsBean = construct(attsMap);
        dirCtx = bindMainPrincipal(attrsBean);
    }

    /**
     * <p>
     * Release binded context of <code>DAnalyzer</code> instance
     */
    public void release() {
        output.clear();
        try {
            if (dirCtx != null) {
                dirCtx.close();
            }
        } catch (NamingException ex) {
            //
        }
    }

    /**
     * <p>
     * Load all matched users from the Group Filter <code>DAnalyzer</code>
     * parameter <code>attsMap</code>
     *
     * @return a sorted Map of users with username and userDN pair.
     */
    public Map loadUsersFromGroup(boolean checkAgileMandatoryAttrs) {

        if (dirCtx == null) {
            return null;
        }

        log2Output(">", "Refresh Users from Group", "-", "-", "");

        StringBuffer mandatoryError = new StringBuffer();
        StringBuffer usersError = new StringBuffer();
        Map<String, String> usernameDNs = new TreeMap<String, String>();
        String stepName1 = "Match GroupFitlers";
        String stepName2 = "Check Mandatory";
        String parameter1 = "";
        String parameter2 = "";
        // search All Users Filter + User From Name Filter
        try {
            SearchControls ctls = new SearchControls();
            String mandatoryAttrsStr = (attrsBean.getAgileMandatoryAttributes());
            String[] groupAttrIDs = {(String) attrsBean.getStaticMemberDNAttribute()};// get group memebers
            String[] userNameAttrIDs = {(String) attrsBean.getUserNameAttribute()};// get user name
            String[] userReturnAttrs = new String[]{};
            String[] mandatoryAttrs = null;

            if (checkAgileMandatoryAttrs) {
                if (mandatoryAttrsStr != null && !mandatoryAttrsStr.equals("")) {
                    mandatoryAttrsStr.replace(" ", ""); // remove whitespace
                    mandatoryAttrs = mandatoryAttrsStr.split(MANDATORY_ATTRS_SEP);
                    userReturnAttrs = new String[mandatoryAttrs.length + userNameAttrIDs.length];
                    System.arraycopy(userNameAttrIDs, 0, userReturnAttrs, 0, userNameAttrIDs.length);
                    System.arraycopy(mandatoryAttrs, 0, userReturnAttrs, userNameAttrIDs.length, mandatoryAttrs.length);
                }
            } else {
                userReturnAttrs = userNameAttrIDs;
            }

            ctls.setCountLimit((int) attrsBean.getSearchCount());
            ctls.setSearchScope((int) attrsBean.getUserSearchScopeInt());
            ctls.setReturningAttributes(groupAttrIDs);
            ctls.setReturningObjFlag(false);
            String filter = "(&(objectClass=" + (String) attrsBean.getStaticGroupObjectClass() + ")(" + (String) attrsBean.getAllGroupsFilter() + "))";
            parameter1 = ATT_GROUP_BASE_DN + ":" + (String) attrsBean.getGroupBaseDN()
                    + NEW_LINE + PARA_MATCH_FILTER + ":" + filter
                    + NEW_LINE + PARA_REQUEST_ATTRS + ":" + Arrays.toString(groupAttrIDs);
            parameter2 = ATT_GROUP_BASE_DN + ":" + (String) attrsBean.getGroupBaseDN()
                    + NEW_LINE + PARA_MATCH_FILTER + ":" + filter
                    + NEW_LINE + PARA_REQUEST_ATTRS + ":" + Arrays.toString(userReturnAttrs);
            NamingEnumeration usergroups = dirCtx.search((String) attrsBean.getGroupBaseDN(), filter, ctls);
            while (usergroups.hasMoreElements()) {
                SearchResult results = (SearchResult) usergroups.next();
                Attributes groups = results.getAttributes();// multipe groups
                if (groups != null) {
                    for (NamingEnumeration group = groups.getAll(); group.hasMore();) {
                        Attribute members = (Attribute) group.next();  // all user memebers in current group
                        for (NamingEnumeration member = members.getAll(); member.hasMore();) {
                            if (member != null) { // current member
                                String userDN = (String)member.nextElement();
                                LdapName userLdapName = new LdapName(userDN);
                                //if ("cn=nobody".equalsIgnoreCase(userDN)) continue;
                                Attributes userAttrs = null;
                                try{ // this try-catch to handle "cn=nobody" case
                                    userAttrs = dirCtx.getAttributes(userLdapName, userReturnAttrs);
                                }catch(Exception e){
                                    usersError.append(userDN + e.getMessage() + NEW_LINE);
                                    continue;
                                }
                                String username = "";
                                
                                try{ // this try-catch to handle "cn=nobody" case
                                    username = (String) (userAttrs.get((String) attrsBean.getUserNameAttribute()).get());
                                }catch(Exception e){
                                    usersError.append(userDN + " has no " + ATT_USER_NAME_ATTRIBUTE + NEW_LINE);
                                    continue;
                                }

                                if (username != null && !username.equals("") && userDN != null && !userDN.equals("")) {
                                    usernameDNs.put(username, userDN);
                                }
                                if (checkAgileMandatoryAttrs && mandatoryAttrs != null) {
                                    for (String at : mandatoryAttrs) {
                                        if (at!=null) at = at.trim();
                                        if ((!"".equals(at)) && userAttrs.get(at) == null) {
                                            mandatoryError.append(username + " has no " + at + NEW_LINE);
                                        }
                                    }
                                }

                            } else {// User Name Attribute property is wrong
                                log2Output("X", stepName1, parameter1, "Wrong " + ATT_STATIC_MEMBER_DN_ATTRIBUTE, "Check " + ATT_STATIC_MEMBER_DN_ATTRIBUTE);
                                return null;
                            }
                        }
                    }

                }

                //Attribute att = result.getAttributes().get((String) attrsBean.getStaticMemberDNAttribute());
            }
            if (usernameDNs.size() <= 0) {
                log2Output("?", stepName1, parameter1, "No users match filter in current " + ATT_USER_SEARCH_SCOPE, "Check " + ATT_ALL_GROUPS_FILTER + ", " + ATT_STATIC_GROUP_NAME_ATTRIBUTE + " and " + ATT_STATIC_MEMBER_DN_ATTRIBUTE);
                return null;
            } else if (usersError.length()>0){
                log2Output("?", stepName1, parameter1, usernameDNs.size() + " users match" + NEW_LINE + usersError.toString(), "");
            }else{
                log2Output("O", stepName1, parameter1, usernameDNs.size() + " users match", "");
            }
            if (checkAgileMandatoryAttrs) {
                if (mandatoryError.toString().equals("")) {
                    log2Output("O", stepName2, parameter2, ATT_AGILE_MANDATORY_ATTRIBUTES + " OK", "");
                } else {
                    log2Output("X", stepName2, parameter2, mandatoryError.toString(), "Check " + ATT_AGILE_MANDATORY_ATTRIBUTES);
                }
            }
            return usernameDNs;
        } catch (InvalidSearchFilterException ne) {
            log2Output("X", stepName1, parameter1, ne.toString(), "Check " + ATT_ALL_GROUPS_FILTER + " or " + ATT_STATIC_GROUP_NAME_ATTRIBUTE + " and " + ATT_STATIC_MEMBER_DN_ATTRIBUTE);
            ne.printStackTrace();
        } catch (NamingException ne) {
            log2Output("X", stepName1, parameter1, ne.toString(), "Check " + ATT_GROUP_BASE_DN);
            ne.printStackTrace();
        } catch (Exception ne) {
            log2Output("X", stepName1, parameter1, ne.toString(), "Check request parameters");
            ne.printStackTrace();
        }
        return null;
    }

    /**
     * <p>
     * Load all matched users from the construction <code>DAnalyzer</code>
     * parameter <code>attsMap</code>
     *
     * @return a sorted Map of users with username and userDN pair.
     */
    public Map loadUsers(boolean checkAgileMandatoryAttrs) {
        if (dirCtx == null) {
            return null;
        }

        log2Output(">", "Refresh Users", "-", "-", "");

        StringBuffer mandatoryError = new StringBuffer();
        Map<String, String> usernameDNs = new TreeMap<String, String>();
        String stepName1 = "Match UsersFitlers";
        String stepName2 = "Check Mandatory";
        String parameter = "";
        // search All Users Filter + User From Name Filter
        try {
            SearchControls ctls = new SearchControls();
            String mandatoryAttrsStr = (attrsBean.getAgileMandatoryAttributes());
            String[] attrIDs = {(String) attrsBean.getUserNameAttribute()};
            String[] returnAttrs = new String[]{};
            String[] mandatoryAttrs = null;

            if (checkAgileMandatoryAttrs) {
                if (mandatoryAttrsStr != null && !mandatoryAttrsStr.equals("")) {
                    mandatoryAttrsStr.replace(" ", ""); // remove whitespace
                    mandatoryAttrs = mandatoryAttrsStr.split(MANDATORY_ATTRS_SEP);
                    returnAttrs = new String[mandatoryAttrs.length + attrIDs.length];
                    System.arraycopy(attrIDs, 0, returnAttrs, 0, attrIDs.length);
                    System.arraycopy(mandatoryAttrs, 0, returnAttrs, attrIDs.length, mandatoryAttrs.length);
                }
            } else {
                returnAttrs = attrIDs;
            }

            ctls.setCountLimit((int) attrsBean.getSearchCount());
            ctls.setSearchScope((int) attrsBean.getUserSearchScopeInt());
            ctls.setReturningAttributes(returnAttrs);
            ctls.setReturningObjFlag(false);
            String filter = "(&(" + (String) attrsBean.getAllUsersFilter() + ")(" + ((String) attrsBean.getUserFromNameFilter()).replace("%u", "*") + "))";
            parameter = ATT_USER_BASE_DN + ":" + (String) attrsBean.getUserBaseDN()
                    + NEW_LINE + PARA_MATCH_FILTER + ":" + filter
                    + NEW_LINE + PARA_REQUEST_ATTRS + ":" + Arrays.toString(returnAttrs);
            NamingEnumeration users = dirCtx.search((String) attrsBean.getUserBaseDN(), filter, ctls);
            while (users.hasMoreElements()) {
                SearchResult result = (SearchResult) users.next();
                Attribute att = result.getAttributes().get((String) attrsBean.getUserNameAttribute());
                if (att != null) {
                    String username = (String) att.get();
                    String dn = result.getNameInNamespace();
                    if (username != null && !username.equals("") && dn != null && !dn.equals("")) {
                        usernameDNs.put(username, dn);
                    }

                    if (checkAgileMandatoryAttrs && mandatoryAttrs != null) {
                        for (String at : mandatoryAttrs) {
                            if (at!=null) at = at.trim();
                            if ((!"".equals(at)) && result.getAttributes().get(at) == null) {
                                mandatoryError.append(username + " has no " + at + NEW_LINE);
                            }
                        }
                    }

                } else {// User Name Attribute property is wrong
                    log2Output("X", stepName1, parameter, "Wrong " + ATT_USER_NAME_ATTRIBUTE, "Check " + ATT_USER_NAME_ATTRIBUTE);
                    return null;
                }
            }
            if (usernameDNs.size() <= 0) {
                log2Output("X", stepName1, parameter, "No users match filter in current " + ATT_USER_SEARCH_SCOPE, "Check " + ATT_ALL_USERS_FILTER + ", " + ATT_USER_FROM_NAME_FILTER + " and " + ATT_USER_SEARCH_SCOPE);
                //return null;
            } else {
                log2Output("O", stepName1, parameter, usernameDNs.size() + " users match", "");
                if (checkAgileMandatoryAttrs) {
                    if (mandatoryError.toString().equals("")) {
                        log2Output("O", stepName2, parameter, ATT_AGILE_MANDATORY_ATTRIBUTES + " OK", "");
                    } else {
                        log2Output("X", stepName2, parameter, mandatoryError.toString(), "Check " + ATT_AGILE_MANDATORY_ATTRIBUTES);
                    }
                }
                
            }
            return usernameDNs;
        } catch (InvalidSearchFilterException ne) {
            log2Output("X", stepName1, parameter, ne.toString(), "Check " + ATT_ALL_USERS_FILTER + " or " + ATT_USER_FROM_NAME_FILTER);
            ne.printStackTrace();
        } catch (NamingException ne) {
            log2Output("X", stepName1, parameter, ne.toString(), "Check " + ATT_USER_BASE_DN);
            ne.printStackTrace();
        } catch (Exception ne) {
            log2Output("X", stepName1, parameter, ne.toString(), "Check request parameters");
            ne.printStackTrace();
        }
        return usernameDNs;
    }

    /**
     * <p>
     * Load all matched groups from the construction <code>DAnalyzer</code>
     * parameter <code>attsMap</code>
     *
     * @return a sorted Map of groups with groupname and groupDN pair.
     */
    public Map loadGroups() {
        if (dirCtx == null) {
            return null;
        }

        log2Output(">", "Refresh Groups", "-", "-", "");

        String stepName = "Match GroupsFitlers";
        Map<String, String> groupnameDNs = new TreeMap<String, String>();//sorted

        String parameter = "";
        try {
            SearchControls ctls = new SearchControls();
            String[] attrIDs = new String[]{(String) attrsBean.getStaticGroupNameAttribute()};
            ctls.setCountLimit((int) attrsBean.getSearchCount());
            ctls.setSearchScope((int) attrsBean.getGroupSearchScopeInt());
            ctls.setReturningAttributes(attrIDs);
            ctls.setReturningObjFlag(false);
            String filter = "(&(" + (String) attrsBean.getAllGroupsFilter() + ")(" + ((String) attrsBean.getGroupFromNameFilter()).replace("%g", "*") + "))";
            parameter = ATT_GROUP_BASE_DN + ":" + (String) attrsBean.getGroupBaseDN()
                    + NEW_LINE + PARA_MATCH_FILTER + ":" + filter
                    + NEW_LINE + PARA_REQUEST_ATTRS + ":" + Arrays.toString(attrIDs);

            NamingEnumeration groups = dirCtx.search((String) attrsBean.getGroupBaseDN(), filter, ctls);
            while (groups.hasMoreElements()) {
                SearchResult result = (SearchResult) groups.next();
                Attribute gnameAttr = result.getAttributes().get((String) attrsBean.getStaticGroupNameAttribute());
                //Attribute gguidAttr = result.getAttributes().get((String) attrsBean.getGuidAttribute());
                if (gnameAttr != null) {
                    String groupname = (String) gnameAttr.get();
                    if (groupname != null && !groupname.equals("")) {
                        String dn = result.getNameInNamespace();
                        groupnameDNs.put(groupname, dn);
                    }
                } else { // Static Group Name Attribute or Guid Attribute property is invalid
                    log2Output("X", stepName, parameter, "Wrong " + ATT_STATIC_GROUP_NAME_ATTRIBUTE , "Check " + ATT_STATIC_GROUP_NAME_ATTRIBUTE + " and " + ATT_GUID_ATTRIBUTE);
                    return null;
                }
            }

            if (groupnameDNs.size() <= 0) {
                log2Output("X", stepName, parameter, "No groups match filter in current " + ATT_GROUP_SEARCH_SCOPE, "Check " + ATT_ALL_GROUPS_FILTER + ", " + ATT_GROUP_FROM_NAME_FILTER + " and " + ATT_GROUP_SEARCH_SCOPE);
                return groupnameDNs;
            } else {
                log2Output("O", stepName, parameter, groupnameDNs.size() + " groups match", "");
                return groupnameDNs;
            }
        } catch (InvalidSearchFilterException ne) {
            log2Output("X", stepName, parameter, ne.toString(), "Check " + ATT_ALL_GROUPS_FILTER + " or " + ATT_GROUP_FROM_NAME_FILTER);
            ne.printStackTrace();
        } catch (NamingException ne) {
            log2Output("X", stepName, parameter, ne.toString(), "Check " + ATT_GROUP_BASE_DN);
            ne.printStackTrace();
        } catch (Exception ne) {
            log2Output("X", stepName, parameter, ne.toString(), "Check init and request parameters");
            ne.printStackTrace();
        }

        return groupnameDNs;
    }

    /**
     * <p>
     * Verify if <code>user</code> and <code>password</code> can logon LDAP
     * through Weblogic
     *
     * @param user
     * @param password
     * @return a <code>boolean</code> variable
     */
    public boolean verifyLogon(String user, String password) {
        if (dirCtx == null) {
            return false;
        }

        log2Output(">", "Validate Logon", user, "-", "");
        /*
        * 1. Find user's DN
        * 2. Authenticate this DN: format of "cn=user9,ou=people,dc=mycompany,dc=com"
        * 3. Check if DN matches "All Users Filter"
        * 4. Check any Groups which contain this user DN, this is reduntant checking
         */

        // 1. Find user's DN
        String curUserDN = getUserDN(user);
        if (curUserDN == null) {
            return false;
        }

        //2. Authenticate this user's DN
        InitialDirContext tmpCtx = bindUser(curUserDN, password);
        if (tmpCtx == null) {
            return false;
        } else {
        }
        try {
            tmpCtx.close();
        } catch (NamingException ex) {
            //
        }

        // 3. Check if DN matches "All Users Filter"
        boolean c = checkUserInFilter(user);

        // 4. Check any Groups which contain this user DN    
        String[] attIDs = new String[]{(String) attrsBean.getStaticGroupNameAttribute(), (String) attrsBean.getGuidAttribute()};
        findGroupMembership(curUserDN, attIDs);

        return c;

    }

    /**
     * <p>
     * Output an <code>ArrayList</code> of data
     *
     * @return an <code>ArrayList</code>, which is format of String[][5].
     * String[][0]: flag String[][1]: request String[][2]: parameter
     * String[][3]: response String[][4]: suggestion/action
     */
    public ArrayList outputReport() {
        ArrayList originalOut = (ArrayList) output.clone();
        output.clear();
        return originalOut;
    }

    private void close() {
        if (dirCtx != null) {
            try {
                dirCtx.close();
                dirCtx = null;
            } catch (NamingException ex) {
                //
            }
        }
    }

    private AttrsBean construct(Map attrs) {
        // construct default value if not input
        String referral = System.getProperty("ldap.referral"); // follow/ignore/throw
        if (referral == null || "".equals(referral)) {
            referral = "follow"; // default
        }

        int intCountLimit = 1001;
        try {
            intCountLimit = Integer.valueOf(System.getProperty("ldap.countlimit")).intValue();
        } catch (Exception e) {
            intCountLimit = 1001; // default
        }

        attrs.put(DAConstants.ATT_SEARCH_COUNT, intCountLimit);
        attrs.put(DAConstants.ATT_REFERRAL, referral);

        AttrsBean ab = AttrsBean.getBean(attrs);
        ab.build();

        log2Output("O", "Init", ab.toString(), "Done", "");
        
        return ab;
    }

    private InitialDirContext bindMainPrincipal(AttrsBean ab) {
        return bindUser(ab.getPrincipal(), ab.getCredential());
    }

    private InitialDirContext bindUser(String username, String password) {
        String stepName = "Bind User";
        Hashtable<String, Object> env = new Hashtable<String, Object>();
        InitialDirContext ctx = null;

        StringBuffer p = new StringBuffer();

        try {
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, (String) attrsBean.getURL());
            env.put(Context.SECURITY_AUTHENTICATION, (String) attrsBean.getMechanism());
            env.put(Context.SECURITY_PRINCIPAL, username);
            env.put(Context.SECURITY_CREDENTIALS, password);
            env.put(Context.REFERRAL, (String) attrsBean.getReferral());

            for (Map.Entry<String, Object> entry : env.entrySet()) {
                p.append(entry.getKey() + ":" + entry.getValue() + DAConstants.NEW_LINE);
            }

            ctx = new InitialDirContext(env);

            log2Output("O", stepName, p.toString(), "Bind OK: " + username, "");

        } catch (NamingException ne) {
            log2Output("X", stepName, p.toString(), ne.toString(), "Check host, port, username/password, " + ATT_MECHANISM + " and error code.");
            ne.printStackTrace();
        } catch (Exception e) {
            log2Output("X", stepName, p.toString(), e.toString(), "Check all required parameters");
        }

        return ctx;

    }

    private void log2Output(String r, String request, String parameter, String response, String action) {
        output.add(new String[]{r, request, parameter, response, action});
    }

    private String getUserDN(String userName) {

        if (dirCtx == null) {
            return null;
        }

        String stepName = "Get UserDN";

        String curUserDN = null;
        String parameter = null;

        SearchControls ctls = new SearchControls();
        String[] attrIDs = new String[]{(String) attrsBean.getUserNameAttribute(), (String) attrsBean.getGuidAttribute()};

        try {
            ctls.setCountLimit((int) attrsBean.getSearchCount());
            ctls.setSearchScope((int) attrsBean.getUserSearchScopeInt());
            ctls.setReturningAttributes(attrIDs);
            ctls.setReturningObjFlag(false);
            parameter = ATT_USER_BASE_DN + ":" + (String) attrsBean.getUserBaseDN()
                    + NEW_LINE + ATT_USER_FROM_NAME_FILTER + ":" + ((String) attrsBean.getUserFromNameFilter()).replace("%u", userName)
                    + NEW_LINE + PARA_REQUEST_ATTRS + ":" + Arrays.toString(attrIDs);
            NamingEnumeration userDNs = dirCtx.search((String) attrsBean.getUserBaseDN(), ((String) attrsBean.getUserFromNameFilter()).replace("%u", userName), ctls);

            if (userDNs != null && userDNs.hasMoreElements()) { // get only one
                SearchResult result = (SearchResult) userDNs.next();
                Attribute unameAttr = result.getAttributes().get((String) attrsBean.getUserNameAttribute());
                Attribute uguidAttr = result.getAttributes().get((String) attrsBean.getGuidAttribute());
                if (unameAttr == null || uguidAttr == null) {
                    log2Output("X", stepName, parameter, "Wrong " + ATT_USER_NAME_ATTRIBUTE + " or " + ATT_GUID_ATTRIBUTE, "Check " + ATT_USER_NAME_ATTRIBUTE + " and " + ATT_GUID_ATTRIBUTE);
                    return null;
                }
                curUserDN = result.getNameInNamespace();
                log2Output("O", stepName, parameter, "UserDN retrieved:" + curUserDN, "");
                return curUserDN;
            } else {
                log2Output("X", stepName, parameter, "No " + ATT_USER_NAME_ATTRIBUTE + " found in current " + ATT_USER_SEARCH_SCOPE, "Check " + ATT_USER_BASE_DN + ", " + ATT_USER_FROM_NAME_FILTER + " and " + ATT_USER_SEARCH_SCOPE);
                return null;
            }
        } catch (InvalidSearchFilterException ne) {
            log2Output("X", stepName, parameter, ne.toString(), "Check " + ATT_USER_FROM_NAME_FILTER);
            ne.printStackTrace();
        } catch (NamingException ne) {
            log2Output("X", stepName, parameter, ne.toString(), "Check " + ATT_USER_BASE_DN);
            ne.printStackTrace();

        } catch (Exception ne) {
            log2Output("X", stepName, parameter, ne.toString(), "Check request parameters");
            ne.printStackTrace();
        }

        return null;
    }

    private String getGroupDN(String groupName) {

        if (dirCtx == null) {
            return null;
        }

        String stepName = "Get GroupDN";

        String curGroupDN = null;
        String parameter = null;

        SearchControls ctls = new SearchControls();
        String[] attrIDs = new String[]{(String) attrsBean.getStaticGroupNameAttribute(), (String) attrsBean.getGuidAttribute()};

        try {
            ctls.setCountLimit((int) attrsBean.getSearchCount());
            ctls.setSearchScope((int) attrsBean.getGroupSearchScopeInt());
            ctls.setReturningAttributes(attrIDs);
            ctls.setReturningObjFlag(false);
            parameter = ATT_GROUP_BASE_DN + ":" + (String) attrsBean.getGroupBaseDN()
                    + NEW_LINE + ATT_GROUP_FROM_NAME_FILTER + ":" + ((String) attrsBean.getGroupFromNameFilter()).replace("%g", groupName)
                    + NEW_LINE + PARA_REQUEST_ATTRS + ":" + Arrays.toString(attrIDs);
            NamingEnumeration groupDNs = dirCtx.search((String) attrsBean.getGroupBaseDN(), ((String) attrsBean.getGroupFromNameFilter()).replace("%g", groupName), ctls);

            if (groupDNs != null && groupDNs.hasMoreElements()) { // get only one
                SearchResult result = (SearchResult) groupDNs.next();
                Attribute gnameAttr = result.getAttributes().get((String) attrsBean.getStaticGroupNameAttribute());
                Attribute gguidAttr = result.getAttributes().get((String) attrsBean.getGuidAttribute());
                if (gnameAttr == null || gguidAttr == null) {
                    log2Output("X", stepName, parameter, "Wrong " + ATT_STATIC_GROUP_NAME_ATTRIBUTE + " or " + ATT_GUID_ATTRIBUTE, "Check " + ATT_STATIC_GROUP_NAME_ATTRIBUTE + " and " + ATT_GUID_ATTRIBUTE);
                    return null;
                }
                curGroupDN = result.getNameInNamespace();
                log2Output("O", stepName, parameter, "GroupDN retrieved:" + curGroupDN, "");
                return curGroupDN;
            } else {
                log2Output("X", stepName, parameter, "No " + ATT_STATIC_GROUP_NAME_ATTRIBUTE + " match " + ATT_GROUP_FROM_NAME_FILTER + " in current " + ATT_GROUP_SEARCH_SCOPE, "Check " + ATT_GROUP_BASE_DN + ", " + ATT_GROUP_FROM_NAME_FILTER + " and " + ATT_GROUP_SEARCH_SCOPE);
                return null;
            }
        } catch (InvalidSearchFilterException ne) {
            log2Output("X", stepName, parameter, ne.toString(), "Check " + ATT_GROUP_FROM_NAME_FILTER);
            ne.printStackTrace();
        } catch (NamingException ne) {
            log2Output("X", stepName, parameter, ne.toString(), "Check " + ATT_GROUP_BASE_DN);
            ne.printStackTrace();

        } catch (Exception ne) {
            log2Output("X", stepName, parameter, ne.toString(), "Check request parameters");
            ne.printStackTrace();
        }

        return null;
    }

    /**
     * <p>
     * Read user or group LDIF profile and save to internal buffer. The LDIF
     * data can be fetched by <code>outputReport</code>
     *
     * @param name The user or group's name
     * @param isUser if current name is user or group
     */
    public void readLdifByName(String name, boolean isUser) {
        log2Output(">", "Export LDIF", name, "-", "");
        String dn = isUser ? getUserDN(name) : getGroupDN(name);
        if (dn!=null && (!"".equals(dn))) _readLdifByDN(dn);
    }

    /**
     * <p>
     * Read user or group LDIF profile and save to internal buffer. The LDIF
     * data can be fetched by <code>outputReport</code>
     *
     * @param dn The Distinguished Name of user or group
     */
    public void readLdifByDN(String dn) {
        log2Output(">", "Export LDIF", dn, "-", "");
        _readLdifByDN(dn);
    }

    private void _readLdifByDN(String dn) {
        try {
            if (dn == null || dn.equals("")) {
                log2Output("X", "Read LDIF", "", "Invalid DN", "Check Init parameters and current name");
                return;
            }
            Attributes atts = dirCtx.getAttributes(dn);
            if (atts != null) {
                StringBuffer sb = new StringBuffer();
                for (NamingEnumeration ae = atts.getAll(); ae.hasMoreElements();) {
                    Attribute a = (Attribute) ae.next();
                    for (NamingEnumeration vals = a.getAll(); vals.hasMoreElements();) {
                        Object val = vals.next();
                        sb.append(a.getID() + ":" + val + NEW_LINE);
                    }
                }
                if (sb != null) {
                    log2Output("O", "Read LDIF", dn, sb.toString(), "");
                }
            } else {
                log2Output("?", "Read LDIF", dn, "No Attributes found", "Not critical, check Init parameters");
            }

        } catch (Exception e) {
            log2Output("X", "Read LDIF", dn, e.toString(), "Check DAnalyzer.log");
        }
    }

    private boolean checkUserInFilter(String userID) {
        String stepName = "Match AllUsersFilter";
        SearchControls sc = new SearchControls();
        String[] attIDs = new String[]{(String) attrsBean.getUserNameAttribute()};
        sc.setSearchScope((int) attrsBean.getUserSearchScopeInt());
        sc.setReturningAttributes(attIDs);
        NamingEnumeration nae = null;
        String tempUserFilter = "(&(" + (String) attrsBean.getAllUsersFilter() + ")(" + (String) attrsBean.getUserNameAttribute() + "=" + userID + "))";
        String parameter = ATT_USER_BASE_DN + ":" + (String) attrsBean.getUserBaseDN()
                + NEW_LINE + PARA_MATCH_FILTER + ":" + tempUserFilter
                + NEW_LINE + PARA_REQUEST_ATTRS + ":" + Arrays.toString(attIDs);
        try {
            nae = dirCtx.search((String) attrsBean.getUserBaseDN(), tempUserFilter, sc);

            if (nae.hasMoreElements()) {
                log2Output("O", stepName, parameter, "User matches required filter:" + tempUserFilter, "");
                return true;
            } else {
                log2Output("X", stepName, parameter, "User does not match filter", "Check " + ATT_ALL_USERS_FILTER + " or " + ATT_USER_NAME_ATTRIBUTE);
                return false;
            }
        } catch (InvalidSearchFilterException ne) {
            log2Output("X", stepName, parameter, ne.toString(), "Check " + ATT_ALL_USERS_FILTER + " or " + ATT_USER_NAME_ATTRIBUTE);
            ne.printStackTrace();
        } catch (NamingException ne) {
            log2Output("X", stepName, parameter, ne.toString(), "Check " + ATT_USER_BASE_DN);
            ne.printStackTrace();
        } catch (Exception ne) {
            log2Output("X", stepName, parameter, ne.toString(), "Check request parameters");
            ne.printStackTrace();
        }

        return false;
    }

    private boolean findGroupMembership(String curUserDN, String[] attIDs) {
        String stepName = "Find GroupMembership";
        String groupFilter = (String) attrsBean.getAllGroupsFilter();
        String searchFilter = ((String) attrsBean.getStaticGroupDNSFromMemberDNFilter()).replace("%M", curUserDN);

        String parameter = ATT_GROUP_BASE_DN + ":" + (String) attrsBean.getGroupBaseDN()
                + NEW_LINE + PARA_MATCH_FILTER + ":" + searchFilter
                + NEW_LINE + PARA_REQUEST_ATTRS + ":" + Arrays.toString(attIDs);
        if ((groupFilter != null && groupFilter.length() > 0)
                && (searchFilter != null && searchFilter.length() > 0)) {
            try {
                SearchControls sc = new SearchControls();
                sc.setSearchScope((int) attrsBean.getUserSearchScopeInt());
                sc.setReturningAttributes(attIDs);
                NamingEnumeration ne = dirCtx.search((String) attrsBean.getGroupBaseDN(), searchFilter, sc);
                if (ne.hasMoreElements()) {
                    log2Output("O", stepName, parameter, "Find membership entries based on limited(0 level) Group Membership Searching", "");
                    return true;
                } else {
                    log2Output("?", stepName, parameter, "No membership entries match filter", "Not critical, check " + ATT_ALL_GROUPS_FILTER + " and " + ATT_STATIC_GROUP_DNS_FROM_MEMBER_DN_FILTER);
                    return false;
                }
            } catch (InvalidSearchFilterException ne) {
                log2Output("?", stepName, parameter, ne.toString(), "Not critical, check " + ATT_ALL_GROUPS_FILTER + " or " + ATT_STATIC_GROUP_DNS_FROM_MEMBER_DN_FILTER);
                //ne.printStackTrace();
            } catch (NamingException ne) {
                log2Output("?", stepName, parameter, ne.toString(), "Not critical, check " + ATT_GROUP_BASE_DN);
                ne.printStackTrace();
            } catch (Exception ne) {
                log2Output("?", stepName, parameter, ne.toString(), "Not critical, check request parameters");
                ne.printStackTrace();
            }

        } else {
            log2Output("?", stepName, parameter, "No GroupMembership entries found", "Not critical, check " + ATT_ALL_GROUPS_FILTER + " and " + ATT_STATIC_GROUP_DNS_FROM_MEMBER_DN_FILTER);
            return false;
        }

        return false;
    }

    /**
     * <p>
     * Verify if <code>user</code> and <code>password</code> can approve Changes
     * through Agile PLM
     *
     * @param user
     * @param password
     * @return a <code>boolean</code> variable
     */
    public boolean verifyApproval(String user, String password) {
        if (dirCtx == null) {
            return false;
        }

        log2Output(">", "Validate Approval", user, "-", "");
        // check if user is able to approve Changes
        /*
         * 1. Get user's entry dn's value / (User Entry DN Attribute)
             * AD: userPrincipalName
             * ADLDS: userPrincipalName
             * SunONE: entrydn
             * OID: dn
         * 2. Authenticate Authenticate entry dn + password 
         * 3. Check if it matches Search Filter
         * 4. Check if it matches availabe Groups + Group Membership
         * 5. If step3||step4 is true, then succeeds
         */

        // step 1, Get user's entry dn's value (User Entry DN Attribute)
        String entryDNValue = getUserEntryDNValue(user);
        if (entryDNValue == null) {
            return false;
        }

        // step 2. Authenticate Authenticate entry dn + password 
        InitialDirContext tmpCtx = bindUser(entryDNValue, password);
        if (tmpCtx == null) {
            return false;
        } else {
            //
        }
        try {
            tmpCtx.close();
        } catch (NamingException ex) {
            //Logger.getLogger(DAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        }

        // 3. Check if it matches Search Filter
        boolean inAllUsersFilter = checkUserInFilter(user);
        if (inAllUsersFilter) {
            return true;
        }

        // 4. Check if it matches availabe Groups + Group Membership if step3 is false
        String[] attIDs = new String[]{(String) attrsBean.getStaticMemberDNAttribute()};
        boolean inGroupShip = findGroupMembership(entryDNValue, attIDs);
        return inGroupShip;
    }

    private String getUserEntryDNValue(String user) {

        String stepName = "Get EntryDN";

        String entryDN = (String) attrsBean.getUserEntryDNAttribute();

        String entryDNValue = "";
        SearchControls sc = new SearchControls();
        String tempUserFilter = "(&(objectclass=" + (String) attrsBean.getUserObjectClass() + ")(" + (String) attrsBean.getUserNameAttribute() + "=" + user + "))";
        sc.setSearchScope((int) attrsBean.getUserSearchScopeInt());
        sc.setReturningAttributes(new String[]{entryDN});

        String parameter = ATT_USER_BASE_DN + ":" + (String) attrsBean.getUserBaseDN()
                + NEW_LINE + PARA_MATCH_FILTER + ":" + tempUserFilter
                + NEW_LINE + PARA_REQUEST_ATTRS + ":" + entryDN;
        try {
            NamingEnumeration ne = dirCtx.search((String) attrsBean.getUserBaseDN(), tempUserFilter, sc);
            if (ne.hasMoreElements()) {
                SearchResult sr = (SearchResult) ne.next();
                Attribute dnAttr = sr.getAttributes().get(entryDN);
                if (dnAttr == null) {
                    log2Output("X", stepName, parameter, "Wrong " + ATT_USER_ENTRY_DN_ATTRIBUTE, "Check " + ATT_USER_ENTRY_DN_ATTRIBUTE);
                    return null;
                }
                entryDNValue = (String) dnAttr.get();
                log2Output("O", stepName, parameter, "Retrieved user's " + ATT_USER_ENTRY_DN_ATTRIBUTE + ":" + entryDNValue, "");
                if (ne.hasMoreElements()) {
                    log2Output("X", stepName, parameter, "Find multiple " + ATT_USER_ENTRY_DN_ATTRIBUTE, "This should not happen, check with your IT");
                }
                return entryDNValue;
            } else {
                log2Output("X", stepName, parameter, "No " + ATT_USER_ENTRY_DN_ATTRIBUTE + " matchs filter in current " + ATT_USER_SEARCH_SCOPE, "Check " + ATT_USER_BASE_DN + ", " + ATT_USER_OBJECT_CLASS + ", " + ATT_USER_NAME_ATTRIBUTE + " and " + ATT_USER_SEARCH_SCOPE);
            }

        } catch (InvalidSearchFilterException ne) {
            log2Output("X", stepName, parameter, ne.toString(), "Check " + ATT_USER_OBJECT_CLASS + " or " + ATT_USER_NAME_ATTRIBUTE);
            ne.printStackTrace();
        } catch (NamingException ne) {
            log2Output("X", stepName, parameter, ne.toString(), "Check " + ATT_USER_BASE_DN);
            ne.printStackTrace();
        } catch (Exception ne) {
            log2Output("X", stepName, parameter, ne.toString(), "Check request parameters");
            ne.printStackTrace();
        }

        return null;
    }

}
