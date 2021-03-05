package com.dream.magic.fido.rpclient;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.dream.magic.fido.rpclient.exception.RPLog;
import com.dream.magic.fido.rpsdk.callback.FIDOCallbackResult;
import com.dream.magic.fido.rpsdk.client.FIDO_UI_TYPE;
import com.dream.magic.fido.rpsdk.client.FidoResult;
import com.dream.magic.fido.rpsdk.client.LOCAL_AUTH_TYPE;
import com.dream.magic.fido.rpsdk.client.MagicFIDOUtil;

import java.util.Hashtable;

public class MainActivity extends Activity implements OnClickListener {
	
	private static String authID 	= null;
	private static boolean isFederation = false;
	private static boolean isCheckSSL 	= true;
	
	public static final String sharedTable = "MAGIC_FIDO";
	public static final String userIDKey = "USER_ID";
	public static final String checked = "checked";
	public static final String sslchecked = "sslchecked";
	
	private Button register 	= null;
	private Button auth 		= null;
	private Button deregister 	= null;
	private Button facetBtn 	= null;
	private Button serverinfo	= null;

	
	private MagicFIDOUtil mFidoUtil = null;
	
	private SharedPreferences 		 pref 	= null;
	public static SharedPreferences.Editor editor	= null;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		PropertyManager.load(this);

		mFidoUtil = new MagicFIDOUtil(this);

		pref = getSharedPreferences(sharedTable, MODE_PRIVATE);
		editor = pref.edit();

		authID = pref.getString(userIDKey, null);
		isFederation = pref.getBoolean(checked, false);
		isCheckSSL = pref.getBoolean(sslchecked, false);

		setContentView(R.layout.rp_activity_main);

		getActionBar().setDisplayShowHomeEnabled(false);
		Drawable background = getResources().getDrawable(R.drawable.bg_top);
		getActionBar().setBackgroundDrawable(background);

		getWindow().setBackgroundDrawableResource(R.drawable.bg);

		register = (Button) findViewById(R.id.Register);
		deregister = (Button) findViewById(R.id.Deregeister);
		auth = (Button) findViewById(R.id.Authentication);
		facetBtn = (Button) findViewById(R.id.facetIDButton);
		serverinfo = (Button) findViewById(R.id.propButton);


		register.setOnClickListener(this);
		deregister.setOnClickListener(this);
		auth.setOnClickListener(this);
		serverinfo.setOnClickListener(this);
		facetBtn.setOnClickListener(this);


		String[] permission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE , Manifest.permission.READ_PHONE_STATE};

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			requestPermissions( permission , 00);
		}
	}



	@Override
	public void onClick(View arg0) {

		Class<?> cls = null;
		if (arg0.getId() == register.getId()) {
			cls = RegisterActivity.class;
		} else if (arg0.getId() == auth.getId()) {
			cls = AuthenticationActivity.class;
		} else if (arg0.getId() == deregister.getId()) {
			cls = DeregisterActivity.class;
		} else if (arg0.getId() == serverinfo.getId()) {
			cls = PropertyActivity.class;
		} else if (arg0.getId() == facetBtn.getId()) {

			String rpFacetID = PropertyManager.getFacetID(this);
            if (rpFacetID != null) {
                RPLog.p(MainActivity.class, "===앱 FacetID 획득===\n" + rpFacetID + "\n=====================");
                Toast.makeText(this, rpFacetID, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error Occurred, While getting the FacetID of RPClient.", Toast.LENGTH_SHORT).show();
            }
            return;
		}  else
			return;
		
		startActivity(new Intent(this, cls));
		overridePendingTransition(0,0 );
	}
	
	public static void setAuthID(String regID){
		authID = regID;
		
		editor.putString(userIDKey, authID);
		editor.commit();
	}
	
	public static String getAuthID(){
		return authID;
	}

    public static void setChecked(boolean isChecked) {
        isFederation = isChecked;
        editor.putBoolean(checked, isFederation);
        editor.commit();
    }

    public static boolean getChecked() {
        return isFederation;
    }
    
    public static void setSSLChecked(boolean isChecked) {
    	isCheckSSL = isChecked;
        editor.putBoolean(sslchecked, isCheckSSL);
        editor.commit();        
    }

    public static boolean getSSLChecked() {
        return isCheckSSL;
    }

	@Override
	protected void onPause() {
		super.onPause();
		overridePendingTransition(0,0 );
	}
}
