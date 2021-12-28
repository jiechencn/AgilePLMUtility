package zigzagsoft.agile.util;

import zigzagsoft.agile.privviewer.PVConstant;
import zigzagsoft.license.LicenseUtil;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Created by jijichen on 7/7/2014.
 */
public class PVLicense {
    public static boolean validate(){
        try {
            String userLicense = PropUtil.getProperties(PVConstant.PV_PROP_FILE).getProperty("pv.license");
            String text = PropUtil.getProperties(PVConstant.AGILE_PROP_FILE).getProperty("db.url");
            String key = PropUtil.getProperties(PVConstant.AGILE_PROP_FILE).getProperty("db.user");
            String validator = PropUtil.getProperties(PVConstant.PV_PROP_FILE).getProperty("pv.validator");
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(userLicense.getBytes());
            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            String hashtext = bigInt.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return (LicenseUtil.validate(userLicense, text, key) &&
                    validator.equalsIgnoreCase(hashtext));
        }catch(Exception e){
            return false;
        }
    }
}
