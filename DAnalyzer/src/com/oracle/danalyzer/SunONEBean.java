// ==============================================================
// Copyright ©2017 by Oracle
// All Rights Reserved.
// ==============================================================

package com.oracle.danalyzer;

/**
 * @author jie.chen@oracle.com
 */
public class SunONEBean extends AttrsBean{

    /**
     *
     */
    @Override
    public void build() {
        setIfNull(DAConstants.ATT_ALL_USERS_FILTER, "(objectclass=person)");
        setIfNull(DAConstants.ATT_ALL_GROUPS_FILTER, "(objectclass=groupofuniquenames)");
        setIfNull(DAConstants.ATT_USER_FROM_NAME_FILTER, "(&(uid=%u)(objectClass=person))");
        setIfNull(DAConstants.ATT_USER_NAME_ATTRIBUTE, "uid");
        setIfNull(DAConstants.ATT_USER_OBJECT_CLASS, "person");
        setIfNull(DAConstants.ATT_GROUP_FROM_NAME_FILTER, "(|(&(cn=%g)(objectclass=groupofuniquenames))(&(cn=%g)(objectclass=groupofURLs)))");
        setIfNull(DAConstants.ATT_STATIC_GROUP_NAME_ATTRIBUTE, "cn");
        setIfNull(DAConstants.ATT_STATIC_GROUP_OBJECT_CLASS, "groupofuniquenames");
        setIfNull(DAConstants.ATT_STATIC_MEMBER_DN_ATTRIBUTE, "uniquemember");
        setIfNull(DAConstants.ATT_STATIC_GROUP_DNS_FROM_MEMBER_DN_FILTER, "(&(uniquemember=%M)(objectclass=groupofuniquenames))");
        setIfNull(DAConstants.ATT_GUID_ATTRIBUTE, "nsuniqueid");
        setIfNull(DAConstants.ATT_USER_ENTRY_DN_ATTRIBUTE, "entrydn");
        super.build();
    }
    
}
