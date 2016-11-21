package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import play.Logger;

/**
 * Created by eduardo on 3/08/16.
 */

public class Cripto
{
  public static String getMD5(final String pass)
  {
    try
    {
      final MessageDigest md = MessageDigest.getInstance("MD5");
      final byte[] passBytes = pass.getBytes();
      md.reset();
      final byte[] digested = md.digest(passBytes);
      final StringBuffer sb = new StringBuffer();
      for (int i = 0; i < digested.length; i++)
      {
        sb.append(Integer.toHexString(0xff & digested[i]));
      }
      return sb.toString();
    }
    catch (final NoSuchAlgorithmException ex)
    {
      Logger.error("Error creatind md5 password", ex);
    }
    return null;
  }
}
