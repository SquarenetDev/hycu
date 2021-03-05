package com.dream.magic.fido.rpclient.cp;

import android.content.Context;
import android.os.Build;

import com.dream.magic.fido.rpclient.MainActivity;
import com.dream.magic.fido.rpclient.PropertyManager;
import com.dream.magic.fido.rpsdk.callback.FIDOCallbackResult;
import com.dream.magic.fido.rpsdk.client.ContextKeys;
import com.dream.magic.fido.rpsdk.client.Deregistration;
import com.dream.magic.fido.rpsdk.client.MagicFIDOUtil;

import java.util.Hashtable;

public class FIDODeRegistration {
	
	public final int ConnectTimeOut 	= 1000 * 30;
	public final int ReadTimeOut 		= 1000 * 30;
	
	//RP SDK 를 사용하기 위한 객체
	private Deregistration 	mDeregist = null;	
	private FIDOCallbackResult mFidoCallback = null;
	
	public FIDODeRegistration(Context context, FIDOCallbackResult fidoCallback){
		mFidoCallback = fidoCallback;

		mDeregist = new Deregistration(context, mFidoCallback, PropertyManager.getReqUrl());
		mDeregist.setNetworkTimeout(ConnectTimeOut, ReadTimeOut);
		
	}
	
	public void deRegistFIDO(String userID){
		
		MagicFIDOUtil.setSSLEnable(MainActivity.getSSLChecked());
		
		// RPContext를 위한 세팅
		Hashtable<Object, Object> settingValue = new Hashtable<Object, Object>();

		settingValue.put(ContextKeys.KEY_USERID,userID);

		mDeregist.startDeregistration(settingValue); //localType 미 설정 시 인증장치 선택 창 나옴(인증장치 2개 이상일 경우)

		
	}
	
	public String getToken(){
		return mDeregist.getToken();
	}

}