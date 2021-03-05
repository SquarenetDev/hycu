package com.dream.magic.fido.rpclient.cp;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Build.VERSION;

import com.dream.magic.fido.rpclient.MainActivity;
import com.dream.magic.fido.rpclient.PropertyManager;
import com.dream.magic.fido.rpclient.R;
import com.dream.magic.fido.rpsdk.callback.FIDOCallbackResult;
import com.dream.magic.fido.rpsdk.client.ContextKeys;
import com.dream.magic.fido.rpsdk.client.FIDO_UI_TYPE;
import com.dream.magic.fido.rpsdk.client.LOCAL_AUTH_TYPE;
import com.dream.magic.fido.rpsdk.client.MagicFIDOUtil;
import com.dream.magic.fido.rpsdk.client.Registration;

import java.util.Hashtable;

public class FIDORegistration {

	public final int ConnectTimeOut 	= 1000 * 30;
	public final int ReadTimeOut 		= 1000 * 30;

	//RP SDK 를 사용하기 위한 객체
	private Registration 	mRegist = null;
	private FIDOCallbackResult mFidoCallback = null;

	private String mAuthCode = null;
	private Context mContext = null;
	private MagicFIDOUtil magicFIDOUtil = null;

	public FIDORegistration(Context context, FIDOCallbackResult fidoCallback){
		mFidoCallback = fidoCallback;

		mRegist = new Registration(context, mFidoCallback, PropertyManager.getReqUrl(), PropertyManager.getResUrl());
//		mRegist.disUseWaitDialog();
		mRegist.setNetworkTimeout(ConnectTimeOut, ReadTimeOut);
		magicFIDOUtil = new MagicFIDOUtil(mContext);
		magicFIDOUtil.setPasscodeUIType(FIDO_UI_TYPE.FIDO_PASSCODE_PIN4);
		mContext = context;
	}

	public void registFIDO(String userID){
		registerFIDO(userID);
	}

	public String getToken(){
		return mRegist.getToken();
	}


	//=========
	private void registerFIDO(Object obj){

		// SSL 검증 (false : SSL 검증 skip / defult : true)
		MagicFIDOUtil.setSSLEnable(MainActivity.getSSLChecked());

		//패턴, 패스코드 인증장치 제한사항 설정
		setAuthenticationOption();

		// RPContext를 위한 세팅
		Hashtable<Object, Object> settingValue = new Hashtable<Object, Object>();

		//============ value 에 String 을 넣음 =============
		settingValue.put(ContextKeys.KEY_USERID, (String) obj);
		settingValue.put(ContextKeys.KEY_DEVICEMODEL, Build.MODEL);
		settingValue.put(ContextKeys.KEY_DEVICEOS, "A|" + VERSION.RELEASE);
		//================================================

		mRegist.startRegistration(settingValue);
	}

	//인증장치(패턴, 패스코드) 제한사항 설정
	private void setAuthenticationOption(){
		Hashtable<String, Object> authOption = new Hashtable<String, Object>();
		authOption.put(MagicFIDOUtil.KEY_RETRY_COUNT_TO_LOCK, 5);
		authOption.put(MagicFIDOUtil.KEY_LOCK_TIME, 30);
		authOption.put(MagicFIDOUtil.KEY_MIN_LENGTH, 4);
		authOption.put(MagicFIDOUtil.KEY_MAX_LENGTH, 4);
		// 숫자키패드 활성화 설정
		authOption.put(MagicFIDOUtil.KEY_USE_NUMBER_KEYPAD, true);
		// 빈칸 숫자키패드 활성화 설정 (숫자키패드가 활성화 되어있을 경우에만 사용 가능함)
		authOption.put(MagicFIDOUtil.KEY_USE_NUMBER_BLANK, true);
//		 숫자키패드 셔플 비활성화 설정 (true : 셔플 비활성화 / false : 셔플 활성화)
//		authOption.put(MagicFIDOUtil.KEY_SHUFFLE_DISABLE, false);
		magicFIDOUtil.setAuthenticatorOptions(LOCAL_AUTH_TYPE.LOCAL_PACODE_TYPE, authOption);

		Hashtable<String, Object> patternOption = new Hashtable<String, Object>();
		patternOption.put(MagicFIDOUtil.KEY_RETRY_COUNT_TO_LOCK, 5);
		patternOption.put(MagicFIDOUtil.KEY_LOCK_TIME, 30);
		magicFIDOUtil.setAuthenticatorOptions(LOCAL_AUTH_TYPE.LOCAL_PATTERN_TYPE, patternOption);
	}


}