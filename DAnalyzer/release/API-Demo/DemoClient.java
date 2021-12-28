
import com.jakewharton.fliptables.FlipTable;

import com.oracle.danalyzer.DAConstants;
import com.oracle.danalyzer.DAnalyzer;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.HashMap;

/**
 * @author jie.chen@oracle.com
 */


/*
-Dinclude.agile=true    // default is true
-Dldap.referral=follow  // default is follow, value could be: follow, ignore or throw
-Dldap.countlimit=20    //default is 1001
 */


public class DemoClient {
    public static void main(String[] args) {
        
        HashMap attrs = new HashMap();
        
        // these are mandotary fields
        attrs.put(DAConstants.ATT_LDAP_PROVIDER, DAConstants.SERVER_OTHER_SERVICE);
        attrs.put(DAConstants.ATT_URL, "ldap://10.64.204.161:389");
        attrs.put(DAConstants.ATT_PRINCIPAL, "user1@mycompany.com");
        attrs.put(DAConstants.ATT_CREDENTIAL, "Oracle1");
        attrs.put(DAConstants.ATT_USER_BASE_DN, "ou=people,dc=mycompany,dc=com");
        attrs.put(DAConstants.ATT_ALL_USERS_FILTER, "(cn=*)");
        attrs.put(DAConstants.ATT_GROUP_BASE_DN, "ou=groups,dc=mycompany,dc=com");
        attrs.put(DAConstants.ATT_ALL_GROUPS_FILTER, "(cn=group*)");
        attrs.put(DAConstants.ATT_MECHANISM, "simple");
                
        // below are optional fields. If not input, DAnalyzer uses default values based on different LDAP Server vendor.
        attrs.put(DAConstants.ATT_USER_SEARCH_SCOPE, "SUB_TREE"); // SUB_TREE or ONE_LEVEL
        attrs.put(DAConstants.ATT_USER_FROM_NAME_FILTER, "(&(sAMAccountName=%u)(objectClass=user))");
        attrs.put(DAConstants.ATT_USER_NAME_ATTRIBUTE, "sAMAccountName");
        attrs.put(DAConstants.ATT_USER_OBJECT_CLASS, "user");
        attrs.put(DAConstants.ATT_GROUP_SEARCH_SCOPE, "SUB_TREE"); // SUB_TREE or ONE_LEVEL
        attrs.put(DAConstants.ATT_GROUP_FROM_NAME_FILTER, "(&(cn=%g)(objectclass=group))");
        attrs.put(DAConstants.ATT_STATIC_GROUP_NAME_ATTRIBUTE, "cn");
        attrs.put(DAConstants.ATT_STATIC_GROUP_OBJECT_CLASS, "group");
        attrs.put(DAConstants.ATT_STATIC_MEMBER_DN_ATTRIBUTE, "member");
        attrs.put(DAConstants.ATT_STATIC_GROUP_DNS_FROM_MEMBER_DN_FILTER, "(&(member=%M)(objectclass=group))");
        attrs.put(DAConstants.ATT_GUID_ATTRIBUTE, "objectguid");
              
        DAnalyzer da = null;
        ArrayList out = null;

        da = new DAnalyzer(attrs);
        da.loadUsers();
        da.loadGroups();
        da.readLdifByName("user2", true);
        da.verifyLogon("user1", "Oracle1");
        da.verifyApproval("user3", "Oracle1");
        da.readLdifByName("group1", false);
        out = da.outputReport();
        saveLog2TextReport("d:\\hello1.txt", out);
        saveLog2HTMLReport("d:\\hello1.html", out);
        da.release();        
        


    }
    
    static void saveLog2HTMLReport(String fileToSave, ArrayList content) {
        Iterator it = content.iterator();
        String tdClass = "style='background-color:yellow;'";
        StringBuffer output = new StringBuffer("<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"utf-8\"><title></title><style type='text/css'>table{border:1px solid;border-collapse:collapse;font:13px/normal Arial,Helvetica,Sans-Serif;}th{background-color:black;color:white;}td{border:1px solid;}</style></head><body><h1>"+ "Report of " + DAConstants.APP_NAME + "</h1>");
        output.append("<table><tr><th>&nbsp;</th><th>Request</th><th>Parameter</th><th>Response</th><th>Action</th></tr>");
        while (it.hasNext()) {
            output.append("<tr>");
            String[] ss = (String[]) it.next();
            for (int i = 0; i < ss.length; i++) {
                String s = ss[i];
                if (s == null) {
                    s = "";
                }
                if (s.equals("X")) {
                    tdClass = "style='background-color:red;'";
                } else if (s.equals("+")) {
                    tdClass = "style='background-color:#d9d9d9;'";
                } else if (s.equals("?")){
                    tdClass = "style='background-color:#FAF602;'";
                } else {
                    tdClass = "";
                }
                output.append("<td " + tdClass + " >" + s + "</td>");
            }
            output.append("</tr>");
        }
        output.append("</table>");
        output.append("<h4>Report Date: " + new Date() + "</h4>");
        output.append("</body></html>");

        String outString = output.toString().replaceAll(DAConstants.NEW_LINE, "<br/>");
        
        byte[] buffer = outString.getBytes();

        try(FileOutputStream fo = new FileOutputStream(fileToSave);
            BufferedOutputStream bos = new BufferedOutputStream(fo, 8*1024);) {
            bos.write(buffer);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    static void saveLog2TextReport(String fileToSave, ArrayList content) {

        String[] columnNames = { 
            "*", 
            "Request", 
            "Parameter", 
            "Respoonse", 
            "Action"
        };

        ArrayList<Object[]> array = new ArrayList();
        
        Iterator it = content.iterator();
        while (it.hasNext()) {
            String[] ss = (String[]) it.next();
            array.add(ss);
        }

        String[][] data = (String [][])array.toArray(new String[0][0]);

        String output = "Report of " + DAConstants.APP_NAME;
        output += "\n" + FlipTable.of(columnNames, data);
        output += "\nReport Date: " + new Date();
        byte[] buffer = output.getBytes();

        try(FileOutputStream fo = new FileOutputStream(fileToSave);
            BufferedOutputStream bos = new BufferedOutputStream(fo, 8*1024);) {
            bos.write(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
