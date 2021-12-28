package com.agile.support;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

import weblogic.security.internal.SerializedSystemIni;
import weblogic.security.internal.encryption.ClearOrEncryptedService;

import com.agile.util.Scrambler;
import com.agile.util.crypto.ContainerCryptoUtil;


public class CredentialUtil {
	public static String BF = "BF"; // Blowfish
	public static String SHA0 = "SHA-0"; // user password in agileuser table: old style
	public static String SHA256 = "SHA-256"; // user password in agileuser table: new style
	public static String SHA384 = "SHA-384";
	public static String SHA512 = "SHA-512";
	public static String AES = "AES"; // weblogic AES
	public static String DES3 = "3DES"; // weblogic 3DES
	public static String AES128 = "AES:128"; // second encryption for
											// token/cookie
	public static String BASE64 = "BASE64"; // first encryption for token/cookie
	public static String TOKEN = "TOKEN"; // token/cookie
	
	static String securityPath = "";
	static String domainPath = "";
	static String printError = "n";
	
	static Method methodSHAxHash = null;
	static boolean bAES128 = false; 

	public static void main(String[] paramArrayOfString) {

		System.out.println("--- Credential Utility for Agile PLM ");
		System.out.println("--- Author: jie.chen@oracle.com");
		System.out.println("--- Note: 1504478.1");
	
		domainPath = System.getProperty("domain.home");
		printError = System.getProperty("print.error");
		securityPath = domainPath + File.separator + "security";			

		System.out.println("Agile Domain: " + domainPath);
		
		
		
		try {
			methodSHAxHash = Scrambler.class.getMethod("generateHash", String.class, String.class);
		} catch (Exception e) {
			methodSHAxHash = null;
			if (printError.equalsIgnoreCase("y"))
				e.printStackTrace();
		}
		try{
			Class.forName("com.agile.util.crypto.ContainerCryptoUtil");
			bAES128 = true;
		}catch (Exception e){
			bAES128 = false;
			if (printError.equalsIgnoreCase("y"))
				e.printStackTrace();
		}
		
		try {

			for (;;) {
				System.out.println("\nOptions:");
				System.out.println("x   --> Exit");
				System.out.println("e   --> Encrypt credential");
				System.out.println("d   --> Decrypt ciphertext");
				System.out.print("Input choice> ");
				String o = new BufferedReader(new InputStreamReader(System.in))
						.readLine();
				if (o.equalsIgnoreCase("x")) {
					break;
				}
				if (o.equalsIgnoreCase("e")) {
					encryptOption();
				} else if (o.equalsIgnoreCase("d")) {
					decryptOption();
				}
			}
		} catch (Exception e) {
			if (printError.equalsIgnoreCase("y"))
				e.printStackTrace();
		}
		System.err.println("------ Bye ------");
	}

	private static void decryptOption() {
		String credential = "";
		try {
			System.out.print("Input ciphertext> ");
			String cipher = new BufferedReader(new InputStreamReader(System.in))
					.readLine();
			if (cipher.startsWith("{" + AES + "}") || cipher.startsWith("{" + DES3 + "}")) {
				ClearOrEncryptedService ces = new ClearOrEncryptedService(SerializedSystemIni.getEncryptionService(securityPath));
				cipher = cipher.replace("\\", "");
			    credential =ces.decrypt(cipher);
			} else if (cipher.startsWith("{" + AES128 + "}") && bAES128) {
				credential = ContainerCryptoUtil.decrypt(cipher);
			} else{ // suppose it's BF
				credential = com.agile.util.BF.decrypt(cipher); 
			} 
			System.out.println("Credential: " + credential);
			
		} catch (Exception e) {
			if (printError.equalsIgnoreCase("y"))
				e.printStackTrace();
		}

		
	}

	private static void encryptOption() {
		String ciphertext = "";
		String pwd = "";
		try {
			System.out.print("Input credential> ");
			pwd = new BufferedReader(new InputStreamReader(System.in)).readLine();
		}catch(Exception e){
			if (printError.equalsIgnoreCase("y"))
				e.printStackTrace();
		}
		try{//BF
			ciphertext = com.agile.util.BF.encrypt(pwd);
			System.out.println(BF + " Ciphertext: " + ciphertext);
		}catch(Exception e){
			if (printError.equalsIgnoreCase("y"))
				e.printStackTrace();
		}
		try{//AES/3DES
			ClearOrEncryptedService ces = new ClearOrEncryptedService(SerializedSystemIni.getEncryptionService(securityPath));
		    ciphertext =ces.encrypt(pwd);
		    System.out.println(AES + "/" + DES3 + " Ciphertext: " + ciphertext);
		}catch(Exception e){
			if (printError.equalsIgnoreCase("y"))
				e.printStackTrace();
		}
		if (bAES128){
			try{//AES128
			    ciphertext = ContainerCryptoUtil.encrypt(pwd);
			    System.out.println(AES128 + " Ciphertext: " + ciphertext);
			}catch(Exception e){
				if (printError.equalsIgnoreCase("y"))
					e.printStackTrace();
			}
		}
		
		try{//SHA0
			ciphertext = Scrambler.generateHash(pwd);
			System.out.println(SHA0 + " Ciphertext: " + ciphertext);
		}catch(Exception e){
			if (printError.equalsIgnoreCase("y"))
				e.printStackTrace();
		}
		if (methodSHAxHash!=null){
			try{//SHA256
				ciphertext = generateSHAxHash(SHA256, pwd);
				System.out.println(SHA256 + " Ciphertext: " + ciphertext);
			}catch(Exception e){
				if (printError.equalsIgnoreCase("y"))
					e.printStackTrace();
			}
			try{//SHA384
				ciphertext = generateSHAxHash(SHA384, pwd);
				System.out.println(SHA384 + " Ciphertext: " + ciphertext);
			}catch(Exception e){
				if (printError.equalsIgnoreCase("y"))
					e.printStackTrace();
			}
			try{//SHA512
				ciphertext = generateSHAxHash(SHA512, pwd);
				System.out.println(SHA512 + " Ciphertext: " + ciphertext);
	
			} catch (Exception e) {
				if (printError.equalsIgnoreCase("y"))
					e.printStackTrace();
			}
		}

	}

	private static String generateSHAxHash(String alg, String pwd) {
		return "{" + alg + "}" + Scrambler.generateHash(alg, pwd);
	}
}
