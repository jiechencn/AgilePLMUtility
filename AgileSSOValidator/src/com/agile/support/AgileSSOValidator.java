package com.agile.support;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.StringTokenizer;

import sun.misc.BASE64Decoder;

import com.agile.admin.security.userregistry.DBUserAdapter;
import com.agile.util.crypto.ContainerCryptoUtil;
import com.agile.util.sql.AgileUtil;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class AgileSSOValidator {

	public static final String COOKIE_PREFIX = new String(Base64.encode("%%%".getBytes()));
	public static final String COOKIE_SUFFIX = new String(Base64.encode("%%%".getBytes()));
	public static final String AGILESSO_PREFIX = "AGILESSO::";
	public static final String AGILESSOONETIME_PREFIX = "AGILESSOONETIME::";
	public static final String COOKIE_DELIMITER = "::";

	public static void main(String args[]) {
		try {
			String jusername = args[0]; // j_username
			String jpassword = args[1]; // j_password

			boolean b = validateSSO(jusername, jpassword);
			System.out.println("** Valid SSO for current currentTimeMillis: " + b);
	

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected static boolean validateSSO(String username, String password)
			throws Exception {
		boolean checkExpiration = true;
		int expirationTime = 86400;

		String checkExpirationString = AgileUtil.getProperty("agile.sso.checkExpiration");
		if (checkExpirationString != null && checkExpirationString.toLowerCase().equals("false")) {
			checkExpiration = false;
		}
		System.out.println("agile.sso.checkExpiration=" + checkExpirationString);
		System.out.println("so checkExpiration=" + checkExpiration + " (default=true)");

		String expirationTimeString = AgileUtil.getProperty("agile.sso.expirationTime");
		try {
			expirationTime = Integer.parseInt(expirationTimeString);
		} catch (Exception exp) {
			// LOG.debug("Error reading expiration time for SSO token", exp);
		}
		System.out.println("agile.sso.expirationTime=" + expirationTimeString);
		System.out.println("so expirationTime=" + expirationTime + " (default=86400)");

		String user = parseUserName(username);
		String decodedUsername = new String(Base64.decode(user));
		System.out.println("username(Post Base64):" + decodedUsername);
		
		String decryptedUser = ContainerCryptoUtil.decrypt(decodedUsername);
		if (decryptedUser != null && decryptedUser.length() > 0) {
			user = decryptedUser; // like admin
		}
		System.out.println("username:" + user);

		if (password.startsWith(COOKIE_PREFIX) && password.endsWith(COOKIE_SUFFIX)) { 
			password = password.substring(COOKIE_PREFIX.length());
			password = password.substring(0, password.length()- COOKIE_PREFIX.length());
			password = new String(Base64.decode(password));
		}
		System.out.println("password (Post Base64):" + password);
		
		String decryptedPwd = ContainerCryptoUtil.decrypt(password);
		if (decryptedPwd != null && decryptedPwd.length() > 0) {
			password = decryptedPwd; // like AGILESSO::1410916549750::tJDCT7qW4X/n7bWHBk7BWMa4PIY=
		}
		System.out.println("password:" + password);

		if (password != null && password.startsWith(AGILESSO_PREFIX)) {
			System.out.println("Checking AGILESSO...");
			password = password.substring(AGILESSO_PREFIX.length());
			long createTime = System.currentTimeMillis();

			try {
				if (checkExpiration) {
					int k = password.indexOf(COOKIE_DELIMITER);
					String createTimeString = password.substring(0, k);
					createTime = Long.parseLong(createTimeString);
					password = password.substring(k + 2);
				}
				byte[] credential = new BASE64Decoder().decodeBuffer(password);
				MessageDigest md = MessageDigest.getInstance("SHA-1");
				md.update(user.getBytes("UTF-8"));
				md.update(AgileUtil.getProperty("db.password").getBytes("UTF-8"));
				byte[] hash = md.digest();
				boolean isValid = Arrays.equals(credential, hash);
				System.out.println("sso credential (object address):" + credential);
				System.out.println("sso user hash (object address):" + hash);
				System.out.println("sso credential matches user hash?" + isValid);
				
				boolean isValid2 = false;
				if (checkExpiration) {
					Long now = System.currentTimeMillis();
					isValid2 = (now <= (createTime + expirationTime * 1000));
					System.out.println("sso createTime:" + createTime);
					System.out.println("currentTimeMillis:" + now + " or real timestamp from log");
					System.out.println("currentTimeMillis <= (createTime + expirationTime*1000)?" + isValid2);
				}
				return isValid && isValid2;
			} catch (Exception tex) {
				tex.printStackTrace();
				return false;
			}
		} else if (password != null && password.startsWith(AGILESSOONETIME_PREFIX)) {
			System.out.println("Checking AGILESSOONETIME...");
			StringTokenizer strTokenizer = new StringTokenizer(password, COOKIE_DELIMITER);
			strTokenizer.nextToken();
			int tokenId = Integer.valueOf(strTokenizer.nextToken()).intValue();
			String token = strTokenizer.nextToken();
			System.out.println("Waiting for you to run SQL: \n SELECT TIME_CREATED FROM ONETIME_PX_TOKENS WHERE ID = " + tokenId);
			System.out.println("Then press Enter");
			new BufferedReader(new InputStreamReader(System.in)).readLine();
			boolean isValid = DBUserAdapter.getInstance().isValidOneTimePXToken(tokenId, new Long(expirationTime * 1000).longValue());
			Long now = System.currentTimeMillis();
			System.out.println("currentTimeMillis:" + now + " or real timestamp from log");
			System.out.println("System.currentTimeMillis() <= timeCreated + expirationTime?" + isValid);

			return isValid;
		} else {
			
			return false;
		}
	}

	private static String parseUserName(String username) {
		int pos = username.indexOf(COOKIE_DELIMITER);
		if (pos != -1) {
			return username.substring(pos + 2);
		} else
			return username;
	}

}
