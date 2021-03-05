package com.dream.magic.fido.rpclient.exception;

import android.util.Log;

public class RPLog {
	
	public final static String LOGPREFIX = "[RPClient]";
	
	public static void p(Class cls, String str){
		Log.v("MAGIC", LOGPREFIX + "[" + cls.getSimpleName() + "] " +  str);
	}

}
