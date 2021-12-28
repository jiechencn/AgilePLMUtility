// ==============================================================
// Copyright ©2017 by Oracle
// All Rights Reserved.
// ==============================================================

package com.oracle.danalyzer;

/**
 * @author jie.chen@oracle.com
 */
public class ADLDSBean extends AttrsBean{

    /**
     *
     */
    @Override
    public void build() {
        setIfNull(DAConstants.ATT_ALL_USERS_FILTER, "(objectclass=user)");
        setIfNull(DAConstants.ATT_ALL_GROUPS_FILTER, "(objectclass=group)");
        setIfNull(DAConstants.ATT_USER_FROM_NAME_FILTER, "(&(uid=%u)(objectClass=user))");
        setIfNull(DAConstants.ATT_USER_NAME_ATTRIBUTE, "uid");
        setIfNull(DAConstants.ATT_USER_OBJECT_CLASS, "user");
        setIfNull(DAConstants.ATT_GROUP_FROM_NAME_FILTER, "(&(cn=%g)(objectclass=group))");
        setIfNull(DAConstants.ATT_STATIC_GROUP_NAME_ATTRIBUTE, "cn");
        setIfNull(DAConstants.ATT_STATIC_GROUP_OBJECT_CLASS, "group");
        setIfNull(DAConstants.ATT_STATIC_MEMBER_DN_ATTRIBUTE, "member");
        setIfNull(DAConstants.ATT_STATIC_GROUP_DNS_FROM_MEMBER_DN_FILTER, "(&(member=%M)(objectclass=group))");
        setIfNull(DAConstants.ATT_GUID_ATTRIBUTE, "objectguid");
        setIfNull(DAConstants.ATT_USER_ENTRY_DN_ATTRIBUTE, "userPrincipalName");
        super.build();
    }
    
}
