package com.example.hour16app2;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5_Fnc {
	private static char[] hextable = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};


	private static String byteArrayToHex(byte[] array) {
	    String s = "";
	    for (int i = 0; i < array.length; ++i) {
	        int di = (array[i] + 256) & 0xFF; // Make it unsigned
	        s = s + hextable[(di >> 4) & 0xF] + hextable[di & 0xF];
	    }
	    return s;
	}

	private static String digest(String s, String algorithm) {
	    MessageDigest m = null;
	    try {
	        m = MessageDigest.getInstance(algorithm);
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	        return s;
	    }

	    try {
	        m.update(s.getBytes("UTF-8"));
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	        m.update(s.getBytes());
	    }
	    return byteArrayToHex(m.digest());
	}

	public static String md5(String s) {
	    return digest(s, "MD5");
	}
}
