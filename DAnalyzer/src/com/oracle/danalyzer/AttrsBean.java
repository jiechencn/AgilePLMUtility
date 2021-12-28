// ==============================================================
// Copyright ©2017 by Oracle
// All Rights Reserved.
// ==============================================================

package com.oracle.danalyzer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author jie.chen@oracle.com
 */
public abstract class AttrsBean {

    static AttrsBean ab = null;

    /**
     *
     * @param attrs
     * @return
     */
    public static AttrsBean getBean(Map attrs) {
        defaultAttrs = attrs;

        String scope = (String)attrs.get(DAConstants.ATT_USER_SEARCH_SCOPE);
        if (scope==null){
            attrs.put(DAConstants.ATT_USER_SEARCH_SCOPE_INT, -999);
        }else if (scope.equals(DAConstants.DEF_SEARCH_SCOPE_ONE_LEVEL)){    
            attrs.put(DAConstants.ATT_USER_SEARCH_SCOPE_INT, 1);
        }else if (scope.equals(DAConstants.DEF_SEARCH_SCOPE_SUB_TREE)){
            attrs.put(DAConstants.ATT_USER_SEARCH_SCOPE_INT, 2);
        }else{
            attrs.put(DAConstants.ATT_USER_SEARCH_SCOPE_INT, -1);
        }
        
        scope = (String)attrs.get(DAConstants.ATT_GROUP_SEARCH_SCOPE);
        if (scope==null){
            attrs.put(DAConstants.ATT_GROUP_SEARCH_SCOPE_INT, -1);
        }else if (scope.equals(DAConstants.DEF_SEARCH_SCOPE_ONE_LEVEL)){    
            attrs.put(DAConstants.ATT_GROUP_SEARCH_SCOPE_INT, 1);
        }else if (scope.equals(DAConstants.DEF_SEARCH_SCOPE_SUB_TREE)){
            attrs.put(DAConstants.ATT_GROUP_SEARCH_SCOPE_INT, 2);
        }else{
            attrs.put(DAConstants.ATT_GROUP_SEARCH_SCOPE_INT, -1);
        }
        
        String mech = (String)attrs.get(DAConstants.ATT_MECHANISM);
        if (mech==null || mech.equals(""))
            mech = "simple";      
        attrs.put(DAConstants.ATT_MECHANISM, mech);

        
        switch ((String)attrs.get(DAConstants.ATT_LDAP_PROVIDER)) {
            case DAConstants.SERVER_ACTIVE_DIR_SERVICE:
                ab = new ADBean();
                break;
            case DAConstants.SERVER_ACTIVE_DIR_LDS_SERVICE:
                ab = new ADLDSBean();
                break;
            case DAConstants.SERVER_SUN_ONE_SERVICE:
                ab =  new SunONEBean();
                break;
            case DAConstants.SERVER_ORACLE_INTERNET_DIRECTORY_SERVICE:
                ab = new OIDBean();
                break;
            case DAConstants.SERVER_ORACLE_VIRTUAL_DIRECTORY_SERVICE:
                ab = new OVDBean();
                break;
            default: // SERVER_OTHER_SERVICE
                ab = new GenericBean();
        }
        return ab;
    }
    
    /**
     *
     */
    public void build(){
        setIfNull(DAConstants.ATT_AGILE_MANDATORY_ATTRIBUTES, "modifyTimestamp");
    };
        
    /**
     *
     * @param key
     * @param value
     */
    protected void setIfNull(String key, Object value){
        String v = (String) defaultAttrs.get(key);
        if (v == null || "".equals(v)){
            defaultAttrs.put(key, value);
        }
    }

    /**
     *
     * @return
     */
    public String toString(){
        LinkedHashMap<String, Object> lhm = new LinkedHashMap<String, Object>();
        
        lhm.put(DAConstants.ATT_LDAP_PROVIDER, getLDAPProvider());
        lhm.put(DAConstants.ATT_URL, getURL());
        lhm.put(DAConstants.ATT_DOMAIN, getDomain());
        lhm.put(DAConstants.ATT_PRINCIPAL, getPrincipal());
        lhm.put(DAConstants.ATT_CREDENTIAL, getCredential());
        lhm.put(DAConstants.ATT_MECHANISM, getMechanism());
        
        lhm.put(DAConstants.ATT_USER_BASE_DN, getUserBaseDN());
        lhm.put(DAConstants.ATT_ALL_USERS_FILTER, getAllUsersFilter());
        lhm.put(DAConstants.ATT_USER_SEARCH_SCOPE, getUserSearchScope());
        lhm.put(DAConstants.ATT_USER_FROM_NAME_FILTER, getUserFromNameFilter());
        lhm.put(DAConstants.ATT_USER_NAME_ATTRIBUTE, getUserNameAttribute());
        lhm.put(DAConstants.ATT_USER_OBJECT_CLASS, getUserObjectClass());
        
        lhm.put(DAConstants.ATT_GROUP_BASE_DN, getGroupBaseDN());
        lhm.put(DAConstants.ATT_ALL_GROUPS_FILTER, getAllGroupsFilter());
        lhm.put(DAConstants.ATT_GROUP_SEARCH_SCOPE, getGroupSearchScope());
        lhm.put(DAConstants.ATT_GROUP_FROM_NAME_FILTER, getGroupFromNameFilter());
        lhm.put(DAConstants.ATT_STATIC_GROUP_NAME_ATTRIBUTE, getStaticGroupNameAttribute());
        lhm.put(DAConstants.ATT_STATIC_GROUP_OBJECT_CLASS, getStaticGroupObjectClass());
        lhm.put(DAConstants.ATT_STATIC_MEMBER_DN_ATTRIBUTE, getStaticMemberDNAttribute());
        lhm.put(DAConstants.ATT_STATIC_GROUP_DNS_FROM_MEMBER_DN_FILTER, getStaticGroupDNSFromMemberDNFilter());
 
        lhm.put(DAConstants.ATT_GUID_ATTRIBUTE, getGuidAttribute());
        lhm.put(DAConstants.ATT_USER_ENTRY_DN_ATTRIBUTE, getUserEntryDNAttribute());
        lhm.put(DAConstants.ATT_AGILE_MANDATORY_ATTRIBUTES, getAgileMandatoryAttributes());
        
        lhm.put(DAConstants.ATT_SEARCH_COUNT, getSearchCount());
        lhm.put(DAConstants.ATT_REFERRAL, getReferral());

        
        StringBuffer out = new StringBuffer();
        for (Map.Entry<String, Object> entry : lhm.entrySet()) {
            out.append(entry.getKey() + ":" + entry.getValue() + DAConstants.NEW_LINE);
        }

        return out.toString();
    }

    /**
     *
     * @return
     */
    public String getURL(){
        return (String)defaultAttrs.get(DAConstants.ATT_URL);
    }

    /**
     *
     * @return
     */
    public String getLDAPProvider(){
        return (String)defaultAttrs.get(DAConstants.ATT_LDAP_PROVIDER);
    }

    /**
     *
     * @return
     */
    public String getDomain(){
        return (String)defaultAttrs.get(DAConstants.ATT_DOMAIN);
    }

    /**
     *
     * @return
     */
    public String getPrincipal(){
        return (String)defaultAttrs.get(DAConstants.ATT_PRINCIPAL);
    }

    /**
     *
     * @return
     */
    public String getCredential(){
        return (String)defaultAttrs.get(DAConstants.ATT_CREDENTIAL);
    }

    /**
     *
     * @return
     */
    public String getMechanism(){
        return (String)defaultAttrs.get(DAConstants.ATT_MECHANISM);
    }

    /**
     *
     * @return
     */
    public String getGroupBaseDN(){
        return (String)defaultAttrs.get(DAConstants.ATT_GROUP_BASE_DN);
    }

    /**
     *
     * @return
     */
    public String getUserBaseDN(){
        return (String)defaultAttrs.get(DAConstants.ATT_USER_BASE_DN);
    }

    /**
     *
     * @return
     */
    public String getReferral(){
        return (String)defaultAttrs.get(DAConstants.ATT_REFERRAL);
    }

    /**
     *
     * @return
     */
    public int getSearchCount(){
        return (int)defaultAttrs.get(DAConstants.ATT_SEARCH_COUNT);
    }
    
    /**
     *
     * @return
     */
    public String getDAProvider(){
        return (String)defaultAttrs.get(DAConstants.ATT_LDAP_PROVIDER);
    }

    /**
     *
     * @return
     */
    public String getAllUsersFilter(){
        return (String)defaultAttrs.get(DAConstants.ATT_ALL_USERS_FILTER);
    }

    /**
     *
     * @return
     */
    public String getUserSearchScope(){
        return (String)defaultAttrs.get(DAConstants.ATT_USER_SEARCH_SCOPE);
    }

    /**
     *
     * @return
     */
    public int getUserSearchScopeInt(){
        return (int)defaultAttrs.get(DAConstants.ATT_USER_SEARCH_SCOPE_INT);
    }

    /**
     *
     * @return
     */
    public String getUserFromNameFilter(){
        return (String)defaultAttrs.get(DAConstants.ATT_USER_FROM_NAME_FILTER);
    }

    /**
     *
     * @return
     */
public String getUserNameAttribute(){
        return (String)defaultAttrs.get(DAConstants.ATT_USER_NAME_ATTRIBUTE);
    }

    /**
     *
     * @return
     */
    public String getUserObjectClass(){
        return (String)defaultAttrs.get(DAConstants.ATT_USER_OBJECT_CLASS);
    }

    /**
     *
     * @return
     */
    public String getAllGroupsFilter(){
        return (String)defaultAttrs.get(DAConstants.ATT_ALL_GROUPS_FILTER);
    }

    /**
     *
     * @return
     */
    public String getGroupSearchScope(){
        return (String)defaultAttrs.get(DAConstants.ATT_GROUP_SEARCH_SCOPE);
    }

    /**
     *
     * @return
     */
    public int getGroupSearchScopeInt(){
        return (int)defaultAttrs.get(DAConstants.ATT_GROUP_SEARCH_SCOPE_INT);
    }

    /**
     *
     * @return
     */
    public String getGroupFromNameFilter(){
        return (String)defaultAttrs.get(DAConstants.ATT_GROUP_FROM_NAME_FILTER);
    }

    /**
     *
     * @return
     */
    public String getStaticGroupNameAttribute(){
        return (String)defaultAttrs.get(DAConstants.ATT_STATIC_GROUP_NAME_ATTRIBUTE);
    }

    /**
     *
     * @return
     */
    public String getStaticGroupObjectClass(){
        return (String)defaultAttrs.get(DAConstants.ATT_STATIC_GROUP_OBJECT_CLASS);
    }

    /**
     *
     * @return
     */
    public String getStaticMemberDNAttribute(){
        return (String)defaultAttrs.get(DAConstants.ATT_STATIC_MEMBER_DN_ATTRIBUTE);
    }

    /**
     *
     * @return
     */
    public String getStaticGroupDNSFromMemberDNFilter(){
        return (String)defaultAttrs.get(DAConstants.ATT_STATIC_GROUP_DNS_FROM_MEMBER_DN_FILTER);
    }

    /**
     *
     * @return
     */
    public String getGuidAttribute(){
        return (String)defaultAttrs.get(DAConstants.ATT_GUID_ATTRIBUTE);
    }

    /**
     *
     * @return
     */
    public String getUserEntryDNAttribute(){
        return (String)defaultAttrs.get(DAConstants.ATT_USER_ENTRY_DN_ATTRIBUTE);
    }
    
    public String getAgileMandatoryAttributes(){
        return (String)defaultAttrs.get(DAConstants.ATT_AGILE_MANDATORY_ATTRIBUTES);
    }

    /**
     *
     */
    protected static Map defaultAttrs = null;
    

}
