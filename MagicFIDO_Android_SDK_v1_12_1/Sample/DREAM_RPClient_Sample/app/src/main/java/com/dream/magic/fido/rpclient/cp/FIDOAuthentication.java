package com.dream.magic.fido.rpclient.cp;

import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;

import com.dream.magic.fido.rpclient.MainActivity;
import com.dream.magic.fido.rpclient.PropertyManager;
import com.dream.magic.fido.rpsdk.callback.FIDOCallbackResult;
import com.dream.magic.fido.rpsdk.client.Authentication;
import com.dream.magic.fido.rpsdk.client.ContextKeys;
import com.dream.magic.fido.rpsdk.client.MagicFIDOUtil;

import java.util.Hashtable;

public class FIDOAuthentication {

    public final int ConnectTimeOut = 1000 * 30;
    public final int ReadTimeOut = 1000 * 30;

    //RP SDK 를 사용하기 위한 객체
    private Authentication mAuth = null;
    private static FIDOAuthentication mFidoAuthentication = null;

    // 여러 Activity 에서 동일한 Authentication를 사용하기 위하여 싱글톤으로 설정함
    public static FIDOAuthentication getInstance() {

        if (mFidoAuthentication == null) {
            mFidoAuthentication = new FIDOAuthentication();
        }

        return mFidoAuthentication;
    }

    public String getToken() {
        return mAuth.getToken();
    }


    //=========

    public void startAuthentication(Context context , FIDOCallbackResult  callbackResult , String userID ) {

        if(mAuth == null){
            mAuth = new Authentication(context, callbackResult, PropertyManager.getReqUrl(), PropertyManager.getResUrl());
        }

        mAuth.setNetworkTimeout(ConnectTimeOut, ReadTimeOut);

//        mAuth.disUseWaitDialog(); // dialog 안보이도록 설정

        MagicFIDOUtil.setSSLEnable(MainActivity.getSSLChecked());

        // RPContext를 위한 세팅
        Hashtable<Object, Object> settingValue = new Hashtable<Object, Object>();
        //============ value 에 String 을 넣음 =============
        settingValue.put(ContextKeys.KEY_USERID, userID);
        settingValue.put(ContextKeys.KEY_DEVICEMODEL, Build.MODEL);
        settingValue.put(ContextKeys.KEY_DEVICEOS, "A|" + VERSION.RELEASE);
        //================================================
        mAuth.startAuthentication(settingValue);

    }


}