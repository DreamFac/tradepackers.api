package utils;

import play.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by eduardo on 3/08/16.
 */

public class Cripto{
        public static String getMD5 (String pass)
        {
            try
            {
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] passBytes = pass.getBytes();
                md.reset();
                byte[] digested = md.digest(passBytes);
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < digested.length; i++)
                {
                    sb.append(Integer.toHexString(0xff & digested[i]));
                }
                Logger.debug("Password: " + sb);
                return sb.toString();
            }
            catch (NoSuchAlgorithmException ex)
            {
                Logger.error("Error creatind md5 password", ex);
            }
            return null;
        }
}
