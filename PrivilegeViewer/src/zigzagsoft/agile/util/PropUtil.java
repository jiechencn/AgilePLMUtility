package zigzagsoft.agile.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.PropertyResourceBundle;

/**
 * Created by jijichen on 7/4/2014.
 */
public final class PropUtil {
    static HashMap propertiesMap = new HashMap();

    public static Properties getProperties(String propFile) {

        try {
            if (propertiesMap.get(propFile) == null) {
                Properties properties = new Properties();

                PropertyResourceBundle bundle = (PropertyResourceBundle) PropertyResourceBundle.getBundle(propFile + "");

                Enumeration keyEnum = bundle.getKeys();
                while (keyEnum.hasMoreElements()) {
                    String key = (String) keyEnum.nextElement();
                    String value = (String) bundle.handleGetObject(key);
                    String JVMValue = System.getProperty(key);
                    if ((JVMValue != null) && (JVMValue.length() > 0)) {
                        value = JVMValue;
                    }
                    if ("db.password".equalsIgnoreCase(key)) {
                        value = ED.decrypt(value);
                    }
                    properties.setProperty(key, value.trim());
                }
                propertiesMap.put(propFile, properties);
                return properties;
            }else
                return (Properties)propertiesMap.get(propFile);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return new Properties();
    }
    /*
    public Object getPropValue(String key){
        return properties.get(key);
    }*/


}
