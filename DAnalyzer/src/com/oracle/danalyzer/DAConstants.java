// ==============================================================
// Copyright ©2017 by Oracle
// All Rights Reserved.
// ==============================================================

package com.oracle.danalyzer;

/**
 * @author jie.chen@oracle.com
 */
public interface DAConstants {
    
    /**
     *
     */
    static final String APP_NAME = "DAnalyzer 1.2";
    /**
     *
     */
    static final String MANDATORY_ATTRS_SEP = ",";
    /**
     *
     */
    static final String SERVER_ACTIVE_DIR_SERVICE = "Active Directory";

    /**
     *
     */
    static final String SERVER_ACTIVE_DIR_LDS_SERVICE = "Active Directory LDS";

    /**
     *
     */
    static final String SERVER_SUN_ONE_SERVICE = "SunONE Directory";

    /**
     *
     */
    static final String SERVER_ORACLE_INTERNET_DIRECTORY_SERVICE = "Oracle Internet Directory";

    /**
     *
     */
    static final String SERVER_ORACLE_VIRTUAL_DIRECTORY_SERVICE = "Oracle Virtual Directory";

    /**
     *
     */
    static final String SERVER_OTHER_SERVICE = "Generic LDAP";
    
    /**
     *
     */
    static final String DEF_SEARCH_SCOPE_SUB_TREE = "SUB_TREE";

    /**
     *
     */
    static final String DEF_SEARCH_SCOPE_ONE_LEVEL = "ONE_LEVEL";

    /**
     *
     */
    static final String ATT_URL = "URL";

    /**
     *
     */
    static final String ATT_LDAP_PROVIDER = "Directory Server";

    /**
     *
     */
    static final String ATT_DOMAIN = "Domain";

    /**
     *
     */
    static final String ATT_PRINCIPAL = "Principal";

    /**
     *
     */
    static final String ATT_PRINCIPAL_JC = "Username";

    /**
     *
     */
    static final String ATT_CREDENTIAL = "Credential";

    /**
     *
     */
    static final String ATT_CREDENTIAL_JC = "Password";

    /**
     *
     */
    static final String ATT_MECHANISM = "Mechanism";

    /**
     *
     */
    static final String ATT_USER_BASE_DN = "User Base DN";

    /**
     *
     */
    static final String ATT_USER_BASE_DN_JC = "User Path";

    /**
     *
     */
    static final String ATT_ALL_USERS_FILTER = "All Users Filter";

    /**
     *
     */
    static final String ATT_ALL_USERS_FILTER_JC = "Search Filter";

    /**
     *
     */
    static final String ATT_USER_SEARCH_SCOPE = "User Search Scope";

    /**
     *
     */
    static final String ATT_USER_SEARCH_SCOPE_JC = "Search Scope";

    /**
     *
     */
    static final String ATT_USER_SEARCH_SCOPE_INT = "Search Scope_INT";

    /**
     *
     */
    static final String ATT_USER_FROM_NAME_FILTER = "User From Name Filter";

    /**
     *
     */
    static final String ATT_USER_NAME_ATTRIBUTE = "User Name Attribute";

    /**
     *
     */
    static final String ATT_USER_OBJECT_CLASS = "User Object Class";

    /**
     *
     */
    static final String ATT_GROUP_BASE_DN = "Group Base DN";

    /**
     *
     */
    static final String ATT_GROUP_BASE_DN_JC = "Group Path";

    /**
     *
     */
    static final String ATT_ALL_GROUPS_FILTER = "All Groups Filter";

    /**
     *
     */
    static final String ATT_ALL_GROUPS_FILTER_JC = "Group Filter";

    /**
     *
     */
    static final String ATT_GROUP_SEARCH_SCOPE = "Group Search Scope";

    /**
     *
     */
    static final String ATT_GROUP_SEARCH_SCOPE_JC = "Group Scope";

    /**
     *
     */
    static final String ATT_GROUP_SEARCH_SCOPE_INT = "Group Scope_INT";

    /**
     *
     */
    static final String ATT_GROUP_FROM_NAME_FILTER = "Group From Name Filter";

    /**
     *
     */
    static final String ATT_STATIC_GROUP_NAME_ATTRIBUTE = "Static Group Name Attribute";

    /**
     *
     */
    static final String ATT_STATIC_GROUP_OBJECT_CLASS = "Static Group Object Class";

    /**
     *
     */
    static final String ATT_STATIC_MEMBER_DN_ATTRIBUTE = "Static Member DN Attribute";

    /**
     *
     */
    static final String ATT_STATIC_GROUP_DNS_FROM_MEMBER_DN_FILTER = "Static Group DNs From Member DN Filter";

    /**
     *
     */
    static final String ATT_STATIC_GROUP_DNS_FROM_MEMBER_DN_FILTER_2 = "Group Membership";

    /**
     *
     */
    static final String ATT_GUID_ATTRIBUTE = "Guid Attribute";

    /**
     *
     */
    static final String ATT_USER_ENTRY_DN_ATTRIBUTE = "User Entry DN Attribute";
    /**
     *
     */
    static final String ATT_AGILE_MANDATORY_ATTRIBUTES = "Mandatory Attributes";
    /**
     *
     */
    static final String ATT_SEARCH_COUNT = "Count Limit";

    /**
     *
     */
    static final String ATT_REFERRAL = "Referral";
    
    /**
     *
     */
    static final String NEW_LINE = "\n";
    
    /**
     *
     */
    final String PARA_MATCH_FILTER = "matchFilter";

    /**
     *
     */
    final String PARA_REQUEST_ATTRS = "reqAttributes";
    
    /**
     *
     */
    static String[][][] recommends = new String[][][]{
        {{"*", null, "Mandatory field"},
        {"*", null, null},
        {"*", null, null},
        {"*", "simple", null},
        {"*", null, null},
        {"*", null, "(objectclass=user) if no special filter in Weblogic"},
        {"*", "SUB_TREE", null},
        {"", "(&(sAMAccountName=%u)(objectClass=user))", "(&(sAMAccountName=%u)(objectClass=user)) if no special filter in Weblogic"},
        {"*", "sAMAccountName", null},
        {"*", "user", null},
        {"", null, null},
        {"", null, "(objectclass=group) if no special filter in Weblogic"},
        {"*", "SUB_TREE", null},
        {"", "(&(cn=%g)(objectclass=group))", "(&(cn=%g)(objectclass=group)) if no special filter in Weblogic"},
        {"*", "cn", null},
        {"*", "group", null},
        {"*", "member", null},
        {"*", "(&(member=%M)(objectclass=group))", null},
        {"*", "objectguid", null},
        {"*", "userPrincipalName", "Agile internal setting: userPrincipalName"},
        {"*", "modifyTimestamp", "Agile mandatory attributes (comma separated)"}
        },// AD
        {{"*", null, "Mandatory field"},
        {"*", null, null},
        {"*", null, null},
        {"*", "simple", null},
        {"*", null, null},
        {"", null, "(objectclass=user) if no special filter in Weblogic"},
        {"*", "SUB_TREE", null},
        {"", "(&(uid=%u)(objectClass=user))", "(&(uid=%u)(objectClass=user)) if no special filter in Weblogic"},
        {"*", "uid", null},
        {"*", "user", null},
        {"", null, null},
        {"", null, "(objectclass=group) if no special filter in Weblogic"},
        {"*", "SUB_TREE", null},
        {"", "(&(cn=%g)(objectclass=group))", "(&(cn=%g)(objectclass=group)) if no special filter in Weblogic"},
        {"*", "cn", null},
        {"*", "group", null},
        {"*", "member", null},
        {"*", "(&(member=%M)(objectclass=group))", null},
        {"*", "objectguid", null},
        {"*", "userPrincipalName", "Agile internal setting: userPrincipalName"},
        {"*", "modifyTimestamp", "Agile mandatory attributes (comma separated)"}
        },// ADLDS
        {{"", null, "Not required"},
        {"*", null, null},
        {"*", null, null},
        {"*", "simple", null},
        {"*", null, null},
        {"", null, "(objectclass=person) if no special filter  in Weblogic"},
        {"*", "SUB_TREE", null},
        {"", "(&(uid=%u)(objectClass=person))", "(&(uid=%u)(objectClass=person)) if no special filter in Weblogic"},
        {"*", "uid", null},
        {"*", "person", null},
        {"", null, null},
        {"", null, "(objectclass=groupofuniquenames) if no special filter  in Weblogic"},
        {"*", "SUB_TREE", null},
        {"", "(|(&(cn=%g)(objectclass=groupofuniquenames))(&(cn=%g)(objectclass=groupofURLs)))", "(|(&(cn=%g)(objectclass=groupofuniquenames))(&(cn=%g)(objectclass=groupofURLs))) in Weblogic"},
        {"*", "cn", null},
        {"*", "groupofuniquenames", null},
        {"*", "uniquemember", null},
        {"*", "(&(uniquemember=%M)(objectclass=groupofuniquenames))", null},
        {"*", "nsuniqueid", null},
        {"*", "entrydn", "Agile internal setting: entrydn"},
        {"*", "modifyTimestamp", "Agile mandatory attributes (comma separated)"}
        },// SunONE
        {{"", null, "Not required"},
        {"*", null, null},
        {"*", null, null},
        {"*", "simple", null},
        {"*", null, null},
        {"", null, "(objectclass=person) if no special filter in Weblogic"},
        {"*", "SUB_TREE", null},
        {"", "(&(cn=%u)(objectClass=person))", "(&(cn=%u)(objectClass=person)) if no special filter in Weblogic"},
        {"*", "cn", null},
        {"*", "person", null},
        {"", null, null},
        {"", null, "(objectclass=groupofuniquenames) if no special filter in Weblogic"},
        {"*", "SUB_TREE", null},
        {"", "(|(&(cn=%g)(objectclass=groupofuniquenames))(&(cn=%g)(objectclass=orcldynamicgroup)))", "(|(&(cn=%g)(objectclass=groupofuniquenames))(&(cn=%g)(objectclass=orcldynamicgroup))) if no special filter in Weblogic"},
        {"*", "cn", null},
        {"*", "groupofuniquenames", null},
        {"*", "uniquemember", null},
        {"*", "(&(uniquemember=%M)(objectclass=groupofuniquenames))", null},
        {"*", "orclGUID", null},
        {"*", "dn", "Agile internal setting: dn"},
        {"*", "modifyTimestamp", "Agile mandatory attributes (comma separated)"}
        },// OID
        {{"", null, "Not required"},
        {"*", null, null},
        {"*", null, null},
        {"*", "simple", null},
        {"*", null, null},
        {"", null, "(objectclass=person) if no special filter in Weblogic"},
        {"*", "SUB_TREE", null},
        {"", "(&(cn=%u)(objectClass=person))", "(&(cn=%u)(objectClass=person)) if no special filter in Weblogic"},
        {"*", "cn", null},
        {"*", "person", null},
        {"", null, null},
        {"", null, "(objectclass=groupofuniquenames) if no special filter in Weblogic"},
        {"*", "SUB_TREE", null},
        {"", "(|(&(cn=%g)(objectclass=groupofuniquenames))(&(cn=%g)(objectclass=groupofurls)))", "(|(&(cn=%g)(objectclass=groupofuniquenames))(&(cn=%g)(objectclass=groupofurls))) if no special filter in Weblogic"},
        {"*", "cn", null},
        {"*", "groupofuniquenames", null},
        {"*", "uniquemember", null},
        {"*", "(&(uniquemember=%M)(objectclass=groupofuniquenames))", null},
        {"*", "orclGUID", null},
        {"*", null, "It depends on Groovy customization"},
        {"*", "modifyTimestamp", "Agile mandatory attributes (comma separated)"}
        },// OVD
        {{"", null, "Not required"},
        {"*", null, null},
        {"*", null, null},
        {"*", "simple", null},
        {"*", null, null},
        {"", null, null},
        {"*", "SUB_TREE", null},
        {"", null, null},
        {"*", null, null},
        {"*", null, null},
        {"", null, null},
        {"", null, null},
        {"*", "SUB_TREE", null},
        {"", null, null},
        {"*", null, null},
        {"*", null, null},
        {"*", null, null},
        {"*", null, null},
        {"*", null, null},
        {"*", null, null},
        {"*", "modifyTimestamp", "Agile mandatory attributes (comma separated)"}
        } // GENERIC
    };
}
