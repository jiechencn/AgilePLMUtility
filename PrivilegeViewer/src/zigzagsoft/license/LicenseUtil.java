package zigzagsoft.license;

import sun.misc.BASE64Decoder;

/**
 * Created by jijichen on 7/6/2014.
 */
public class LicenseUtil {
    public static final String DEFAULT_ENCODING = "UTF-8";
    static BASE64Decoder dec = new BASE64Decoder();

    public static boolean validate(String encrypted, String text, String key) {

        if (encrypted==null || key==null || text==null
                || encrypted.equals("") || key.equals("")) return false;
        String decode = base64decode(encrypted);
        String original = xorMessage(decode, key);

        return text.trim().equals(original.trim());
    }


    public static String base64decode(String text) {

        try {
            return new String(dec.decodeBuffer(text), DEFAULT_ENCODING);
        } catch (Exception e) {
            return null;
        }

    }//base64decode

    private static String xorMessage(String message, String key) {
        try {
            if (message == null || key == null) return null;

            char[] keys = key.toCharArray();
            char[] mesg = message.toCharArray();

            int ml = mesg.length;
            int kl = keys.length;
            char[] newmsg = new char[ml];

            for (int i = 0; i < ml; i++) {
                newmsg[i] = (char) (mesg[i] ^ keys[i % kl]);
            }//for i
            mesg = null;
            keys = null;
            return new String(newmsg);
        } catch (Exception e) {
            return null;
        }
    }//xorMessage

}
