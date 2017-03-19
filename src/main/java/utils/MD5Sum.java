/*  JSCOUR - Java Scour search and download interface
 *  Copyright (C) 2000  jscour@priest.com
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package utils;

import java.io.*;

import java.security.*;

public class MD5Sum{
        // something around 356000 bytes...
        // < 308,032 bytes
        //public static long scourMd5ByteLimit = 10000;
    public static int scourMd5ByteLimit = (300 * 1024);
    private static MessageDigest mes = null;

    /**
     *  Method:         md5Sum 
     *  Purpose:        calculate the MD5 in a way compatible with how
     *                          the scour.net protocol encodes its passwords
     *                          (incidentally, it also outputs a string identical
     *                          to the md5sum unix command).
     *  @param          str     the String from which to calculate the sum
     *  @return         the MD5 checksum
     */
    public static String md5Sum (final String str)
    {
        try
            {
                return md5Sum (str.getBytes ("UTF-8"));
            }
        catch (UnsupportedEncodingException e)
            {
                throw new IllegalStateException (e.getMessage ());
            }
    }

    public static String md5Sum (final byte [] input)
    {
        return md5Sum (input, -1);
    }

    public static String md5Sum (final byte [] input, final int limit)
    {
        try
            {
                if (mes == null){
                    mes = MessageDigest.getInstance ("MD5");
                }

                mes.reset ();
                byte [] digest;

                if (limit == -1)
                    {
                        digest = mes.digest (input);
                    }
                else
                    {
                        mes.update (input, 0, 
                                    limit > input.length ? input.length : limit);
                        digest = mes.digest ();
                    }

                final StringBuffer hexString = new StringBuffer (); 

                for (int i = 0; i < digest.length; i++) 
                    {
                        hexString.append (hexDigit (digest [i])); 
                    }

                return hexString.toString ();
            }
        catch (NoSuchAlgorithmException e)
            {
                throw new IllegalStateException (e.getMessage ());
            }
    }

    /**
     *  Method:         hexDigit 
     *  Purpose:        convert a hex digit to a String, used
     *                          by md5Sum.
     *  @param          xByte   the digit to translate
     *  @return         the hex code for the digit
     */
    static private String hexDigit (final byte xByte) 
    {
        final StringBuffer strb = new StringBuffer ();
        char ctmp;

        // First nibble
        ctmp = (char) ((xByte >> 4) & 0xf);
        if (ctmp > 9) 
            {
                ctmp = (char) ((ctmp - 10) + 'a');
            } 
        else 
            {
                ctmp = (char) (ctmp + '0');
            }

        strb.append (ctmp);

        // Second nibble
        ctmp = (char) (xByte & 0xf);
        if (ctmp > 9) 
            {
                ctmp = (char)((ctmp - 10) + 'a');
            } 
        else 
            {
                ctmp = (char)(ctmp + '0');
            }

        strb.append (ctmp);
        return strb.toString ();
    }

    /**
     *  Method:         getFileMD5Sum 
     *  Purpose:        get the MD5 sum of a file. Scour exchange
     *                          only counts the first scourMd5ByteLimit
     *                          bytes of a file for caclulating checksums
     *                          (probably for efficiency or better comaprison
     *                          counts against unfinished downloads).
     *  @param          fpt the file to read
     *  @return         the MD5 sum string
     *  @throws         IOException on IO error
     */
    public static String getFileMD5Sum (final File fpt) throws IOException
    {
        String sum = null;
        final FileInputStream inp = new FileInputStream (fpt.getAbsolutePath ());

        final byte [] btmp = new byte [1024];
        int num = 0;
        final ByteArrayOutputStream out = new ByteArrayOutputStream ();

        while (num != -1)
            {
                num = inp.read (btmp);
                out.write (btmp, 0, num);

                if (out.size () > scourMd5ByteLimit)
                    {
                        sum = md5Sum (out.toByteArray (), scourMd5ByteLimit);
                        break;
                    }
            }

        if (sum == null){
            sum = md5Sum (out.toByteArray (), scourMd5ByteLimit);
        }

        inp.close ();
        out.close ();

        return sum;
    }
}

