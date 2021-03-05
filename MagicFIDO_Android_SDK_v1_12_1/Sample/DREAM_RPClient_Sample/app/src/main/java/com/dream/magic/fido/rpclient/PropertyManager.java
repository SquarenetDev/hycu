package com.dream.magic.fido.rpclient;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Properties;

import com.dream.magic.fido.rpclient.exception.RPLog;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.util.Base64;


public class PropertyManager {
	public static final String PROPNAME = "rpclient.properties";
	private static final String SERVERURL = "ServerURL";
	private static final String REQURL = "RequestURL";
	private static final String RESURL = "ResponseURL";			
	private static final String VERIFIER = "HostNameVerifier";
	
	private static Properties prop;
	private static Activity act;
	
	public static void load(Activity param){

		act = param;
		try {						
			FileInputStream fis = act.openFileInput(PROPNAME);			
			prop = new Properties();
			prop.load(fis);
			fis.close();	
		} catch (FileNotFoundException e) {

			RPLog.p(PropertyActivity.class, "properties file doesn't exist..creation..");
			try {
				
				AssetManager assetManager = act.getApplicationContext().getAssets();	
				InputStream ins = assetManager.open(PROPNAME);
				prop = new Properties();				
				prop.load(ins);
				ins.close();							
				
				FileOutputStream fos = act.openFileOutput(PROPNAME, Activity.MODE_PRIVATE);				
				prop.store(fos, "fromAssets");
				fos.close();
				
				Intent intent = new Intent(act.getApplicationContext(),
						com.dream.magic.fido.rpclient.PropertyActivity.class);
				act.startActivity(intent);
				act.overridePendingTransition(0,0);
				
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}	
	
	public static void save(){

		try {
			FileOutputStream fos = act.openFileOutput(PROPNAME, Activity.MODE_PRIVATE);
			prop.store(fos, "changeurl");
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public static String getReqUrl(){
		RPLog.p(PropertyManager.class, "getRequestURL : " + prop.getProperty(SERVERURL) + prop.getProperty(REQURL));
		return prop.getProperty(SERVERURL) + prop.getProperty(REQURL);
	}
	
	public static String getResUrl(){
		RPLog.p(PropertyManager.class, "getResponseURL : " + prop.getProperty(SERVERURL) + prop.getProperty(RESURL));
		return prop.getProperty(SERVERURL) + prop.getProperty(RESURL);
	}
	
	public static String getRestReq(){
		return prop.getProperty(REQURL);
	}
	
	public static String getRestRes(){
		return prop.getProperty(RESURL);
	}
	
	public static String getHostNameVerifier(){
		return prop.getProperty(VERIFIER);
	}
	
	public static void setServerUrl(String url, String reqUrl, String resUrl){
		RPLog.p(PropertyManager.class, "setServerURL : " + url);
		prop.setProperty(SERVERURL, url);
		prop.setProperty(REQURL, reqUrl);
		prop.setProperty(RESURL, resUrl);
	}
	
	public static String getServerUrl(){
		return prop.getProperty(SERVERURL);
	}
	
	public static String getFacetID(Context context) {

		try {
			
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);

			byte[] cert = info.signatures[0].toByteArray();
			InputStream input = new ByteArrayInputStream(cert);

			CertificateFactory cf = CertificateFactory.getInstance("X509");
			X509Certificate c = (X509Certificate) cf.generateCertificate(input);

			MessageDigest md = MessageDigest.getInstance("SHA1");
			
			String rpfacetinfo = "android:apk-key-hash:"
					+ Base64.encodeToString(md.digest(c.getEncoded()),
							Base64.DEFAULT | Base64.NO_WRAP | Base64.NO_PADDING);
			
			return rpfacetinfo;

		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return null;
	}


}